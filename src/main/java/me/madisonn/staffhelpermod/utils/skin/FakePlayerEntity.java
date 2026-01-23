package me.madisonn.staffhelpermod.utils.skin;

import me.madisonn.staffhelpermod.StaffHelperClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.scoreboard.Team;

public class FakePlayerEntity extends OtherClientPlayerEntity {
    private static final byte ALL_LAYERS = (byte) 0x7E;  // 01111110 - No Cape
    private static final byte NO_LAYERS = (byte) 0x00;   // 00000000 - No Cape

    private boolean showSecondLayer;
    private Identifier customSkin;

    public FakePlayerEntity(ClientWorld world, GameProfile profile, boolean showSecondLayer) {
        super(world, profile);
        this.showSecondLayer = showSecondLayer;
        initializeEntity();
        updateSkinLayers();
    }

    private void initializeEntity() {
        this.setYaw(180f);
        this.setBodyYaw(180f);
        this.setHeadYaw(180f);
        this.setSneaking(false);
    }

    public void setShowSecondLayer(boolean show) {
        if (this.showSecondLayer != show) {
            this.showSecondLayer = show;
            updateSkinLayers();
        }
    }

    public void setCustomSkin(Identifier skin) {
        this.customSkin = skin;
    }

    private void updateSkinLayers() {
        try {
            byte skinLayersValue = showSecondLayer ? ALL_LAYERS : NO_LAYERS;
            this.getDataTracker().set(PlayerEntity.PLAYER_MODEL_PARTS, skinLayersValue);
        } catch (Exception e) {
            StaffHelperClient.LOGGER.error("Failed to update skin layers", e);
        }
    }

    @Override
    public SkinTextures getSkinTextures() {
        if (customSkin != null) {
            try {
                return new SkinTextures(customSkin, null, customSkin, null, SkinTextures.Model.WIDE, false);
            } catch (Exception e) {
                StaffHelperClient.LOGGER.error("Failed to create custom skin textures", e);
            }
        }
        return super.getSkinTextures();
    }

    @Override public boolean shouldRenderName() { return false; }
    @Override public Text getName() { return Text.empty(); }
    @Override public Text getDisplayName() { return Text.empty(); }
    @Override public boolean isCustomNameVisible() { return false; }
    @Override public boolean hasCustomName() { return false; }
    @Override public Text getCustomName() { return null; }
    @Override public Team getScoreboardTeam() { return null; }
    @Override public boolean isSpectator() { return false; }
    @Override public boolean isCreative() { return true; }
    @Override public boolean shouldRender(double distance) { return true; }
    @Override public boolean isInvisible() { return false; }
}