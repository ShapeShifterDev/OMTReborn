package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import omtreborn.config.OMTConfig;
import omtreborn.entity.projectiles.TurretProjectile;
import omtreborn.turret.TurretHeadUtil;

import java.util.Random;

public abstract class ProjectileTurretBlockEntity extends AbstractDirectedTurretBlockEntity {

    protected ProjectileTurretBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int turretTier) {
        super(type, pos, state, turretTier);
    }

    protected abstract float getProjectileGravity();

    public abstract TurretProjectile createProjectile(Level world, Entity target, ItemStack ammo);

    protected void onShotFired() {}

    @Override
    protected void doTargetedShot(LivingEntity target, ItemStack ammo) {
        if (level == null) return;
        // Track target motion
        double speedX = target instanceof ServerPlayer sp ? targetSpeedX : target.getX() - target.xOld;
        double speedY = target instanceof ServerPlayer sp ? targetSpeedY : target.getY() - target.yOld;
        double speedZ = target instanceof ServerPlayer sp ? targetSpeedZ : target.getZ() - target.zOld;

        double d0 = target.getX() - (worldPosition.getX() + 0.5);
        double d1 = target.getEyeY() - (worldPosition.getY() + 0.5);
        double d2 = target.getZ() - (worldPosition.getZ() + 0.5);

        double dist = Math.sqrt(d0*d0 + d1*d1 + d2*d2);
        double inaccuracy = getActualTurretAccuracyDeviation() / 20.0;

        double time = dist / (getProjectileGravity() == 0f ? 3.0 : 1.6);
        double adjustedX = d0 + speedX * time;
        double adjustedY = d1 + speedY * time;
        double adjustedZ = d2 + speedZ * time;

        double dist2 = Math.sqrt(adjustedX*adjustedX + adjustedY*adjustedY + adjustedZ*adjustedZ);
        float speedFactor = (float)(dist2 / dist);

        shootProjectile(adjustedX, adjustedY - 0.1, adjustedZ, 3.0f * speedFactor, (float) inaccuracy, ammo);
    }

    @Override
    public boolean forceShot() {
        if (level == null || base == null) return false;
        if (isOnCooldown()) return false;

        ItemStack ammo = getAmmoStack();
        if (ammo.isEmpty() && requiresAmmo() && OMTConfig.TURRETS.doTurretsNeedAmmo.get()) return false;

        base.getEnergyStorage().extractEnergy(getPowerRequiredForNextShot(), false);

        int scatter = cachedScattershot;
        double accuracy = getActualTurretAccuracyDeviation() / 20.0;
        Random rand = new Random();

        for (int i = 0; i <= scatter; i++) {
            TurretProjectile projectile = createProjectile(level, target, ammo);
            if (projectile == null) continue;
            projectile.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
            Vec3 velocity = getVelocityFromYawPitch(yaw, pitch,
                    getProjectileGravity() == 0f ? 3.0f : 1.6f);
            if (getProjectileGravity() == 0f) {
                projectile.shoot(velocity.x, velocity.y, velocity.z, (float) velocity.length(), (float) accuracy);
            } else {
                double mx = velocity.x + rand.nextGaussian() * 0.0075 * accuracy;
                double my = velocity.y + rand.nextGaussian() * 0.0075 * accuracy;
                double mz = velocity.z + rand.nextGaussian() * 0.0075 * accuracy;
                projectile.setDeltaMovement(mx, my, mz);
            }
            level.addFreshEntity(projectile);
        }
        float amplitude = Math.min(1.0f, getLaunchSoundVolume()) * OMTConfig.TURRETS.turretSoundVolume.get().floatValue();
        playTurretSoundWithAttenuation(level, worldPosition, getLaunchSoundEffect(), getLaunchSoundMaxRange(), amplitude);
        onShotFired();
        ticks = 0;
        return true;
    }

    protected void shootProjectile(double dx, double dy, double dz, float speed, float inaccuracy, ItemStack ammo) {
        if (level == null || base == null) return;
        base.getEnergyStorage().extractEnergy(getPowerRequiredForNextShot(), false);

        int scatter = cachedScattershot;
        int ampLevel = TurretHeadUtil.getAmpLevel(base);
        Random rand = new Random();

        for (int i = 0; i <= scatter; i++) {
            TurretProjectile projectile = createProjectile(level, target, ammo);
            if (projectile == null) continue;
            projectile.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5);
            projectile.shoot(dx, dy, dz, speed, inaccuracy);
            if (ampLevel != 0) {
                projectile.ampLevel = ampLevel;
                projectile.isAmped = true;
            }
            level.addFreshEntity(projectile);
        }
        float amplitude = Math.min(1.0f, getLaunchSoundVolume()) * OMTConfig.TURRETS.turretSoundVolume.get().floatValue();
        playTurretSoundWithAttenuation(level, worldPosition, getLaunchSoundEffect(), getLaunchSoundMaxRange(), amplitude);
        onShotFired();
    }

    public static Vec3 getVelocityFromYawPitch(float yawDeg, float pitchDeg, float speed) {
        double yaw = Math.toRadians(yawDeg);
        double pitch = Math.toRadians(pitchDeg);
        double cosP = Math.cos(pitch);
        double sinP = Math.sin(pitch);
        double cosY = Math.cos(yaw);
        double sinY = Math.sin(yaw);
        return new Vec3(cosP * cosY * speed, sinP * speed, cosP * sinY * speed);
    }
}
