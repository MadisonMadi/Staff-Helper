package me.madisonn.staffhelpermod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.madisonn.staffhelpermod.commands.CommandRegistry;
import me.madisonn.staffhelpermod.config.StaffHelperConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffHelperClient implements ClientModInitializer {
    public static final String MOD_ID = "staffhelper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static StaffHelperConfig CONFIG;

    @Override
    public void onInitializeClient() {
        // MUST BE FIRST
        Gson newGson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        AutoConfig.register(StaffHelperConfig.class, (definition, configClass) -> new GsonConfigSerializer<>(definition, configClass, newGson));
        CONFIG = AutoConfig.getConfigHolder(StaffHelperConfig.class).getConfig();

        CommandRegistry.registerCommands(CONFIG);
    }
}