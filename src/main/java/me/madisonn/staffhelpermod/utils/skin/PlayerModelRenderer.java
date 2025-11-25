package me.madisonn.staffhelpermod.utils.skin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.io.InputStream;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import java.util.HashMap;

public class PlayerModelRenderer {
    private static FakePlayerEntity currentFakePlayer = null;
    private static String currentPlayerName = "";
    private static boolean currentShowSecondLayer = true;
    private static final Map<String, Identifier> loadedSkins = new HashMap<>();
    private static final Map<String, Boolean> skinLoadingAttempts = new HashMap<>();

    public static void renderPlayerModel(DrawContext context, String playerName, int x, int y, int size, float rotation, boolean showSecondLayer) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        try {
            // Fake Player
            if (currentFakePlayer == null || !currentPlayerName.equals(playerName)) {
                currentFakePlayer = createPlayerWithSkin(client, playerName, showSecondLayer);
                currentPlayerName = playerName;
                currentShowSecondLayer = showSecondLayer;
            }

            // Second Layer Toggle
            if (currentShowSecondLayer != showSecondLayer) {
                currentFakePlayer.setShowSecondLayer(showSecondLayer);
                currentShowSecondLayer = showSecondLayer;
            }

            MatrixStack matrices = context.getMatrices();
            VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();

            matrices.push();

            // Position model
            matrices.translate(x, y, 50);
            matrices.scale(size, -size, size);

            // Rotation - 1.21.5
            matrices.multiply(new org.joml.Quaternionf().rotateY((float) Math.toRadians(rotation)));

            // Position adjustment
            matrices.translate(0, -0.5, 0);

            // Render Entity
            renderEntity(client, currentFakePlayer, matrices, vertexConsumers);

            vertexConsumers.draw();
            matrices.pop();

        } catch (Exception e) {
            context.drawText(client.textRenderer, "3D Model", x, y, 0xFFFFFF, false);
        }
    }

    private static void renderEntity(MinecraftClient client, FakePlayerEntity entity, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers) {
        try {
            client.getEntityRenderDispatcher().render(
                    entity,
                    0.0, 0.0, 0.0,
                    180f,
                    matrices,
                    vertexConsumers,
                    15728880
            );
        } catch (Exception e) {
            System.out.println("Entity rendering failed: " + e.getMessage());
        }
    }

    private static FakePlayerEntity createPlayerWithSkin(MinecraftClient client, String playerName, boolean showSecondLayer) {
        PlayerListEntry playerEntry = null;
        if (client.getNetworkHandler() != null) {
            playerEntry = client.getNetworkHandler().getPlayerListEntry(playerName);
        }

        GameProfile profile;
        if (playerEntry != null) {
            // Online player - use their skin
            profile = playerEntry.getProfile();
            System.out.println("Using online player skin for: " + playerName);
        } else {
            // Offline player - get UUID and load skin
            UUID uuid = getRealUUID(playerName).join();
            profile = new GameProfile(uuid, playerName);
            System.out.println("Loading skin for offline player: " + playerName);

            // Only try to load skin once per player session
            if (!skinLoadingAttempts.containsKey(playerName.toLowerCase())) {
                skinLoadingAttempts.put(playerName.toLowerCase(), true);
                loadSkinFromMinecraftTextureServer(client, playerName, uuid);
            }
        }

        FakePlayerEntity fakePlayer = new FakePlayerEntity(client.world, profile, showSecondLayer);

        if (loadedSkins.containsKey(playerName.toLowerCase())) {
            fakePlayer.setCustomSkin(loadedSkins.get(playerName.toLowerCase()));
        }

        return fakePlayer;
    }

    private static CompletableFuture<UUID> getRealUUID(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String apiUrl = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
                HttpURLConnection connection = (HttpURLConnection) new URI(apiUrl).toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    try (InputStream inputStream = connection.getInputStream()) {
                        String response = new String(inputStream.readAllBytes());
                        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                        if (json.has("id")) {
                            String uuidStr = json.get("id").getAsString();
                            uuidStr = uuidStr.replaceAll(
                                    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                                    "$1-$2-$3-$4-$5"
                            );
                            UUID uuid = UUID.fromString(uuidStr);
                            System.out.println("Found real UUID for " + playerName + ": " + uuid);
                            return uuid;
                        }
                    }
                }
                System.out.println("No Mojang profile found for: " + playerName);
            } catch (Exception e) {
                System.out.println("Mojang API error for " + playerName + ": " + e.getMessage());
            }

            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        });
    }

    private static void loadSkinFromMinecraftTextureServer(MinecraftClient client, String playerName, UUID uuid) {
        CompletableFuture.runAsync(() -> {
            try {
                // Get skin data from Mojang session server
                String sessionUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "");
                HttpURLConnection sessionConnection = (HttpURLConnection) new URI(sessionUrl).toURL().openConnection();
                sessionConnection.setRequestMethod("GET");
                sessionConnection.setConnectTimeout(5000);
                sessionConnection.setReadTimeout(5000);

                if (sessionConnection.getResponseCode() == 200) {
                    try (InputStream inputStream = sessionConnection.getInputStream()) {
                        String response = new String(inputStream.readAllBytes());
                        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

                        if (json.has("properties")) {
                            var properties = json.getAsJsonArray("properties");
                            for (var prop : properties) {
                                JsonObject property = prop.getAsJsonObject();
                                if (property.has("name") && property.get("name").getAsString().equals("textures")) {
                                    String textureData = property.get("value").getAsString();
                                    String decoded = new String(java.util.Base64.getDecoder().decode(textureData));
                                    JsonObject texturesJson = JsonParser.parseString(decoded).getAsJsonObject();

                                    if (texturesJson.has("textures")) {
                                        JsonObject textures = texturesJson.getAsJsonObject("textures");
                                        if (textures.has("SKIN")) {
                                            JsonObject skin = textures.getAsJsonObject("SKIN");
                                            String skinUrl = skin.get("url").getAsString();

                                            System.out.println("Downloading skin from Minecraft CDN: " + skinUrl);
                                            downloadAndRegisterSkin(client, playerName, skinUrl);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                System.out.println("No skin data found for: " + playerName);
            } catch (Exception e) {
                System.out.println("Error loading from Minecraft texture server: " + e.getMessage());
            }
        });
    }

    private static void downloadAndRegisterSkin(MinecraftClient client, String playerName, String skinUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URI(skinUrl).toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (connection.getResponseCode() == 200) {
                byte[] imageData;
                try (InputStream inputStream = connection.getInputStream()) {
                    imageData = inputStream.readAllBytes();
                }

                client.execute(() -> {
                    try (InputStream imageStream = new java.io.ByteArrayInputStream(imageData)) {
                        Identifier skinId = Identifier.of("staffhelper", "skin_" + playerName.toLowerCase());

                        var nativeImage = net.minecraft.client.texture.NativeImage.read(imageStream);

                        var texture = new net.minecraft.client.texture.NativeImageBackedTexture(
                                () -> "staffhelper_skin_" + playerName.toLowerCase(),
                                nativeImage
                        );

                        client.getTextureManager().registerTexture(skinId, texture);
                        loadedSkins.put(playerName.toLowerCase(), skinId);

                        System.out.println("Successfully loaded skin for: " + playerName);

                        if (currentPlayerName.equals(playerName)) {
                            currentFakePlayer = null;
                        }

                    } catch (Exception e) {
                        System.out.println("Failed to load skin texture: " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error downloading skin: " + e.getMessage());
        }
    }

    // Clear only the current player cache (for switching players)
    public static void clearCache() {
        currentFakePlayer = null;
        currentPlayerName = "";
    }

    // Clear ALL cache including skins (for closing the GUI)
    public static void clearAllCache() {
        currentFakePlayer = null;
        currentPlayerName = "";
        loadedSkins.clear();
        skinLoadingAttempts.clear();
        System.out.println("Cleared all skin viewer cache from memory");
    }

    public static boolean isPlayerOnline(String playerName) {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.getNetworkHandler() != null &&
                client.getNetworkHandler().getPlayerListEntry(playerName) != null;
    }
}