package omtreborn.entity.projectiles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class OMTDamageSources {

    public static final ResourceKey<DamageType> TURRET_NORMAL = ResourceKey.create(
            Registries.DAMAGE_TYPE, new ResourceLocation("omtreborn:turret_normal"));

    public static final ResourceKey<DamageType> TURRET_BYPASS = ResourceKey.create(
            Registries.DAMAGE_TYPE, new ResourceLocation("omtreborn:turret_bypass"));

    public static DamageSource normal(Level level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(TURRET_NORMAL));
    }

    public static DamageSource bypass(Level level) {
        return new DamageSource(level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(TURRET_BYPASS));
    }
}
