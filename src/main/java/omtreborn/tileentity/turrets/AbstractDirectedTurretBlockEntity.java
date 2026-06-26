package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.network.OMTNetwork;
import omtreborn.network.messages.MessageUpdateTurret;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.turret.TurretHeadUtil;
import omtreborn.turret.TurretTargetingUtils;

public abstract class AbstractDirectedTurretBlockEntity extends TurretHeadBlockEntity {

    protected float pitch = 0;
    protected float yaw = 0;
    protected float maxPitch = 360;
    protected float maxYaw = 360;
    protected float minPitch = 0;
    protected float minYaw = 0;

    protected AbstractDirectedTurretBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int turretTier) {
        super(type, pos, state, turretTier);
    }

    protected abstract void doTargetedShot(LivingEntity target, ItemStack ammo);

    public abstract boolean forceShot();

    public float getPitch() { return pitch; }
    public void setPitch(float pitch) { this.pitch = pitch; }

    public float getYaw() { return yaw; }
    public void setYaw(float yaw) { this.yaw = yaw; }

    public float getMaxPitch() { return maxPitch; }
    public void setMaxPitch(float v) { maxPitch = v; }

    public float getMinPitch() { return minPitch; }
    public void setMinPitch(float v) { minPitch = v; }

    public float getMaxYaw() { return maxYaw; }
    public void setMaxYaw(float v) { maxYaw = v; }

    public float getMinYaw() { return minYaw; }
    public void setMinYaw(float v) { minYaw = v; }

    protected void updateRotationAnimation() {
        rotationAnimation += 0.03f;
        if (rotationAnimation >= 360f) rotationAnimation = 0f;
    }

    public void sendRotationUpdate() {
        if (level instanceof ServerLevel sl) {
            OMTNetwork.sendToNearby(worldPosition, sl, new MessageUpdateTurret(this));
        }
    }

    protected boolean isTargetInYawPitch() {
        float y = yaw % 360;
        float p = pitch % 360;
        if (y < 0) y += 360;
        if (p < 0) p += 360;
        return minYaw <= y && y <= maxYaw && minPitch <= p && p <= maxPitch;
    }

    public static void tickDirected(Level level, BlockPos pos, BlockState state,
                                     AbstractDirectedTurretBlockEntity be) {
        // Base turret head tick (cache reset)
        TurretHeadBlockEntity.tickTurret(level, pos, state, be);

        // Client side: just animate
        if (level.isClientSide) {
            be.updateRotationAnimation();
            return;
        }

        // Validate block is still a turret head
        if (!(level.getBlockState(pos).getBlock() instanceof BlockAbstractTurretHead)) return;

        TurretBaseBlockEntity base = be.base;
        if (base == null || base.getTier() < be.turretTier) {
            level.destroyBlock(pos, true);
            return;
        }

        if (!base.isActive()) {
            be.target = null;
            return;
        }

        be.ticks++;

        // Power check
        if (base.getEnergyStorage().getEnergyStored() < be.getPowerRequiredForNextShot()) return;

        // Warn players
        if (base.isAttacksPlayers() && OMTConfig.TURRETS.globalCanTargetPlayers.get()) {
            TurretHeadUtil.warnPlayers(base, level, pos, be.getTurretBaseRange());
        }

        // Target tracking
        if (be.target instanceof ServerPlayer sp) {
            be.targetSpeedX = sp.getX() - be.targetLastX;
            be.targetSpeedY = sp.getY() - be.targetLastY;
            be.targetSpeedZ = sp.getZ() - be.targetLastZ;
            be.targetLastX = sp.getX();
            be.targetLastY = sp.getY();
            be.targetLastZ = sp.getZ();
        }

        if (be.target == null && be.targetingTicks < OMTConfig.TURRETS.turretTargetSearchTicks.get()) {
            be.targetingTicks++;
        } else {
            be.targetingChecks();
            if (be.target != null && be.isTargetInYawPitch()) {
                float newYaw = TurretHeadUtil.getAimYaw(be.target, pos);
                float newPitch = TurretHeadUtil.getAimPitch(be.target, pos);
                if (be.yaw != newYaw || be.pitch != newPitch) {
                    be.yaw = newYaw;
                    be.pitch = newPitch;
                    be.sendRotationUpdate();
                }
            }
            be.targetingTicks = 0;
        }

        if ((be.target != null && be.target.isAlive()) || be.autoFire) {
            if (be.isOnCooldown()) return;

            ItemStack ammo = be.getAmmoStack();
            if (ammo.isEmpty() && be.requiresAmmo() && OMTConfig.TURRETS.doTurretsNeedAmmo.get()) return;

            if (be.target != null) {
                be.doTargetedShot(be.target, ammo);
            } else if (be.autoFire) {
                be.forceShot();
            }
            be.ticks = 0;
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("pitch", pitch);
        tag.putFloat("yaw", yaw);
        tag.putFloat("maxPitch", maxPitch);
        tag.putFloat("minPitch", minPitch);
        tag.putFloat("maxYaw", maxYaw);
        tag.putFloat("minYaw", minYaw);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        pitch = tag.getFloat("pitch");
        yaw = tag.getFloat("yaw");
        maxPitch = tag.contains("maxPitch") ? tag.getFloat("maxPitch") : 360;
        minPitch = tag.getFloat("minPitch");
        maxYaw = tag.contains("maxYaw") ? tag.getFloat("maxYaw") : 360;
        minYaw = tag.getFloat("minYaw");
    }
}
