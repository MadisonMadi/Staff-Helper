package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.madisonn.staffhelpermod.config.StaffHelperConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CommandRegistry {
    public static void registerCommands(StaffHelperConfig config) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralArgumentBuilder<FabricClientCommandSource> mainCommand = ClientCommandManager.literal(config.commands.mainCommand);

            addCommand(mainCommand, "armor", config.messages.armorMessage);
            addCommand(mainCommand, "artisan", config.messages.artisanMessage);
            addCommand(mainCommand, "armorinfusions", config.messages.armorinfusionsMessage);
            addCommand(mainCommand, "auction", config.messages.auctionMessage);
            addCommand(mainCommand, "bait", config.messages.baitMessage);
            addCommand(mainCommand, "baitpack", config.messages.baitpackMessage);
            addCommand(mainCommand, "bloomingoasis", config.messages.bloomingoasisMessage);
            addCommand(mainCommand, "calibrator", config.messages.calibratorMessage);
            addCommand(mainCommand, "calibratorloc", config.messages.calibratorlocMessage);
            addCommand(mainCommand, "chummer", config.messages.chummerMessage);
            addCommand(mainCommand, "contest", config.messages.contestMessage);
            addCommand(mainCommand, "cosmetics", config.messages.cosmeticsMessage);
            addCommand(mainCommand, "crew", config.messages.crewMessage);
            addCommand(mainCommand, "cryptidsighting", config.messages.cryptidsightingMessage);
            addCommand(mainCommand, "dailymissions", config.messages.dailymissionsMessage);
            addCommand(mainCommand, "earnmoney", config.messages.earnmoneyMessage);
            addCommand(mainCommand, "event", config.messages.eventMessage);
            addCommand(mainCommand, "fabled", config.messages.fabledMessage);
            addCommand(mainCommand, "findnpc", config.messages.findnpcMessage);
            addCommand(mainCommand, "foe", config.messages.foeMessage);
            addCommand(mainCommand, "forge", config.messages.forgeMessage);
            addCommand(mainCommand, "goldrush", config.messages.goldrushMessage);
            addCommand(mainCommand, "howfish", config.messages.howfishMessage);
            addCommand(mainCommand, "identifier", config.messages.identifierMessage);
            addCommand(mainCommand, "instances", config.messages.instancesMessage);
            addCommand(mainCommand, "locationroll", config.messages.locationrollMessage);
            addCommand(mainCommand, "luckscaleprospect", config.messages.luckscaleprospectMessage);
            addCommand(mainCommand, "moonevents", config.messages.mooneventsMessage);
            addCommand(mainCommand, "overflow", config.messages.overflowMessage);
            addCommand(mainCommand, "petdrop", config.messages.petdropMessage);
            addCommand(mainCommand, "petmerge", config.messages.petmergeMessage);
            addCommand(mainCommand, "power", config.messages.powerMessage);
            addCommand(mainCommand, "quests", config.messages.questsMessage);
            addCommand(mainCommand, "rainbow", config.messages.rainbowMessage);
            addCommand(mainCommand, "rainshower", config.messages.rainshowerMessage);
            addCommand(mainCommand, "recipes", config.messages.recipesMessage);
            addCommand(mainCommand, "reelbiteline", config.messages.reelbitelineMessage);
            addCommand(mainCommand, "scrapper", config.messages.scrapperMessage);
            addCommand(mainCommand, "showitem", config.messages.showitemMessage);
            addCommand(mainCommand, "sitting", config.messages.sittingMessage);
            addCommand(mainCommand, "store", config.messages.storeMessage);
            addCommand(mainCommand, "supercellstorm", config.messages.supercellstormMessage);
            addCommand(mainCommand, "supercharge", config.messages.superchargeMessage);
            addCommand(mainCommand, "tackleshop", config.messages.tackleshopMessage);
            addCommand(mainCommand, "tackleshoploc", config.messages.tackleshoplocMessage);
            addCommand(mainCommand, "thunderstorm", config.messages.thunderstormMessage);
            addCommand(mainCommand, "tutorial", config.messages.tutorialMessage);
            addCommand(mainCommand, "vehicles", config.messages.vehiclesMessage);
            addCommand(mainCommand, "variants", config.messages.variantsMessage);
            addCommand(mainCommand, "wiki", config.messages.wikiMessage);
            addCommand(mainCommand, "xpmoney", config.messages.xpmoneyMessage);

            // Error Message
            mainCommand.then(ClientCommandManager.argument("unknown", StringArgumentType.word())
                    .executes(context -> {
                        context.getSource().sendFeedback(Text.literal("Error! Command doesn't exist, check spelling!").withColor(0xFF0000));
                        return 1;
                    }));

            // Get Skin Command + Error
            mainCommand.then(ClientCommandManager.literal("getskin")
                    .then(ClientCommandManager.argument("playername", StringArgumentType.word())
                            .suggests(SkinGrabber::suggestPlayers)
                            .executes(SkinGrabber::getskin))
                    .executes(context -> {
                        context.getSource().sendFeedback(Text.literal("StaffHelper » Usage: /staffhelp getskin <playername>").withColor(0xFF0000));
                        return 1;
                    })
            );

            dispatcher.register(mainCommand);
        });
    }

    // Command Creator
    private static void addCommand(LiteralArgumentBuilder<FabricClientCommandSource> mainCommand, String commandName, String message) {
        mainCommand.then(ClientCommandManager.literal(commandName)
                .executes(context -> sendAsPlayer(message)));
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