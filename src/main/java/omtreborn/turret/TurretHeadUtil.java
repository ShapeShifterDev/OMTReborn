package omtreborn.turret;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import omtreborn.api.lists.AmmoList;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModItems;
import omtreborn.init.ModSounds;
import omtreborn.tileentity.ExpanderBlockEntity;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.tileentity.turrets.TurretHeadBlockEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TurretHeadUtil {

    private record WarnKey(UUID playerUUID, BlockPos basePos) {}
    private static final Map<WarnKey, Long> warnedPlayers = new HashMap<>();

    public static void warnPlayers(TurretBaseBlockEntity base, Level level, BlockPos pos, int turretRange) {
        if (!base.isAttacksPlayers()) return;
        int warnDist = OMTConfig.TURRETS.turretWarningDistance.get();
        int r = turretRange + warnDist;
        net.minecraft.world.phys.AABB aabb = new net.minecraft.world.phys.AABB(
                pos.getX() - r, pos.getY() - r, pos.getZ() - r,
                pos.getX() + r, pos.getY() + r, pos.getZ() + r);
        for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, aabb)) {
            if (!omtreborn.lib.util.PlayerUtil.isOwner(base, player)
                    && !base.getTrustManager().isTrusted(player, omtreborn.lib.permission.EnumAccessLevel.OPEN_GUI)
                    && !player.isCreative()) {
                WarnKey key = new WarnKey(player.getUUID(), base.getBlockPos());
                long now = level.getGameTime();
                if (!warnedPlayers.containsKey(key) || warnedPlayers.get(key) < now) {
                    warnedPlayers.remove(key);
                    if (OMTConfig.TURRETS.turretAlarmSound.get()) {
                        player.playSound(ModSounds.WARNING.get(), 1.0f, 1.0f);
                    }
                    if (OMTConfig.TURRETS.turretWarnMessage.get()) {
                        player.sendSystemMessage(
                                Component.translatable("omtreborn.status.warning").withStyle(net.minecraft.ChatFormatting.RED));
                    }
                    warnedPlayers.put(key, now + 12000L);
                }
            }
        }
    }

    @Nullable
    public static TurretBaseBlockEntity getTurretBase(@Nullable Level level, BlockPos pos) {
        if (level == null) return null;
        for (Direction dir : Direction.values()) {
            BlockEntity be = level.getBlockEntity(pos.relative(dir));
            if (be instanceof TurretBaseBlockEntity base) return base;
        }
        return null;
    }

    @Nullable
    public static Direction getTurretBaseFacing(@Nullable Level level, BlockPos pos) {
        if (level == null) return null;
        for (Direction dir : Direction.values()) {
            if (level.getBlockEntity(pos.relative(dir)) instanceof TurretBaseBlockEntity) return dir;
        }
        return null;
    }

    public static Map<Direction, TurretHeadBlockEntity> getBaseTurrets(@Nullable Level level, BlockPos pos) {
        Map<Direction, TurretHeadBlockEntity> map = new HashMap<>();
        if (level == null) return map;
        for (Direction dir : Direction.values()) {
            BlockEntity be = level.getBlockEntity(pos.relative(dir));
            if (be instanceof TurretHeadBlockEntity head) map.put(dir, head);
        }
        return map;
    }

    public static float getAimYaw(LivingEntity target, BlockPos pos) {
        double dX = target.getX() - (pos.getX() + 0.5);
        double dZ = target.getZ() - (pos.getZ() + 0.5);
        float yaw = (float) Math.atan2(dZ, dX);
        if (yaw < 0) yaw += 2 * Math.PI;
        return yaw / (float) Math.PI * 180f;
    }

    public static float getAimPitch(LivingEntity target, BlockPos pos) {
        double dX = (target.getX() - 0.5) - (pos.getX() + 0.5);
        double dY = (target.getY() + 0.5) - (pos.getY() - 0.5);
        double dZ = (target.getZ() - 0.5) - (pos.getZ() + 0.5);
        // Angle from horizontal: 0° = level, positive = target above, negative = target below
        return (float) Math.toDegrees(Math.atan2(dY, Math.sqrt(dZ*dZ + dX*dX)));
    }

    // --- Upgrade helpers ---

    public static int getRangeUpgrades(TurretBaseBlockEntity base, TurretHeadBlockEntity turretHead) {
        int value = 0;
        for (int i : base.getUpgradeSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.RANGE_UPGRADE.get())) {
                value += (turretHead.getTurretType().getSettings().rangeUpgrade.get() * stack.getCount());
            }
        }
        return value;
    }

    public static int getScattershotUpgrades(TurretBaseBlockEntity base) {
        int value = 0;
        for (int i : base.getUpgradeSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.SCATTER_SHOT_UPGRADE.get())) {
                value += stack.getCount();
            }
        }
        return value;
    }

    public static float getAccuracyUpgrades(TurretBaseBlockEntity base, TurretHeadBlockEntity turretHead) {
        float accuracy = 0.0f;
        for (int i : base.getUpgradeSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.ACCURACY_UPGRADE.get())) {
                accuracy += (turretHead.getTurretType().getSettings().accuracyUpgrade.get() * stack.getCount());
            }
        }
        return accuracy;
    }

    public static float getEfficiencyUpgrades(TurretBaseBlockEntity base, TurretHeadBlockEntity turretHead) {
        float efficiency = 0.0f;
        for (int i : base.getUpgradeSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.EFFICIENCY_UPGRADE.get())) {
                efficiency += (turretHead.getTurretType().getSettings().efficiencyUpgrade.get() * stack.getCount());
            }
        }
        return efficiency;
    }

    public static float getFireRateUpgrades(TurretBaseBlockEntity base, TurretHeadBlockEntity turretHead) {
        float rof = 0.0f;
        for (int i : base.getUpgradeSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.FIRE_RATE_UPGRADE.get())) {
                rof += (turretHead.getTurretType().getSettings().fireRateUpgrade.get() * stack.getCount());
            }
        }
        return rof;
    }

    // --- Addon helpers ---

    public static int getAmpLevel(TurretBaseBlockEntity base) {
        int level = 0;
        for (int i : base.getAddonSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.DAMAGE_AMP_ADDON.get())) {
                level += stack.getCount();
            }
        }
        return level;
    }

    public static int getFakeDropsLevel(TurretBaseBlockEntity base) {
        int level = 0;
        if (base.getTier() < 2) return -1;
        for (int i : base.getAddonSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.FAKE_DROPS_ADDON.get())) {
                level += stack.getCount();
            }
        }
        return Math.min(level > 0 ? level - 1 : -1, 3);
    }

    private static boolean hasRecyclerAddon(TurretBaseBlockEntity base) {
        for (int i : base.getAddonSlots()) {
            ItemStack stack = base.getInventory().getStackInSlot(i);
            if (!stack.isEmpty() && stack.is(ModItems.RECYCLER_ADDON.get())) return true;
        }
        return false;
    }

    public static boolean baseHasNoLootDeleter(TurretBaseBlockEntity base) {
        return true; // Loot deleter block dropped in port
    }

    // --- Power expander capacity ---

    public static int getPowerExpanderTotalExtraCapacity(TurretBaseBlockEntity base) {
        int total = 0;
        for (ExpanderBlockEntity exp : base.getExpanderMap().values()) {
            if (exp != null && exp.isPowerExpander()) {
                total += getPowerExtenderCapacityValue(exp);
            }
        }
        return total;
    }

    private static int getPowerExtenderCapacityValue(ExpanderBlockEntity expander) {
        int tier = expander.getTier();
        return switch (tier) {
            case 1 -> OMTConfig.MISCELLANEOUS.expanderPowerTierOneCapacity.get();
            case 2 -> OMTConfig.MISCELLANEOUS.expanderPowerTierTwoCapacity.get();
            case 3 -> OMTConfig.MISCELLANEOUS.expanderPowerTierThreeCapacity.get();
            case 4 -> OMTConfig.MISCELLANEOUS.expanderPowerTierFourCapacity.get();
            default -> OMTConfig.MISCELLANEOUS.expanderPowerTierFiveCapacity.get();
        };
    }

    // --- Ammo deduction ---

    public static ItemStack deductItemStackFromInventories(@Nullable ItemStack itemStack,
                                                            TurretBaseBlockEntity base,
                                                            @Nullable TurretHeadBlockEntity turretHead) {
        boolean disposable = (itemStack == null);

        if (turretHead != null && hasRecyclerAddon(base)) {
            double negateChance = turretHead.getTurretType().getSettings().recyclerNegateChance.get();
            if (Math.random() < negateChance) {
                return disposable ? new ItemStack(net.minecraft.world.item.Items.AIR) : itemStack.copy();
            }
        }

        int needed = itemStack != null ? itemStack.getCount() : 1;
        int found = 0;
        record SlotInfo(net.minecraftforge.items.ItemStackHandler inv, int slot, int count) {}
        List<SlotInfo> foundSlots = new ArrayList<>();

        for (net.minecraftforge.items.ItemStackHandler inv : base.getAmmoInventories()) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack slot = inv.getStackInSlot(i);
                if (slot.isEmpty()) continue;
                boolean matches = disposable ? AmmoList.contains(slot)
                        : slot.is(itemStack.getItem());
                if (matches) {
                    int take = Math.min(slot.getCount(), needed - found);
                    foundSlots.add(new SlotInfo(inv, i, take));
                    found += take;
                }
                if (found >= needed) break;
            }
            if (found >= needed) break;
        }

        if (found < needed) return ItemStack.EMPTY;

        for (SlotInfo info : foundSlots) {
            info.inv().extractItem(info.slot(), info.count(), false);
        }

        return disposable ? new ItemStack(net.minecraft.world.item.Items.AIR) : itemStack;
    }

    public static int getAmmoLevel(TurretHeadBlockEntity turret, TurretBaseBlockEntity base) {
        ItemStack ammoRequired = turret.getAmmo();
        if (!OMTConfig.TURRETS.doTurretsNeedAmmo.get() && ammoRequired != null) {
            return Integer.MAX_VALUE;
        }
        if (ammoRequired == null) {
            return base.getEnergyStorage().getEnergyStored() / turret.getTurretBasePowerUsage();
        }
        int count = 0;
        for (net.minecraftforge.items.ItemStackHandler inv : base.getAmmoInventories()) {
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty() && stack.is(ammoRequired.getItem())) {
                    count += stack.getCount();
                }
            }
        }
        return count;
    }

}
