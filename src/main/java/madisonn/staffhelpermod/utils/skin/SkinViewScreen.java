package madisonn.staffhelpermod.utils.skin;

import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.stream.Collectors;

@NullMarked
public class SkinViewScreen extends Screen {
    private String currentPlayerName;
    private float rotation;
    private boolean isDragging;
    private double lastMouseX;
    private boolean showSecondLayer = true;

    private List<String> onlinePlayers = new ArrayList<>();
    private int currentPlayerIndex;

    private static final int MODEL_WIDTH = 120;
    private static final int MODEL_HEIGHT = 120;

    private @Nullable Button titleLabel;
    private @Nullable Button counterLabel;
    private @Nullable Button dragLabel;
    private @Nullable Button offlineLabel;
    private @Nullable Button prevButton;
    private @Nullable Button nextButton;

    public SkinViewScreen(String playerName) {
        super(Component.literal("Skin Viewer - " + playerName));
        this.currentPlayerName = playerName;
        updateOnlinePlayersList();
        this.currentPlayerIndex = findPlayerIndex(playerName);
    }

    @Override
    protected void init() {
        super.init();
        createLabels();
        addNavigationButtons();
        addControlButtons();
    }

    private void createLabels() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        titleLabel = createFixedLabel("Skin Viewer - " + currentPlayerName, centerX, 10, 200);
        addRenderableWidget(titleLabel);
        counterLabel = createFixedLabel("", centerX, 60, 80);
        counterLabel.visible = false;
        addRenderableWidget(counterLabel);
        dragLabel = createFixedLabel("Drag to rotate", centerX, centerY + MODEL_HEIGHT / 2 + 30, 120);
        addRenderableWidget(dragLabel);
        offlineLabel = createFixedLabel("Offline Player", centerX, centerY + MODEL_HEIGHT / 2 + 10, 150);
        offlineLabel.visible = false;
        addRenderableWidget(offlineLabel);
        updateLabels();
    }

    private Button createFixedLabel(String text, int centerX, int y, int width) {
        Button label = Button.builder(Component.literal(text), ignored -> {})
                .bounds(0, 0, width, 15)
                .build();
        label.active = false;
        label.setX(centerX - width / 2);
        label.setY(y);
        return label;
    }

    private void updateLabels() {
        if (titleLabel != null) titleLabel.setMessage(Component.literal("Skin Viewer - " + currentPlayerName));
        boolean multi = onlinePlayers.size() > 1;
        if (counterLabel != null) {
            counterLabel.visible = multi;
            if (multi) {
                String counterText = "(" + (currentPlayerIndex + 1) + "/" + onlinePlayers.size() + ")";
                counterLabel.setMessage(Component.literal(counterText));
            }
        }
        if (dragLabel != null) dragLabel.visible = true;
        boolean offline = !PlayerModelRenderer.isPlayerOnline(currentPlayerName);
        if (offlineLabel != null) {
            offlineLabel.visible = offline;
            if (offline) offlineLabel.setMessage(Component.literal("Offline Player"));
        }
        updateNavigationButtonsState();
    }

    private void updateNavigationButtonsState() {
        boolean visible = onlinePlayers.size() > 1;
        if (prevButton != null) {
            prevButton.visible = visible;
            prevButton.active = visible;
        }
        if (nextButton != null) {
            nextButton.visible = visible;
            nextButton.active = visible;
        }
    }

    @Override
    public void tick() {
        Minecraft client = Minecraft.getInstance();
        int currentCount = client.getConnection() != null ? client.getConnection().getOnlinePlayers().size() : 0;
        if (currentCount != onlinePlayers.size()) {
            updateOnlinePlayersList();
            syncCurrentPlayerIndex();
            updateLabels();
        }
        super.tick();
    }

    private void syncCurrentPlayerIndex() {
        int newIndex = findPlayerIndex(currentPlayerName);
        if (newIndex >= 0) {
            currentPlayerIndex = newIndex;
        } else if (!onlinePlayers.isEmpty()) {
            currentPlayerIndex = 0;
            currentPlayerName = onlinePlayers.getFirst();
        } else {
            currentPlayerIndex = -1;
        }
    }

    private void updateOnlinePlayersList() {
        Minecraft client = Minecraft.getInstance();
        if (client.getConnection() == null) {
            this.onlinePlayers = new ArrayList<>();
            return;
        }
        this.onlinePlayers = client.getConnection().getOnlinePlayers().stream()
                .map(info -> info.getProfile().name())
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.toList());
    }

    private int findPlayerIndex(String playerName) {
        for (int i = 0; i < onlinePlayers.size(); i++) {
            if (onlinePlayers.get(i).equalsIgnoreCase(playerName)) return i;
        }
        return -1;
    }

    private void addNavigationButtons() {
        int centerX = this.width / 2;
        int navY = 35;
        int buttonWidth = 95;
        int leftX = centerX - 100;
        int rightX = centerX + 5;

        prevButton = Button.builder(Component.literal("← Previous"), ignored -> navigateToPreviousPlayer())
                .bounds(leftX, navY, buttonWidth, 20)
                .build();
        nextButton = Button.builder(Component.literal("Next →"), ignored -> navigateToNextPlayer())
                .bounds(rightX, navY, buttonWidth, 20)
                .build();
        addRenderableWidget(prevButton);
        addRenderableWidget(nextButton);
        updateNavigationButtonsState();
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
        currentPlayerName = playerName;
        updateLabels();
    }

    private void addControlButtons() {
        int centerX = this.width / 2;
        int buttonY = this.height - 60;
        int closeButtonY = this.height - 30;
        int buttonWidth = 100;

        Button nameMCButton = createButton("NameMC", centerX - 102, buttonY, buttonWidth, ignored -> openNameMC());
        Button layerButton = createButton("Second Layer: " + (showSecondLayer ? "ON" : "OFF"),
                centerX + 2, buttonY, buttonWidth, this::toggleSecondLayer);
        Button closeButton = createButton("Close", centerX - 50, closeButtonY, buttonWidth, ignored -> closeScreen());

        addRenderableWidget(nameMCButton);
        addRenderableWidget(layerButton);
        addRenderableWidget(closeButton);
    }

    private Button createButton(String text, int x, int y, int width, Button.OnPress action) {
        return Button.builder(Component.literal(text), action).bounds(x, y, width, 20).build();
    }

    private void openNameMC() {
        Util.getPlatform().openUri("https://namemc.com/profile/" + currentPlayerName);
    }

    private void toggleSecondLayer(Button button) {
        showSecondLayer = !showSecondLayer;
        button.setMessage(Component.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")));
    }

    private void closeScreen() {
        PlayerModelRenderer.clearAllCache();
        onClose();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(guiGraphicsExtractor);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int modelX = centerX - MODEL_WIDTH / 2;
        int modelY = centerY - MODEL_HEIGHT / 2;
        updateLabels();
        PlayerModelRenderer.renderPlayerModel(guiGraphicsExtractor, currentPlayerName,
                modelX, modelY, MODEL_WIDTH, MODEL_HEIGHT, rotation, showSecondLayer);
        super.extractRenderState(guiGraphicsExtractor, mouseX, mouseY, delta);
    }

    private void renderTransparentBackground(GuiGraphicsExtractor guiGraphicsExtractor) {
        guiGraphicsExtractor.fill(0, 0, this.width, this.height, 0x80000000);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.key();
        if (key == GLFW.GLFW_KEY_ESCAPE) { closeScreen(); return true; }
        if (onlinePlayers.size() > 1) {
            if (key == GLFW.GLFW_KEY_LEFT) { navigateToPreviousPlayer(); return true; }
            if (key == GLFW.GLFW_KEY_RIGHT) { navigateToNextPlayer(); return true; }
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean dragged) {
        if (super.mouseClicked(event, dragged)) return true;
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            isDragging = true; lastMouseX = event.x(); return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) isDragging = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (isDragging && event.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            updateRotation(event.x()); return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    private void updateRotation(double mouseX) {
        double delta = mouseX - lastMouseX;
        rotation = (rotation + (float)(delta * 0.5)) % 360f;
        lastMouseX = mouseX;
    }

    @Override
    public void onClose() {
        PlayerModelRenderer.clearAllCache();
        super.onClose();
    }
}
