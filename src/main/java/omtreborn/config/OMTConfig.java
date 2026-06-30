package omtreborn.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import omtreborn.api.lists.AmmoList;
import omtreborn.api.lists.MobBlacklist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OMTConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final ForgeConfigSpec SPEC;
    public static final ConfigTurrets TURRETS;
    public static final ConfigBases BASES;
    public static final ConfigGeneral GENERAL;
    public static final ConfigMisc MISCELLANEOUS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        TURRETS = new ConfigTurrets(builder);
        BASES = new ConfigBases(builder);
        GENERAL = new ConfigGeneral(builder);
        MISCELLANEOUS = new ConfigMisc(builder);
        SPEC = builder.build();
    }

    public static void onLoad(ModConfigEvent event) {
        parseLists();
    }

    public static void onReload(ModConfigEvent event) {
        parseLists();
    }

    private static void parseLists() {
        parseAmmoAllowList();
        parseMobBlacklist();
    }

    private static final String[] OWN_AMMO = {
        "omtreborn:ammo_bullet", "omtreborn:ammo_blazing_clay",
        "omtreborn:ammo_ferro_slug", "omtreborn:ammo_grenade", "omtreborn:ammo_rocket",
        "omtreborn:throwable_bullet", "omtreborn:throwable_grenade",
        "minecraft:potato", "minecraft:poisonous_potato"
    };

    @SuppressWarnings("deprecation")
    private static void parseAmmoAllowList() {
        try {
            AmmoList.clear();
            for (String entry : GENERAL.stringAmmoAllowList.get()) {
                ResourceLocation id = new ResourceLocation(entry);
                Item item = ForgeRegistries.ITEMS.getValue(id);
                if (item != null) {
                    AmmoList.add(item);
                } else {
                    LOGGER.warn("Ammo allow-list entry '{}' did not resolve to a known item", entry);
                }
            }
            for (String id : OWN_AMMO) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
                if (item != null) AmmoList.addIfAbsent(item);
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing ammo allow-list config", e);
        }
    }

    private static void parseMobBlacklist() {
        try {
            MobBlacklist.clear();
            for (String entry : GENERAL.stringMobBlackList.get()) {
                MobBlacklist.add(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing mob blacklist config", e);
        }
    }

    // -------------------------------------------------------------------------

    public static class ConfigGeneral {

        public final ForgeConfigSpec.ConfigValue<String> recipes;
        public final ForgeConfigSpec.BooleanValue doTurretsKillsDropMobLoot;
        public final ForgeConfigSpec.BooleanValue doLootAddonsOverrideMobLootSetting;
        public final ForgeConfigSpec.BooleanValue useWhitelistForAmmo;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> stringAmmoAllowList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> stringMobBlackList;

        private ConfigGeneral(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            recipes = builder
                    .comment("Which recipes to use. Valid values: auto, enderio, mekanism, vanilla")
                    .define("recipes", "auto");
            doTurretsKillsDropMobLoot = builder
                    .comment("If turret kills drop loot")
                    .define("doTurretsKillsDropMobLoot", true);
            doLootAddonsOverrideMobLootSetting = builder
                    .comment("If loot is disabled, do loot addons enable loot anyway?")
                    .define("doLootAddonsOverrideMobLootSetting", true);
            useWhitelistForAmmo = builder
                    .comment("Use a whitelist for ammo (which items fit into ammo slots of base)?")
                    .define("useWhitelistForAmmo", true);
            stringAmmoAllowList = builder
                    .comment("Registry names of items that fit into ammo slots besides mod's own ammo. Format: 'modid:itemname'")
                    .defineListAllowEmpty("stringAmmoAllowList",
                            List.of("minecraft:cobblestone", "minecraft:oak_planks"),
                            e -> e instanceof String);
            stringMobBlackList = builder
                    .comment("Registry names of entity types that should not be targeted by turrets. Format: 'modid:entity_type'")
                    .defineListAllowEmpty("stringMobBlackList",
                            List.of("minecraft:armor_stand"),
                            e -> e instanceof String);
            builder.pop();
        }
    }

    public static class ConfigMisc {

        public final ForgeConfigSpec.IntValue expanderPowerTierOneCapacity;
        public final ForgeConfigSpec.IntValue expanderPowerTierTwoCapacity;
        public final ForgeConfigSpec.IntValue expanderPowerTierThreeCapacity;
        public final ForgeConfigSpec.IntValue expanderPowerTierFourCapacity;
        public final ForgeConfigSpec.IntValue expanderPowerTierFiveCapacity;
        public final ForgeConfigSpec.IntValue redstoneReactorAddonGen;

        public final ForgeConfigSpec.DoubleValue ferroniteOreFrequency;

        private ConfigMisc(ForgeConfigSpec.Builder builder) {
            builder.push("Miscellaneous");
            ferroniteOreFrequency = builder
                    .comment("Frequency multiplier for Ferronite Ore worldgen in overworld chunks.",
                             "Mirrors vanilla iron ore generation (three distributions: middle, upper, small).",
                             "1.0 = same frequency as iron ore, 2.0 = double rate, 0.5 = half rate, 0 = disabled.")
                    .defineInRange("ferroniteOreFrequency", 1.0, 0.0, 100.0);
            expanderPowerTierOneCapacity = builder.defineInRange("expanderPowerTierOneCapacity", 2500, 1, Integer.MAX_VALUE);
            expanderPowerTierTwoCapacity = builder.defineInRange("expanderPowerTierTwoCapacity", 25000, 1, Integer.MAX_VALUE);
            expanderPowerTierThreeCapacity = builder.defineInRange("expanderPowerTierThreeCapacity", 75000, 1, Integer.MAX_VALUE);
            expanderPowerTierFourCapacity = builder.defineInRange("expanderPowerTierFourCapacity", 250000, 1, Integer.MAX_VALUE);
            expanderPowerTierFiveCapacity = builder.defineInRange("expanderPowerTierFiveCapacity", 5000000, 1, Integer.MAX_VALUE);
            redstoneReactorAddonGen = builder.defineInRange("redstoneReactorAddonGen", 1600, 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static class ConfigTurrets {

        public final ForgeConfigSpec.BooleanValue canRocketsHome;
        public final ForgeConfigSpec.BooleanValue canRocketsHurtEnderDragon;
        public final ForgeConfigSpec.BooleanValue canRocketsDestroyBlocks;
        public final ForgeConfigSpec.BooleanValue canGrenadesDestroyBlocks;
        public final ForgeConfigSpec.BooleanValue canRailgunDestroyBlocks;
        public final ForgeConfigSpec.BooleanValue doTurretsNeedAmmo;
        public final ForgeConfigSpec.BooleanValue globalCanTargetPlayers;
        public final ForgeConfigSpec.BooleanValue globalCanTargetNeutrals;
        public final ForgeConfigSpec.BooleanValue globalCanTargetMobs;
        public final ForgeConfigSpec.BooleanValue turretAlarmSound;
        public final ForgeConfigSpec.BooleanValue turretBreakable;
        public final ForgeConfigSpec.BooleanValue turretDamageTrustedPlayers;
        public final ForgeConfigSpec.DoubleValue turretSoundVolume;
        public final ForgeConfigSpec.IntValue turretTargetSearchTicks;
        public final ForgeConfigSpec.BooleanValue turretWarnMessage;
        public final ForgeConfigSpec.IntValue turretWarningDistance;

        public final TurretSetting disposable_turret;
        public final TurretSetting potato_cannon_turret;
        public final TurretSetting machine_gun_turret;
        public final TurretSetting incendiary_turret;
        public final TurretSetting grenade_turret;
        public final TurretSetting relativistic_turret;
        public final TurretSetting rocket_turret;
        public final TurretSetting teleporter_turret;
        public final TurretSetting laser_turret;
        public final TurretSetting railgun_turret;
        public final TurretSetting plasma_turret;

        private ConfigTurrets(ForgeConfigSpec.Builder builder) {
            builder.push("Turrets");

            canRocketsHome = builder.define("canRocketsHome", false);
            canRocketsHurtEnderDragon = builder.define("canRocketsHurtEnderDragon", false);
            canRocketsDestroyBlocks = builder.define("canRocketsDestroyBlocks", false);
            canGrenadesDestroyBlocks = builder.define("canGrenadesDestroyBlocks", false);
            canRailgunDestroyBlocks = builder.define("canRailgunDestroyBlocks", false);
            doTurretsNeedAmmo = builder.define("doTurretsNeedAmmo", true);
            globalCanTargetPlayers = builder.comment("If turrets can target players").define("globalCanTargetPlayers", true);
            globalCanTargetNeutrals = builder.comment("If turrets can target neutrals (cow, sheep, etc.)").define("globalCanTargetNeutrals", true);
            globalCanTargetMobs = builder.comment("If turrets can target hostile mobs").define("globalCanTargetMobs", true);
            turretAlarmSound = builder.comment("If turrets play audible alarm when a player enters warning range").define("turretAlarmSound", true);
            turretBreakable = builder.comment("If turrets can be broken with tools").define("turretBreakable", true);
            turretDamageTrustedPlayers = builder.comment("If trusted players take damage when accidentally hit").define("turretDamageTrustedPlayers", false);
            turretSoundVolume = builder.comment("Volume of turret firing sounds").defineInRange("turretSoundVolume", 4.0, 0.0, 10.0);
            turretTargetSearchTicks = builder.comment("Ticks between target search cycles").defineInRange("turretTargetSearchTicks", 10, 1, Integer.MAX_VALUE);
            turretWarnMessage = builder.comment("If turrets send a chat warning when a player enters warning range").define("turretWarnMessage", true);
            turretWarningDistance = builder.comment("Extra blocks added to turret range for warning distance").defineInRange("turretWarningDistance", 5, 0, Integer.MAX_VALUE);

            //                                        en    range  minR  fr  dmg    pw    acc  sim  dmpAmp  frUp  rngUp accUp  effUp  rcyNeg rcyAdd
            disposable_turret    = new TurretSetting(builder, "Disposable Turret",   true,  10, 1,  25,  1,      2, 20.0, 4, 0.05, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            potato_cannon_turret = new TurretSetting(builder, "Potato Cannon Turret",true,  12, 1,  20,  1,     10, 20.0, 4, 0.05, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            machine_gun_turret   = new TurretSetting(builder, "Machine Gun Turret",  true,  18, 1,  10,  3,    100, 10.0, 4, 0.06, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            incendiary_turret    = new TurretSetting(builder, "Incendiary Turret",   true,  18, 4,  38,  2,   1200, 26.5, 4, 0.05, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            grenade_turret       = new TurretSetting(builder, "Grenade Turret",      true,  18, 4,  40,  5,    500, 26.5, 3, 0.08, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            relativistic_turret  = new TurretSetting(builder, "Relativistic Turret", true,  12, 2,  40,  0,   7500,  0.0, 4, 0.00, 0.1, 2, 0.00, 0.08, 0.10, 0.05);
            rocket_turret        = new TurretSetting(builder, "Rocket Turret",       true,  30, 5,  42,  8,   5000, 10.0, 3, 0.08, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            teleporter_turret    = new TurretSetting(builder, "Teleporter Turret",   true,  20, 4, 100,  0,  30000,  0.0, 1, 0.00, 0.1, 2, 0.20, 0.08, 0.10, 0.05);
            laser_turret         = new TurretSetting(builder, "Laser Turret",        true,  25, 1,   9,  4,   2500,  5.0, 4, 0.06, 0.125, 2, 0.20, 0.08, 0.10, 0.05);
            railgun_turret       = new TurretSetting(builder, "Railgun Turret",      true,  30, 4,  90, 30,  30000,  0.0, 2, 0.10, 0.2, 2, 0.20, 0.08, 0.10, 0.05);
            plasma_turret        = new TurretSetting(builder, "Plasma Turret",       true,  20, 1,  40, 10,  16000,  0.0, 1, 0.10, 0.2, 1, 0.20, 0.08, 0.10, 0.05);

            builder.pop();
        }
    }

    public static class ConfigBases {

        public final BaseSetting baseTierOne;
        public final BaseSetting baseTierTwo;
        public final BaseSetting baseTierThree;
        public final BaseSetting baseTierFour;
        public final BaseSetting baseTierFive;
        public final ForgeConfigSpec.BooleanValue baseBreakable;

        private ConfigBases(ForgeConfigSpec.Builder builder) {
            builder.push("Bases");
            baseTierOne   = new BaseSetting(builder, "Tier1",    500,      50,   1,  5, 20);
            baseTierTwo   = new BaseSetting(builder, "Tier2",  35000,    100,   1, 10, 30);
            baseTierThree = new BaseSetting(builder, "Tier3", 150000,   1000,   2, 15, 40);
            baseTierFour  = new BaseSetting(builder, "Tier4", 500000,   2500,   3, 20, 50);
            baseTierFive  = new BaseSetting(builder, "Tier5", 1000000, 50000,   4, 25, 60);
            baseBreakable = builder.define("baseBreakable", true);
            builder.pop();
        }
    }
}
