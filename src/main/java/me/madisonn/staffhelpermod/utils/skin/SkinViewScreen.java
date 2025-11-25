package me.madisonn.staffhelpermod.utils.skin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class SkinViewScreen extends Screen {
    private final String playerName;
    private float rotation = 0f;
    private boolean isDragging = false;
    private int lastMouseX = 0;
    private boolean showSecondLayer = true;

    public SkinViewScreen(String playerName) {
        super(Text.literal("Skin Viewer - " + playerName));
        this.playerName = playerName;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonSpacing = 5;
        int totalWidth = (buttonWidth * 2) + buttonSpacing;
        int leftButtonX = centerX - totalWidth / 2;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("NameMC"), button ->
                        Util.getOperatingSystem().open("https://nl.namemc.com/profile/" + playerName))
                .dimensions(leftButtonX, this.height - 60, buttonWidth, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")),
                        button -> {
                            showSecondLayer = !showSecondLayer;
                            button.setMessage(Text.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")));
                            PlayerModelRenderer.clearCache();
                        })
                .dimensions(leftButtonX + buttonWidth + buttonSpacing, this.height - 60, buttonWidth, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> closeScreen())
                .dimensions(centerX - 50, this.height - 30, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(context);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        PlayerModelRenderer.renderPlayerModel(context, playerName, centerX, centerY, 60, rotation, showSecondLayer);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Player: " + playerName, this.width / 2, centerY + 70, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Drag to rotate", this.width / 2, centerY + 85, 0xAAAAAA);

        if (!PlayerModelRenderer.isPlayerOnline(playerName)) {
            context.drawCenteredTextWithShadow(this.textRenderer, "Offline Player", this.width / 2, centerY + 100, 0xFF0000);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderTransparentBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, 0x80000000);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            closeScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void closeScreen() {
        PlayerModelRenderer.clearAllCache();
        this.close();
    }

    @Override
    public void close() {
        PlayerModelRenderer.clearAllCache();
        super.close();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (button == 0) {
            this.isDragging = true;
            this.lastMouseX = (int) mouseX;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isDragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging && button == 0) {
            int currentMouseX = (int) mouseX;
            int delta = currentMouseX - this.lastMouseX;
            rotation += delta * 0.5f;
            if (rotation >= 360f) rotation -= 360f;
            if (rotation < 0f) rotation += 360f;
            this.lastMouseX = currentMouseX;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
}