package omtreborn.client.render;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.client.render.models.*;
import omtreborn.init.ModBlocks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class TurretItemRenderers {

    private static final Map<ResourceKey<Block>, TurretHeadItemRenderer> RENDERERS = new HashMap<>();

    public static void init() {
        register(ModBlocks.MACHINE_GUN_TURRET,      ModelMachineGun::new,          "machine_gun_turret");
        register(ModBlocks.DISPOSABLE_ITEM_TURRET,  ModelDisposableItemTurret::new, "disposable_item_turret");
        register(ModBlocks.GRENADE_TURRET,          ModelGrenadeLauncher::new,      "grenade_turret");
        register(ModBlocks.PLASMA_TURRET,           ModelGrenadeLauncher::new,      "grenade_turret");
        register(ModBlocks.LASER_TURRET,            ModelLaserTurret::new,          "laser_turret");
        register(ModBlocks.INCENDIARY_TURRET,       ModelIncendiaryTurret::new,     "incendiary_turret");
        register(ModBlocks.POTATO_CANNON_TURRET,    ModelPotatoCannon::new,         "potato_cannon_turret");
        register(ModBlocks.RAIL_GUN_TURRET,         ModelRailgun::new,              "rail_gun_turret");
        register(ModBlocks.ROCKET_TURRET,           ModelRocketTurret::new,         "rocket_turret");
        register(ModBlocks.RELATIVISTIC_TURRET,     ModelRelativisticTurret::new,   "relativistic_turret");
        register(ModBlocks.TELEPORTER_TURRET,       ModelTeleporterTurret::new,     "teleporter_turret");
    }

    private static void register(RegistryObject<Block> blockObj,
                                  Supplier<AbstractTurretModel> modelFactory,
                                  String texName) {
        ResourceLocation texture = new ResourceLocation(OpenModularTurrets.MODID, "textures/block/" + texName + ".png");
        RENDERERS.put(blockObj.getKey(), new TurretHeadItemRenderer(modelFactory.get(), texture));
    }

    public static BlockEntityWithoutLevelRenderer get(Block block) {
        if (block == null) return null;
        ResourceKey<Block> key = block.builtInRegistryHolder().key();
        return RENDERERS.get(key);
    }
}
