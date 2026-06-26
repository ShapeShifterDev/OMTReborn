package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.LaserBoltProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class LaserTurretBlockEntity extends ProjectileTurretBlockEntity {

    public LaserTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_LASER.get(), pos, state, 5);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("laserTurret", OMTConfig.TURRETS.laser_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.LASER_TURRET_FIRE.get();
    }

    @Override
    public boolean requiresAmmo() { return false; }

    @Override
    public boolean requiresSpecificAmmo() { return false; }

    @Override
    protected float getLaunchSoundVolume() { return 0.25f; }

    @Override
    protected float getLaunchSoundMaxRange() { return 64.0f; }

    @Override
    public float getProjectileGravity() { return 0.0f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new LaserBoltProjectile(level, this.base);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LaserTurretBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}
