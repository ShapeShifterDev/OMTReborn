package omtreborn.turret;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import omtreborn.api.lists.MobBlacklist;
import omtreborn.api.lists.NeutralList;
import omtreborn.config.OMTConfig;
import omtreborn.lib.util.PlayerUtil;
import omtreborn.lib.util.TargetingSettings;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.tileentity.turrets.TurretHeadBlockEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TurretTargetingUtils {

    private final TurretHeadBlockEntity turret;
    private final TurretBaseBlockEntity base;
    private final TargetingSettings settings;

    public TurretTargetingUtils(TurretHeadBlockEntity turret) {
        this.turret = turret;
        this.base = turret.getBase();
        this.settings = base != null ? base.getTargetingSettings() : null;
    }

    @Nullable
    public LivingEntity getBestEntity(List<LivingEntity> candidates) {
        if (base == null || settings == null || turret.getLevel() == null) return null;

        List<LivingEntity> valid = new ArrayList<>();
        for (LivingEntity e : candidates) {
            if (validTarget(e)) valid.add(e);
        }
        if (valid.isEmpty()) return null;

        Integer[] priorities = turret.getPriorities();
        BlockPos pos = turret.getBlockPos();

        valid.sort((e1, e2) -> {
            int score = 0;
            for (EnumTargetingPriority p : EnumTargetingPriority.values()) {
                int v1 = EnumTargetingPriority.getValueByPriority(e1, pos, p, settings, priorities);
                int v2 = EnumTargetingPriority.getValueByPriority(e2, pos, p, settings, priorities);
                score = Integer.compare(v2, v1); // descending
                if (score != 0) break;
            }
            return score;
        });

        return valid.isEmpty() ? null : valid.get(0);
    }

    private boolean validTarget(LivingEntity entity) {
        if (!entity.isAlive() || entity.getHealth() <= 0) return false;

        // Skip turret owner / trusted players
        if (base != null && entity instanceof Player player) {
            if (PlayerUtil.isOwner(base, player)) return false;
            if (base.getTrustManager().isTrusted(player, omtreborn.lib.permission.EnumAccessLevel.OPEN_GUI)) return false;
        }

        // Range check
        if (chebyshevDistance(turret, entity)) return false;
        if (chebyshevTooClose(turret, entity)) return false;

        // Line-of-sight check — prevents targeting underground or occluded entities
        if (!canSeeTargetFromPos(turret, entity)) return false;

        // Mob blacklist
        if (MobBlacklist.contains(entity)) return false;

        // Multi-targeting check
        if (base != null && !base.isMultiTargeting()) {
            if (isTargetAlreadyTargeted(entity, turret)) return false;
        }

        if (entity instanceof Player player) {
            if (!OMTConfig.TURRETS.globalCanTargetPlayers.get()) return false;
            return settings != null && settings.isTargetPlayers() && !player.isCreative() && !player.isSpectator();
        }

        if (entity instanceof Monster) {
            return settings != null && settings.isTargetMobs() && isEntityValidMob(entity);
        }

        // Passive / neutral mobs (non-player, non-monster)
        if (settings != null && settings.isTargetPassive()) {
            return isEntityValidNeutral(entity);
        }

        return false;
    }

    private static boolean isEntityValidMob(LivingEntity entity) {
        return !MobBlacklist.contains(entity);
    }

    private static boolean isEntityValidNeutral(LivingEntity entity) {
        return !NeutralList.contains(entity);
    }

    public static boolean canSeeTargetFromPos(TurretHeadBlockEntity turret, LivingEntity target) {
        if (turret.getLevel() == null) return false;
        BlockPos pos = turret.getBlockPos();
        // Origin must be outside the turret head block (pos occupies Y to Y+1); start just above it
        Vec3 origin = new Vec3(pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5);
        Vec3 targetVec = new Vec3(target.getX(), target.getY() + target.getEyeHeight() * 0.85, target.getZ());
        BlockHitResult result = turret.getLevel().clip(
                new ClipContext(origin, targetVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        return result.getType() == HitResult.Type.MISS;
    }

    /** @return true if target is OUTSIDE range (out of range = should drop target) */
    public static boolean chebyshevDistance(TurretHeadBlockEntity turret, LivingEntity target) {
        if (turret.getBase() == null) return true;
        int range = turret.getBase().getRange();
        BlockPos pos = turret.getBlockPos();
        double dx = Math.abs(target.getX() - (pos.getX() + 0.5));
        double dy = Math.abs(target.getY() - (pos.getY() + 0.5));
        double dz = Math.abs(target.getZ() - (pos.getZ() + 0.5));
        return Math.max(dx, Math.max(dy, dz)) > range;
    }

    /** @return true if target is INSIDE minimum range (too close to engage) */
    public static boolean chebyshevTooClose(TurretHeadBlockEntity turret, LivingEntity target) {
        int minRange = turret.getTurretMinRange();
        if (minRange <= 0) return false;
        BlockPos pos = turret.getBlockPos();
        double dx = Math.abs(target.getX() - (pos.getX() + 0.5));
        double dy = Math.abs(target.getY() - (pos.getY() + 0.5));
        double dz = Math.abs(target.getZ() - (pos.getZ() + 0.5));
        return Math.max(dx, Math.max(dy, dz)) < minRange;
    }

    private static boolean isTargetAlreadyTargeted(LivingEntity target, TurretHeadBlockEntity self) {
        if (self.getLevel() == null || self.getBase() == null) return false;
        for (TurretHeadBlockEntity other : TurretHeadUtil.getBaseTurrets(self.getLevel(), self.getBase().getBlockPos()).values()) {
            if (other != self && other.target == target) return true;
        }
        return false;
    }
}
