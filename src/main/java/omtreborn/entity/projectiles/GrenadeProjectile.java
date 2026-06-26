package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModEntities;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.List;

public class GrenadeProjectile extends TurretProjectile {

    private int explodeAtTick = 39;

    public GrenadeProjectile(EntityType<? extends GrenadeProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.03f;
    }

    public GrenadeProjectile(Level level, TurretBaseBlockEntity base, ItemStack ammo) {
        this(ModEntities.GRENADE_PROJECTILE.get(), level);
        initFromBase(base);
    }

    @Override
    public void tick() {
        super.tick();
        Level l = level();
        if (!l.isClientSide && !isRemoved() && tickCount >= explodeAtTick) {
            explode(l);
        }
    }

    private void explode(Level l) {
        if (isRemoved()) return;
        float strength = OMTConfig.TURRETS.canGrenadesDestroyBlocks.get() ? 1.4f : 0.1f;
        l.explode(null, getX(), getY(), getZ(), strength,
                OMTConfig.TURRETS.canGrenadesDestroyBlocks.get()
                        ? Level.ExplosionInteraction.BLOCK
                        : Level.ExplosionInteraction.NONE);

        AABB aabb = new AABB(getX() - 3, getY() - 3, getZ() - 3,
                             getX() + 3, getY() + 3, getZ() + 3);
        List<LivingEntity> targets = l.getEntitiesOfClass(LivingEntity.class, aabb);

        for (LivingEntity entity : targets) {
            float damage = OMTConfig.TURRETS.grenade_turret.baseDamage.get();
            if (isAmped) damage += (float) (entity.getHealth() * (getDamageAmpBonus() * ampLevel));

            if (entity instanceof Player player) {
                if (!canDamagePlayer(player)) continue;
            } else if (!canDamageEntity(entity)) {
                continue;
            }

            if (!(entity instanceof Player)) setTagsForTurretHit(entity);
            entity.hurt(OMTDamageSources.normal(l), damage * 0.9f);
            entity.hurt(OMTDamageSources.bypass(l), damage * 0.1f);
            entity.invulnerableTime = 0;
        }
        discard();
    }

    @Override
    public void onHitBlock(BlockState block, BlockPos pos) {
        if (block.getBlock() instanceof BlockAbstractTurretHead) return;
        if (!block.isSolid()) return;
        setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void onHitEntity(Entity entity) {
        if (canDamageEntity(entity)) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x * 0.2, motion.y * 1.2, motion.z * 0.2);
            explodeAtTick = Math.min(explodeAtTick, tickCount + 9);
        }
    }

    @Override
    public boolean isInvulnerableTo(net.minecraft.world.damagesource.DamageSource source) {
        return source.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(source);
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.grenade_turret.damageAmp.get();
    }
}
