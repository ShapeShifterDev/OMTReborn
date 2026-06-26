package omtreborn.tileentity.turrets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModBlockEntities;
import omtreborn.init.ModSounds;
import omtreborn.turret.TurretType;
import omtreborn.util.OMTUtil;


public class TeleporterTurretBlockEntity extends TurretHeadBlockEntity {

    public TeleporterTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_TELEPORTER.get(), pos, state, 4);
    }

    @Override
    public TurretType getTurretType() {
        return TurretType.of("teleporterTurret", OMTConfig.TURRETS.teleporter_turret);
    }

    @Override
    public SoundEvent getLaunchSoundEffect() {
        return ModSounds.TELEPORTER_TURRET_ACTIVATION.get();
    }

    @Override
    public boolean requiresAmmo() { return false; }

    @Override
    public boolean requiresSpecificAmmo() { return false; }

    @Override
    protected float getLaunchSoundVolume() { return 0.375f; }

    @Override
    protected float getLaunchSoundMaxRange() { return 48.0f; }

    @Override
    public Integer[] getDefaultPriorities() {
        return new Integer[]{2, 1, 3, 0, 4};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TeleporterTurretBlockEntity be) {
        tickTurret(level, pos, state, be);
        if (level.isClientSide) {
            be.rotationAnimation += 0.03f;
            if (be.rotationAnimation >= 360f) be.rotationAnimation = 0f;
            return;
        }
        if (be.base == null) return;

        if (!be.updateChecks()) return;
        if (be.target == null) return;

        LivingEntity target = be.target;
        // Teleport target to the base position (on top of the base)
        BlockPos basePos = be.base.getBlockPos();
        double x = basePos.getX() + 0.5;
        double y = basePos.getY() + 1.0;
        double z = basePos.getZ() + 0.5;

        boolean teleported = false;
        if (target instanceof Player player) {
            if (OMTUtil.canDamagePlayer(player, be.base)) {
                target.teleportTo(x, y, z);
                teleported = true;
            }
        } else {
            if (OMTUtil.canDamageEntity(target, be.base)) {
                OMTUtil.setTagsForTurretHit(target, be.base);
                target.teleportTo(x, y, z);
                teleported = true;
            }
        }
        if (teleported && level instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.PORTAL,
                    basePos.getX() + 0.5, basePos.getY() + 1.0, basePos.getZ() + 0.5,
                    20, 0.5, 0.5, 0.5, 0.3);
        }

        float amplitude = Math.min(1.0f, be.getLaunchSoundVolume()) * OMTConfig.TURRETS.turretSoundVolume.get().floatValue();
        playTurretSoundWithAttenuation(level, pos, be.getLaunchSoundEffect(), be.getLaunchSoundMaxRange(), amplitude);
        be.ticks = 0;
        be.target = null;
    }
}
