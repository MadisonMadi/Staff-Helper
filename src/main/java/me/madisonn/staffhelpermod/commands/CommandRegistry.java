package me.madisonn.staffhelpermod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.madisonn.staffhelpermod.commands.utils.SkinGrabberCommand;
import me.madisonn.staffhelpermod.config.Configs;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.minecraft.client.Minecraft;

public class CommandRegistry {
    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(commands("staffhelp"));
            dispatcher.register(commands("sh"));
        });
    }

    private static LiteralArgumentBuilder<FabricClientCommandSource> commands(String commandName) {
        return ClientCommandManager.literal(commandName)
                .then(ClientCommandManager.literal("armorinfusions")
                        .executes(context -> sendAsPlayer(Configs.messages.armorinfusionsMessage)))

                .then(ClientCommandManager.literal("artisan")
                        .executes(context -> sendAsPlayer(Configs.messages.artisanMessage)))

                .then(ClientCommandManager.literal("auction")
                        .executes(context -> sendAsPlayer(Configs.messages.auctionMessage)))

                .then(ClientCommandManager.literal("bait")
                        .executes(context -> sendAsPlayer(Configs.messages.baitMessage)))

                .then(ClientCommandManager.literal("baitpack")
                        .executes(context -> sendAsPlayer(Configs.messages.baitpackMessage)))

                .then(ClientCommandManager.literal("bloomingoasis")
                        .executes(context -> sendAsPlayer(Configs.messages.bloomingoasisMessage)))

                .then(ClientCommandManager.literal("calibrator")
                        .executes(context -> sendAsPlayer(Configs.messages.calibratorMessage)))

                .then(ClientCommandManager.literal("calibratorloc")
                        .executes(context -> sendAsPlayer(Configs.messages.calibratorlocMessage)))

                .then(ClientCommandManager.literal("chummer")
                        .executes(context -> sendAsPlayer(Configs.messages.chummerMessage)))

                .then(ClientCommandManager.literal("contest")
                        .executes(context -> sendAsPlayer(Configs.messages.contestMessage)))

                .then(ClientCommandManager.literal("cosmetics")
                        .executes(context -> sendAsPlayer(Configs.messages.cosmeticsMessage)))

                .then(ClientCommandManager.literal("craft")
                        .executes(context -> sendAsPlayer(Configs.messages.craftMessage)))

                .then(ClientCommandManager.literal("crew")
                        .executes(context -> sendAsPlayer(Configs.messages.crewMessage)))

                .then(ClientCommandManager.literal("cryptidsighting")
                        .executes(context -> sendAsPlayer(Configs.messages.cryptidsightingMessage)))

                .then(ClientCommandManager.literal("dailymissions")
                        .executes(context -> sendAsPlayer(Configs.messages.dailymissionsMessage)))

                .then(ClientCommandManager.literal("earnmoney")
                        .executes(context -> sendAsPlayer(Configs.messages.earnmoneyMessage)))

                .then(ClientCommandManager.literal("event")
                        .executes(context -> sendAsPlayer(Configs.messages.eventMessage)))

                .then(ClientCommandManager.literal("fabled")
                        .executes(context -> sendAsPlayer(Configs.messages.fabledMessage)))

                .then(ClientCommandManager.literal("findnpc")
                        .executes(context -> sendAsPlayer(Configs.messages.findnpcMessage)))

                .then(ClientCommandManager.literal("foe")
                        .executes(context -> sendAsPlayer(Configs.messages.foeMessage)))

                .then(ClientCommandManager.literal("forge")
                        .executes(context -> sendAsPlayer(Configs.messages.forgeMessage)))

                .then(ClientCommandManager.literal("goldrush")
                        .executes(context -> sendAsPlayer(Configs.messages.goldrushMessage)))

                .then(ClientCommandManager.literal("howfish")
                        .executes(context -> sendAsPlayer(Configs.messages.howfishMessage)))

                .then(ClientCommandManager.literal("identifier")
                        .executes(context -> sendAsPlayer(Configs.messages.identifierMessage)))

                .then(ClientCommandManager.literal("instances")
                        .executes(context -> sendAsPlayer(Configs.messages.instancesMessage)))

                .then(ClientCommandManager.literal("locationroll")
                        .executes(context -> sendAsPlayer(Configs.messages.locationrollMessage)))

                .then(ClientCommandManager.literal("luckscaleprospect")
                        .executes(context -> sendAsPlayer(Configs.messages.luckscaleprospectMessage)))

                .then(ClientCommandManager.literal("moonevents")
                        .executes(context -> sendAsPlayer(Configs.messages.mooneventsMessage)))

                .then(ClientCommandManager.literal("overflow")
                        .executes(context -> sendAsPlayer(Configs.messages.overflowMessage)))

                .then(ClientCommandManager.literal("petdrop")
                        .executes(context -> sendAsPlayer(Configs.messages.petdropMessage)))

                .then(ClientCommandManager.literal("petmerge")
                        .executes(context -> sendAsPlayer(Configs.messages.petmergeMessage)))

                .then(ClientCommandManager.literal("power")
                        .executes(context -> sendAsPlayer(Configs.messages.powerMessage)))

                .then(ClientCommandManager.literal("presets")
                        .executes(context -> sendAsPlayer(Configs.messages.presetsMessage)))

                .then(ClientCommandManager.literal("quests")
                        .executes(context -> sendAsPlayer(Configs.messages.questsMessage)))

                .then(ClientCommandManager.literal("rainbow")
                        .executes(context -> sendAsPlayer(Configs.messages.rainbowMessage)))

                .then(ClientCommandManager.literal("rainshower")
                        .executes(context -> sendAsPlayer(Configs.messages.rainshowerMessage)))

                .then(ClientCommandManager.literal("recipes")
                        .executes(context -> sendAsPlayer(Configs.messages.recipesMessage)))

                .then(ClientCommandManager.literal("reelbiteline")
                        .executes(context -> sendAsPlayer(Configs.messages.reelbitelineMessage)))

                .then(ClientCommandManager.literal("scrapper")
                        .executes(context -> sendAsPlayer(Configs.messages.scrapperMessage)))

                .then(ClientCommandManager.literal("showitem")
                        .executes(context -> sendAsPlayer(Configs.messages.showitemMessage)))

                .then(ClientCommandManager.literal("sitting")
                        .executes(context -> sendAsPlayer(Configs.messages.sittingMessage)))

                .then(ClientCommandManager.literal("store")
                        .executes(context -> sendAsPlayer(Configs.messages.storeMessage)))

                .then(ClientCommandManager.literal("supercellstorm")
                        .executes(context -> sendAsPlayer(Configs.messages.supercellstormMessage)))

                .then(ClientCommandManager.literal("supercharge")
                        .executes(context -> sendAsPlayer(Configs.messages.superchargeMessage)))

                .then(ClientCommandManager.literal("tackleshop")
                        .executes(context -> sendAsPlayer(Configs.messages.tackleshopMessage)))

                .then(ClientCommandManager.literal("tackleshoploc")
                        .executes(context -> sendAsPlayer(Configs.messages.tackleshoplocMessage)))

                .then(ClientCommandManager.literal("thunderstorm")
                        .executes(context -> sendAsPlayer(Configs.messages.thunderstormMessage)))

                .then(ClientCommandManager.literal("tournaments")
                        .executes(context -> sendAsPlayer(Configs.messages.tournamentsMessage)))

                .then(ClientCommandManager.literal("tutorial")
                        .executes(context -> sendAsPlayer(Configs.messages.tutorialMessage)))

                .then(ClientCommandManager.literal("vehicles")
                        .executes(context -> sendAsPlayer(Configs.messages.vehiclesMessage)))

                .then(ClientCommandManager.literal("variants")
                        .executes(context -> sendAsPlayer(Configs.messages.variantsMessage)))

                .then(ClientCommandManager.literal("wiki")
                        .executes(context -> sendAsPlayer(Configs.messages.wikiMessage)))

                .then(ClientCommandManager.literal("xpmoney")
                        .executes(context -> sendAsPlayer(Configs.messages.xpmoneyMessage)))

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
