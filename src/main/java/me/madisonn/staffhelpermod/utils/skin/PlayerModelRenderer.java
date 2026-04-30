package me.madisonn.staffhelpermod.utils.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerModelRenderer {
    private static PlayerModel wideModel;
    private static PlayerModel slimModel;
    private static PlayerModel wideModelNoOverlay;
    private static PlayerModel slimModelNoOverlay;

    private static final Map<String, Identifier> skinCache = new HashMap<>();
    private static final Map<String, Boolean> skinSlimCache = new HashMap<>();
    private static final Set<String> skinLoadingAttempts = new HashSet<>();
    private static final Map<String, CompletableFuture<Optional<PlayerSkin>>> skinFutures = new HashMap<>();

    public static void renderPlayerModel(GuiGraphics graphics, String playerName,
                                         int x, int y, int width, int height,
                                         float rotationY, boolean showOverlay) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        bakeModelsOnce(client);
        PlayerSkinInfo skinInfo = getSkinInfo(playerName, client);
        PlayerModel model = chooseModel(skinInfo.slim, showOverlay);
        Identifier textureId = skinInfo.textureId;

        float modelHeight = 2.125F;
        float fitScale = 0.97F * (float)height / modelHeight;
        float pivotY = -1.0625F;
        float defaultRotX = -5.0F;

        graphics.submitSkinRenderState(
                model, textureId, fitScale, defaultRotX, rotationY, pivotY,
                x, y, x + width, y + height
        );
    }

    private static void bakeModelsOnce(Minecraft client) {
        if (wideModel != null) return;
        EntityModelSet entityModels = client.getEntityModels();
        wideModel = new PlayerModel(entityModels.bakeLayer(ModelLayers.PLAYER), false);
        slimModel = new PlayerModel(entityModels.bakeLayer(ModelLayers.PLAYER_SLIM), true);

        wideModelNoOverlay = new PlayerModel(entityModels.bakeLayer(ModelLayers.PLAYER), false);
        wideModelNoOverlay.jacket.visible = false;
        wideModelNoOverlay.hat.visible = false;
        wideModelNoOverlay.leftSleeve.visible = false;
        wideModelNoOverlay.rightSleeve.visible = false;
        wideModelNoOverlay.leftPants.visible = false;
        wideModelNoOverlay.rightPants.visible = false;

        slimModelNoOverlay = new PlayerModel(entityModels.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        slimModelNoOverlay.jacket.visible = false;
        slimModelNoOverlay.hat.visible = false;
        slimModelNoOverlay.leftSleeve.visible = false;
        slimModelNoOverlay.rightSleeve.visible = false;
        slimModelNoOverlay.leftPants.visible = false;
        slimModelNoOverlay.rightPants.visible = false;
    }

    private static PlayerModel chooseModel(boolean slim, boolean showOverlay) {
        if (showOverlay) {
            return slim ? slimModel : wideModel;
        } else {
            return slim ? slimModelNoOverlay : wideModelNoOverlay;
        }
    }

    private record PlayerSkinInfo(Identifier textureId, boolean slim) {}

    private static PlayerSkinInfo getSkinInfo(String playerName, Minecraft client) {
        PlayerInfo playerInfo = client.getConnection() != null
                ? client.getConnection().getPlayerInfo(playerName) : null;
        if (playerInfo != null) {
            PlayerSkin skin = playerInfo.getSkin();
            return new PlayerSkinInfo(skin.body().texturePath(), skin.model() == PlayerModelType.SLIM);
        }

        Identifier customId = skinCache.get(playerName.toLowerCase());
        if (customId != null) {
            boolean slim = skinSlimCache.getOrDefault(playerName.toLowerCase(), false);
            return new PlayerSkinInfo(customId, slim);
        }

        if (!skinLoadingAttempts.contains(playerName.toLowerCase())) {
            skinLoadingAttempts.add(playerName.toLowerCase());
            UUID uuid = getUUID(playerName);
            loadSkin(playerName, uuid);
        }

        UUID uuid = getUUID(playerName);
        CompletableFuture<Optional<PlayerSkin>> future = skinFutures.get(playerName.toLowerCase());
        if (future == null) {
            GameProfile profile = new GameProfile(uuid, playerName);
            future = client.getSkinManager().get(profile);
            skinFutures.put(playerName.toLowerCase(), future);
        }
        Optional<PlayerSkin> opt = future.getNow(null);
        PlayerSkin fallback = (opt != null && opt.isPresent()) ? opt.get() : DefaultPlayerSkin.get(uuid);
        return new PlayerSkinInfo(fallback.body().texturePath(), fallback.model() == PlayerModelType.SLIM);
    }

    private static UUID getUUID(String playerName) {
        PlayerInfo entry = getPlayerEntry(playerName);
        if (entry != null) return entry.getProfile().id();
        try { return fetchUUID(playerName).get(); } catch (Exception e) {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        }
    }

    private static CompletableFuture<UUID> fetchUUID(String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            String response = fetch("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            if (response == null) return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
            try {
                JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                if (json.has("id")) {
                    String uuidStr = json.get("id").getAsString()
                            .replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                    return UUID.fromString(uuidStr);
                }
            } catch (Exception ignored) {}
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
        });
    }

    private static void loadSkin(String playerName, UUID uuid) {
        CompletableFuture.runAsync(() -> {
            try {
                String response = fetch("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
                extractAndDownloadSkin(playerName, response);
            } catch (Exception ignored) {}
        });
    }

    private static void extractAndDownloadSkin(String playerName, String response) {
        if (response == null) return;
        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        if (json.has("properties")) {
            json.getAsJsonArray("properties").forEach(prop -> {
                JsonObject p = prop.getAsJsonObject();
                if ("textures".equals(p.get("name").getAsString())) {
                    String decoded = new String(Base64.getDecoder().decode(p.get("value").getAsString()));
                    JsonObject tex = JsonParser.parseString(decoded).getAsJsonObject();
                    if (tex.has("textures")) {
                        JsonObject skins = tex.getAsJsonObject("textures");
                        if (skins.has("SKIN")) {
                            String url = skins.getAsJsonObject("SKIN").get("url").getAsString();
                            downloadAndRegisterSkin(playerName, url);
                        }
                    }
                }
            });
        }
    }

    private static void downloadAndRegisterSkin(String playerName, String skinUrl) {
        CompletableFuture.supplyAsync(() -> downloadSkin(skinUrl))
                .thenAccept(bytes -> bytes.ifPresent(data -> registerSkin(playerName, data)));
    }

    private static Optional<byte[]> downloadSkin(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
            conn.setRequestMethod("GET"); conn.setConnectTimeout(5000); conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                try (InputStream in = conn.getInputStream()) {
                    return Optional.of(in.readAllBytes());
                }
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    private static String fetch(String url) {
        Optional<byte[]> bytes = downloadSkin(url);
        return bytes.map(String::new).orElse(null);
    }

    private static void registerSkin(String playerName, byte[] imageData) {
        Minecraft.getInstance().execute(() -> {
            try (InputStream stream = new java.io.ByteArrayInputStream(imageData)) {
                Identifier skinId = Identifier.fromNamespaceAndPath("staffhelper", "skin_" + playerName.toLowerCase());
                var nativeImage = com.mojang.blaze3d.platform.NativeImage.read(stream);
                int pixel = nativeImage.getPixel(47, 20);
                boolean slim = ((pixel >> 24) & 0xFF) == 0;
                skinSlimCache.put(playerName.toLowerCase(), slim);
                var texture = new DynamicTexture(() -> "Skin for " + playerName, nativeImage);
                Minecraft.getInstance().getTextureManager().register(skinId, texture);
                skinCache.put(playerName.toLowerCase(), skinId);
            } catch (Exception ignored) {}
        });
    }

    private static PlayerInfo getPlayerEntry(String playerName) {
        var connection = Minecraft.getInstance().getConnection();
        return connection != null ? connection.getPlayerInfo(playerName) : null;
    }

    public static void clearCache() {}

    public static void clearAllCache() {
        skinFutures.clear();
        skinCache.clear();
        skinSlimCache.clear();
        skinLoadingAttempts.clear();
        wideModel = null; slimModel = null;
        wideModelNoOverlay = null; slimModelNoOverlay = null;
    }

    public static boolean isPlayerOnline(String playerName) {
        return getPlayerEntry(playerName) != null;
    }
}
