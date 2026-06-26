package omtreborn.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.blocks.AbstractBaseAttachment;
import omtreborn.blocks.BlockBaseAttachment;
import omtreborn.blocks.BlockCreativeEnergySource;
import omtreborn.blocks.BlockExpander;
import omtreborn.blocks.BlockTurretBase;
import omtreborn.blocks.turretheads.*;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OpenModularTurrets.MODID);

    private static BlockBehaviour.Properties base() {
        return BlockBehaviour.Properties.of().strength(2.0f, 6.0f).requiresCorrectToolForDrops();
    }

    private static BlockBehaviour.Properties turretHead(float hardness) {
        return BlockBehaviour.Properties.of().strength(hardness, 6.0f).noOcclusion().requiresCorrectToolForDrops();
    }

    // --- Creative utility ---

    public static final RegistryObject<Block> CREATIVE_ENERGY_SOURCE = BLOCKS.register("creative_energy_source",
            () -> new BlockCreativeEnergySource(BlockBehaviour.Properties.of().strength(1.5f, 6.0f).lightLevel(s -> 15)));

    // --- Structural ---

    public static final RegistryObject<Block> TURRET_BASE = BLOCKS.register("turret_base",
            () -> new BlockTurretBase(base()));

    public static final RegistryObject<Block> EXPANDER = BLOCKS.register("expander",
            () -> new BlockExpander(base().noOcclusion()));

    public static final RegistryObject<Block> BASE_ADDON = BLOCKS.register("base_addon",
            () -> new BlockBaseAttachment(base().noOcclusion()));

    // --- Turret heads ---

    public static final RegistryObject<Block> DISPOSABLE_ITEM_TURRET = BLOCKS.register("disposable_item_turret",
            () -> new BlockDisposableTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> POTATO_CANNON_TURRET = BLOCKS.register("potato_cannon_turret",
            () -> new BlockPotatoCannonTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> MACHINE_GUN_TURRET = BLOCKS.register("machine_gun_turret",
            () -> new BlockGunTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> INCENDIARY_TURRET = BLOCKS.register("incendiary_turret",
            () -> new BlockIncendiaryTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> GRENADE_TURRET = BLOCKS.register("grenade_turret",
            () -> new BlockGrenadeTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> RELATIVISTIC_TURRET = BLOCKS.register("relativistic_turret",
            () -> new BlockRelativisticTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> ROCKET_TURRET = BLOCKS.register("rocket_turret",
            () -> new BlockRocketTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> TELEPORTER_TURRET = BLOCKS.register("teleporter_turret",
            () -> new BlockTeleporterTurret(turretHead(2.0f)));

    public static final RegistryObject<Block> LASER_TURRET = BLOCKS.register("laser_turret",
            () -> new BlockLaserTurret(turretHead(3.5f)));

    public static final RegistryObject<Block> RAIL_GUN_TURRET = BLOCKS.register("rail_gun_turret",
            () -> new BlockRailGunTurret(turretHead(3.5f)));

    public static final RegistryObject<Block> PLASMA_TURRET = BLOCKS.register("plasma_turret",
            () -> new BlockPlasmaTurret(turretHead(3.5f)));

    // --- Ferronite ores and storage ---

    public static final RegistryObject<Block> FERRONITE_ORE = BLOCKS.register("ferronite_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0f, 3.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> DEEPSLATE_FERRONITE_ORE = BLOCKS.register("deepslate_ferronite_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> BLOCK_OF_FERRONITE = BLOCKS.register("block_of_ferronite",
            () -> new Block(BlockBehaviour.Properties.of().strength(5.0f, 6.0f).sound(SoundType.METAL).requiresCorrectToolForDrops()));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
