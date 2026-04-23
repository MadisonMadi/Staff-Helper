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
    private static final Map<String, CompletableFuture<Optional<PlayerSkin>>> skinFutures = new HashMap<>();

    public static void renderPlayerModel(GuiGraphics graphics, String playerName,
                                         int x, int y, int width, int height,
                                         float rotationY, boolean showOverlay) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        if (wideModel == null) {
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

        PlayerSkin skin = getPlayerSkin(playerName, client);
        PlayerModel model = (showOverlay)
                ? (skin.model() == PlayerModelType.SLIM ? slimModel : wideModel)
                : (skin.model() == PlayerModelType.SLIM ? slimModelNoOverlay : wideModelNoOverlay);
        Identifier textureId = skin.body().texturePath();

        float modelHeight = 2.125F;
        float fitScale = 0.97F * (float)height / modelHeight;
        float pivotY = -1.0625F;
        float defaultRotX = -5.0F;

        graphics.submitSkinRenderState(
                model, textureId, fitScale, defaultRotX, rotationY, pivotY,
                x, y, x + width, y + height
        );
    }

    private static PlayerSkin getPlayerSkin(String playerName, Minecraft client) {
        PlayerInfo playerInfo = client.getConnection() != null
                ? client.getConnection().getPlayerInfo(playerName) : null;
        if (playerInfo != null) return playerInfo.getSkin();

        UUID uuid = getUUID(playerName);
        CompletableFuture<Optional<PlayerSkin>> future = skinFutures.get(playerName.toLowerCase());
        if (future == null || future.isDone()) {
            GameProfile profile = new GameProfile(uuid, playerName);
            future = client.getSkinManager().get(profile);
            skinFutures.put(playerName.toLowerCase(), future);
        }
        Optional<PlayerSkin> opt = future.getNow(null);
        return (opt != null && opt.isPresent()) ? opt.get() : DefaultPlayerSkin.get(uuid);
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

    private static String fetch(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
            conn.setRequestMethod("GET"); conn.setConnectTimeout(5000); conn.setReadTimeout(5000);
            if (conn.getResponseCode() == 200) {
                try (InputStream in = conn.getInputStream()) {
                    return new String(in.readAllBytes());
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static PlayerInfo getPlayerEntry(String playerName) {
        var connection = Minecraft.getInstance().getConnection();
        return connection != null ? connection.getPlayerInfo(playerName) : null;
    }

    public static void clearCache() {}
    public static void clearAllCache() {
        skinFutures.clear();
        wideModel = null; slimModel = null;
        wideModelNoOverlay = null; slimModelNoOverlay = null;
    }
    public static boolean isPlayerOnline(String playerName) {
        return getPlayerEntry(playerName) != null;
    }
}
