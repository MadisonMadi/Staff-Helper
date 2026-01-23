package me.madisonn.staffhelpermod.utils.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import me.madisonn.staffhelpermod.StaffHelperClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerModelRenderer {
    private record PlayerModelCache(FakePlayerEntity fakePlayer, String playerName, boolean showSecondLayer) {}

    private static PlayerModelCache currentCache;
    private static final Map<String, Identifier> skinCache = new HashMap<>();
    private static final Map<String, Boolean> skinLoadingAttempts = new HashMap<>();

    public static void renderPlayerModel(DrawContext context, String playerName, int x, int y, int size, float rotation, boolean showSecondLayer) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        try {
            updateCurrentPlayer(client, playerName, showSecondLayer);
            renderEntity(context, client, x, y, size, rotation);
        } catch (Exception e) {
            StaffHelperClient.LOGGER.error("Failed to render player model: {}", playerName, e);
            context.drawText(client.textRenderer, "3D Model", x, y, 0xFFFFFF, false);
        }
    }

    private static void updateCurrentPlayer(MinecraftClient client, String playerName, boolean showSecondLayer) {
        if (currentCache == null || !currentCache.playerName.equals(playerName)) {
            currentCache = new PlayerModelCache(createPlayer(client, playerName, showSecondLayer), playerName, showSecondLayer);
        } else if (currentCache.showSecondLayer != showSecondLayer) {
            currentCache.fakePlayer.setShowSecondLayer(showSecondLayer);
            currentCache = new PlayerModelCache(currentCache.fakePlayer, playerName, showSecondLayer);
        }
    }

    private static void renderEntity(DrawContext context, MinecraftClient client, int x, int y, int size, float rotation) {
        MatrixStack matrices = context.getMatrices();
        VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();

        matrices.push();
        matrices.translate(x, y, 50);
        matrices.scale(size, -size, size);
        matrices.multiply(net.minecraft.util.math.RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
        matrices.translate(0, -0.5, 0);

        client.getEntityRenderDispatcher().render(
                currentCache.fakePlayer, 0, 0, 0, 180f,
                matrices, vertexConsumers, LightmapTextureManager.pack(15, 15)
        );

        vertexConsumers.draw();
        matrices.pop();
    }

    private static FakePlayerEntity createPlayer(MinecraftClient client, String playerName, boolean showSecondLayer) {
        UUID uuid = getUUID(playerName);
        GameProfile profile = new GameProfile(uuid, playerName);

        if (!skinLoadingAttempts.containsKey(playerName.toLowerCase()) && !isOnline(playerName)) {
            skinLoadingAttempts.put(playerName.toLowerCase(), true);
            loadSkin(playerName, uuid);
        }

        FakePlayerEntity fakePlayer = new FakePlayerEntity(client.world, profile, showSecondLayer);
        Identifier skin = skinCache.get(playerName.toLowerCase());
        if (skin != null) fakePlayer.setCustomSkin(skin);

        return fakePlayer;
    }

    private static UUID getUUID(String playerName) {
        PlayerListEntry entry = getPlayerEntry(playerName);
        if (entry != null) return entry.getProfile().getId();

        try {
            return fetchUUID(playerName).get();
        } catch (Exception e) {
            StaffHelperClient.LOGGER.warn("Failed to fetch UUID for {}", playerName, e);
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        }
    }

    private static CompletableFuture<UUID> fetchUUID(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String response = fetch("https://api.mojang.com/users/profiles/minecraft/" + playerName);
                if (response != null) {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    if (json.has("id")) {
                        String uuidStr = formatUUID(json.get("id").getAsString());
                        StaffHelperClient.LOGGER.debug("Found UUID for {}: {}", playerName, uuidStr);
                        return UUID.fromString(uuidStr);
                    }
                }
            } catch (Exception e) {
                StaffHelperClient.LOGGER.error("Mojang API error: {}", playerName, e);
            }
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        });
    }

    private static void loadSkin(String playerName, UUID uuid) {
        CompletableFuture.runAsync(() -> {
            try {
                String response = fetch("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
                if (response != null) extractAndDownloadSkin(playerName, response);
            } catch (Exception e) {
                StaffHelperClient.LOGGER.error("Error loading skin: {}", playerName, e);
            }
        });
    }

    private static void extractAndDownloadSkin(String playerName, String response) {
        try {
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            if (json.has("properties")) {
                json.getAsJsonArray("properties").forEach(prop -> {
                    JsonObject property = prop.getAsJsonObject();
                    if (property.has("name") && "textures".equals(property.get("name").getAsString())) {
                        String decoded = new String(Base64.getDecoder().decode(property.get("value").getAsString()));
                        JsonObject texturesJson = JsonParser.parseString(decoded).getAsJsonObject();

                        if (texturesJson.has("textures")) {
                            JsonObject textures = texturesJson.getAsJsonObject("textures");
                            if (textures.has("SKIN")) {
                                String skinUrl = textures.getAsJsonObject("SKIN").get("url").getAsString();
                                downloadAndRegisterSkin(playerName, skinUrl);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            StaffHelperClient.LOGGER.error("Error extracting skin: {}", playerName, e);
        }
    }

    private static void downloadAndRegisterSkin(String playerName, String skinUrl) {
        CompletableFuture.supplyAsync(() -> downloadSkin(skinUrl))
                .thenAccept(bytes -> bytes.ifPresent(data -> registerSkin(playerName, data)))
                .exceptionally(e -> {
                    StaffHelperClient.LOGGER.error("Failed to download skin: {}", playerName, e);
                    return null;
                });
    }

    private static Optional<byte[]> downloadSkin(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() == 200) {
                try (InputStream in = conn.getInputStream()) {
                    return Optional.of(in.readAllBytes());
                }
            }
        } catch (Exception e) {
            StaffHelperClient.LOGGER.error("Download error: {}", url, e);
        }
        return Optional.empty();
    }

    private static String fetch(String url) {
        Optional<byte[]> bytes = downloadSkin(url);
        return bytes.map(String::new).orElse(null);
    }

    private static void registerSkin(String playerName, byte[] imageData) {
        MinecraftClient.getInstance().execute(() -> {
            try (InputStream stream = new java.io.ByteArrayInputStream(imageData)) {
                Identifier skinId = Identifier.of("staffhelper", "skin_" + playerName.toLowerCase());
                var nativeImage = net.minecraft.client.texture.NativeImage.read(stream);
                var texture = new net.minecraft.client.texture.NativeImageBackedTexture(nativeImage);

                MinecraftClient.getInstance().getTextureManager().registerTexture(skinId, texture);
                skinCache.put(playerName.toLowerCase(), skinId);
                StaffHelperClient.LOGGER.info("Loaded skin: {}", playerName);
                if (currentCache != null && currentCache.playerName.equals(playerName)) currentCache = null;
            } catch (Exception e) {
                StaffHelperClient.LOGGER.error("Failed to register skin: {}", playerName, e);
            }
        });
    }

    private static PlayerListEntry getPlayerEntry(String playerName) {
        var network = MinecraftClient.getInstance().getNetworkHandler();
        return network != null ? network.getPlayerListEntry(playerName) : null;
    }

    private static boolean isOnline(String playerName) {
        return getPlayerEntry(playerName) != null;
    }

    private static String formatUUID(String uuid) {
        return uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }

    public static void clearCache() {
        currentCache = null;
    }

    public static void clearAllCache() {
        currentCache = null;
        skinCache.clear();
        skinLoadingAttempts.clear();
        StaffHelperClient.LOGGER.info("Cleared skin cache");
    }

    public static boolean isPlayerOnline(String playerName) {
        return isOnline(playerName);
    }
}