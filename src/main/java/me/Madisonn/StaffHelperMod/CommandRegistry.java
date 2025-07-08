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

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal(CONFIG.commands.mainCommand)
                .then(ClientCommandManager.literal("bait")
                        .executes(context -> sendAsPlayer(CONFIG.messages.baitMessage)))

                .then(ClientCommandManager.literal("baitpack")
                        .executes(context -> sendAsPlayer(CONFIG.messages.baitpackMessage)))

                .then(ClientCommandManager.literal("petdrop")
                        .executes(context -> sendAsPlayer(CONFIG.messages.petdropMessage)))

                .then(ClientCommandManager.literal("armor")
                        .executes(context -> sendAsPlayer(CONFIG.messages.armorMessage)))

                .then(ClientCommandManager.literal("artisan")
                        .executes(context -> sendAsPlayer(CONFIG.messages.variantsMessage)))

                .then(ClientCommandManager.literal("artisan")
                        .executes(context -> sendAsPlayer(CONFIG.messages.artisanMessage)))

                .then(ClientCommandManager.literal("forge")
                        .executes(context -> sendAsPlayer(CONFIG.messages.forgeMessage)))

                .then(ClientCommandManager.literal("scrapper")
                        .executes(context -> sendAsPlayer(CONFIG.messages.scrapperMessage)))

                .then(ClientCommandManager.literal("auction")
                        .executes(context -> sendAsPlayer(CONFIG.messages.auctionMessage)))

                .then(ClientCommandManager.literal("cosmetics")
                        .executes(context -> sendAsPlayer(CONFIG.messages.cosmeticsMessage)))

                .then(ClientCommandManager.literal("crew")
                        .executes(context -> sendAsPlayer(CONFIG.messages.crewMessage)))

                .then(ClientCommandManager.literal("foe")
                        .executes(context -> sendAsPlayer(CONFIG.messages.foeMessage)))

                .then(ClientCommandManager.literal("luckscaleprospect")
                        .executes(context -> sendAsPlayer(CONFIG.messages.luckscaleprospectMessage)))

                .then(ClientCommandManager.literal("reelbiteline")
                        .executes(context -> sendAsPlayer(CONFIG.messages.reelbitelineMessage)))

                .then(ClientCommandManager.literal("xpmoney")
                        .executes(context -> sendAsPlayer(CONFIG.messages.xpmoneyMessage)))

                .then(ClientCommandManager.literal("howfish")
                        .executes(context -> sendAsPlayer(CONFIG.messages.howfishMessage)))

                .then(ClientCommandManager.literal("tutorial")
                        .executes(context -> sendAsPlayer(CONFIG.messages.tutorialMessage)))

                .then(ClientCommandManager.literal("calibrator")
                        .executes(context -> sendAsPlayer(CONFIG.messages.calibratorMessage)))

                .then(ClientCommandManager.literal("locationroll")
                        .executes(context -> sendAsPlayer(CONFIG.messages.locationrollMessage)))

                .then(ClientCommandManager.literal("quests")
                        .executes(context -> sendAsPlayer(CONFIG.messages.questsMessage)))

                .then(ClientCommandManager.literal("earnmoney")
                        .executes(context -> sendAsPlayer(CONFIG.messages.earnmoneyMessage)))

                .then(ClientCommandManager.literal("wiki")
                        .executes(context -> sendAsPlayer(CONFIG.messages.wikiMessage)))

                .then(ClientCommandManager.literal("supercharged")
                        .executes(context -> sendAsPlayer(CONFIG.messages.superchargedMessage)))

                .then(ClientCommandManager.literal("lightningstorm")
                        .executes(context -> sendAsPlayer(CONFIG.messages.lightningstormMessage)))
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