package omtreborn.lib.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class InvUtil {

    private InvUtil() {}

    public static boolean canInsert(IItemHandler handler, ItemStack stack) {
        return ItemHandlerHelper.insertItemStacked(handler, stack.copy(), true).isEmpty();
    }

    public static ItemStack extractFirst(IItemHandler handler, int maxAmount) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.extractItem(i, maxAmount, false);
            if (!stack.isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }

    public static boolean isEmpty(IItemHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            if (!handler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }
}
