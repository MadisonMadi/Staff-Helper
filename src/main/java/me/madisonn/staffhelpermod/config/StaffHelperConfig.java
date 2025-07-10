package me.madisonn.staffhelpermod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "staffhelpermod")
public class StaffHelperConfig implements ConfigData {

    @ConfigEntry.Category(value = "messages")
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public StaffHelperConfig.Messages messages = new StaffHelperConfig.Messages();

    @ConfigEntry.Category(value = "commands")
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public StaffHelperConfig.Commands commands = new StaffHelperConfig.Commands();

    public static class Messages {
        @ConfigEntry.Gui.Tooltip(count = 0)
        public String armorMessage = "Shards are used for armor with default armor recipes, no helmets.";
        public String artisanMessage = "To craft or upgrade rod pieces you can go to the artisan at spawn or use '/artisan' (Angler Rank).";
        public String auctionMessage = "You can buy and sell items in the '/ah', there's a small tax when listing an item '/ah sell price' expired items go to '/overflow'.";
        public String baitMessage = "To use bait press 'Q' while holding your fishing rod or right click it in your inventory, click the tackle box and drop your baits in there!";
        public String baitpackMessage = "To get bait packages you can do '/vote', '/ah', '/collections' and Fish Compendium.";
        public String calibratorMessage = "Calibrate reels and poles for better stats and bonuses. Higher rarity increases costs and bonus percentage. Upgrading in Artisan removes bonus. '/calibrator' (Mariner Rank)";
        public String cosmeticsMessage = "Cosmetics can be bought with credits from '/buy' these are purely cosmetic and will not influence your gameplay.";
        public String crewMessage = "You can create a crew '/crew create name' a crew island is mostly used for building and storage! crews are also used to compete for fun!";
        public String earnmoneyMessage = "You can earn money from '/collections', '/ah', '/event', '/vote' and '/quests'";
        public String foeMessage = "You can install the FishonMC-Extras Mod here modrinth.com/mod/fishonmc-extras.";
        public String forgeMessage = "To tier up your armor you can go to the forge at spawn or use '/forge' (Sailor Rank), T2 16 Shards, T3 48 Shards, T4 96 Shards, T5 256 Shards.";
        public String howfishMessage = "Sneak to raise line tension (blue bar right), stop to lower it (left). Fill the green bar to catch the fish!";
        public String lightningstormMessage = "A lightning storm has a 1 in 5 chance to appear and has a 1 in 250 chance for a lightning bottle while fishing.";
        public String locationrollMessage = "Each location has a bonus roll per level 1-20, slots can be rolled for Species Size Bonus, General Bonus: luck, prospect and scale, also Sell and XP bonus.";
        public String luckscaleprospectMessage = "Luck boosts fish rarity chance, Scale boosts bigger fish chance, Prospect boosts shard chance: base chance is 1/50.";
        public String petdropMessage = "Pet Drop Chances are Common 1/750 - Rare 1/4000 - Epic 1/10000 - Legendary 1/30000 - Mythical 1/80000!";
        public String questsMessage = "There are Easy, Medium (lvl 20) and Hard (lvl 50) quests, each tier gives more rewards and at hard quests you get shards and a chance for a pet!";
        public String reelbitelineMessage = "Reel Speed increases progress per tick, Bite Speed reduces wait time for bites, Line Strength makes catching fish easier.";
        public String scrapperMessage = "To scrap your armor pieces you can go to the scrapper at spawn or use '/scrapper' to scrap your pieces and get some shards in return! '/scrapper'.";
        public String superchargeMessage = "For a supercharge you need 1 lightning bottle and 8 shards of choice, increases base stats by each tier 1.1x, 1.25x and 1.5x and the effect lasts for x amount of catches.";
        public String tutorialMessage = "You can use the command '/tasks' to see a quick tutorial which will give you the rundown on the basics of the gameplay loop.";
        public String variantsMessage = "Albino 1/5000, Melanistic 1/10000 and Trophy 1/15000.";
        public String wikiMessage = "https://wiki.fishonmc.net/";
        public String xpmoneyMessage = "Bonus XP boosts player, location, and crew XP. Bonus Money increases earnings from catches.";
    }

    public static class Commands {
        @ConfigEntry.Gui.Tooltip(count = 0)
        public String mainCommand = "staffhelp";
    }
}