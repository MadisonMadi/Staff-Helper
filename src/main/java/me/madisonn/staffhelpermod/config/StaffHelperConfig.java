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

    public static class Messages {
        @ConfigEntry.Gui.Tooltip(count = 0)
        public String armorinfusionsMessage = "You can craft infusion capsules with the recipe in the recipes book. There are three types of infusions Albino, Melanistic and Trophy. The bonus improves by +5% on every tier up to +25% on a single piece.";
        public String artisanMessage = "To craft or upgrade rod pieces you can go to the artisan at spawn or use '/artisan' (Angler Rank).";
        public String auctionMessage = "You can buy items on the action house by using '/ah', you can also put items up for sale by using '/ah sell price'. There is a 2% tax when listing items and expired items go to '/overflow'.";
        public String baitMessage = "You can use bait by holding your rod and pressing 'Q' or right click it in your inventory. Now you can open the tacklebox and move your bait into it, the bait usage starts from the top left.";
        public String baitpackMessage = "You can get bait packages from '/vote', '/ah', '/collections', '/compendium', '/contest', '/tournament', '/event' and power.";
        public String bloomingoasisMessage = "The Blooming Oasis Gives +100% pet luck for 15-16 minutes.";
        public String calibratorMessage = "Calibrate reels and poles for better stats and bonuses. Higher rarity increases costs and bonus percentage. Upgrading in Artisan removes bonus. '/calibrator' (Mariner Rank).";
        public String calibratorlocMessage = "The Calibrator can be found by going to '/spawn' and turn around, go inside and follow the sign directions.";
        public String chummerMessage = "The Chummer boosts bite speed for all players within 10 blocks. It lasts 15 minutes after being placed. Common +75, Rare +130, Epic +180, Legendary +250 and Mythical +380.";
        public String contestMessage = "Winning a contest will give you money, location xp, shards, top 3 for a chance for a pet and bait package!";
        public String cosmeticsMessage = "Cosmetics can be bought with credits from '/buy' these are purely cosmetic and will not influence your gameplay.";
        public String craftMessage = "You can use climate shards to craft armor by typing '/craft'! You can't craft helmets, this slot is reserved for cosmetics.";
        public String crewMessage = "You can create a crew '/crew create name' a crew island is mostly used for building and storage! crews are also used to compete for fun!";
        public String cryptidsightingMessage = "Fishing at Cypress Lake between 1AM-3AM, there is a 1/300 change bigfoot will steal your fish, and gives you either a Bigfoot Tooth or Bigfoot Fur, these are used for pet items.";
        public String dailymissionsMessage = "Open the green book and check the bottom-right corner for Daily Missions. Each mission rewards 2-4 shards based on your location. Completing all grants XP, Money, a pet, and Location XP.";
        public String earnmoneyMessage = "You can earn money from '/collections', '/ah', '/event', '/vote' and '/quests'.";
        public String eventMessage = "Make sure you check '/event' and hover over the book to see how to obtain event points.";
        public String fabledMessage = "The Fabled Event happens twice per day, and has a 1/3000 chance in a location declared in chat. Only one person per fabled event can catch it.";
        public String findnpcMessage = "Do '/spawn' walk forwards towards the 6 npc's and then walk to the left you'll find every npc with a huge sign stuck to their building!";
        public String foeMessage = "You can install the FishonMC-Extras Mod here modrinth.com/mod/fishonmc-extras.";
        public String forgeMessage = "To tier up your armor you can go to the forge at spawn or use '/forge' (Sailor Rank), T2 16 Shards, T3 48 Shards, T4 96 Shards, T5 256 Shards, it's recommended to only forge pieces with 95%+.";
        public String goldrushMessage = "Gold Rush lasts for 15-16m and changes the base chance for shards to 1/25, also has a 1/1000 chance to drop a Prospecting Amulet.";
        public String howfishMessage = "Sneak to raise line tension (blue bar right), stop to lower it (left). Fill the green bar to catch the fish!";
        public String identifierMessage = "You can identify you armor pieces with '/identifier' (Sailor Rank) it goes from 1% to 100%.";
        public String instancesMessage = "If you can't find someone, make sure you're in the same '/instance'.";
        public String locationrollMessage = "Each location has a bonus roll per level 1-20, slots can be rolled for Species Size Bonus, General Bonus: luck, prospect and scale, also Sell and XP bonus.";
        public String luckscaleprospectMessage = "Luck boosts fish rarity chance, Scale boosts bigger fish chance, Prospect boosts shard chance: base chance is 1/50.";
        public String mooneventsMessage = "Moon Events occur every full moon and last 7 minutes, Full Moon +120 Bite Speed, Blue Moon +120 Bite Speed and 5x XP, Super Moon +120 Bite Speed and +150 Reel Speed, Blood Moon +120 Bite Speed and 1/300 chance for an infusion capsule.";
        public String overflowMessage = "Type '/overflow' to check if you have any lost loot from previous contests. Make sure you have some free slots in your inventory.";
        public String petdropMessage = "Pet Drop odds are, Common 1/750, Rare 1/4000, Epic 1/10000, Legendary 1/30000 and Mythical 1/80000!";
        public String petmergeMessage = "You can merge 2 pets that are the same type and rarity. Both must be level 100, right click one of them in your inventory, then left click the other one.";
        public String powerMessage = "Power comes from collections, compendiums and unique fish, Power will give you a bunch of rewards and free quest slots to unlock!.";
        public String presetsMessage = "Presets are used for storing armor and rod parts, use '/presets' to open.";
        public String questsMessage = "There are Easy, Medium (lvl 20) and Hard (lvl 50) quests, each tier gives more rewards and at hard quests you get shards and a chance for a pet!";
        public String rainbowMessage = "Rainbows have a 1 in 4 chance to spawn after it stops raining and grants +500 luck and lasts for 3-7 minutes.";
        public String rainshowerMessage = "Rain can start randomly every 1 to 2 hours, lasts 8-10 minutes and gives +50 Bite Speed.";
        public String recipesMessage = "Make sure to check out recipes to see the crafting patterns, open your green book and click the Recipes book.";
        public String reelbitelineMessage = "Reel Speed increases progress per tick, Bite Speed reduces wait time for bites, Line Strength makes catching fish easier.";
        public String scrapperMessage = "To scrap your armor pieces you can go to the scrapper at spawn or use '/scrapper' (Sailor Rank) to scrap your pieces and get some shards in return!";
        public String showitemMessage = "You can showcase an item simply by holding it in your hand, then type in chat '[ item]' (no space).";
        public String sittingMessage = "To find places where you can sit down you need to enable F3+B to find interactable hitboxes, click these spots to sit down and cast your rod!";
        public String storeMessage = "You can check out the store page by doing '/buy', to purchase ranks, boosters and credits!";
        public String supercellstormMessage = "A Supercell is a buffed version of the Thunderstorm and happens 1 in 15 times that it starts to rain. This storm gives +150 bite speed and the chance for a Lightning in a Bottle is 1/250 and lasts for around 4-9 minutes.";
        public String superchargeMessage = "For a supercharge you need 1 lightning bottle and 8 shards of choice, increases base stats by each tier 1.1x, 1.25x and 1.5x and the effect lasts for x amount of catches.";
        public String tackleshopMessage = "The Tackle Shop is for bait (unique per player), gives 7 different bait and will reroll every 4 hours, from Common Bait all the wait to extremely rare Variant Bait.";
        public String tackleshoplocMessage = "The Tackle Shop can be found by going to '/spawn' and turn around, go inside and follow the sign directions.";
        public String thunderstormMessage = "Thunderstorms spawn 1 in 3 times when it starts to rain and grants +100 bite speed as well as a 1/500 chance to fish up a lightning bottle and lasts for around 5-11 minutes.";
        public String tournamentsMessage = "Tournaments happen every weekend for 1 location, catch 5 fish and if bigger the lowest one will get replaces! '/tournaments' to open and see your stats/bracket and rewards!";
        public String tutorialMessage = "You can type '/tasks' to get a quick tutorial for the basics of the game. When finished you will receive a common pet from the location that you're in.";
        public String vehiclesMessage = "You can buy vehicles in the building behind spawn to the left, purchase with credits or money, spawn your vehicle with '/vehicles' or walk to a dock to spawn your water vehicles.";
        public String variantsMessage = "Variant odds are, Albino 1/5000, Melanistic 1/10000, Trophy 1/15000 and Fabled 1/3000";
        public String wikiMessage = "You can learn more at wiki.fishonmc.net";
        public String xpmoneyMessage = "Bonus XP boosts player, location, and crew XP. Bonus Money increases earnings from catches.";
    }
}