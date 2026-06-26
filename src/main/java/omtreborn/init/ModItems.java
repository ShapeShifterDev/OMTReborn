package omtreborn.init;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.config.OMTConfig;
import omtreborn.items.ExpanderItem;
import omtreborn.items.OMTAddonItem;
import omtreborn.items.OMTAmmoItem;
import omtreborn.items.OMTItem;
import omtreborn.items.OMTUpgradeItem;
import omtreborn.items.TurretBaseItem;
import omtreborn.items.TurretHeadItem;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OpenModularTurrets.MODID);

    private static Item.Properties props() {
        return new Item.Properties();
    }

    // --- Creative utility ---

    public static final RegistryObject<Item> CREATIVE_ENERGY_SOURCE_ITEM = ITEMS.register("creative_energy_source",
            () -> new BlockItem(ModBlocks.CREATIVE_ENERGY_SOURCE.get(), props()));

    // --- Block items: structural ---

    public static final RegistryObject<Item> TURRET_BASE_ITEM = ITEMS.register("turret_base",
            () -> new TurretBaseItem(ModBlocks.TURRET_BASE.get(), props()));

    public static final RegistryObject<Item> EXPANDER_ITEM = ITEMS.register("expander",
            () -> new ExpanderItem(ModBlocks.EXPANDER.get(), props()));

    public static final RegistryObject<Item> BASE_ADDON_ITEM = ITEMS.register("base_addon",
            () -> new BlockItem(ModBlocks.BASE_ADDON.get(), props()));

    // --- Block items: turret heads ---

    public static final RegistryObject<Item> DISPOSABLE_ITEM_TURRET_ITEM = ITEMS.register("disposable_item_turret",
            () -> new TurretHeadItem(ModBlocks.DISPOSABLE_ITEM_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.disposable_turret,
                    "tooltip.omtreborn.turret_head.desc.disposable",
                    "tooltip.omtreborn.turret_head.damage.disposable",
                    "tooltip.omtreborn.turret_head.ammo.disposable",
                    false));

    public static final RegistryObject<Item> POTATO_CANNON_TURRET_ITEM = ITEMS.register("potato_cannon_turret",
            () -> new TurretHeadItem(ModBlocks.POTATO_CANNON_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.potato_cannon_turret,
                    "tooltip.omtreborn.turret_head.desc.potato_cannon",
                    "tooltip.omtreborn.turret_head.damage.potato_cannon",
                    "tooltip.omtreborn.turret_head.ammo.potato_cannon",
                    false));

    public static final RegistryObject<Item> MACHINE_GUN_TURRET_ITEM = ITEMS.register("machine_gun_turret",
            () -> new TurretHeadItem(ModBlocks.MACHINE_GUN_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.machine_gun_turret,
                    "tooltip.omtreborn.turret_head.desc.machine_gun",
                    "tooltip.omtreborn.turret_head.damage.machine_gun",
                    "tooltip.omtreborn.turret_head.ammo.machine_gun",
                    false));

    public static final RegistryObject<Item> INCENDIARY_TURRET_ITEM = ITEMS.register("incendiary_turret",
            () -> new TurretHeadItem(ModBlocks.INCENDIARY_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.incendiary_turret,
                    "tooltip.omtreborn.turret_head.desc.incendiary",
                    "tooltip.omtreborn.turret_head.damage.incendiary",
                    "tooltip.omtreborn.turret_head.ammo.incendiary",
                    false));

    public static final RegistryObject<Item> GRENADE_TURRET_ITEM = ITEMS.register("grenade_turret",
            () -> new TurretHeadItem(ModBlocks.GRENADE_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.grenade_turret,
                    "tooltip.omtreborn.turret_head.desc.grenade",
                    "tooltip.omtreborn.turret_head.damage.grenade",
                    "tooltip.omtreborn.turret_head.ammo.grenade",
                    false));

    public static final RegistryObject<Item> RELATIVISTIC_TURRET_ITEM = ITEMS.register("relativistic_turret",
            () -> new TurretHeadItem(ModBlocks.RELATIVISTIC_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.relativistic_turret,
                    "tooltip.omtreborn.turret_head.desc.relativistic",
                    "tooltip.omtreborn.turret_head.damage.relativistic",
                    null,
                    true));

    public static final RegistryObject<Item> ROCKET_TURRET_ITEM = ITEMS.register("rocket_turret",
            () -> new TurretHeadItem(ModBlocks.ROCKET_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.rocket_turret,
                    "tooltip.omtreborn.turret_head.desc.rocket",
                    "tooltip.omtreborn.turret_head.damage.rocket",
                    "tooltip.omtreborn.turret_head.ammo.rocket",
                    false));

    public static final RegistryObject<Item> TELEPORTER_TURRET_ITEM = ITEMS.register("teleporter_turret",
            () -> new TurretHeadItem(ModBlocks.TELEPORTER_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.teleporter_turret,
                    "tooltip.omtreborn.turret_head.desc.teleporter",
                    "tooltip.omtreborn.turret_head.damage.teleporter",
                    null,
                    true));

    public static final RegistryObject<Item> LASER_TURRET_ITEM = ITEMS.register("laser_turret",
            () -> new TurretHeadItem(ModBlocks.LASER_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.laser_turret,
                    "tooltip.omtreborn.turret_head.desc.laser",
                    "tooltip.omtreborn.turret_head.damage.laser",
                    null,
                    false));

    public static final RegistryObject<Item> RAIL_GUN_TURRET_ITEM = ITEMS.register("rail_gun_turret",
            () -> new TurretHeadItem(ModBlocks.RAIL_GUN_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.railgun_turret,
                    "tooltip.omtreborn.turret_head.desc.rail_gun",
                    "tooltip.omtreborn.turret_head.damage.rail_gun",
                    "tooltip.omtreborn.turret_head.ammo.rail_gun",
                    false));

    public static final RegistryObject<Item> PLASMA_TURRET_ITEM = ITEMS.register("plasma_turret",
            () -> new TurretHeadItem(ModBlocks.PLASMA_TURRET.get(), props(),
                    () -> OMTConfig.TURRETS.plasma_turret,
                    "tooltip.omtreborn.turret_head.desc.plasma",
                    "tooltip.omtreborn.turret_head.damage.plasma",
                    null,
                    false));

    // --- Addons (concealer and solar panel dropped) ---

    private static final String ADDON_LABEL = "tooltip.omtreborn.addon.label";

    public static final RegistryObject<Item> DAMAGE_AMP_ADDON = ITEMS.register("addon_damage_amp",
            () -> new OMTAddonItem(props(), ADDON_LABEL, ChatFormatting.RED, "tooltip.omtreborn.addon.damage_amp"));

    public static final RegistryObject<Item> POTENTIA_ADDON = ITEMS.register("addon_potentia",
            () -> new OMTAddonItem(props(), ADDON_LABEL, ChatFormatting.RED, "tooltip.omtreborn.addon.potentia"));

    public static final RegistryObject<Item> RECYCLER_ADDON = ITEMS.register("addon_recycler",
            () -> new OMTAddonItem(props(), ADDON_LABEL, ChatFormatting.RED, "tooltip.omtreborn.addon.recycler"));

    public static final RegistryObject<Item> FAKE_DROPS_ADDON = ITEMS.register("addon_fake_drops",
            () -> new OMTAddonItem(props(), ADDON_LABEL, ChatFormatting.RED, "tooltip.omtreborn.addon.fake_drops"));

    // --- Upgrades ---

    private static final String UPGRADE_LABEL = "tooltip.omtreborn.upgrade.label";

    public static final RegistryObject<Item> ACCURACY_UPGRADE = ITEMS.register("upgrade_accuracy",
            () -> new OMTUpgradeItem(props(), UPGRADE_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.upgrade.accuracy"));

    public static final RegistryObject<Item> EFFICIENCY_UPGRADE = ITEMS.register("upgrade_efficiency",
            () -> new OMTUpgradeItem(props(), UPGRADE_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.upgrade.efficiency"));

    public static final RegistryObject<Item> FIRE_RATE_UPGRADE = ITEMS.register("upgrade_fire_rate",
            () -> new OMTUpgradeItem(props(), UPGRADE_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.upgrade.fire_rate"));

    public static final RegistryObject<Item> RANGE_UPGRADE = ITEMS.register("upgrade_range",
            () -> new OMTUpgradeItem(props(), UPGRADE_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.upgrade.range"));

    public static final RegistryObject<Item> SCATTER_SHOT_UPGRADE = ITEMS.register("upgrade_scatter_shot",
            () -> new OMTUpgradeItem(props(), UPGRADE_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.upgrade.scatter_shot"));

    // --- Ammo ---

    private static final String AMMO_LABEL = "tooltip.omtreborn.ammo.label";

    public static final RegistryObject<Item> BLAZING_CLAY = ITEMS.register("ammo_blazing_clay",
            () -> new OMTAmmoItem(props().stacksTo(64), AMMO_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.ammo.blazing_clay"));

    public static final RegistryObject<Item> BULLET = ITEMS.register("ammo_bullet",
            () -> new OMTAmmoItem(props().stacksTo(64), AMMO_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.ammo.bullet"));

    public static final RegistryObject<Item> FERRO_SLUG = ITEMS.register("ammo_ferro_slug",
            () -> new OMTAmmoItem(props().stacksTo(64), AMMO_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.ammo.ferro_slug"));

    public static final RegistryObject<Item> GRENADE = ITEMS.register("ammo_grenade",
            () -> new OMTAmmoItem(props().stacksTo(64), AMMO_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.ammo.grenade"));

    public static final RegistryObject<Item> ROCKET = ITEMS.register("ammo_rocket",
            () -> new OMTAmmoItem(props().stacksTo(64), AMMO_LABEL, ChatFormatting.BLUE, "tooltip.omtreborn.ammo.rocket"));

    // --- Throwables (right-click behavior in Phase 7) ---

    public static final RegistryObject<Item> THROWABLE_BULLET = ITEMS.register("throwable_bullet",
            () -> new Item(props().stacksTo(1)));

    public static final RegistryObject<Item> THROWABLE_GRENADE = ITEMS.register("throwable_grenade",
            () -> new Item(props().stacksTo(1)));

    // --- Tiered intermediate products (no tooltip — crafting components only) ---

    public static final RegistryObject<Item> SENSOR_TIER_ONE   = ITEMS.register("sensor_tier_one",   () -> new Item(props()));
    public static final RegistryObject<Item> SENSOR_TIER_TWO   = ITEMS.register("sensor_tier_two",   () -> new Item(props()));
    public static final RegistryObject<Item> SENSOR_TIER_THREE = ITEMS.register("sensor_tier_three", () -> new Item(props()));
    public static final RegistryObject<Item> SENSOR_TIER_FOUR  = ITEMS.register("sensor_tier_four",  () -> new Item(props()));
    public static final RegistryObject<Item> SENSOR_TIER_FIVE  = ITEMS.register("sensor_tier_five",  () -> new Item(props()));

    public static final RegistryObject<Item> CHAMBER_TIER_ONE   = ITEMS.register("chamber_tier_one",   () -> new Item(props()));
    public static final RegistryObject<Item> CHAMBER_TIER_TWO   = ITEMS.register("chamber_tier_two",   () -> new Item(props()));
    public static final RegistryObject<Item> CHAMBER_TIER_THREE = ITEMS.register("chamber_tier_three", () -> new Item(props()));
    public static final RegistryObject<Item> CHAMBER_TIER_FOUR  = ITEMS.register("chamber_tier_four",  () -> new Item(props()));
    public static final RegistryObject<Item> CHAMBER_TIER_FIVE  = ITEMS.register("chamber_tier_five",  () -> new Item(props()));

    public static final RegistryObject<Item> BARREL_TIER_ONE   = ITEMS.register("barrel_tier_one",   () -> new Item(props()));
    public static final RegistryObject<Item> BARREL_TIER_TWO   = ITEMS.register("barrel_tier_two",   () -> new Item(props()));
    public static final RegistryObject<Item> BARREL_TIER_THREE = ITEMS.register("barrel_tier_three", () -> new Item(props()));
    public static final RegistryObject<Item> BARREL_TIER_FOUR  = ITEMS.register("barrel_tier_four",  () -> new Item(props()));
    public static final RegistryObject<Item> BARREL_TIER_FIVE  = ITEMS.register("barrel_tier_five",  () -> new Item(props()));

    // --- Regular intermediate products ---

    public static final RegistryObject<Item> IO_BUS = ITEMS.register("io_bus",
            () -> new Item(props()));

    // --- Ferronite block items ---

    public static final RegistryObject<Item> FERRONITE_ORE_ITEM = ITEMS.register("ferronite_ore",
            () -> new BlockItem(ModBlocks.FERRONITE_ORE.get(), props()));

    public static final RegistryObject<Item> DEEPSLATE_FERRONITE_ORE_ITEM = ITEMS.register("deepslate_ferronite_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_FERRONITE_ORE.get(), props()));

    public static final RegistryObject<Item> BLOCK_OF_FERRONITE_ITEM = ITEMS.register("block_of_ferronite",
            () -> new BlockItem(ModBlocks.BLOCK_OF_FERRONITE.get(), props()));

    // --- Ferronite materials ---

    public static final RegistryObject<Item> RAW_FERRONITE = ITEMS.register("raw_ferronite",
            () -> new Item(props()));

    public static final RegistryObject<Item> FERRONITE_INGOT = ITEMS.register("ferronite_ingot",
            () -> new Item(props()));

    public static final RegistryObject<Item> FERRONITE_COIL = ITEMS.register("ferronite_coil",
            () -> new Item(props()));

    public static final RegistryObject<Item> FERRONITE_FRAME = ITEMS.register("ferronite_frame",
            () -> new Item(props()));

    public static final RegistryObject<Item> FERRONITE_LENS = ITEMS.register("ferronite_lens",
            () -> new Item(props()));

    public static final RegistryObject<Item> BLAZING_FERRONITE = ITEMS.register("blazing_ferronite",
            () -> new Item(props()));

    public static final RegistryObject<Item> NETHRONITE = ITEMS.register("nethronite",
            () -> new Item(props()));

    public static final RegistryObject<Item> FERRONITE_NUGGET = ITEMS.register("ferronite_nugget",
            () -> new Item(props()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
