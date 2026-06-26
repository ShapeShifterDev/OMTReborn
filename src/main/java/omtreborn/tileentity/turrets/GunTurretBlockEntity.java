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
import omtreborn.entity.projectiles.BulletProjectile;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModItems;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;

public class GunTurretBlockEntity extends ProjectileTurretBlockEntity {

    public GunTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_GUN.get(), pos, state, 2);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("gunTurret", OMTConfig.TURRETS.machine_gun_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.MACHINE_GUN_LAUNCH.get();
    }

    @Override
    public boolean requiresAmmo() { return true; }

    @Override
    public boolean requiresSpecificAmmo() { return true; }

    @Override
    public ItemStack getAmmo() { return new ItemStack(ModItems.BULLET.get()); }

    @Override
    public float getProjectileGravity() { return 0.00f; }

    @Override
    public TurretProjectile createProjectile(Level level, Entity target, ItemStack ammo) {
        return new BulletProjectile(level, this.base);
    }

    @Override
    protected float getLaunchSoundVolume() { return 2.0f; }

    @Override
    protected void onShotFired() {
        if (!(level instanceof ServerLevel sl)) return;
        sl.sendParticles(ParticleTypes.SMOKE,
                worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5,
                2, 0.1, 0.1, 0.1, 0.05);
    }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GunTurretBlockEntity be) {
        tickDirected(level, pos, state, be);
    }
}
