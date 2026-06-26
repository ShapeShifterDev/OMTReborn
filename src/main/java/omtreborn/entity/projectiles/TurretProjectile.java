package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.turret.TurretHeadUtil;
import omtreborn.util.OMTUtil;

import javax.annotation.Nullable;

public abstract class TurretProjectile extends ThrowableProjectile {

    public float gravity = 0.0f;
    public int ampLevel = 0;
    public boolean isAmped = false;
    public int fakeDrops = 0;
    public boolean dropLoot = true;

    @Nullable protected BlockPos turretBasePos;
    protected ItemStack ammo = ItemStack.EMPTY;

    protected TurretProjectile(EntityType<? extends TurretProjectile> type, Level level) {
        super(type, level);
    }

    public void initFromBase(@Nullable TurretBaseBlockEntity base) {
        if (base != null) {
            this.turretBasePos = base.getBlockPos();
            this.ampLevel = TurretHeadUtil.getAmpLevel(base);
            this.isAmped = this.ampLevel > 0;
            this.fakeDrops = TurretHeadUtil.getFakeDropsLevel(base);
            this.dropLoot = TurretHeadUtil.baseHasNoLootDeleter(base);
        }
    }

    @Nullable
    protected TurretBaseBlockEntity getTurretBase() {
        Level l = level();
        if (turretBasePos == null || l == null) return null;
        BlockEntity be = l.getBlockEntity(turretBasePos);
        return be instanceof TurretBaseBlockEntity tbe ? tbe : null;
    }

    @Override
    protected float getGravity() {
        return gravity;
    }

    @Override
    protected void defineSynchedData() {
        // no additional synced data for turret projectiles
    }

    @Override
    public void tick() {
        if (!level().isClientSide && tickCount > 40) {
            discard();
            return;
        }
        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!level().isClientSide) {
            onHitBlock(level().getBlockState(result.getBlockPos()), result.getBlockPos());
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!level().isClientSide) {
            onHitEntity(result.getEntity());
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return !(entity instanceof TurretProjectile) && super.canHitEntity(entity);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public abstract void onHitBlock(BlockState block, BlockPos pos);
    public abstract void onHitEntity(Entity entity);
    public abstract double getDamageAmpBonus();

    protected void setTagsForTurretHit(Entity entity) {
        TurretBaseBlockEntity base = getTurretBase();
        if (base != null) OMTUtil.setTagsForTurretHit(entity, base);
    }

    protected boolean canDamagePlayer(Player player) {
        TurretBaseBlockEntity base = getTurretBase();
        return base != null && OMTUtil.canDamagePlayer(player, base);
    }

    protected boolean canDamageEntity(Entity entity) {
        TurretBaseBlockEntity base = getTurretBase();
        return base != null && OMTUtil.canDamageEntity(entity, base);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        discard();
    }
}
