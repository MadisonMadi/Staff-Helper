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
        addControlButtons();
    }

    private void addControlButtons() {
        int centerX = this.width / 2;
        int buttonY = this.height - 60;
        int closeButtonY = this.height - 30;

        int buttonWidth = 100;
        int buttonSpacing = 5;
        int totalWidth = (buttonWidth * 2) + buttonSpacing;
        int leftButtonX = centerX - totalWidth / 2;

        addDrawableChild(createNameMCButton(leftButtonX, buttonY));
        addDrawableChild(createSecondLayerButton(leftButtonX + buttonWidth + buttonSpacing, buttonY));
        addDrawableChild(createCloseButton(centerX - 50, closeButtonY));
    }

    private ButtonWidget createNameMCButton(int x, int y) {
        return ButtonWidget.builder(Text.literal("NameMC"),
                        button -> openNameMC())
                .dimensions(x, y, 100, 20)
                .build();
    }

    private ButtonWidget createSecondLayerButton(int x, int y) {
        return ButtonWidget.builder(
                        Text.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")),
                        this::toggleSecondLayer)
                .dimensions(x, y, 100, 20)
                .build();
    }

    private ButtonWidget createCloseButton(int x, int y) {
        return ButtonWidget.builder(Text.literal("Close"),
                        button -> closeScreen())
                .dimensions(x, y, 100, 20)
                .build();
    }

    private void openNameMC() {
        String nameMCUrl = "https://nl.namemc.com/profile/" + playerName;
        Util.getOperatingSystem().open(nameMCUrl);
    }

    private void toggleSecondLayer(ButtonWidget button) {
        showSecondLayer = !showSecondLayer;
        button.setMessage(Text.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")));
        PlayerModelRenderer.clearCache();
    }

    private void closeScreen() {
        PlayerModelRenderer.clearAllCache();
        close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(context);
        renderPlayerModel(context);
        renderTextInfo(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderTransparentBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, 0x80000000);
    }

    private void renderPlayerModel(DrawContext context) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        PlayerModelRenderer.renderPlayerModel(context, playerName, centerX, centerY, 60, rotation, showSecondLayer);
    }

    private void renderTextInfo(DrawContext context) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, 10, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Player: " + playerName, centerX, centerY + 70, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Drag to rotate", centerX, centerY + 85, 0xAAAAAA);

        if (!PlayerModelRenderer.isPlayerOnline(playerName)) {
            context.drawCenteredTextWithShadow(this.textRenderer, "Offline Player", centerX, centerY + 100, 0xFF0000);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            closeScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
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
            updateRotation(mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateRotation(double mouseX) {
        int currentMouseX = (int) mouseX;
        int delta = currentMouseX - this.lastMouseX;
        rotation = (rotation + delta * 0.5f) % 360f;
        this.lastMouseX = currentMouseX;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
}