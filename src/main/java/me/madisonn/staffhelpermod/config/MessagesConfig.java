package me.madisonn.staffhelpermod.config;

import me.fzzyhmstrs.fzzy_config.annotations.Version;
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import me.madisonn.staffhelpermod.StaffHelperClient;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

@Version(version = 1)
@Translatable.Name("Messages Config")
@Translatable.Desc("&7Configure Messages")
public class MessagesConfig extends Config {
    public MessagesConfig() {
        super(Identifier.fromNamespaceAndPath(StaffHelperClient.MOD_ID, "messages_config"));
    }

    @Name("Armor Infusions Message")
    public String armorinfusionsMessage = "You can craft infusion capsules with the recipe in the recipes book. There are three types of infusions Albino, Melanistic and Trophy. The bonus improves by +5% on every tier up to +25% on a single piece.";

    @Name("Artisan Message")
    public String artisanMessage = "To craft or upgrade rod pieces you can go to the artisan at spawn or use '/artisan' (Angler Rank).";

    @Name("Auction Message")
    public String auctionMessage = "You can buy items on the action house by using '/ah', you can also put items up for sale by using '/ah sell price'. There is a 2% tax when listing items and expired items go to '/overflow'.";

    @Name("Bait Message")
    public String baitMessage = "You can use bait by holding your rod and pressing 'Q' or right click it in your inventory. Now you can open the tacklebox and move your bait into it, the bait usage starts from the top left.";

    @Name("Bait Package Message")
    public String baitpackMessage = "You can get bait packages from '/vote', '/ah', '/collections', '/compendium', '/contest', '/tournament', '/event' and power.";

    @Name("Blooming Oasis Message")
    public String bloomingoasisMessage = "The Blooming Oasis is an event that gives +100% pet luck for 15-16 minutes. '/events' to see what event is active at the time.";

    @Name("Calibrator Message")
    public String calibratorMessage = "Calibrate reels and poles for better stats and bonuses. Higher rarity increases costs and bonus percentage. Upgrading in Artisan removes bonus. '/calibrator' (Mariner Rank).";

    @Name("Calibrator Location Message")
    public String calibratorlocMessage = "The Calibrator can be found by going to '/spawn' and turn around, go inside and follow the sign directions.";

    @Name("Chummer Message")
    public String chummerMessage = "The Chummer boosts bite speed for all players within 10 blocks. It lasts 15 minutes after being placed. Common +75, Rare +130, Epic +180, Legendary +250 and Mythical +380.";

    @Name("Contest Message")
    public String contestMessage = "Winning a contest will give you money, location xp, shards, top 3 for a chance for a pet and bait package!";

    @Name("Cosmetics Message")
    public String cosmeticsMessage = "Cosmetics can be bought with credits from '/buy' these are purely cosmetic and will not influence your gameplay.";

    @Name("Craft Message")
    public String craftMessage = "You can use climate shards to craft armor by typing '/craft'! You can't craft helmets, this slot is reserved for cosmetics.";

    @Name("Crew Message")
    public String crewMessage = "You can create a crew '/crew create name' a crew island is mostly used for building and storage! crews are also used to compete for fun!";

    @Name("Cryptid Sighting Message")
    public String cryptidsightingMessage = "Fishing at Cypress Lake between 1AM-3AM, there is a 1/300 change bigfoot will steal your fish, and gives you either a Bigfoot Tooth or Bigfoot Fur, these are used for pet items.";

    @Name("Daily Missions Message")
    public String dailymissionsMessage = "Open the green book and check the bottom-right corner for Daily Missions. Each mission rewards 2-4 shards based on your location. Completing all grants XP, Money, a pet, and Location XP.";

    @Name("Earn Money Message")
    public String earnmoneyMessage = "You can earn money from '/collections', '/ah', '/event', '/vote' and '/quests'.";

    @Name("Event Message")
    public String eventMessage = "Make sure you check '/event' and hover over the book to see how to obtain event points.";

    @Name("Fabled Event Message")
    public String fabledMessage = "The Fabled Event happens twice per day, and has a 1/3000 chance in a location declared in chat. Only one person per fabled event can catch it.";

    @Name("Find NPC Message")
    public String findnpcMessage = "Do '/spawn' walk forwards towards the 6 npc's and then walk to the left you'll find every npc with a huge sign stuck to their building!";

    @Name("FishOnMC Extras R Message")
    public String foerMessage = "You can install the FishOnMC Extras R Mod here modrinth.com/mod/fishonmc-extras-r.";

    @Name("Forge Message")
    public String forgeMessage = "To tier up your armor you can go to the forge at spawn or use '/forge' (Sailor Rank), T2 16 Shards, T3 48 Shards, T4 96 Shards, T5 256 Shards, it's recommended to only forge pieces with 95%+.";

    @Name("Gold Rush Message")
    public String goldrushMessage = "Gold Rush lasts for 15-16m and changes the base chance for shards to 1/25, also has a 1/1000 chance to drop a Prospecting Amulet.";

    @Name("How to Fish Message")
    public String howfishMessage = "Sneak to raise line tension (blue bar right), stop to lower it (left). Fill the green bar to catch the fish!";

    @Name("Identifier Message")
    public String identifierMessage = "You can identify you armor pieces with '/identifier' (Sailor Rank) it goes from 1% to 100%.";

    @Name("Instance Message")
    public String instancesMessage = "If you can't find someone, make sure you're in the same '/instance'.";

