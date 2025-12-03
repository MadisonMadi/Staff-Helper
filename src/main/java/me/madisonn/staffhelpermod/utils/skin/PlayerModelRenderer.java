package me.madisonn.staffhelpermod.utils.skin;

import me.madisonn.staffhelpermod.StaffHelperClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.io.InputStream;
import java.net.HttpURLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class PlayerModelRenderer {
    private static final Logger LOGGER = StaffHelperClient.LOGGER;

    private record PlayerModelCache(FakePlayerEntity fakePlayer, String playerName, boolean showSecondLayer) {}

    private static PlayerModelCache currentCache = null;
    private static final Map<String, Identifier> skinCache = new HashMap<>();
    private static final Map<String, Boolean> skinLoadingAttempts = new HashMap<>();

    public static void renderPlayerModel(DrawContext context, String playerName, int x, int y, int size, float rotation, boolean showSecondLayer) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        try {
            updateCurrentPlayer(client, playerName, showSecondLayer);
            renderEntity(context, client, x, y, size, rotation);
        } catch (Exception e) {
            LOGGER.error("Failed to render player model for: {}", playerName, e);
            context.drawText(client.textRenderer, "3D Model", x, y, 0xFFFFFF, false);
        }
    }

    private static void updateCurrentPlayer(MinecraftClient client, String playerName, boolean showSecondLayer) {
        if (currentCache == null || !currentCache.playerName.equals(playerName)) {
            FakePlayerEntity fakePlayer = createPlayerWithSkin(client, playerName, showSecondLayer);
            currentCache = new PlayerModelCache(fakePlayer, playerName, showSecondLayer);
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
                currentCache.fakePlayer, 0, 0, 0, 180f, matrices, vertexConsumers, LightmapTextureManager.pack(15, 15)
        );

        vertexConsumers.draw();
        matrices.pop();
    }

    private static FakePlayerEntity createPlayerWithSkin(MinecraftClient client, String playerName, boolean showSecondLayer) {
        UUID uuid = getPlayerUUID(client, playerName);
        GameProfile profile = new GameProfile(uuid, playerName);

        loadSkinIfNeeded(client, playerName, uuid);

        FakePlayerEntity fakePlayer = new FakePlayerEntity(client.world, profile, showSecondLayer);
        applyCachedSkin(fakePlayer, playerName);

        return fakePlayer;
    }

    private static UUID getPlayerUUID(MinecraftClient client, String playerName) {
        PlayerListEntry playerEntry = getPlayerListEntry(client, playerName);
        return playerEntry != null ? playerEntry.getProfile().getId() : fetchUUIDFromAPI(playerName);
    }

    private static PlayerListEntry getPlayerListEntry(MinecraftClient client, String playerName) {
        return client.getNetworkHandler() != null ? client.getNetworkHandler().getPlayerListEntry(playerName) : null;
    }

    private static UUID fetchUUIDFromAPI(String playerName) {
        try {
            return getRealUUID(playerName).get();
        } catch (Exception e) {
            LOGGER.warn("Failed to fetch UUID for {}, using offline UUID", playerName, e);
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        }
    }

    private static void loadSkinIfNeeded(MinecraftClient client, String playerName, UUID uuid) {
        if (shouldLoadSkin(playerName)) {
            skinLoadingAttempts.put(playerName.toLowerCase(), true);
            loadSkinFromMinecraftTextureServer(client, playerName, uuid);
        }
    }

    private static boolean shouldLoadSkin(String playerName) {
        return !skinLoadingAttempts.containsKey(playerName.toLowerCase()) &&
                getPlayerListEntry(MinecraftClient.getInstance(), playerName) == null;
    }

    private static void applyCachedSkin(FakePlayerEntity fakePlayer, String playerName) {
        Identifier cachedSkin = skinCache.get(playerName.toLowerCase());
        if (cachedSkin != null) {
            fakePlayer.setCustomSkin(cachedSkin);
        }
    }

    private static CompletableFuture<UUID> getRealUUID(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String apiUrl = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
                HttpURLConnection connection = createConnection(apiUrl);

                if (connection.getResponseCode() == 200) {
                    String response = readResponse(connection);
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();

                    if (json.has("id")) {
                        String uuidStr = formatUUID(json.get("id").getAsString());
                        UUID uuid = UUID.fromString(uuidStr);
                        LOGGER.debug("Found UUID for {}: {}", playerName, uuid);
                        return uuid;
                    }
                }
                LOGGER.debug("No Mojang profile found for: {}", playerName);
            } catch (Exception e) {
                LOGGER.error("Mojang API error for {}", playerName, e);
            }

            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        });
    }

    private static void loadSkinFromMinecraftTextureServer(MinecraftClient client, String playerName, UUID uuid) {
        CompletableFuture.runAsync(() -> {
            try {
                String sessionUrl = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "");
                HttpURLConnection sessionConnection = createConnection(sessionUrl);

                if (sessionConnection.getResponseCode() == 200) {
                    String response = readResponse(sessionConnection);
                    extractAndDownloadSkin(client, playerName, response);
                }
            } catch (Exception e) {
                LOGGER.error("Error loading skin from texture server for {}", playerName, e);
            }
        });
    }

    private static void extractAndDownloadSkin(MinecraftClient client, String playerName, String response) {
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();

        if (json.has("properties")) {
            json.getAsJsonArray("properties").forEach(prop -> {
                JsonObject property = prop.getAsJsonObject();
                if (property.has("name") && "textures".equals(property.get("name").getAsString())) {
                    String textureData = property.get("value").getAsString();
                    String decoded = new String(java.util.Base64.getDecoder().decode(textureData));
                    JsonObject texturesJson = JsonParser.parseString(decoded).getAsJsonObject();

                    if (texturesJson.has("textures")) {
                        JsonObject textures = texturesJson.getAsJsonObject("textures");
                        if (textures.has("SKIN")) {
                            String skinUrl = textures.getAsJsonObject("SKIN").get("url").getAsString();
                            LOGGER.debug("Downloading skin for {} from: {}", playerName, skinUrl);
                            downloadAndRegisterSkin(client, playerName, skinUrl);
                        }
                    }
                }
            });
        }
    }

    private static void downloadAndRegisterSkin(MinecraftClient client, String playerName, String skinUrl) {
        CompletableFuture.supplyAsync(() -> downloadSkinImage(skinUrl))
                .thenAccept(imageData -> imageData.ifPresent(data -> registerSkin(client, playerName, data)))
                .exceptionally(throwable -> {
                    LOGGER.error("Failed to download skin for {}", playerName, throwable);
                    return null;
                });
    }

    private static Optional<byte[]> downloadSkinImage(String skinUrl) {
        try {
            HttpURLConnection connection = createConnection(skinUrl);
            if (connection.getResponseCode() == 200) {
                try (InputStream inputStream = connection.getInputStream()) {
                    return Optional.of(inputStream.readAllBytes());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error downloading skin image", e);
        }
        return Optional.empty();
    }

    private static void registerSkin(MinecraftClient client, String playerName, byte[] imageData) {
        client.execute(() -> {
            try (InputStream imageStream = new java.io.ByteArrayInputStream(imageData)) {
                Identifier skinId = Identifier.of("staffhelper", "skin_" + playerName.toLowerCase());
                var nativeImage = net.minecraft.client.texture.NativeImage.read(imageStream);
                var texture = new net.minecraft.client.texture.NativeImageBackedTexture(nativeImage);

                client.getTextureManager().registerTexture(skinId, texture);
                skinCache.put(playerName.toLowerCase(), skinId);
                LOGGER.info("Successfully loaded skin for: {}", playerName);

                if (currentCache != null && currentCache.playerName().equals(playerName)) {
                    currentCache = null;
                }
            } catch (Exception e) {
                LOGGER.error("Failed to register skin texture for {}", playerName, e);
            }
        });
    }

    private static HttpURLConnection createConnection(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    private static String readResponse(HttpURLConnection connection) throws Exception {
        try (InputStream inputStream = connection.getInputStream()) {
            return new String(inputStream.readAllBytes());
        }
    }

    private static String formatUUID(String uuidStr) {
        return uuidStr.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }

    public static void clearCache() {
        currentCache = null;
    }

    public static void clearAllCache() {
        currentCache = null;
        skinCache.clear();
        skinLoadingAttempts.clear();
        LOGGER.info("Cleared all skin viewer cache");
    }

    public static boolean isPlayerOnline(String playerName) {
        MinecraftClient client = MinecraftClient.getInstance();
        return getPlayerListEntry(client, playerName) != null;
    }
}