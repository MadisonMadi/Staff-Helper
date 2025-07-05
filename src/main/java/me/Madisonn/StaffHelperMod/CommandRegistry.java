package me.Madisonn.StaffHelperMod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public class CommandRegistry implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("staffhelp")
                .then(ClientCommandManager.literal("bait")
                        .executes(context -> {
                            sendAsPlayer("To use bait press 'Q' while holding your fishing rod, click the tackle box and drop your baits in there!");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("baitpack")
                        .executes(context -> {
                            sendAsPlayer("To get bait packages you can do /vote, purchase them in /ah or from Fish Compendium and '/collections'.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("petdrop")
                        .executes(context -> {
                            sendAsPlayer("Pet Drop Chances are Common 1/750 - Rare 1/4000 - Epic 1/10000 - Legendary 1/30000 - Mythical 1/80000!");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("armor")
                        .executes(context -> {
                            sendAsPlayer("Shards are used for armor with default armor recipes, no helmets.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("variants")
                        .executes(context -> {
                            sendAsPlayer("Albino 1/5000, Melanistic 1/10000 and Trophy 1/15000.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("artisan")
                        .executes(context -> {
                            sendAsPlayer("To craft or upgrade rod pieces you can go to the artisan at spawn or use '/artisan' (Angler Rank).");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("forge")
                        .executes(context -> {
                            sendAsPlayer("To tier up your armor you can go to the forge at spawn or use '/forge' (Sailor Rank), T2 16 Shards, T3 48 Shards, T4 96 Shards, T5 256 Shards.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("scrapper")
                        .executes(context -> {
                            sendAsPlayer("To scrap your armor pieces you can go to the scrapper at spawn or use '/scrapper' to scrap your pieces and get some shards in return! '/scrapper'.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("auction")
                        .executes(context -> {
                            sendAsPlayer("You can buy and sell items in the '/ah', there's a small tax when listing an item '/ah sell price'");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("cosmetics")
                        .executes(context -> {
                            sendAsPlayer("Cosmetics can be bought with credits from '/buy' these are purely cosmetic and will not influence your gameplay.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("crew")
                        .executes(context -> {
                            sendAsPlayer("You can create a crew '/crew create name' a crew island is mostly used for building and storage! crews are also used to compete for fun!");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("foe")
                        .executes(context -> {
                            sendAsPlayer("You can install the FishonMC-Extras Mod here modrinth.com/mod/fishonmc-extras.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("luckscaleprospect")
                        .executes(context -> {
                            sendAsPlayer("Luck boosts fish rarity chance, Scale boosts bigger fish chance, Prospect boosts shard chance.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("reelbiteline")
                        .executes(context -> {
                            sendAsPlayer("Reel Speed increases progress per tick; Bite Speed reduces wait time for bites.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("xpmoney")
                        .executes(context -> {
                            sendAsPlayer("Bonus XP boosts player, location, and crew XP. Bonus Money increases earnings from catches.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("howfish")
                        .executes(context -> {
                            sendAsPlayer("Sneak to raise line tension (blue bar right), stop to lower it (left). Fill the green bar to catch the fish!");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("tutorial")
                        .executes(context -> {
                            sendAsPlayer("You can use the command '/tasks' to see a quick tutorial which will give you the rundown on the basics of the gameplay loop.");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("calibrator")
                        .executes(context -> {
                            sendAsPlayer("Calibrate reels and poles for better stats and bonuses. Higher rarity increases costs and bonus percentage. Upgrading in Artisan removes bonus. '/calibrator' (Mariner Rank)");
                            return 1;
                        }))

                .then(ClientCommandManager.literal("locationroll")
                        .executes(context -> {
                            sendAsPlayer("Each location has a bonus roll per level 1-20, slots can be rolled for Species Size Bonus, General Bonus: luck, prospect and scale, also Sell and XP bonus.");
                            return 1;
                        }))
        ));
    }

    private void sendAsPlayer(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendChatMessage(message);
        }
    }
}