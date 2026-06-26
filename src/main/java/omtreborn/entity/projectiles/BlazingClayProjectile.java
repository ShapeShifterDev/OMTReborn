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

public class BlazingClayProjectile extends TurretProjectile {

    public BlazingClayProjectile(EntityType<? extends BlazingClayProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.00f;
    }

    public BlazingClayProjectile(Level level, TurretBaseBlockEntity base) {
        this(ModEntities.BLAZING_CLAY_PROJECTILE.get(), level);
        initFromBase(base);
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), 0, 0, 0);
        }
        super.tick();
    }

    private void explodeAoE() {
        if (isRemoved()) return;
        Level l = level();
        AABB aabb = new AABB(getX() - 5, getY() - 5, getZ() - 5,
                             getX() + 5, getY() + 5, getZ() + 5);
        List<LivingEntity> targets = l.getEntitiesOfClass(LivingEntity.class, aabb);
        float damage = OMTConfig.TURRETS.incendiary_turret.baseDamage.get();

        for (LivingEntity entity : targets) {
            float d = damage;
            if (isAmped) d += (float) (entity.getHealth() * (getDamageAmpBonus() * ampLevel));

            if (entity instanceof Player player) {
                if (canDamagePlayer(player)) {
                    entity.hurt(OMTDamageSources.normal(l), d);
                    entity.invulnerableTime = 0;
                    entity.setSecondsOnFire(5);
                }
            } else if (canDamageEntity(entity)) {
                setTagsForTurretHit(entity);
                entity.hurt(OMTDamageSources.normal(l), d);
                entity.invulnerableTime = 0;
                entity.setSecondsOnFire(5);
            }
        }
        if (l instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.LARGE_SMOKE, getX(), getY(), getZ(), 10, 0.5, 0.5, 0.5, 0.05);
        }
        discard();
    }

    @Override
    public void onHitBlock(BlockState block, BlockPos pos) {
        if (block.getBlock() instanceof BlockAbstractTurretHead) return;
        if (!block.isSolid()) return;
        if (!level().isClientSide) explodeAoE();
    }

    @Override
    public void onHitEntity(Entity entity) {
        if (!level().isClientSide && canDamageEntity(entity)) {
            explodeAoE();
        }
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.incendiary_turret.damageAmp.get();
    }
}
