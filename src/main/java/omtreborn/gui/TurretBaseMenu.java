package omtreborn.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import omtreborn.gui.slot.AddonSlot;
import omtreborn.gui.slot.AmmoSlot;
import omtreborn.gui.slot.FuelSlot;
import omtreborn.gui.slot.UpgradeSlot;
import omtreborn.init.ModMenuTypes;
import omtreborn.items.OMTAddonItem;
import omtreborn.items.OMTUpgradeItem;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.List;

public class TurretBaseMenu extends AbstractContainerMenu {

    private final TurretBaseBlockEntity base;

    public static final int PLAYER_SIZE = 36;
    public final int ammoStart;
    public final int ammoEnd;
    public final int fuelSlotIndex;
    public final int addonStart;
    public final int addonEnd;
    public final int upgradeStart;
    public final int upgradeEnd;

    public TurretBaseMenu(int windowId, Inventory playerInv, TurretBaseBlockEntity base) {
        super(ModMenuTypes.TURRET_BASE.get(), windowId);
        this.base = base;

        // Player hotbar (inv slots 0-8)
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInv, i, 8 + i * 18, 172));
        }
        // Player main inventory (inv slots 9-35)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, 9 + col + row * 9, 8 + col * 18, 114 + row * 18));
            }
        }

        // Ammo slots (BE inv slots 0-8): 3×3 grid
        ammoStart = slots.size();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new AmmoSlot(base.getInventory(), col + row * 3, 8 + col * 18, 17 + row * 18));
            }
        }
        ammoEnd = slots.size();

        int tier = base != null ? base.getTier() : 0;

        // Fuel slot — Tier 1 only, at position (62, 17)
        if (tier == 1) {
            fuelSlotIndex = slots.size();
            addSlot(new FuelSlot(base.getFuelInventory(), 0, 80, 17));
            addDataSlots(base.getContainerData());
        } else {
            fuelSlotIndex = -1;
        }

        // Addon and upgrade slots — tier >= 2 only
        if (tier >= 2) {
            addonStart = slots.size();
            List<Integer> addonIndices = base.getAddonSlots();
            addSlot(new AddonSlot(base.getInventory(), addonIndices.get(0), 62, 18));
            addSlot(new AddonSlot(base.getInventory(), addonIndices.get(1), 80, 18));
            addonEnd = slots.size();

            upgradeStart = slots.size();
            List<Integer> upgradeIndices = base.getUpgradeSlots();
            addSlot(new UpgradeSlot(base.getInventory(), upgradeIndices.get(0), 62, 52));
            if (tier >= 5) {
                addSlot(new UpgradeSlot(base.getInventory(), upgradeIndices.get(1), 80, 52));
            }
            upgradeEnd = slots.size();
        } else {
            addonStart = -1;
            addonEnd = -1;
            upgradeStart = -1;
            upgradeEnd = -1;
        }
    }

    public TurretBaseBlockEntity getBase() {
        return base;
    }

    public boolean isBurning() {
        return base != null && base.isBurning();
    }

    public int getLitProgress() {
        if (base == null) return 0;
        int bt = base.getBurnTime();
        int mbt = base.getMaxBurnTime();
        return (mbt > 0 && bt > 0) ? bt * 13 / mbt : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        if (base == null) return false;
        var pos = base.getBlockPos();
        return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return result;

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if (index < PLAYER_SIZE) {
            // From player inventory → route to appropriate turret slot
            if (stack.getItem() instanceof OMTAddonItem) {
                if (addonStart < 0 || !moveItemStackTo(stack, addonStart, addonEnd, false))
                    return ItemStack.EMPTY;
            } else if (stack.getItem() instanceof OMTUpgradeItem) {
                if (upgradeStart < 0 || !moveItemStackTo(stack, upgradeStart, upgradeEnd, false))
                    return ItemStack.EMPTY;
            } else if (fuelSlotIndex >= 0 && ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0) {
                if (!moveItemStackTo(stack, fuelSlotIndex, fuelSlotIndex + 1, false))
                    return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(stack, ammoStart, ammoEnd, false))
                    return ItemStack.EMPTY;
            }
        } else {
            // From turret slot → player inventory
            if (!moveItemStackTo(stack, 0, PLAYER_SIZE, false))
                return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return result;
    }
}
