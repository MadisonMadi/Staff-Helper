package me.madisonn.staffhelpermod;

import me.madisonn.staffhelpermod.commands.CommandRegistry;
import me.madisonn.staffhelpermod.config.StaffHelperConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class StaffHelperClient implements ClientModInitializer {
    public static StaffHelperConfig CONFIG;

    @Override
    public void onInitializeClient() {
        // MUST BE FIRST
        AutoConfig.register(StaffHelperConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(StaffHelperConfig.class).getConfig();

        CommandRegistry.registerCommands(CONFIG);
    }
}