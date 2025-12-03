package me.madisonn.staffhelpermod;

import me.madisonn.staffhelpermod.commands.CommandRegistry;
import me.madisonn.staffhelpermod.config.StaffHelperConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffHelperClient implements ClientModInitializer {
    public static final String MOD_ID = "staffhelper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static StaffHelperConfig CONFIG;

    @Override
    public void onInitializeClient() {
        // MUST BE FIRST
        AutoConfig.register(StaffHelperConfig.class, GsonConfigSerializer::new);
        ConfigHolder<StaffHelperConfig> configHolder = AutoConfig.getConfigHolder(StaffHelperConfig.class);
        CONFIG = configHolder.getConfig();

        // Register commands initially
        CommandRegistry.registerCommands(CONFIG);

        // Auto reload function + relog
        configHolder.registerSaveListener((holder, newConfig) -> {
            CONFIG = newConfig;
            if (net.minecraft.client.MinecraftClient.getInstance().player != null) {
                net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("StaffHelper » Command updated, disconnect and re-join!").withColor(0xFFFF00),
                        false
                );
            }
            return null;
        });
    }
}