package me.madisonn.staffhelpermod.utils.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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

    private static final Set<UUID> verifiedUUIDs = new HashSet<>();
    private static final Set<String> pendingVerifications = new HashSet<>();
    private static boolean forceTrustAll;
    private static long forceTrustAllTime;

    private int lastTabCount;

    public SkinViewScreen(String playerName) {
        super(Component.literal("Skin Viewer - " + playerName));
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
        clearWidgets();
        createLabels();
        if (onlinePlayers.size() > 1) addNavigationButtons();
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
    }

    @Override
    public void tick() {
        Minecraft client = Minecraft.getInstance();
        int currentCount = client.getConnection() != null ? client.getConnection().getOnlinePlayers().size() : 0;
        if (currentCount != lastTabCount || (forceTrustAll && System.currentTimeMillis() - forceTrustAllTime > 300_000)) {
            if (forceTrustAll && System.currentTimeMillis() - forceTrustAllTime > 300_000) {
                forceTrustAll = false;
            }
            updateOnlinePlayersList();
            lastTabCount = currentCount;
            clearAndReinitializeButtons();
        }
        updateCurrentPlayerIndex();
        super.tick();
    }

    private void updateOnlinePlayersList() {
        Minecraft client = Minecraft.getInstance();
        if (client.getConnection() == null) {
            this.onlinePlayers = new ArrayList<>();
            return;
        }
        if (forceTrustAll) {
            this.onlinePlayers = client.getConnection().getOnlinePlayers().stream()
                    .map(info -> info.getProfile().name())
                    .sorted(Comparator.comparing(String::toLowerCase))
                    .collect(Collectors.toList());
            return;
        }

        List<PlayerInfo> allEntries = new ArrayList<>(client.getConnection().getOnlinePlayers());
        List<String> verifiedNames = new ArrayList<>();

        for (PlayerInfo entry : allEntries) {
            UUID uuid = entry.getProfile().id();
            String name = entry.getProfile().name();
            if (verifiedUUIDs.contains(uuid)) {
                verifiedNames.add(name);
            } else if (!pendingVerifications.contains(name)) {
                pendingVerifications.add(name);
                startVerification(name, uuid);
            }
        }
        this.onlinePlayers = verifiedNames.stream()
                .sorted(Comparator.comparing(String::toLowerCase))
                .collect(Collectors.toList());
    }

    private void startVerification(String playerName, UUID uuidFromTab) {
        if (forceTrustAll) return;
        CompletableFuture.supplyAsync(() -> {
            String response = fetch("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            if (response == null) return null;
            try {
                JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                if (json.has("id")) {
                    String uuidStr = json.get("id").getAsString()
                            .replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
                    return UUID.fromString(uuidStr);
                }
            } catch (Exception ignored) {}
            return null;
        }).thenAccept(mojangUUID -> {
            pendingVerifications.remove(playerName);
            if (mojangUUID != null && mojangUUID.equals(uuidFromTab)) {
                verifiedUUIDs.add(uuidFromTab);
            }
        }).exceptionally(ignored -> {
            pendingVerifications.remove(playerName);
            forceTrustAll = true;
            forceTrustAllTime = System.currentTimeMillis();
            return null;
        });
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
            if (onlinePlayers.get(i).equalsIgnoreCase(playerName)) return i;
        }
        return -1;
    }

    private void addNavigationButtons() {
        int centerX = this.width / 2;
        int navY = 35;
        int buttonWidth = 95;
        int[] buttonX = {centerX - 100, centerX + 5};
        String[] buttonTexts = {"← Previous", "Next →"};
        Runnable[] buttonActions = { this::navigateToPreviousPlayer, this::navigateToNextPlayer };
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            addRenderableWidget(Button.builder(
                            Component.literal(buttonTexts[i]),
                            ignored -> buttonActions[finalI].run())
                    .bounds(buttonX[i], navY, buttonWidth, 20)
                    .build());
        }
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
        Util.getPlatform().openUri("https://nl.namemc.com/profile/" + currentPlayerName);
    }

    private void toggleSecondLayer(Button button) {
        showSecondLayer = !showSecondLayer;
        button.setMessage(Component.literal("Second Layer: " + (showSecondLayer ? "ON" : "OFF")));
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
        onClose();
    }

    // ---------- Official rendering hook ----------
    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        renderTransparentBackground(graphics);
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int modelX = centerX - MODEL_WIDTH / 2;
        int modelY = centerY - MODEL_HEIGHT / 2;
        updateLabels();
        PlayerModelRenderer.renderPlayerModel(graphics, currentPlayerName,
                modelX, modelY, MODEL_WIDTH, MODEL_HEIGHT, rotation, showSecondLayer);
        // Let Screen draw all buttons and labels
        super.extractRenderState(graphics, mouseX, mouseY, delta);
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

    private void renderTransparentBackground(GuiGraphicsExtractor graphics) {
        graphics.fill(0, 0, this.width, this.height, 0x80000000);
    }

    @Override
    public void onClose() {
        PlayerModelRenderer.clearAllCache();
        super.onClose();
    }

    private static @Nullable String fetch(String url) {
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
}
