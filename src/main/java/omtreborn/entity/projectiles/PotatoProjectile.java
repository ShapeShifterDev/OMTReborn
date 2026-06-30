package omtreborn.entity.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModEntities;
import omtreborn.tileentity.TurretBaseBlockEntity;

import javax.annotation.Nullable;

public class PotatoProjectile extends TurretProjectile {

    @Nullable private ItemEntity itemBound;
    private boolean spawned = false;

    public PotatoProjectile(EntityType<? extends PotatoProjectile> type, Level level) {
        super(type, level);
        this.gravity = 0.03f;
    }

    public PotatoProjectile(Level level, TurretBaseBlockEntity base, ItemStack ammo) {
        this(ModEntities.POTATO_PROJECTILE.get(), level);
        this.ammo = ammo.copy();
        initFromBase(base);
    }

    @Override
    public void tick() {
        super.tick();
        Level l = level();
        if (!spawned && !l.isClientSide && !isRemoved() && !ammo.isEmpty()) {
            Vec3 motion = getDeltaMovement();
            ItemEntity item = new ItemEntity(l, getX(), getY() - 0.2, getZ(), ammo.copy());
            item.setDeltaMovement(motion.x, motion.y + gravity, motion.z);
            item.setPickUpDelay(10000);
            item.lifespan = 6000;
            l.addFreshEntity(item);
            itemBound = item;
            spawned = true;
        }
    }

    @Override
    public void onHitBlock(BlockState block, BlockPos pos) {
        if (block.getBlock() instanceof BlockAbstractTurretHead) return;
        if (!block.isSolid()) return;
        if (itemBound != null) itemBound.discard();
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

        float damage = OMTConfig.TURRETS.potato_cannon_turret.baseDamage.get();
        if (isAmped && entity instanceof LivingEntity elb) {
            damage += (float) (elb.getHealth() * (getDamageAmpBonus() * ampLevel));
        }

        if (!(entity instanceof Player)) setTagsForTurretHit(entity);
        entity.hurt(OMTDamageSources.normal(l), damage);
        entity.invulnerableTime = 0;
        if (!ammo.isEmpty() && ammo.is(Items.POISONOUS_POTATO) && entity instanceof LivingEntity le) {
            le.addEffect(new MobEffectInstance(MobEffects.POISON, 60, 1));
        }
        if (itemBound != null) itemBound.discard();
        discard();
    }

    @Override
    public double getDamageAmpBonus() {
        return OMTConfig.TURRETS.potato_cannon_turret.damageAmp.get();
    }
}
