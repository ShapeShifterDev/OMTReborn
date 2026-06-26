package omtreborn.turret;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import omtreborn.lib.util.TargetingSettings;

public enum EnumTargetingPriority {
    MAX_HP, HP_REMAINING, DISTANCE, ARMOR, PLAYER;

    public static int getValueByPriority(LivingEntity entity, BlockPos pos, EnumTargetingPriority priority,
                                         TargetingSettings settings, Integer[] priorities) {
        if (priority == MAX_HP)
            return (int) Math.floor(entity.getHealth()) * priorities[MAX_HP.ordinal()];
        if (priority == HP_REMAINING)
            return (int) Math.floor(entity.getMaxHealth() - entity.getHealth()) * priorities[HP_REMAINING.ordinal()];
        if (priority == DISTANCE) {
            double dx = entity.getX() - (pos.getX() + 0.5);
            double dy = entity.getY() - (pos.getY() + 0.5);
            double dz = entity.getZ() - (pos.getZ() + 0.5);
            return (int) Math.floor(Math.sqrt(dx*dx + dy*dy + dz*dz)) * priorities[DISTANCE.ordinal()];
        }
        if (priority == ARMOR)
            return (int) (Math.floor(entity.getArmorValue()) + 1) * priorities[ARMOR.ordinal()];
        if (priority == PLAYER)
            return entity instanceof Player ? 100 * priorities[PLAYER.ordinal()] : -100;
        return 1;
    }
}
