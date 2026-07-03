package madisonn.staffhelpermod.config;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;

public class Configs {
    public static final MessagesConfig messages =
            ConfigApiJava.registerAndLoadConfig(MessagesConfig::new, RegisterType.CLIENT);

    public static void init() {}
}
