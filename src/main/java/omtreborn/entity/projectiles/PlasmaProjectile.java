package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModEntities;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.List;

public class PlasmaProjectile extends TurretProjectile {

    public PlasmaProjectile(EntityType<? extends PlasmaProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.001f;
    }

    public PlasmaProjectile(Level level, TurretBaseBlockEntity base) {
        this(ModEntities.PLASMA_PROJECTILE.get(), level);
        initFromBase(base);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount > 30 && !isRemoved()) {
            discard();
        }
    }

    private void explode() {
        if (isRemoved()) return;
        Level l = level();

        if (l instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 15, 2, 1, 2, 0.2);
            sl.sendParticles(ParticleTypes.LARGE_SMOKE, getX(), getY(), getZ(), 15, 2, 1, 2, 0.2);
        }

        AABB aabb = new AABB(getX() - 2, getY() - 2, getZ() - 2,
                             getX() + 2, getY() + 2, getZ() + 2);
        List<LivingEntity> targets = l.getEntitiesOfClass(LivingEntity.class, aabb);

        for (LivingEntity entity : targets) {
            float damage = OMTConfig.TURRETS.plasma_turret.baseDamage.get();
            if (isAmped) damage += (float) (entity.getHealth() * (getDamageAmpBonus() * ampLevel));

            if (entity instanceof Player player) {
                if (!canDamagePlayer(player)) continue;
            } else if (!canDamageEntity(entity)) {
                continue;
            }

            if (!(entity instanceof Player)) setTagsForTurretHit(entity);
            entity.hurt(OMTDamageSources.normal(l), damage * 0.5f);
            entity.hurt(OMTDamageSources.bypass(l), damage * 0.5f);
            entity.invulnerableTime = 0;
        }
        discard();
    }

    @Override
    public void onHitBlock(BlockState block, BlockPos pos) {
        if (block.getBlock() instanceof BlockAbstractTurretHead) return;
        if (!block.isSolid()) return;
        if (!level().isClientSide) explode();
    }

    @Override
    public void onHitEntity(Entity entity) {
        if (canDamageEntity(entity)) explode();
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource source) {
        return source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(source);
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.plasma_turret.damageAmp.get();
    }
}