    @Name("Location Roll Message")
    public String locationrollMessage = "Each location has a bonus roll per level 1-20, slots can be rolled for Species Size Bonus, General Bonus: luck, prospect and scale, also Sell and XP bonus.";

    @Name("Luck, Scale and Prospect Message")
    public String luckscaleprospectMessage = "Luck boosts fish rarity chance, Scale boosts bigger fish chance, Prospect boosts shard chance: base chance is 1/50.";

    @Name("Moon Events Message")
    public String mooneventsMessage = "Moon Events occur every full moon and last 7 minutes, Full Moon +120 Bite Speed, Blue Moon +120 Bite Speed and 5x XP, Super Moon +120 Bite Speed and +150 Reel Speed, Blood Moon +120 Bite Speed and 1/300 chance for an infusion capsule.";

    @Name("Overflow Message")
    public String overflowMessage = "Type '/overflow' to check if you have any lost loot from previous contests. Make sure you have some free slots in your inventory.";

    @Name("Pet Drop Message")
    public String petdropMessage = "Pet Drop odds are, Common 1/750, Rare 1/4000, Epic 1/10000, Legendary 1/30000 and Mythical 1/80000!";

    @Name("Pet Merge Message")
    public String petmergeMessage = "You can merge 2 pets that are the same type and rarity. Both must be level 100, right click one of them in your inventory, then left click the other one.";

    @Name("Power Message")
    public String powerMessage = "Power comes from collections, compendiums and unique fish, Power will give you a bunch of rewards and free quest slots to unlock!.";

    @Name("Presets Message")
    public String presetsMessage = "Presets are used for storing armor and rod parts, use '/presets' to open.";

    @Name("Quests Message")
    public String questsMessage = "There are Easy, Medium (lvl 20) and Hard (lvl 50) quests, each tier gives more rewards and at hard quests you get shards and a chance for a pet!";

    @Name("Rainbow Message")
    public String rainbowMessage = "Rainbows have a 1 in 4 chance to spawn after it stops raining and grants +500 luck and lasts for 3-7 minutes.";

    @Name("Rain Shower Message")
    public String rainshowerMessage = "Rain can start randomly every 1 to 2 hours, lasts 8-10 minutes and gives +50 Bite Speed.";

    @Name("Recipes Message")
    public String recipesMessage = "Make sure to check out recipes to see the crafting patterns, open your green book and click the Recipes book.";

    @Name("Reel, Bite and Line Message")
    public String reelbitelineMessage = "Reel Speed increases progress per tick, Bite Speed reduces wait time for bites, Line Strength makes catching fish easier.";

    @Name("Scrapper Message")
    public String scrapperMessage = "To scrap your armor pieces you can go to the scrapper at spawn or use '/scrapper' (Sailor Rank) to scrap your pieces and get some shards in return!";

    @Name("Show Item Message")
    public String showitemMessage = "You can showcase an item simply by holding it in your hand, then type in chat '[ item]' (no space).";

    @Name("Sitting Message")
    public String sittingMessage = "To find places where you can sit down you need to enable F3+B to find interactable hitboxes, click these spots to sit down and cast your rod!";

    @Name("Store Message")
    public String storeMessage = "You can check out the store page by doing '/buy', to purchase ranks, boosters and credits!";

    @Name("Supercell Storm Message")
    public String supercellstormMessage = "A Supercell is a buffed version of the Thunderstorm and happens 1 in 15 times that it starts to rain. This storm gives +150 bite speed and the chance for a Lightning in a Bottle is 1/250 and lasts for around 4-9 minutes.";

    @Name("Supercharge Message")
    public String superchargeMessage = "For a supercharge you need 1 lightning bottle and 8 shards of choice, increases base stats by each tier 1.1x, 1.25x and 1.5x and the effect lasts for x amount of catches.";

    @Name("Tackle Shop Message")
    public String tackleshopMessage = "The Tackle Shop is for bait (unique per player), gives 7 different bait and will reroll every 4 hours, from Common Bait all the wait to extremely rare Variant Bait.";

    @Name("Tackle Shop Location Message")
    public String tackleshoplocMessage = "The Tackle Shop can be found by going to '/spawn' and turn around, go inside and follow the sign directions.";

    @Name("Thunderstorm Message")
    public String thunderstormMessage = "Thunderstorms spawn 1 in 3 times when it starts to rain and grants +100 bite speed as well as a 1/500 chance to fish up a lightning bottle and lasts for around 5-11 minutes.";

    @Name("Tournaments Message")
    public String tournamentsMessage = "Tournaments happen every weekend for 1 location, catch 5 fish and if bigger the lowest one will get replaces! '/tournaments' to open and see your stats/bracket and rewards!";

    @Name("Tutorial Message")
    public String tutorialMessage = "You can type '/tasks' to get a quick tutorial for the basics of the game. When finished you will receive a common pet from the location that you're in.";

    @Name("Vehicles Message")
    public String vehiclesMessage = "You can buy vehicles in the building behind spawn to the left, purchase with credits or money, spawn your vehicle with '/vehicles' or walk to a dock to spawn your water vehicles.";

    @Name("Variants Message")
    public String variantsMessage = "Variant odds are, Albino 1/5000, Melanistic 1/10000, Trophy 1/15000 and Fabled 1/3000";

    @Name("Wiki Message")
    public String wikiMessage = "You can learn more at wiki.fishonmc.net";

    @Name("XP and Money Message")
    public String xpmoneyMessage = "Bonus XP boosts player, location, and crew XP. Bonus Money increases earnings from catches.";

    @Override
    public @NotNull FileType fileType() {
        return FileType.JSONC;
    }
}
