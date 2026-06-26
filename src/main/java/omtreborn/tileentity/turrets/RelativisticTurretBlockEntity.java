package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretHeadUtil;
import omtreborn.turret.TurretTargetingUtils;
import omtreborn.turret.TurretType;
import omtreborn.util.OMTUtil;

import java.util.List;
import java.util.Random;

public class RelativisticTurretBlockEntity extends TurretHeadBlockEntity {

    public RelativisticTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_RELATIVISTIC.get(), pos, state, 4);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("relativisticTurret", OMTConfig.TURRETS.relativistic_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.RELATIVISTIC_LAUNCH.get();
    }

    @Override
    public boolean requiresAmmo() { return false; }

    @Override
    public boolean requiresSpecificAmmo() { return false; }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    @Override
    protected float getLaunchSoundVolume() { return 3.0f; }

    private void applyEffects(LivingEntity entity) {
        int duration = getTurretType().getSettings().baseFireRate.get() * 2;
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, 1, false, true));
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, duration, 1, false, true));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RelativisticTurretBlockEntity be) {
        tickTurret(level, pos, state, be);
        if (level.isClientSide) {
            be.rotationAnimation += 0.03f;
            if (be.rotationAnimation >= 360f) be.rotationAnimation = 0f;
            return;
        }
        if (be.base == null) return;

        if (!be.updateChecks()) return;

        // AoE effect on all valid targets in range
        int range = be.getTurretBaseRange() + TurretHeadUtil.getRangeUpgrades(be.base, be);
        AABB aabb = new AABB(
                pos.getX() - range, pos.getY() - range, pos.getZ() - range,
                pos.getX() + range, pos.getY() + range, pos.getZ() + range);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);

        Random rand = new Random();
        for (LivingEntity entity : entities) {
            if (!TurretTargetingUtils.canSeeTargetFromPos(be, entity)) continue;
            if (TurretTargetingUtils.chebyshevTooClose(be, entity)) continue;
            if (entity instanceof Player player) {
                if (OMTUtil.canDamagePlayer(player, be.base)) {
                    be.applyEffects(entity);
                    if (level instanceof ServerLevel sl) {
                        sl.sendParticles(ParticleTypes.WITCH,
                                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                                5, 0.3, 0.3, 0.3, 0.05);
                    }
                }
            } else {
                if (OMTUtil.canDamageEntity(entity, be.base)) {
                    OMTUtil.setTagsForTurretHit(entity, be.base);
                    be.applyEffects(entity);
                    if (level instanceof ServerLevel sl) {
                        sl.sendParticles(ParticleTypes.WITCH,
                                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                                5, 0.3, 0.3, 0.3, 0.05);
                    }
                }
            }
        }
        if (level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.ENCHANT,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    20, range, range, range, 0.5);
        }
        float amplitude = Math.min(1.0f, be.getLaunchSoundVolume()) * OMTConfig.TURRETS.turretSoundVolume.get().floatValue();
        playTurretSoundWithAttenuation(level, pos, be.getLaunchSoundEffect(), be.getLaunchSoundMaxRange(), amplitude);
        be.ticks = 0;
    }
}
