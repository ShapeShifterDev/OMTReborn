package omtreborn;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import omtreborn.client.render.TurretItemRenderers;
import omtreborn.client.render.renderers.*;
import omtreborn.gui.ExpanderInvScreen;
import omtreborn.gui.TurretBaseScreen;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModEntities;
import omtreborn.init.ModMenuTypes;

@Mod.EventBusSubscriber(modid = OpenModularTurrets.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.TURRET_BASE.get(), TurretBaseScreen::new);
            MenuScreens.register(ModMenuTypes.EXPANDER_INV.get(), ExpanderInvScreen::new);
            TurretItemRenderers.init();
            registerBlockEntityRenderers();
            registerProjectileRenderers();
        });
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.TURRET_GUN.get(), GunTurretBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_DISPOSABLE_ITEM.get(), DisposableItemTurretBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_GRENADE_LAUNCHER.get(), GrenadeLauncherBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_PLASMA_LAUNCHER.get(), PlasmaLauncherBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_LASER.get(), LaserTurretBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_INCENDIARY.get(), IncendiaryTurretBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_POTATO_CANNON.get(), PotatoCannonBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_RAIL_GUN.get(), RailGunBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_ROCKET.get(), RocketTurretBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_RELATIVISTIC.get(), RelativisticTurretBER::new);
        BlockEntityRenderers.register(ModBlockEntities.TURRET_TELEPORTER.get(), TeleporterTurretBER::new);
    }

    @SuppressWarnings("deprecation")
    private static <T extends Entity> void noopRenderer(EntityType<T> type) {
        ResourceLocation tex = new ResourceLocation("minecraft", "textures/particle/snowflake.png");
        EntityRenderers.register(type, ctx -> new EntityRenderer<>(ctx) {
            @Override public ResourceLocation getTextureLocation(T e) { return tex; }
        });
    }

    private static void registerProjectileRenderers() {
        noopRenderer(ModEntities.BULLET_PROJECTILE.get());
        noopRenderer(ModEntities.BLAZING_CLAY_PROJECTILE.get());
        noopRenderer(ModEntities.DISPOSABLE_ITEM_PROJECTILE.get());
        noopRenderer(ModEntities.POTATO_PROJECTILE.get());
        noopRenderer(ModEntities.GRENADE_PROJECTILE.get());
        noopRenderer(ModEntities.PLASMA_PROJECTILE.get());
        noopRenderer(ModEntities.ROCKET_PROJECTILE.get());
        noopRenderer(ModEntities.LASER_BOLT_PROJECTILE.get());
        noopRenderer(ModEntities.RAIL_GUN_BOLT_PROJECTILE.get());
    }
}
