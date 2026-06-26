package omtreborn.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import omtreborn.gui.slot.AmmoSlot;
import omtreborn.init.ModMenuTypes;
import omtreborn.tileentity.ExpanderBlockEntity;

public class ExpanderInvMenu extends AbstractContainerMenu {

    private final ExpanderBlockEntity expander;

    public static final int AMMO_SIZE = 9;
    public static final int PLAYER_SIZE = 36;

    public ExpanderInvMenu(int windowId, Inventory playerInv, ExpanderBlockEntity expander) {
        super(ModMenuTypes.EXPANDER_INV.get(), windowId);
        this.expander = expander;

        // Ammo slots (BE inv slots 0-8): 3×3 grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new AmmoSlot(expander.getInventory(), col + row * 3, 62 + col * 18, 17 + row * 18));
            }
        }

        // Player hotbar (inv slots 0-8)
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
        // Player main inventory (inv slots 9-35): 3 rows
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInv, 9 + col + row * 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    public ExpanderBlockEntity getExpander() {
        return expander;
    }

    @Override
    public boolean stillValid(Player player) {
        if (expander == null) return false;
        var pos = expander.getBlockPos();
        return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return result;

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if (index < AMMO_SIZE) {
            // From ammo slot → player inventory
            if (!moveItemStackTo(stack, AMMO_SIZE, AMMO_SIZE + PLAYER_SIZE, false))
                return ItemStack.EMPTY;
        } else {
            // From player inventory → ammo slots
            if (!moveItemStackTo(stack, 0, AMMO_SIZE, false))
                return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return result;
    }
}
