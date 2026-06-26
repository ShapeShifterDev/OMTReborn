package omtreborn.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OpenModularTurrets.MODID);

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(
                new ResourceLocation(OpenModularTurrets.MODID + ":" + name)));
    }

    private static RegistryObject<SoundEvent> registerFixed(String name, float range) {
        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(
                new ResourceLocation(OpenModularTurrets.MODID + ":" + name), range));
    }

    public static final RegistryObject<SoundEvent> TURRET_DEPLOY = register("turret_deploy");
    public static final RegistryObject<SoundEvent> TURRET_RETRACT = register("turret_retract");
    public static final RegistryObject<SoundEvent> WARNING = register("warning");
    public static final RegistryObject<SoundEvent> BULLET_HIT = register("bullet_hit");
    public static final RegistryObject<SoundEvent> RAIL_GUN_HIT = register("rail_gun_hit");
    public static final RegistryObject<SoundEvent> LASER_HIT = register("laser_hit");
    public static final RegistryObject<SoundEvent> DISPOSABLE_LAUNCH = register("disposable");
    public static final RegistryObject<SoundEvent> GRENADE_LAUNCH = register("grenade");
    public static final RegistryObject<SoundEvent> MACHINE_GUN_LAUNCH = register("machine_gun");
    public static final RegistryObject<SoundEvent> INCENDIARY_LAUNCH = register("incendiary");
    public static final RegistryObject<SoundEvent> LASER_LAUNCH = register("laser");
    public static final RegistryObject<SoundEvent> POTATO_LAUNCH = register("potato");
    public static final RegistryObject<SoundEvent> RAILGUN_LAUNCH = register("rail_gun");
    public static final RegistryObject<SoundEvent> PLASMA_LAUNCH = register("plasma_launch");
    public static final RegistryObject<SoundEvent> RELATIVISTIC_LAUNCH = register("relativistic");
    public static final RegistryObject<SoundEvent> ROCKET_LAUNCH = register("rocket");
    public static final RegistryObject<SoundEvent> TELEPORT_LAUNCH = register("teleport");
    public static final RegistryObject<SoundEvent> AMPED = register("amped");
    public static final RegistryObject<SoundEvent> LASER_TURRET_FIRE = registerFixed("laser_turret_fire", 64);
    public static final RegistryObject<SoundEvent> RAILGUN_TURRET_FIRE = registerFixed("railgun_turret_fire", 96);
    public static final RegistryObject<SoundEvent> TELEPORTER_TURRET_ACTIVATION = registerFixed("teleporter_turret_activation", 48);

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }
}
