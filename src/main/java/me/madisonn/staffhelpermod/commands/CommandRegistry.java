package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.madisonn.staffhelpermod.config.StaffHelperConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CommandRegistry {
    public static void registerCommands(StaffHelperConfig config) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal(config.commands.mainCommand)
                .then(ClientCommandManager.literal("armor")
                        .executes(context -> sendAsPlayer(config.messages.armorMessage)))

                .then(ClientCommandManager.literal("artisan")
                        .executes(context -> sendAsPlayer(config.messages.artisanMessage)))

                .then(ClientCommandManager.literal("auction")
                        .executes(context -> sendAsPlayer(config.messages.auctionMessage)))

                .then(ClientCommandManager.literal("bait")
                        .executes(context -> sendAsPlayer(config.messages.baitMessage)))

                .then(ClientCommandManager.literal("baitpack")
                        .executes(context -> sendAsPlayer(config.messages.baitpackMessage)))

                .then(ClientCommandManager.literal("bloomingoasis")
                        .executes(context -> sendAsPlayer(config.messages.bloomingoasisMessage)))

                .then(ClientCommandManager.literal("calibrator")
                        .executes(context -> sendAsPlayer(config.messages.calibratorMessage)))

                .then(ClientCommandManager.literal("calibratorloc")
                        .executes(context -> sendAsPlayer(config.messages.calibratorlocMessage)))

                .then(ClientCommandManager.literal("contest")
                        .executes(context -> sendAsPlayer(config.messages.contestMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("cosmetics")
                        .executes(context -> sendAsPlayer(config.messages.cosmeticsMessage)))

                .then(ClientCommandManager.literal("crew")
                        .executes(context -> sendAsPlayer(config.messages.crewMessage)))

                .then(ClientCommandManager.literal("earnmoney")
                        .executes(context -> sendAsPlayer(config.messages.earnmoneyMessage)))

                .then(ClientCommandManager.literal("event")
                        .executes(context -> sendAsPlayer(config.messages.eventMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("fabled")
                        .executes(context -> sendAsPlayer(config.messages.fabledMessage)))

                .then(ClientCommandManager.literal("findnpc")
                        .executes(context -> sendAsPlayer(config.messages.findnpcMessage)))

                .then(ClientCommandManager.literal("foe")
                        .executes(context -> sendAsPlayer(config.messages.foeMessage)))

                .then(ClientCommandManager.literal("forge")
                        .executes(context -> sendAsPlayer(config.messages.forgeMessage)))

                .then(ClientCommandManager.literal("goldrush")
                        .executes(context -> sendAsPlayer(config.messages.goldrushMessage)))

                .then(ClientCommandManager.literal("howfish")
                        .executes(context -> sendAsPlayer(config.messages.howfishMessage)))

                .then(ClientCommandManager.literal("identifier")
                        .executes(context -> sendAsPlayer(config.messages.identifierMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("instances")
                        .executes(context -> sendAsPlayer(config.messages.instancesMessage)))

                .then(ClientCommandManager.literal("locationroll")
                        .executes(context -> sendAsPlayer(config.messages.locationrollMessage)))

                .then(ClientCommandManager.literal("luckscaleprospect")
                        .executes(context -> sendAsPlayer(config.messages.luckscaleprospectMessage)))

                .then(ClientCommandManager.literal("moonevents")
                        .executes(context -> sendAsPlayer(config.messages.mooneventsMessage)))

                .then(ClientCommandManager.literal("overflow")
                        .executes(context -> sendAsPlayer(config.messages.overflowMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("petdrop")
                        .executes(context -> sendAsPlayer(config.messages.petdropMessage)))

                .then(ClientCommandManager.literal("petmerge")
                        .executes(context -> sendAsPlayer(config.messages.petmergeMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("power")
                        .executes(context -> sendAsPlayer(config.messages.powerMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("quests")
                        .executes(context -> sendAsPlayer(config.messages.questsMessage)))

                .then(ClientCommandManager.literal("rainbow")
                        .executes(context -> sendAsPlayer(config.messages.rainbowMessage)))

                .then(ClientCommandManager.literal("rainshower")
                        .executes(context -> sendAsPlayer(config.messages.rainshowerMessage)))

                .then(ClientCommandManager.literal("recipes")
                        .executes(context -> sendAsPlayer(config.messages.recipesMessage)))

                .then(ClientCommandManager.literal("reelbiteline")
                        .executes(context -> sendAsPlayer(config.messages.reelbitelineMessage)))

                .then(ClientCommandManager.literal("scrapper")
                        .executes(context -> sendAsPlayer(config.messages.scrapperMessage)))

                .then(ClientCommandManager.literal("showitem")
                        .executes(context -> sendAsPlayer(config.messages.showitemMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("sitting")
                        .executes(context -> sendAsPlayer(config.messages.sittingMessage)))

                .then(ClientCommandManager.literal("store")
                        .executes(context -> sendAsPlayer(config.messages.storeMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("supercellstorm")
                        .executes(context -> sendAsPlayer(config.messages.supercellstormMessage)))

                .then(ClientCommandManager.literal("supercharge")
                        .executes(context -> sendAsPlayer(config.messages.superchargeMessage)))

                .then(ClientCommandManager.literal("tackleshop")
                        .executes(context -> sendAsPlayer(config.messages.tackleshopMessage)))

                .then(ClientCommandManager.literal("tackleshoploc")
                        .executes(context -> sendAsPlayer(config.messages.tackleshoplocMessage))) // Danuh Suggestion

                .then(ClientCommandManager.literal("thunderstorm")
                        .executes(context -> sendAsPlayer(config.messages.thunderstormMessage)))

                .then(ClientCommandManager.literal("tutorial")
                        .executes(context -> sendAsPlayer(config.messages.tutorialMessage)))

                .then(ClientCommandManager.literal("vehicles")
                        .executes(context -> sendAsPlayer(config.messages.vehiclesMessage)))

                .then(ClientCommandManager.literal("variants")
                        .executes(context -> sendAsPlayer(config.messages.variantsMessage)))

                .then(ClientCommandManager.literal("wiki")
                        .executes(context -> sendAsPlayer(config.messages.wikiMessage)))

                .then(ClientCommandManager.literal("xpmoney")
                        .executes(context -> sendAsPlayer(config.messages.xpmoneyMessage)))

                .then(ClientCommandManager.argument("unknown", StringArgumentType.string())
                        .executes(context -> {
                            Text ErrorMessage = Text.literal("Error! Command doesn't exist, check spelling!").withColor(0xFF0000);

                            context.getSource().sendFeedback(ErrorMessage);
                            return 1;
                        }))
        ));
    }

    // Sends message as player
    private static int sendAsPlayer(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendChatMessage(message);
        }
        return 1;
    }
}