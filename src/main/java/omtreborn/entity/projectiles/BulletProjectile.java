package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
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

public class BulletProjectile extends TurretProjectile {

    public BulletProjectile(EntityType<? extends BulletProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.00f;
    }

    public BulletProjectile(Level level, TurretBaseBlockEntity base) {
        this(ModEntities.BULLET_PROJECTILE.get(), level);
        initFromBase(base);
    }

    @Override
    public void tick() {
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), 0, 0, 0);
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

        float damage = OMTConfig.TURRETS.machine_gun_turret.baseDamage.get();
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
        return OMTConfig.TURRETS.machine_gun_turret.damageAmp.get();
    }
}
