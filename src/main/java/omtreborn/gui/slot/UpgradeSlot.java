package omtreborn.gui.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import omtreborn.items.OMTUpgradeItem;

public class UpgradeSlot extends SlotItemHandler {

    public UpgradeSlot(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof OMTUpgradeItem;
    }

    @Override
    public int getMaxStackSize() {
        return 4;
    }
}
