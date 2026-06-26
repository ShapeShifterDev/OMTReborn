package omtreborn.gui.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import omtreborn.util.OMTUtil;

public class AmmoSlot extends SlotItemHandler {

    public AmmoSlot(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return OMTUtil.isItemStackValidAmmo(stack);
    }
}
