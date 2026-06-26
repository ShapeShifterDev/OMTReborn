package omtreborn.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import omtreborn.api.lists.AmmoList;
import omtreborn.config.OMTConfig;
import omtreborn.lib.permission.EnumAccessLevel;
import omtreborn.lib.util.PlayerUtil;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.turret.TurretHeadUtil;

import javax.annotation.Nullable;

public class OMTUtil {

    public static boolean isItemStackValidAmmo(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!OMTConfig.GENERAL.useWhitelistForAmmo.get()) return true;
        return AmmoList.contains(stack);
    }

    public static boolean canDamagePlayer(Player player, @Nullable TurretBaseBlockEntity base) {
        if (!OMTConfig.TURRETS.globalCanTargetPlayers.get()) return false;
        if (player.isCreative() || player.isSpectator()) return false;
        if (base == null) return true;
        if (!base.isAttacksPlayers()) return false;
        if (PlayerUtil.isOwner(base, player)) return false;
        if (base.getTrustManager().isTrusted(player, EnumAccessLevel.OPEN_GUI)) return false;
        return true;
    }

    public static boolean canDamageEntity(Entity entity, @Nullable TurretBaseBlockEntity base) {
        if (entity instanceof Player player) return canDamagePlayer(player, base);
        return base == null || base.isAttacksMobs() || base.isAttacksNeutrals();
    }

    public static void setTagsForTurretHit(Entity entity, @Nullable TurretBaseBlockEntity base) {
        if (base == null) return;
        CompoundTag tag = entity.getPersistentData();
        tag.putBoolean("hitByTurret", true);
        if (!OMTConfig.GENERAL.doTurretsKillsDropMobLoot.get()) {
            tag.putBoolean("noDrops", true);
        }
        int fakeDrops = TurretHeadUtil.getFakeDropsLevel(base);
        if (OMTConfig.GENERAL.doLootAddonsOverrideMobLootSetting.get() && fakeDrops >= 0) {
            tag.putBoolean("noDrops", false);
            tag.putInt("fakeDropsLevel", fakeDrops);
        }
    }
}
