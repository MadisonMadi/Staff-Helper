package me.madisonn.staffhelpermod.utils.skin;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.text.Text;
import net.minecraft.scoreboard.Team;

public class FakePlayerEntity extends OtherClientPlayerEntity {
    private static final TrackedData<Byte> SKIN_LAYERS_FIELD = initializeSkinLayersField();
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

    @SuppressWarnings("unchecked")
    private static TrackedData<Byte> initializeSkinLayersField() {
        try {
            java.lang.reflect.Field field = net.minecraft.entity.player.PlayerEntity.class.getDeclaredField("field_7518");
            field.setAccessible(true);
            return (TrackedData<Byte>) field.get(null);
        } catch (Exception e) {
            System.err.println("Failed to initialize skin layers field: " + e.getMessage());
            return null;
        }
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
        if (SKIN_LAYERS_FIELD == null) return;

        try {
            byte skinLayersValue = showSecondLayer ? (byte) 0xFE : (byte) 0x00;
            this.getDataTracker().set(SKIN_LAYERS_FIELD, skinLayersValue);
        } catch (Exception e) {
            System.err.println("Failed to update skin layers: " + e.getMessage());
        }
    }

    @Override
    public SkinTextures getSkinTextures() {
        if (customSkin != null) {
            try {
                return new SkinTextures(customSkin, null, customSkin, null, SkinTextures.Model.WIDE, false);
            } catch (Exception e) {
                System.err.println("Failed to create custom skin textures: " + e.getMessage());
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