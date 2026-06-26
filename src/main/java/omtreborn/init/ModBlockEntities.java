package omtreborn.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.tileentity.BaseAddonBlockEntity;
import omtreborn.tileentity.CreativeEnergySourceBlockEntity;
import omtreborn.tileentity.ExpanderBlockEntity;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.tileentity.turrets.*;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OpenModularTurrets.MODID);

    // --- Creative utility ---

    public static final RegistryObject<BlockEntityType<CreativeEnergySourceBlockEntity>> CREATIVE_ENERGY_SOURCE =
            BLOCK_ENTITIES.register("creative_energy_source",
                    () -> BlockEntityType.Builder.of(CreativeEnergySourceBlockEntity::new,
                            ModBlocks.CREATIVE_ENERGY_SOURCE.get()).build(null));

    // --- Structural ---

    public static final RegistryObject<BlockEntityType<TurretBaseBlockEntity>> TURRET_BASE =
            BLOCK_ENTITIES.register("turret_base",
                    () -> BlockEntityType.Builder.of(TurretBaseBlockEntity::new,
                            ModBlocks.TURRET_BASE.get()).build(null));

    public static final RegistryObject<BlockEntityType<ExpanderBlockEntity>> EXPANDER =
            BLOCK_ENTITIES.register("expander",
                    () -> BlockEntityType.Builder.of(ExpanderBlockEntity::new,
                            ModBlocks.EXPANDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BaseAddonBlockEntity>> BASE_ADDON =
            BLOCK_ENTITIES.register("base_addon",
                    () -> BlockEntityType.Builder.of(BaseAddonBlockEntity::new,
                            ModBlocks.BASE_ADDON.get()).build(null));

    // --- Turret heads ---

    public static final RegistryObject<BlockEntityType<DisposableItemTurretBlockEntity>> TURRET_DISPOSABLE_ITEM =
            BLOCK_ENTITIES.register("disposable_item_turret",
                    () -> BlockEntityType.Builder.of(DisposableItemTurretBlockEntity::new,
                            ModBlocks.DISPOSABLE_ITEM_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<PotatoCannonTurretBlockEntity>> TURRET_POTATO_CANNON =
            BLOCK_ENTITIES.register("potato_cannon_turret",
                    () -> BlockEntityType.Builder.of(PotatoCannonTurretBlockEntity::new,
                            ModBlocks.POTATO_CANNON_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<GunTurretBlockEntity>> TURRET_GUN =
            BLOCK_ENTITIES.register("machine_gun_turret",
                    () -> BlockEntityType.Builder.of(GunTurretBlockEntity::new,
                            ModBlocks.MACHINE_GUN_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<IncendiaryTurretBlockEntity>> TURRET_INCENDIARY =
            BLOCK_ENTITIES.register("incendiary_turret",
                    () -> BlockEntityType.Builder.of(IncendiaryTurretBlockEntity::new,
                            ModBlocks.INCENDIARY_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<GrenadeLauncherBlockEntity>> TURRET_GRENADE_LAUNCHER =
            BLOCK_ENTITIES.register("grenade_turret",
                    () -> BlockEntityType.Builder.of(GrenadeLauncherBlockEntity::new,
                            ModBlocks.GRENADE_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<RelativisticTurretBlockEntity>> TURRET_RELATIVISTIC =
            BLOCK_ENTITIES.register("relativistic_turret",
                    () -> BlockEntityType.Builder.of(RelativisticTurretBlockEntity::new,
                            ModBlocks.RELATIVISTIC_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<RocketTurretBlockEntity>> TURRET_ROCKET =
            BLOCK_ENTITIES.register("rocket_turret",
                    () -> BlockEntityType.Builder.of(RocketTurretBlockEntity::new,
                            ModBlocks.ROCKET_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<TeleporterTurretBlockEntity>> TURRET_TELEPORTER =
            BLOCK_ENTITIES.register("teleporter_turret",
                    () -> BlockEntityType.Builder.of(TeleporterTurretBlockEntity::new,
                            ModBlocks.TELEPORTER_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<LaserTurretBlockEntity>> TURRET_LASER =
            BLOCK_ENTITIES.register("laser_turret",
                    () -> BlockEntityType.Builder.of(LaserTurretBlockEntity::new,
                            ModBlocks.LASER_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<RailGunTurretBlockEntity>> TURRET_RAIL_GUN =
            BLOCK_ENTITIES.register("rail_gun_turret",
                    () -> BlockEntityType.Builder.of(RailGunTurretBlockEntity::new,
                            ModBlocks.RAIL_GUN_TURRET.get()).build(null));

    public static final RegistryObject<BlockEntityType<PlasmaLauncherBlockEntity>> TURRET_PLASMA_LAUNCHER =
            BLOCK_ENTITIES.register("plasma_turret",
                    () -> BlockEntityType.Builder.of(PlasmaLauncherBlockEntity::new,
                            ModBlocks.PLASMA_TURRET.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
