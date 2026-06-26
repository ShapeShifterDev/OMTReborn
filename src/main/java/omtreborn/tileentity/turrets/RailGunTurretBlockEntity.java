package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.RailGunBoltProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModItems;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class RailGunTurretBlockEntity extends ProjectileTurretBlockEntity {

    public RailGunTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_RAIL_GUN.get(), pos, state, 5);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("railGunTurret", OMTConfig.TURRETS.railgun_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.RAILGUN_TURRET_FIRE.get();
    }

    @Override
    public boolean requiresAmmo() { return true; }

    @Override
    public boolean requiresSpecificAmmo() { return true; }

    @Override
    public ItemStack getAmmo() { return new ItemStack(ModItems.FERRO_SLUG.get()); }

    @Override
    protected float getLaunchSoundVolume() { return 0.5625f; }

    @Override
    protected float getLaunchSoundMaxRange() { return 96.0f; }

    @Override
    public float getProjectileGravity() { return 0.0f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new RailGunBoltProjectile(level, this.base);
    }

    @Override
    protected void onShotFired() {
        if (!(level instanceof ServerLevel sl)) return;
        sl.sendParticles(ParticleTypes.SMOKE,
                worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5,
                5, 0.1, 0.1, 0.1, 0.05);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{0, 3, 2, 1, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RailGunTurretBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}
