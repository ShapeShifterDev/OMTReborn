package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModEntities;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.Random;

public class LaserBoltProjectile extends TurretProjectile {

    private static final Random RAND = new Random();

    public LaserBoltProjectile(EntityType<? extends LaserBoltProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.0f;
    }

    public LaserBoltProjectile(Level level, TurretBaseBlockEntity base) {
        this(ModEntities.LASER_BOLT_PROJECTILE.get(), level);
        initFromBase(base);
    }

    @Override
    public void tick() {
        if (level().isClientSide && tickCount > 0) {
            Vec3 motion = getDeltaMovement();
            for (int i = 0; i < 5; i++) {
                double ox = RAND.nextGaussian() * 0.05;
                double oy = RAND.nextGaussian() * 0.05;
                double oz = RAND.nextGaussian() * 0.05;
                level().addParticle(ParticleTypes.END_ROD,
                        getX() + ox, getY() + oy, getZ() + oz,
                        motion.x, motion.y, motion.z);
            }
        }
        super.tick();
    }

    @Override
    public void onHitBlock(BlockState block, BlockPos pos) {
        if (block.getBlock() instanceof BlockAbstractTurretHead) return;
        if (!block.isSolid()) return;
        discard();
    }

    @Override
    public void onHitEntity(Entity entity) {
        Level l = level();
        if (entity == null || l.isClientSide || isRemoved()) return;
        if (entity instanceof Player player) {
            if (!canDamagePlayer(player)) return;
        } else if (!canDamageEntity(entity)) {
            return;
        }

        float damage = OMTConfig.TURRETS.laser_turret.baseDamage.get();
        if (entity instanceof LivingEntity le) {
            damage = damage / Math.max(1.0f, le.getArmorValue() / 5.0f);
        }
        if (isAmped && entity instanceof LivingEntity elb) {
            damage += (float) (elb.getHealth() * (getDamageAmpBonus() * ampLevel));
        }

        if (!(entity instanceof Player)) setTagsForTurretHit(entity);
        entity.hurt(OMTDamageSources.normal(l), damage);
        entity.invulnerableTime = 0;
        discard();
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.laser_turret.damageAmp.get();
    }
}
