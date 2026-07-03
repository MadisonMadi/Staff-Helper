package madisonn.staffhelpermod;

import madisonn.staffhelpermod.commands.CommandRegistry;
import madisonn.staffhelpermod.config.Configs;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaffHelperClient implements ClientModInitializer {
    public static final String MOD_ID = "staffhelpermod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        Configs.init();
        CommandRegistry.registerCommands();
    }
}