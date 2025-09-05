package me.madisonn.staffhelpermod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "staffhelpermod")
public class StaffHelperConfig implements ConfigData {

    // Create Categories
    @ConfigEntry.Category(value = "messages")
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public StaffHelperConfig.Messages messages = new StaffHelperConfig.Messages();

    @ConfigEntry.Category(value = "commands")
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public StaffHelperConfig.Commands commands = new StaffHelperConfig.Commands();

    // Category options
    public static class Messages {
        @ConfigEntry.Gui.Tooltip(count = 0)
        public String armorMessage = "Shards are used for armor with default armor recipes, no helmets. '/craft'";
        public String artisanMessage = "To craft or upgrade rod pieces you can go to the artisan at spawn or use '/artisan' (Angler Rank).";
        public String auctionMessage = "You can buy and sell items in the '/ah', there's a small tax when listing an item '/ah sell price' expired items go to '/overflow'.";
        public String baitMessage = "To use bait press 'Q' while holding your fishing rod or right click it in your inventory, click the tackle box and drop your baits in there!";
        public String baitpackMessage = "To get bait packages you can do '/vote', '/ah', '/collections', Fish Compendium and from Contests if top 3 at 5 participants.";
        public String calibratorMessage = "Calibrate reels and poles for better stats and bonuses. Higher rarity increases costs and bonus percentage. Upgrading in Artisan removes bonus. '/calibrator' (Mariner Rank).";
        public String calibratorlocMessage = "The Calibrator can be found by going to '/spawn' and turn around, go inside and follow the sign directions.";
        public String contestMessage = "Winning a contest top 3 will give you money, location xp, shards and if 5 people participate top 3 has a chance to win a pet and bait package!";
        public String cosmeticsMessage = "Cosmetics can be bought with credits from '/buy' these are purely cosmetic and will not influence your gameplay.";
        public String crewMessage = "You can create a crew '/crew create name' a crew island is mostly used for building and storage! crews are also used to compete for fun!";
        public String earnmoneyMessage = "You can earn money from '/collections', '/ah', '/event', '/vote' and '/quests'.";
        public String eventMessage = "Make sure you check '/event' and hover over the book to see how to obtain event points.";
        public String findnpcMessage = "Do '/spawn' walk forwards towards the 6 npc's and then walk to the left you'll find every npc with a huge sign stuck to their building!";
        public String foeMessage = "You can install the FishonMC-Extras Mod here modrinth.com/mod/fishonmc-extras.";
        public String forgeMessage = "To tier up your armor you can go to the forge at spawn or use '/forge' (Sailor Rank), T2 16 Shards, T3 48 Shards, T4 96 Shards, T5 256 Shards.";
        public String howfishMessage = "Sneak to raise line tension (blue bar right), stop to lower it (left). Fill the green bar to catch the fish!";
        public String identifierMessage = "You can identify you armor pieces with '/identifier' (Sailor Rank) it goes from 1% to 100%, and it's recommended to only forge pieces with 90%+.";
        public String instancesMessage = "If you can't find someone, make sure you're in the same '/instance'.";
        public String lightningstormMessage = "A lightning storm has a 1 in 5 chance to appear and has a 1 in 250 chance for a lightning bottle while fishing.";
        public String locationrollMessage = "Each location has a bonus roll per level 1-20, slots can be rolled for Species Size Bonus, General Bonus: luck, prospect and scale, also Sell and XP bonus.";
        public String luckscaleprospectMessage = "Luck boosts fish rarity chance, Scale boosts bigger fish chance, Prospect boosts shard chance: base chance is 1/50.";
        public String overflowMessage = "Type '/overflow' to check if you have any lost loot from previous contests. Make sure you have some free slots in your inventory.";
        public String petdropMessage = "Pet Drop Chances are Common 1/750, Rare 1/4000, Epic 1/10000, Legendary 1/30000 and Mythical 1/80000!";
        public String petmergeMessage = "You can merge 2 pets that are the same type and rarity. Both must be level 100 - right click one of them in your inventory, then left click the other one.";
        public String powerMessage = "Power comes from completing collections, compendiums, and other tasks, this is cosmetic for now and will have a use later.";
        public String questsMessage = "There are Easy, Medium (lvl 20) and Hard (lvl 50) quests, each tier gives more rewards and at hard quests you get shards and a chance for a pet!";
        public String recipesMessage = "Make sure to check out recipes to see the crafting patterns, open your green book and click the Recipes book.";
        public String reelbitelineMessage = "Reel Speed increases progress per tick, Bite Speed reduces wait time for bites, Line Strength makes catching fish easier.";
        public String scrapperMessage = "To scrap your armor pieces you can go to the scrapper at spawn or use '/scrapper' (Sailor Rank) to scrap your pieces and get some shards in return!";
        public String showitemMessage = "You can showcase an item simply by holding it in your hand, then type in chat '[ item]' (no space).";
        public String sittingMessage = "To find places where you can sit down you need to enable F3+B to find interactable hitboxes, click these spots to sit down and cast your rod!";
        public String storeMessage = "You can check out the store page by doing '/buy', to purchase ranks, boosters and credits!";
        public String superchargeMessage = "For a supercharge you need 1 lightning bottle and 8 shards of choice, increases base stats by each tier 1.1x, 1.25x and 1.5x and the effect lasts for x amount of catches.";
        public String tackleshopMessage = "The Tackle Shop is for bait (unique per player), gives 7 different bait and will reroll every 4 hours, from Common Bait all the wait to extremely rare Variant Bait.";
        public String tackleshoplocMessage = "The Tackle Shop can be found by going to '/spawn' and turn around, go inside and follow the sign directions.";
        public String tutorialMessage = "You can use the command '/tasks' to see a quick tutorial which will give you the rundown on the basics of the gameplay loop.";
        public String vehiclesMessage = "You can buy vehicles in the building behind spawn to the left, purchase with credits or money, spawn your vehicle with '/vehicles' or walk to a dock to spawn your water vehicles.";
        public String variantsMessage = "Variant chances are Albino 1/5000, Melanistic 1/10000 and Trophy 1/15000.";
        public String wikiMessage = "You can learn more at wiki.fishonmc.net";
        public String xpmoneyMessage = "Bonus XP boosts player, location, and crew XP. Bonus Money increases earnings from catches.";
    }

    // Category options
    public static class Commands {
        @ConfigEntry.Gui.Tooltip(count = 0)
        public String mainCommand = "staffhelp";
    }
}