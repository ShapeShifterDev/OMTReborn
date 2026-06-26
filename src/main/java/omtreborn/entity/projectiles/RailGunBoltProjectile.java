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
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModEntities;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.Random;

public class RailGunBoltProjectile extends TurretProjectile {

    private static final Random RAND = new Random();

    public RailGunBoltProjectile(EntityType<? extends RailGunBoltProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.0f;
    }

    public RailGunBoltProjectile(Level level, TurretBaseBlockEntity base) {
        this(ModEntities.RAIL_GUN_BOLT_PROJECTILE.get(), level);
        initFromBase(base);
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            for (int i = 0; i < 9; i++) {
                double ox = RAND.nextGaussian() * 0.1;
                double oy = RAND.nextGaussian() * 0.1;
                double oz = RAND.nextGaussian() * 0.1;
                level().addParticle(ParticleTypes.END_ROD, getX() + ox, getY() + oy, getZ() + oz, 0, 0, 0);
            }
        }
        super.tick();
    }

    private void spawnImpactParticles(double x, double y, double z) {
        if (level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);
            sl.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 3, 0.3, 0.3, 0.3, 0.1);
        }
    }

    @Override
    public void onHitBlock(BlockState block, BlockPos pos) {
        if (block.getBlock() instanceof BlockAbstractTurretHead) return;
        if (!block.isSolid()) return;
        spawnImpactParticles(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        if (OMTConfig.TURRETS.canRailgunDestroyBlocks.get()) {
            Level l = level();
            if (!block.isAir() && block.getDestroySpeed(l, pos) >= 0) {
                l.destroyBlock(pos, true);
            }
        }
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

        float damage = OMTConfig.TURRETS.railgun_turret.baseDamage.get();
        if (isAmped && entity instanceof LivingEntity elb) {
            damage += (float) (elb.getHealth() * (getDamageAmpBonus() * ampLevel));
        }

        if (!(entity instanceof Player)) setTagsForTurretHit(entity);
        entity.hurt(OMTDamageSources.bypass(l), damage);
        entity.invulnerableTime = 0;
        spawnImpactParticles(entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ());
        discard();
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.railgun_turret.damageAmp.get();
    }
}
