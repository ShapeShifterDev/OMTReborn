package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import omtreborn.config.OMTConfig;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.turret.TurretHeadUtil;
import omtreborn.turret.TurretTargetingUtils;
import omtreborn.util.OMTUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class RayTracingTurretBlockEntity extends AbstractDirectedTurretBlockEntity {

    protected RayTracingTurretBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int turretTier) {
        super(type, pos, state, turretTier);
    }

    protected abstract void renderRay(Vec3 start, Vec3 end);

    protected abstract SoundEvent getHitSound();

    protected abstract float getDamageModifier(Entity entity);

    protected abstract float getNormalDamageFactor();

    protected abstract float getBypassDamageFactor();

    protected abstract void applyHitEffects(Entity entity);

    protected abstract void applyLaunchEffects();

    protected abstract void handleBlockHit(BlockState hitBlock, BlockPos pos);

    @Override
    protected void doTargetedShot(LivingEntity target, ItemStack ammo) {
        if (!target.isAlive() || target.getHealth() <= 0 || !TurretTargetingUtils.canSeeTargetFromPos(this, target)
                || !isEntityValidTarget(target)) {
            this.target = null;
            return;
        }
        Vec3 targetPos = target.position();
        shootRay(targetPos.x, targetPos.y + target.getEyeHeight(), targetPos.z,
                getActualTurretAccuracyDeviation());
    }

    @Override
    public boolean forceShot() {
        Vec3 dir = getVectorFromYawPitch(yaw, pitch).scale(5.0);
        Vec3 base = new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.6, worldPosition.getZ() + 0.5);
        Vec3 result = base.add(dir);
        shootRay(result.x, result.y, result.z, getActualTurretAccuracyDeviation());
        return true;
    }

    protected void shootRay(double targetX, double targetY, double targetZ, double accuracy) {
        if (level == null || this.base == null) return;
        this.base.getEnergyStorage().extractEnergy(getPowerRequiredForNextShot(), false);
        applyLaunchEffects();

        Random rand = new Random();
        int scatter = cachedScattershot;
        for (int i = 0; i <= scatter; i++) {
            Vec3 vector = new Vec3(targetX, targetY, targetZ);
            Vec3 origin = new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.6, worldPosition.getZ() + 0.5);

            double deviationModifier = 10.0 * (target != null && target.getBbHeight() < 0.5 ? 1.5 : 1.0)
                    * ((vector.distanceTo(origin) * 0.2 / (getTurretBaseRange() + TurretHeadUtil.getRangeUpgrades(this.base, this))) + 0.3);

            double xDev = rand.nextGaussian() * 0.003 * deviationModifier * accuracy;
            double yDev = rand.nextGaussian() * 0.003 * deviationModifier * accuracy;
            double zDev = rand.nextGaussian() * 0.003 * deviationModifier * accuracy;
            vector = vector.add(xDev, yDev, zDev);
            origin = origin.add(vector.subtract(origin).normalize().scale(0.8));

            level.playSound(null, worldPosition, getLaunchSoundEffect(), SoundSource.BLOCKS,
                    OMTConfig.TURRETS.turretSoundVolume.get().floatValue(), rand.nextFloat() + 0.5f);

            BlockHitResult blockTrace = level.clip(new ClipContext(origin, vector,
                    ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
            double blockRange = (blockTrace.getType() == HitResult.Type.BLOCK)
                    ? blockTrace.getLocation().distanceTo(origin) : 500.0;

            // Simple entity raytrace
            boolean hit = false;
            for (Entity entity : getEntitiesAlongRay(origin, vector, blockRange)) {
                if (onHitEntity(entity)) {
                    renderRay(origin, vector);
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                if (blockTrace.getType() == HitResult.Type.BLOCK) {
                    renderRay(origin, blockTrace.getLocation());
                    handleBlockHit(level.getBlockState(blockTrace.getBlockPos()), blockTrace.getBlockPos());
                } else {
                    renderRay(origin, vector.add(vector.subtract(origin).scale(2.0)));
                }
            }
        }
    }

    private List<Entity> getEntitiesAlongRay(Vec3 origin, Vec3 end, double maxDist) {
        AABB searchBox = new AABB(origin, end).inflate(1.0);
        List<Entity> candidates = new java.util.ArrayList<>(level.getEntitiesOfClass(LivingEntity.class, searchBox));
        List<Entity> result = new ArrayList<>();
        for (Entity e : candidates) {
            Optional<Vec3> hit = e.getBoundingBox().inflate(0.3).clip(origin, end);
            if (hit.isPresent() && origin.distanceTo(hit.get()) <= maxDist) {
                result.add(e);
            }
        }
        result.sort(Comparator.comparingDouble(e -> origin.distanceTo(e.position())));
        return result;
    }

    protected boolean onHitEntity(Entity entity) {
        if (entity == null || level == null || level.isClientSide) return false;
        if (entity instanceof Player player) {
            if (OMTUtil.canDamagePlayer(player, base)) {
                damageEntity(entity);
                applyHitEffects(entity);
                entity.invulnerableTime = 0;
                level.playSound(null, entity.blockPosition(), getHitSound(), SoundSource.AMBIENT,
                        OMTConfig.TURRETS.turretSoundVolume.get().floatValue(), new Random().nextFloat() + 0.5f);
                return true;
            }
            return false;
        } else if (OMTUtil.canDamageEntity(entity, base)) {
            OMTUtil.setTagsForTurretHit(entity, base);
            damageEntity(entity);
            applyHitEffects(entity);
            entity.invulnerableTime = 0;
            level.playSound(null, entity.blockPosition(), getHitSound(), SoundSource.AMBIENT,
                    OMTConfig.TURRETS.turretSoundVolume.get().floatValue(), new Random().nextFloat() + 0.5f);
            return true;
        }
        return false;
    }

    protected void damageEntity(Entity entity) {
        float damageModifier = getDamageModifier(entity);
        float damage = getTurretType().getSettings().baseDamage.get() * damageModifier;
        int fakeDrops = TurretHeadUtil.getFakeDropsLevel(base);

        if (getTurretDamageAmpBonus() * TurretHeadUtil.getAmpLevel(base) > 0 && entity instanceof LivingEntity elb) {
            damage += (int) (elb.getHealth() * getTurretDamageAmpBonus() * TurretHeadUtil.getAmpLevel(base));
        }

        DamageSource normalSrc = level.damageSources().magic();
        DamageSource bypassSrc = level.damageSources().magic();
        entity.hurt(normalSrc, damage * getNormalDamageFactor());
        entity.hurt(bypassSrc, damage * getBypassDamageFactor());
    }

    private static Vec3 getVectorFromYawPitch(float yawDeg, float pitchDeg) {
        double yaw = Math.toRadians(yawDeg);
        double pitch = Math.toRadians(pitchDeg);
        double cosP = Math.cos(pitch);
        return new Vec3(cosP * Math.cos(yaw), Math.sin(pitch), cosP * Math.sin(yaw));
    }
}
