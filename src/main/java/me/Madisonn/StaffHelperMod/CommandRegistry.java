package me.Madisonn.StaffHelperMod;

import me.Madisonn.StaffHelperMod.Config.StaffHelperConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class CommandRegistry implements ClientModInitializer {
    public static StaffHelperConfig CONFIG;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(StaffHelperConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(StaffHelperConfig.class).getConfig();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("staffhelp")
                .then(ClientCommandManager.literal("bait")
                        .executes(context -> sendAsPlayer(CONFIG.baitMessage)))

                .then(ClientCommandManager.literal("baitpack")
                        .executes(context -> sendAsPlayer(CONFIG.baitpackMessage)))

                .then(ClientCommandManager.literal("petdrop")
                        .executes(context -> sendAsPlayer(CONFIG.petdropMessage)))

                .then(ClientCommandManager.literal("armor")
                        .executes(context -> sendAsPlayer(CONFIG.armorMessage)))

                .then(ClientCommandManager.literal("variants")
                        .executes(context -> sendAsPlayer(CONFIG.variantsMessage)))

                .then(ClientCommandManager.literal("artisan")
                        .executes(context -> sendAsPlayer(CONFIG.artisanMessage)))

                .then(ClientCommandManager.literal("forge")
                        .executes(context -> sendAsPlayer(CONFIG.forgeMessage)))

                .then(ClientCommandManager.literal("scrapper")
                        .executes(context -> sendAsPlayer(CONFIG.scrapperMessage)))

                .then(ClientCommandManager.literal("auction")
                        .executes(context -> sendAsPlayer(CONFIG.auctionMessage)))

                .then(ClientCommandManager.literal("cosmetics")
                        .executes(context -> sendAsPlayer(CONFIG.cosmeticsMessage)))

                .then(ClientCommandManager.literal("crew")
                        .executes(context -> sendAsPlayer(CONFIG.crewMessage)))

                .then(ClientCommandManager.literal("foe")
                        .executes(context -> sendAsPlayer(CONFIG.foeMessage)))

                .then(ClientCommandManager.literal("luckscaleprospect")
                        .executes(context -> sendAsPlayer(CONFIG.luckscaleprospectMessage)))

                .then(ClientCommandManager.literal("reelbiteline")
                        .executes(context -> sendAsPlayer(CONFIG.reelbitelineMessage)))

                .then(ClientCommandManager.literal("xpmoney")
                        .executes(context -> sendAsPlayer(CONFIG.xpmoneyMessage)))

                .then(ClientCommandManager.literal("howfish")
                        .executes(context -> sendAsPlayer(CONFIG.howfishMessage)))

                .then(ClientCommandManager.literal("tutorial")
                        .executes(context -> sendAsPlayer(CONFIG.tutorialMessage)))

                .then(ClientCommandManager.literal("calibrator")
                        .executes(context -> sendAsPlayer(CONFIG.calibratorMessage)))

                .then(ClientCommandManager.literal("locationroll")
                        .executes(context -> sendAsPlayer(CONFIG.locationrollMessage)))

                .then(ClientCommandManager.literal("quests")
                        .executes(context -> sendAsPlayer(CONFIG.questsMessage)))

                .then(ClientCommandManager.literal("earnmoney")
                        .executes(context -> sendAsPlayer(CONFIG.earnmoneyMessage)))

                .then(ClientCommandManager.literal("wiki")
                        .executes(context -> sendAsPlayer(CONFIG.wikiMessage)))
        ));
    }

    private int sendAsPlayer(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendChatMessage(message);
        }
        return 1;
    }
}