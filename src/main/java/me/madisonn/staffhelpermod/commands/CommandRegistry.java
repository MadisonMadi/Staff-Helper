package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.madisonn.staffhelpermod.commands.utils.SkinGrabberCommand;
import me.madisonn.staffhelpermod.config.Configs;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.minecraft.client.Minecraft;

public class CommandRegistry {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, ignored) -> {
            dispatcher.register(commands("staffhelp"));
            dispatcher.register(commands("sh"));
        });
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> commands(String commandName) {
        return ClientCommands.literal(commandName)
                .then(ClientCommands.literal("armorinfusions")
                        .executes(ignored -> sendAsPlayer(Configs.messages.armorinfusionsMessage)))

                .then(ClientCommands.literal("artisan")
                        .executes(ignored -> sendAsPlayer(Configs.messages.artisanMessage)))

                .then(ClientCommands.literal("auction")
                          .executes(ignored -> sendAsPlayer(Configs.messages.auctionMessage)))

                .then(ClientCommands.literal("bait")
                        .executes(ignored -> sendAsPlayer(Configs.messages.baitMessage)))

                .then(ClientCommands.literal("baitpack")
                        .executes(ignored -> sendAsPlayer(Configs.messages.baitpackMessage)))

                .then(ClientCommands.literal("bloomingoasis")
                        .executes(ignored -> sendAsPlayer(Configs.messages.bloomingoasisMessage)))

                .then(ClientCommands.literal("calibrator")
                        .executes(ignored -> sendAsPlayer(Configs.messages.calibratorMessage)))

                .then(ClientCommands.literal("calibratorloc")
                        .executes(ignored -> sendAsPlayer(Configs.messages.calibratorlocMessage)))

                .then(ClientCommands.literal("chummer")
                        .executes(ignored -> sendAsPlayer(Configs.messages.chummerMessage)))

                .then(ClientCommands.literal("contest")
                        .executes(ignored -> sendAsPlayer(Configs.messages.contestMessage)))

                .then(ClientCommands.literal("cosmetics")
                        .executes(ignored -> sendAsPlayer(Configs.messages.cosmeticsMessage)))

                .then(ClientCommands.literal("craft")
                        .executes(ignored -> sendAsPlayer(Configs.messages.craftMessage)))

                .then(ClientCommands.literal("crew")
                        .executes(ignored -> sendAsPlayer(Configs.messages.crewMessage)))

                .then(ClientCommands.literal("cryptidsighting")
                        .executes(ignored -> sendAsPlayer(Configs.messages.cryptidsightingMessage)))

                .then(ClientCommands.literal("dailymissions")
                        .executes(ignored -> sendAsPlayer(Configs.messages.dailymissionsMessage)))

                .then(ClientCommands.literal("earnmoney")
                        .executes(ignored -> sendAsPlayer(Configs.messages.earnmoneyMessage)))

                .then(ClientCommands.literal("event")
                        .executes(ignored -> sendAsPlayer(Configs.messages.eventMessage)))

                .then(ClientCommands.literal("fabled")
                        .executes(ignored -> sendAsPlayer(Configs.messages.fabledMessage)))

                .then(ClientCommands.literal("findnpc")
                        .executes(ignored -> sendAsPlayer(Configs.messages.findnpcMessage)))

                .then(ClientCommands.literal("foer")
                        .executes(ignored -> sendAsPlayer(Configs.messages.foerMessage)))

                .then(ClientCommands.literal("forge")
                        .executes(ignored -> sendAsPlayer(Configs.messages.forgeMessage)))

                .then(ClientCommands.literal("goldrush")
                        .executes(ignored -> sendAsPlayer(Configs.messages.goldrushMessage)))

                .then(ClientCommands.literal("howfish")
                        .executes(ignored -> sendAsPlayer(Configs.messages.howfishMessage)))

                .then(ClientCommands.literal("identifier")
                        .executes(ignored -> sendAsPlayer(Configs.messages.identifierMessage)))

                .then(ClientCommands.literal("instances")
                        .executes(ignored -> sendAsPlayer(Configs.messages.instancesMessage)))

                .then(ClientCommands.literal("locationroll")
                        .executes(ignored -> sendAsPlayer(Configs.messages.locationrollMessage)))

                .then(ClientCommands.literal("luckscaleprospect")
                        .executes(ignored -> sendAsPlayer(Configs.messages.luckscaleprospectMessage)))

                .then(ClientCommands.literal("moonevents")
                        .executes(ignored -> sendAsPlayer(Configs.messages.mooneventsMessage)))

                .then(ClientCommands.literal("overflow")
                        .executes(ignored -> sendAsPlayer(Configs.messages.overflowMessage)))

                .then(ClientCommands.literal("petdrop")
                        .executes(ignored -> sendAsPlayer(Configs.messages.petdropMessage)))

                .then(ClientCommands.literal("petmerge")
                        .executes(ignored -> sendAsPlayer(Configs.messages.petmergeMessage)))

                .then(ClientCommands.literal("power")
                        .executes(ignored -> sendAsPlayer(Configs.messages.powerMessage)))

                .then(ClientCommands.literal("presets")
                        .executes(ignored -> sendAsPlayer(Configs.messages.presetsMessage)))

                .then(ClientCommands.literal("quests")
                        .executes(ignored -> sendAsPlayer(Configs.messages.questsMessage)))

                .then(ClientCommands.literal("rainbow")
                        .executes(ignored -> sendAsPlayer(Configs.messages.rainbowMessage)))

                .then(ClientCommands.literal("rainshower")
                        .executes(ignored -> sendAsPlayer(Configs.messages.rainshowerMessage)))

                .then(ClientCommands.literal("recipes")
                        .executes(ignored -> sendAsPlayer(Configs.messages.recipesMessage)))

                .then(ClientCommands.literal("reelbiteline")
                        .executes(ignored -> sendAsPlayer(Configs.messages.reelbitelineMessage)))

                .then(ClientCommands.literal("scrapper")
                        .executes(ignored -> sendAsPlayer(Configs.messages.scrapperMessage)))

                .then(ClientCommands.literal("showitem")
                        .executes(ignored -> sendAsPlayer(Configs.messages.showitemMessage)))

                .then(ClientCommands.literal("sitting")
                        .executes(ignored -> sendAsPlayer(Configs.messages.sittingMessage)))

                .then(ClientCommands.literal("store")
                        .executes(ignored -> sendAsPlayer(Configs.messages.storeMessage)))

                .then(ClientCommands.literal("supercellstorm")
                        .executes(ignored -> sendAsPlayer(Configs.messages.supercellstormMessage)))

                .then(ClientCommands.literal("supercharge")
                        .executes(ignored -> sendAsPlayer(Configs.messages.superchargeMessage)))

                .then(ClientCommands.literal("tackleshop")
                        .executes(ignored -> sendAsPlayer(Configs.messages.tackleshopMessage)))

                .then(ClientCommands.literal("tackleshoploc")
                        .executes(ignored -> sendAsPlayer(Configs.messages.tackleshoplocMessage)))

                .then(ClientCommands.literal("thunderstorm")
                        .executes(ignored -> sendAsPlayer(Configs.messages.thunderstormMessage)))

                .then(ClientCommands.literal("tournaments")
                        .executes(ignored -> sendAsPlayer(Configs.messages.tournamentsMessage)))

                .then(ClientCommands.literal("tutorial")
                        .executes(ignored -> sendAsPlayer(Configs.messages.tutorialMessage)))

                .then(ClientCommands.literal("vehicles")
                        .executes(ignored -> sendAsPlayer(Configs.messages.vehiclesMessage)))

                .then(ClientCommands.literal("variants")
                        .executes(ignored -> sendAsPlayer(Configs.messages.variantsMessage)))

                .then(ClientCommands.literal("wiki")
                        .executes(ignored -> sendAsPlayer(Configs.messages.wikiMessage)))

                .then(ClientCommands.literal("xpmoney")
                        .executes(ignored -> sendAsPlayer(Configs.messages.xpmoneyMessage)))

                .then(SkinGrabberCommand.register(commandName));
    }

    private static int sendAsPlayer(String message) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.connection.sendChat(message);
        }
        return 1;
    }
}
