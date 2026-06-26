package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import omtreborn.config.OMTConfig;
import omtreborn.lib.tileentity.OMLBlockEntity;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.turret.EnumTargetingPriority;
import omtreborn.turret.TurretHeadUtil;
import omtreborn.turret.TurretTargetingUtils;
import omtreborn.turret.TurretType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class TurretHeadBlockEntity extends OMLBlockEntity {

    public LivingEntity target = null;
    public float rotationAnimation = 0.0f;

    protected TurretBaseBlockEntity base;
    protected int ticks = 0;
    protected int targetingTicks = 0;
    protected final int turretTier;
    protected boolean autoFire = false;
    protected boolean resetCaches = true;
    protected double cachedAccuracy = 0.0;
    protected int cachedScattershot = 0;
    protected double targetLastX = 0, targetLastY = 0, targetLastZ = 0;
    protected double targetSpeedX = 0, targetSpeedY = 0, targetSpeedZ = 0;

    protected Integer[] priorities = {};

    protected TurretHeadBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int turretTier) {
        super(type, pos, state);
        this.turretTier = turretTier;
        this.resetCaches = true;
    }

    public abstract TurretType getTurretType();

    public abstract SoundEvent getLaunchSoundEffect();

    protected float getLaunchSoundVolume() { return 1.0f; }

    protected float getLaunchSoundMaxRange() { return getLaunchSoundVolume() * 16.0f; }

    public abstract boolean requiresAmmo();

    public abstract boolean requiresSpecificAmmo();

    @Nullable
    public ItemStack getAmmo() {
        return null;
    }

    protected abstract Integer[] getDefaultPriorities();

    public boolean isEntityValidTarget(LivingEntity entity) {
        return true;
    }

    public TurretBaseBlockEntity getBase() {
        return base;
    }

    @Nullable
    protected TurretBaseBlockEntity getBaseFromWorld() {
        return TurretHeadUtil.getTurretBase(level, worldPosition);
    }

    public Integer[] getPriorities() {
        return priorities.length != 0 ? priorities : getDefaultPriorities();
    }

    public boolean getAutoFire() {
        return autoFire;
    }

    public void setAutoFire(boolean autoFire) {
        this.autoFire = autoFire;
    }

    public void triggerResetCaches() {
        this.resetCaches = true;
    }

    public int getTurretBasePowerUsage() {
        return getTurretType().getSettings().powerUsage.get();
    }

    public int getTurretBaseFireRate() {
        return getTurretType().getSettings().baseFireRate.get();
    }

    public double getBaseTurretAccuracyDeviation() {
        return getTurretType().getSettings().baseAccuracyDeviation.get();
    }

    public int getTurretBaseRange() {
        return getTurretType().getSettings().baseRange.get();
    }

    public int getTurretMinRange() {
        return getTurretType().getSettings().baseMinRange.get();
    }

    public double getTurretDamageAmpBonus() {
        return getTurretType().getSettings().damageAmp.get();
    }

    public double getActualTurretAccuracyDeviation() {
        if (resetCaches && base != null) {
            double baseAcc = getBaseTurretAccuracyDeviation();
            double accuracyUpgrades = TurretHeadUtil.getAccuracyUpgrades(base, this);
            int scattershot = TurretHeadUtil.getScattershotUpgrades(base);
            cachedAccuracy = baseAcc / Math.pow(1 + accuracyUpgrades, 1.5) * (1 + scattershot / 10.0);
        }
        return cachedAccuracy;
    }

    public boolean isOnCooldown() {
        int fireRate = getTurretBaseFireRate();
        float rateUpgrade = TurretHeadUtil.getFireRateUpgrades(base, this);
        return ticks < (fireRate / (1.0 + rateUpgrade));
    }

    protected int getPowerRequiredForNextShot() {
        float efficiency = TurretHeadUtil.getEfficiencyUpgrades(base, this);
        int scatter = TurretHeadUtil.getScattershotUpgrades(base);
        return Math.round(getTurretBasePowerUsage() * (1 - efficiency) * (1 + scatter));
    }

    @Nullable
    protected LivingEntity getTarget() {
        if (level == null || level.isClientSide || base == null) return null;
        int range = base.getRange();
        AABB aabb = new AABB(
                worldPosition.getX() - range - 1, worldPosition.getY() - range - 1, worldPosition.getZ() - range - 1,
                worldPosition.getX() + range + 1, worldPosition.getY() + range + 1, worldPosition.getZ() + range + 1);
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, aabb);
        return new TurretTargetingUtils(this).getBestEntity(candidates);
    }

    protected void targetingChecks() {
        if (target == null || !target.isAlive() || level.getEntity(target.getId()) == null
                || target.getHealth() <= 0 || !TurretTargetingUtils.canSeeTargetFromPos(this, target)
                || TurretTargetingUtils.chebyshevTooClose(this, target)) {
            target = getTarget();
        }
    }

    protected ItemStack getAmmoStack() {
        if (!requiresAmmo() || !OMTConfig.TURRETS.doTurretsNeedAmmo.get()) {
            return ItemStack.EMPTY;
        }
        ItemStack ammoNeeded = getAmmo();
        if (requiresSpecificAmmo()) {
            if (ammoNeeded != null) ammoNeeded.setCount(TurretHeadUtil.getScattershotUpgrades(base) + 1);
        }
        return TurretHeadUtil.deductItemStackFromInventories(ammoNeeded, base, this);
    }

    protected boolean updateChecks() {
        ticks++;

        if (base == null || base.getTier() < turretTier) {
            if (level != null && !level.isClientSide) level.destroyBlock(worldPosition, true);
            return false;
        }

        int powerRequired = getPowerRequiredForNextShot();
        if (base.getEnergyStorage().getEnergyStored() < powerRequired || !base.isActive()) {
            return false;
        }

        if (target == null && targetingTicks < OMTConfig.TURRETS.turretTargetSearchTicks.get()) {
            targetingTicks++;
            return false;
        }
        targetingTicks = 0;

        targetingChecks();
        if (target == null) return false;
        if (isOnCooldown()) return false;

        if (!TurretTargetingUtils.canSeeTargetFromPos(this, target)) { target = null; return false; }
        if (TurretTargetingUtils.chebyshevDistance(this, target)) { target = null; return false; }
        if (TurretTargetingUtils.chebyshevTooClose(this, target)) { target = null; return false; }

        base.getEnergyStorage().extractEnergy(powerRequired, false);
        return true;
    }

    public static void tickTurret(Level level, BlockPos pos, BlockState state, TurretHeadBlockEntity be) {
        if (be.base == null) be.base = be.getBaseFromWorld();
        if (be.resetCaches) {
            if (be.base != null) {
                be.cachedScattershot = TurretHeadUtil.getScattershotUpgrades(be.base);
                be.getActualTurretAccuracyDeviation();
            }
            be.resetCaches = false;
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ticksBeforeFire", ticks);
        tag.putBoolean("autoFire", autoFire);
        Integer[] p = getPriorities();
        if (p.length >= 5) {
            for (EnumTargetingPriority priority : EnumTargetingPriority.values()) {
                tag.putInt("priority_" + priority.name(), p[priority.ordinal()]);
            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.ticks = tag.getInt("ticksBeforeFire");
        this.autoFire = tag.getBoolean("autoFire");
        EnumTargetingPriority[] vals = EnumTargetingPriority.values();
        if (tag.contains("priority_" + vals[0].name())) {
            Integer[] p = new Integer[vals.length];
            for (EnumTargetingPriority priority : vals) {
                p[priority.ordinal()] = tag.getInt("priority_" + priority.name());
            }
            this.priorities = p;
        } else {
            this.priorities = getDefaultPriorities();
        }
    }

    protected void playLaunchSound() {
        if (level != null) {
            float configVol = OMTConfig.TURRETS.turretSoundVolume.get().floatValue();
            float amplitude = Math.min(1.0f, getLaunchSoundVolume()) * configVol;
            playTurretSoundWithAttenuation(level, worldPosition, getLaunchSoundEffect(), getLaunchSoundMaxRange(), amplitude);
        }
    }

    protected static void playTurretSoundWithAttenuation(Level level, BlockPos pos, SoundEvent sound, float maxRange, float amplitude) {
        if (!(level instanceof ServerLevel sl)) return;
        float pitch = sl.getRandom().nextFloat() + 0.5f;
        double cx = pos.getX() + 0.5, cy = pos.getY() + 0.5, cz = pos.getZ() + 0.5;
        for (ServerPlayer player : sl.players()) {
            double distSq = player.distanceToSqr(cx, cy, cz);
            if (distSq >= (double) maxRange * maxRange) continue;
            float vol = amplitude * (float)(1.0 - Math.sqrt(distSq) / maxRange);
            if (vol > 0.001f) {
                player.playNotifySound(sound, SoundSource.BLOCKS, vol, pitch);
            }
        }
    }
}
