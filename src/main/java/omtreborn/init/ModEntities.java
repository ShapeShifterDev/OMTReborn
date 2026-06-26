package omtreborn.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.entity.projectiles.BlazingClayProjectile;
import omtreborn.entity.projectiles.BulletProjectile;
import omtreborn.entity.projectiles.DisposableItemProjectile;
import omtreborn.entity.projectiles.GrenadeProjectile;
import omtreborn.entity.projectiles.LaserBoltProjectile;
import omtreborn.entity.projectiles.PlasmaProjectile;
import omtreborn.entity.projectiles.PotatoProjectile;
import omtreborn.entity.projectiles.RailGunBoltProjectile;
import omtreborn.entity.projectiles.RocketProjectile;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OpenModularTurrets.MODID);

    public static final RegistryObject<EntityType<BulletProjectile>> BULLET_PROJECTILE =
            ENTITIES.register("bullet_projectile",
                    () -> EntityType.Builder.<BulletProjectile>of(BulletProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).noSave().build(null));

    public static final RegistryObject<EntityType<BlazingClayProjectile>> BLAZING_CLAY_PROJECTILE =
            ENTITIES.register("blazing_clay_projectile",
                    () -> EntityType.Builder.<BlazingClayProjectile>of(BlazingClayProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).noSave().build(null));

    public static final RegistryObject<EntityType<DisposableItemProjectile>> DISPOSABLE_ITEM_PROJECTILE =
            ENTITIES.register("disposable_item_projectile",
                    () -> EntityType.Builder.<DisposableItemProjectile>of(DisposableItemProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).noSave().build(null));

    public static final RegistryObject<EntityType<PotatoProjectile>> POTATO_PROJECTILE =
            ENTITIES.register("potato_projectile",
                    () -> EntityType.Builder.<PotatoProjectile>of(PotatoProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f).noSave().build(null));

    public static final RegistryObject<EntityType<GrenadeProjectile>> GRENADE_PROJECTILE =
            ENTITIES.register("grenade_projectile",
                    () -> EntityType.Builder.<GrenadeProjectile>of(GrenadeProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f).noSave().build(null));

    public static final RegistryObject<EntityType<PlasmaProjectile>> PLASMA_PROJECTILE =
            ENTITIES.register("plasma_projectile",
                    () -> EntityType.Builder.<PlasmaProjectile>of(PlasmaProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f).noSave().build(null));

    public static final RegistryObject<EntityType<RocketProjectile>> ROCKET_PROJECTILE =
            ENTITIES.register("rocket_projectile",
                    () -> EntityType.Builder.<RocketProjectile>of(RocketProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f).noSave().build(null));

    public static final RegistryObject<EntityType<LaserBoltProjectile>> LASER_BOLT_PROJECTILE =
            ENTITIES.register("laser_bolt_projectile",
                    () -> EntityType.Builder.<LaserBoltProjectile>of(LaserBoltProjectile::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f).noSave().build(null));

    public static final RegistryObject<EntityType<RailGunBoltProjectile>> RAIL_GUN_BOLT_PROJECTILE =
            ENTITIES.register("rail_gun_bolt_projectile",
                    () -> EntityType.Builder.<RailGunBoltProjectile>of(RailGunBoltProjectile::new, MobCategory.MISC)
                            .sized(0.15f, 0.15f).noSave().build(null));

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
