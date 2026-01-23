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
    private static String currentCommand; // Track current command

    @Override
    public void onInitializeClient() {
        // MUST BE FIRST
        AutoConfig.register(StaffHelperConfig.class, GsonConfigSerializer::new);
        ConfigHolder<StaffHelperConfig> configHolder = AutoConfig.getConfigHolder(StaffHelperConfig.class);
        CONFIG = configHolder.getConfig();

        currentCommand = CONFIG.commands.mainCommand;

        CommandRegistry.registerCommands(CONFIG);

        configHolder.registerSaveListener((holder, newConfig) -> {
            boolean commandChanged = !currentCommand.equals(newConfig.commands.mainCommand);

            if (commandChanged) {
                currentCommand = newConfig.commands.mainCommand;

                if (net.minecraft.client.MinecraftClient.getInstance().player != null) {
                    net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("StaffHelper » Main command has changed to '" +
                                    newConfig.commands.mainCommand + "'. Disconnect and re-join!").withColor(0xFFFF00),
                            false
                    );
                }
            }

            CONFIG = newConfig;
            CommandRegistry.registerCommands(CONFIG);

            return null;
        });
    }
}