package me.madisonn.staffhelpermod.utils.skin;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.Identifier;
import net.minecraft.client.util.SkinTextures;

public class FakePlayerEntity extends OtherClientPlayerEntity {
    private static TrackedData<Byte> SKIN_LAYERS_FIELD = null;
    private boolean showSecondLayer = true;
    private Identifier customSkin = null;

    public FakePlayerEntity(ClientWorld world, GameProfile profile, boolean showSecondLayer) {
        super(world, profile);

        this.showSecondLayer = showSecondLayer;

        this.setYaw(180f);
        this.setBodyYaw(180f);
        this.setHeadYaw(180f);
        this.setSneaking(false);

        updateSkinLayers();
    }

    public void setShowSecondLayer(boolean show) {
        if (this.showSecondLayer != show) {
            this.showSecondLayer = show;
            updateSkinLayers();
        }
    }

    public void setCustomSkin(Identifier skin) {
        this.customSkin = skin;
        // Removed debug print to reduce console spam
    }

    private void updateSkinLayers() {
        try {
            if (SKIN_LAYERS_FIELD == null) {
                java.lang.reflect.Field field = net.minecraft.entity.player.PlayerEntity.class.getDeclaredField("field_7518");
                field.setAccessible(true);
                SKIN_LAYERS_FIELD = (TrackedData<Byte>) field.get(null);
            }

            byte skinLayersValue = showSecondLayer ? (byte) 0xFE : (byte) 0x00;
            this.getDataTracker().set(SKIN_LAYERS_FIELD, skinLayersValue);

        } catch (Exception e) {
            System.out.println("Failed to update skin layers: " + e.getMessage());
        }
    }

    // Override to use custom skin if available
    @Override
    public SkinTextures getSkinTextures() {
        if (customSkin != null) {
            return new SkinTextures(
                    customSkin,
                    null,        // textureUrl
                    customSkin,  // cape texture
                    null,        // elytra texture
                    SkinTextures.Model.WIDE,
                    false
            );
        }
        return super.getSkinTextures();
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return true;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }
}