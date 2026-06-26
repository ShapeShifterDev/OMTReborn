package omtreborn.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.items.ExpanderItem;
import omtreborn.items.TurretBaseItem;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OpenModularTurrets.MODID);

    public static final RegistryObject<CreativeModeTab> OMT_TAB = CREATIVE_MODE_TABS.register("omtreborn_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.omtreborn"))
                    .icon(() -> TurretBaseItem.ofTier(new ItemStack(ModItems.TURRET_BASE_ITEM.get()), 1))
                    .build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
        bus.addListener(ModCreativeTab::buildContents);
    }

    private static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(OMT_TAB.getKey())) return;

        // Turret Base: one entry per tier (1-5)
        ItemStack baseBase = new ItemStack(ModItems.TURRET_BASE_ITEM.get());
        for (int tier = 1; tier <= 5; tier++) {
            event.accept(TurretBaseItem.ofTier(baseBase, tier));
        }

        // Expander: inventory tiers 1-5 (types 5-9) then power tiers 1-5 (types 0-4)
        ItemStack baseExpander = new ItemStack(ModItems.EXPANDER_ITEM.get());
        for (int type = 5; type <= 9; type++) {
            event.accept(ExpanderItem.ofType(baseExpander, type));
        }
        for (int type = 0; type <= 4; type++) {
            event.accept(ExpanderItem.ofType(baseExpander, type));
        }

        // Base addon
        event.accept(ModItems.BASE_ADDON_ITEM);

        // Turret heads
        event.accept(ModItems.DISPOSABLE_ITEM_TURRET_ITEM);
        event.accept(ModItems.POTATO_CANNON_TURRET_ITEM);
        event.accept(ModItems.MACHINE_GUN_TURRET_ITEM);
        event.accept(ModItems.INCENDIARY_TURRET_ITEM);
        event.accept(ModItems.GRENADE_TURRET_ITEM);
        event.accept(ModItems.RELATIVISTIC_TURRET_ITEM);
        event.accept(ModItems.ROCKET_TURRET_ITEM);
        event.accept(ModItems.TELEPORTER_TURRET_ITEM);
        event.accept(ModItems.LASER_TURRET_ITEM);
        event.accept(ModItems.RAIL_GUN_TURRET_ITEM);
        event.accept(ModItems.PLASMA_TURRET_ITEM);

        // Addons
        event.accept(ModItems.DAMAGE_AMP_ADDON);
        event.accept(ModItems.POTENTIA_ADDON);
        event.accept(ModItems.RECYCLER_ADDON);
        event.accept(ModItems.FAKE_DROPS_ADDON);

        // Upgrades
        event.accept(ModItems.ACCURACY_UPGRADE);
        event.accept(ModItems.EFFICIENCY_UPGRADE);
        event.accept(ModItems.FIRE_RATE_UPGRADE);
        event.accept(ModItems.RANGE_UPGRADE);
        event.accept(ModItems.SCATTER_SHOT_UPGRADE);

        // Ammo
        event.accept(ModItems.BLAZING_CLAY);
        event.accept(ModItems.BULLET);
        event.accept(ModItems.FERRO_SLUG);
        event.accept(ModItems.GRENADE);
        event.accept(ModItems.ROCKET);
        // Tiered components
        event.accept(ModItems.SENSOR_TIER_ONE);
        event.accept(ModItems.SENSOR_TIER_TWO);
        event.accept(ModItems.SENSOR_TIER_THREE);
        event.accept(ModItems.SENSOR_TIER_FOUR);
        event.accept(ModItems.SENSOR_TIER_FIVE);
        event.accept(ModItems.CHAMBER_TIER_ONE);
        event.accept(ModItems.CHAMBER_TIER_TWO);
        event.accept(ModItems.CHAMBER_TIER_THREE);
        event.accept(ModItems.CHAMBER_TIER_FOUR);
        event.accept(ModItems.CHAMBER_TIER_FIVE);
        event.accept(ModItems.BARREL_TIER_ONE);
        event.accept(ModItems.BARREL_TIER_TWO);
        event.accept(ModItems.BARREL_TIER_THREE);
        event.accept(ModItems.BARREL_TIER_FOUR);
        event.accept(ModItems.BARREL_TIER_FIVE);

        // Regular components
        event.accept(ModItems.IO_BUS);

        // Ferronite content
        event.accept(ModItems.FERRONITE_ORE_ITEM);
        event.accept(ModItems.DEEPSLATE_FERRONITE_ORE_ITEM);
        event.accept(ModItems.BLOCK_OF_FERRONITE_ITEM);
        event.accept(ModItems.RAW_FERRONITE);
        event.accept(ModItems.FERRONITE_INGOT);
        event.accept(ModItems.FERRONITE_NUGGET);
        event.accept(ModItems.FERRONITE_COIL);
        event.accept(ModItems.FERRONITE_FRAME);
        event.accept(ModItems.FERRONITE_LENS);
        event.accept(ModItems.BLAZING_FERRONITE);
        event.accept(ModItems.NETHRONITE);

        // Creative utility
        event.accept(ModItems.CREATIVE_ENERGY_SOURCE_ITEM);
    }
}
