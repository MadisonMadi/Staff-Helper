package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import me.madisonn.staffhelpermod.config.StaffHelperConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CommandRegistry {
    public static void registerCommands(StaffHelperConfig config) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(config.commands.mainCommand)
                    .then(ClientCommandManager.literal("armor")
                            .executes(context -> sendAsPlayer(config.messages.armorMessage)))

                    .then(ClientCommandManager.literal("artisan")
                            .executes(context -> sendAsPlayer(config.messages.artisanMessage)))

                    .then(ClientCommandManager.literal("armorinfusions")
                            .executes(context -> sendAsPlayer(config.messages.armorinfusionsMessage)))

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

                    .then(ClientCommandManager.literal("chummer")
                            .executes(context -> sendAsPlayer(config.messages.chummerMessage)))

                    .then(ClientCommandManager.literal("contest")
                            .executes(context -> sendAsPlayer(config.messages.contestMessage)))

                    .then(ClientCommandManager.literal("cosmetics")
                            .executes(context -> sendAsPlayer(config.messages.cosmeticsMessage)))

                    .then(ClientCommandManager.literal("crew")
                            .executes(context -> sendAsPlayer(config.messages.crewMessage)))

                    .then(ClientCommandManager.literal("cryptidsighting")
                            .executes(context -> sendAsPlayer(config.messages.cryptidsightingMessage)))

                    .then(ClientCommandManager.literal("dailymissions")
                            .executes(context -> sendAsPlayer(config.messages.dailymissionsMessage)))

                    .then(ClientCommandManager.literal("earnmoney")
                            .executes(context -> sendAsPlayer(config.messages.earnmoneyMessage)))

                    .then(ClientCommandManager.literal("event")
                            .executes(context -> sendAsPlayer(config.messages.eventMessage)))

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
                            .executes(context -> sendAsPlayer(config.messages.identifierMessage)))

                    .then(ClientCommandManager.literal("instances")
                            .executes(context -> sendAsPlayer(config.messages.instancesMessage)))

                    .then(ClientCommandManager.literal("locationroll")
                            .executes(context -> sendAsPlayer(config.messages.locationrollMessage)))

                    .then(ClientCommandManager.literal("luckscaleprospect")
                            .executes(context -> sendAsPlayer(config.messages.luckscaleprospectMessage)))

                    .then(ClientCommandManager.literal("moonevents")
                            .executes(context -> sendAsPlayer(config.messages.mooneventsMessage)))

                    .then(ClientCommandManager.literal("overflow")
                            .executes(context -> sendAsPlayer(config.messages.overflowMessage)))

                    .then(ClientCommandManager.literal("petdrop")
                            .executes(context -> sendAsPlayer(config.messages.petdropMessage)))

                    .then(ClientCommandManager.literal("petmerge")
                            .executes(context -> sendAsPlayer(config.messages.petmergeMessage)))

                    .then(ClientCommandManager.literal("power")
                            .executes(context -> sendAsPlayer(config.messages.powerMessage)))

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
                            .executes(context -> sendAsPlayer(config.messages.showitemMessage)))

                    .then(ClientCommandManager.literal("sitting")
                            .executes(context -> sendAsPlayer(config.messages.sittingMessage)))

                    .then(ClientCommandManager.literal("store")
                            .executes(context -> sendAsPlayer(config.messages.storeMessage)))

                    .then(ClientCommandManager.literal("supercellstorm")
                            .executes(context -> sendAsPlayer(config.messages.supercellstormMessage)))

                    .then(ClientCommandManager.literal("supercharge")
                            .executes(context -> sendAsPlayer(config.messages.superchargeMessage)))

                    .then(ClientCommandManager.literal("tackleshop")
                            .executes(context -> sendAsPlayer(config.messages.tackleshopMessage)))

                    .then(ClientCommandManager.literal("tackleshoploc")
                            .executes(context -> sendAsPlayer(config.messages.tackleshoplocMessage)))

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

                    // Error Message
                    .then(ClientCommandManager.argument("unknown", StringArgumentType.word())
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.literal("StaffHelper » Error! Command doesn't exist, check spelling!").withColor(0xFF0000));
                                return 1;
                            }))

                    // Get Skin Command + Error
                    .then(ClientCommandManager.literal("getskin")
                            .then(ClientCommandManager.argument("playername", StringArgumentType.word())
                                    .suggests(SkinGrabber::suggestPlayers)
                                    .executes(SkinGrabber::getskin))
                            .executes(context -> {
                                context.getSource().sendFeedback(Text.literal("StaffHelper » Usage: /" + config.commands.mainCommand + " getskin <playername>").withColor(0xFF0000));
                                return 1;
                            }))
            );
        });
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