package me.madisonn.staffhelpermod.utils.skin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class SkinViewScreen extends Screen {
    private String currentPlayerName;
    private float rotation = 0f;
    private boolean isDragging = false;
    private int lastMouseX = 0;
    private boolean showSecondLayer = true;

    private List<String> onlinePlayers = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public SkinViewScreen(String playerName) {
        super(Text.literal("Skin Viewer - " + playerName));
        this.currentPlayerName = playerName;
        updateOnlinePlayersList();
        this.currentPlayerIndex = findPlayerIndex(playerName);
    }

    @Override
    protected void init() {
        super.init();
        clearAndReinitializeButtons();
    }

    private void clearAndReinitializeButtons() {
        clearChildren();

        if (onlinePlayers.size() > 1) {
            addNavigationButtons();
        }

        addControlButtons();
    }

    @Override
    public void tick() {
        updateOnlinePlayersList();
        updateCurrentPlayerIndex();
        super.tick();
    }

    private void updateOnlinePlayersList() {
        MinecraftClient client = MinecraftClient.getInstance();
        this.onlinePlayers = client.getNetworkHandler() != null
                ? client.getNetworkHandler().getPlayerList()
                .stream()
                .map(entry -> entry.getProfile().getName())
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    private void updateCurrentPlayerIndex() {
        int newIndex = findPlayerIndex(currentPlayerName);
        if (newIndex >= 0 && newIndex < onlinePlayers.size()) {
            currentPlayerIndex = newIndex;
        } else if (!onlinePlayers.isEmpty()) {
            currentPlayerIndex = Math.min(currentPlayerIndex, onlinePlayers.size() - 1);
        }
    }

    private int findPlayerIndex(String playerName) {
        for (int i = 0; i < onlinePlayers.size(); i++) {
            if (onlinePlayers.get(i).equalsIgnoreCase(playerName)) {
                return i;
            }
        }
        return -1;
    }

    private void addNavigationButtons() {
        int centerX = this.width / 2;
        int navY = 35;
        int buttonWidth = 95;

        int[] buttonX = {centerX - 100, centerX + 5};
        String[] buttonTexts = {"← Previous", "Next →"};
        Runnable[] buttonActions = {
                this::navigateToPreviousPlayer,
                this::navigateToNextPlayer
        };

        for (int i = 0; i < 2; i++) {
            int finalI = i;
            addDrawableChild(ButtonWidget.builder(
                            Text.literal(buttonTexts[i]),
                            button -> buttonActions[finalI].run())
                    .dimensions(buttonX[i], navY, buttonWidth, 20)
                    .build());
        }
    }

    private void addControlButtons() {
        int centerX = this.width / 2;
        int buttonY = this.height - 60;
        int closeButtonY = this.height - 30;
        int buttonWidth = 100;

        ButtonWidget nameMCButton = createButton("NameMC",
                centerX - 102, buttonY, buttonWidth,
                button -> openNameMC());

        ButtonWidget layerButton = createButton("Second Layer: " + (showSecondLayer ? "ON" : "OFF"),
                centerX + 2, buttonY, buttonWidth,
                this::toggleSecondLayer);

        ButtonWidget closeButton = createButton("Close",
                centerX - 50, closeButtonY, buttonWidth,
                button -> closeScreen());

        addDrawableChild(nameMCButton);
        addDrawableChild(layerButton);
        addDrawableChild(closeButton);
    }

    private ButtonWidget createButton(String text, int x, int y, int width, ButtonWidget.PressAction action) {
        return ButtonWidget.builder(Text.literal(text), action)
                .dimensions(x, y, width, 20)
                .build();
    }

    private void openNameMC() {
        String nameMCUrl = "https://nl.namemc.com/profile/" + currentPlayerName;
        Util.getOperatingSystem().open(nameMCUrl);
    }

    private void toggleSecondLayer(ButtonWidget button) {
        showSecondLayer = !showSecondLayer;
        button.setMessage(Text.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")));
        PlayerModelRenderer.clearCache();
    }

    private void navigateToPreviousPlayer() {
        if (onlinePlayers.isEmpty()) return;

        currentPlayerIndex = (currentPlayerIndex - 1 + onlinePlayers.size()) % onlinePlayers.size();
        switchToPlayer(onlinePlayers.get(currentPlayerIndex));
    }

    private void navigateToNextPlayer() {
        if (onlinePlayers.isEmpty()) return;

        currentPlayerIndex = (currentPlayerIndex + 1) % onlinePlayers.size();
        switchToPlayer(onlinePlayers.get(currentPlayerIndex));
    }

    private void switchToPlayer(String playerName) {
        PlayerModelRenderer.clearCache();
        currentPlayerName = playerName;
        clearAndReinitializeButtons();
    }

    private void closeScreen() {
        PlayerModelRenderer.clearAllCache();
        close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(context);
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        PlayerModelRenderer.renderPlayerModel(context, currentPlayerName, centerX, centerY, 60, rotation, showSecondLayer);

        context.drawCenteredTextWithShadow(this.textRenderer, "Skin Viewer - " + currentPlayerName, centerX, 10, 0xFFFFFF);

        if (onlinePlayers.size() > 1) {
            String counterText = String.format("(%d/%d)", currentPlayerIndex + 1, onlinePlayers.size());
            context.drawCenteredTextWithShadow(this.textRenderer, counterText, centerX, 60, 0xAAAAAA);
        }

        int infoY = centerY + 70;
        context.drawCenteredTextWithShadow(this.textRenderer, "Player: " + currentPlayerName,
                centerX, infoY, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Drag to rotate",
                centerX, infoY + 15, 0xAAAAAA);

        if (!PlayerModelRenderer.isPlayerOnline(currentPlayerName)) {
            context.drawCenteredTextWithShadow(this.textRenderer, "Offline Player",
                    centerX, infoY + 30, 0xFFAA00);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            closeScreen();
            return true;
        }

        if (onlinePlayers.size() > 1) {
            switch (keyCode) {
                case 263: // LEFT ARROW
                    navigateToPreviousPlayer();
                    return true;
                case 262: // RIGHT ARROW
                    navigateToNextPlayer();
                    return true;
            }
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

    private void renderTransparentBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, 0x80000000);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
}