package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModEntities;
import omtreborn.tileentity.TurretBaseBlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RocketProjectile extends TurretProjectile {

    @Nullable private Entity target;

    public RocketProjectile(EntityType<? extends RocketProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.00f;
    }

    public RocketProjectile(Level level, @Nullable Entity target, TurretBaseBlockEntity base) {
        this(ModEntities.ROCKET_PROJECTILE.get(), level);
        this.target = target;
        initFromBase(base);
    }

    @Override
    public void tick() {
        super.tick();
        if (isRemoved()) return;
        Level l = level();

        if (!l.isClientSide) {
            if (OMTConfig.TURRETS.canRocketsHome.get()) {
                if (target != null && target.isAlive()) {
                    double dx = target.getX() - getX();
                    double dy = target.getY() + target.getEyeHeight() - 1.1f - getY();
                    double dz = target.getZ() - getZ();
                    shoot(dx, dy, dz, 0.24f, 0.0f);
                } else {
                    discard();
                    return;
                }
            }
        }

        if (l instanceof ServerLevel sl) {
            Random rand = new Random();
            for (int i = 0; i < 20; i++) {
                sl.sendParticles(ParticleTypes.SMOKE,
                        getX() + rand.nextGaussian() / 10,
                        getY() + rand.nextGaussian() / 10,
                        getZ() + rand.nextGaussian() / 10,
                        1, 0, 0, 0, 0);
            }
        }
    }

    private void explode() {
        if (isRemoved()) return;
        Level l = level();
        if (!(l instanceof ServerLevel sl)) {
            discard();
            return;
        }

        // Lightweight visual/audio effect — skips vanilla block-damage ray-cast entirely
        sl.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
        l.playSound(null, getX(), getY(), getZ(),
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,
                4.0f, (1.0f + (l.random.nextFloat() - l.random.nextFloat()) * 0.2f) * 0.7f);

        AABB aabb = new AABB(getX() - 5, getY() - 5, getZ() - 5,
                             getX() + 5, getY() + 5, getZ() + 5);
        List<LivingEntity> targets = l.getEntitiesOfClass(LivingEntity.class, aabb);

        for (LivingEntity entity : targets) {
            float damage = OMTConfig.TURRETS.rocket_turret.baseDamage.get();
            if (isAmped) damage += (float) (entity.getHealth() * (getDamageAmpBonus() * ampLevel));

            if (OMTConfig.TURRETS.canRocketsHurtEnderDragon.get() && entity instanceof EnderDragon dragon) {
                setTagsForTurretHit(dragon);
                dragon.setHealth(dragon.getHealth() - damage);
                dragon.invulnerableTime = 0;
            } else if (entity instanceof Player player) {
                if (!canDamagePlayer(player)) continue;
                entity.hurt(OMTDamageSources.normal(l), damage);
                entity.invulnerableTime = 0;
            } else if (canDamageEntity(entity)) {
                setTagsForTurretHit(entity);
                entity.hurt(OMTDamageSources.normal(l), damage);
                entity.invulnerableTime = 0;
            }
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
        Level l = level();
        if (l.isClientSide) return;
        if (entity instanceof Player player && !canDamagePlayer(player)) return;
        if (!canDamageEntity(entity)) return;
        explode();
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource source) {
        return source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(source);
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.rocket_turret.damageAmp.get();
    }
}
