package omtreborn.gui.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import omtreborn.items.OMTAddonItem;

public class AddonSlot extends SlotItemHandler {

    public AddonSlot(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof OMTAddonItem;
    }

    @Override
    public int getMaxStackSize() {
        return 4;
    }
}
