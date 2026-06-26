package omtreborn.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import omtreborn.blocks.BlockExpander;
import omtreborn.gui.ExpanderInvMenu;
import omtreborn.init.ModBlockEntities;
import omtreborn.lib.tileentity.OMLContainerBlockEntity;
import omtreborn.turret.TurretHeadUtil;
import omtreborn.util.OMTUtil;

public class ExpanderBlockEntity extends OMLContainerBlockEntity implements MenuProvider {

    private static final int INVENTORY_SIZE = 9;

    private boolean powerExpander;
    private int tier;
    private Direction orientation = Direction.NORTH;

    public ExpanderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXPANDER.get(), pos, state);
        // Read type and tier from blockstate
        if (state.hasProperty(BlockExpander.EXPANDER_TYPE)) {
            int type = state.getValue(BlockExpander.EXPANDER_TYPE);
            this.powerExpander = type < 5;
            this.tier = (type % 5) + 1;
        } else {
            this.powerExpander = false;
            this.tier = 1;
        }
    }

    @Override
    protected ItemStackHandler createInventory() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!isPowerExpander() && !OMTUtil.isItemStackValidAmmo(stack)) return stack;
                if (isPowerExpander()) return stack; // power expanders hold no items
                return super.insertItem(slot, stack, simulate);
            }
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.omtreborn.expander_inv");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
        return new ExpanderInvMenu(windowId, inv, this);
    }

    public boolean isPowerExpander() {
        return powerExpander;
    }

    public int getTier() {
        return tier;
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setSide() {
        if (level != null) {
            Direction facing = TurretHeadUtil.getTurretBaseFacing(level, worldPosition);
            if (facing != null) this.orientation = facing;
        }
    }

    public TurretBaseBlockEntity getBase() {
        return TurretHeadUtil.getTurretBase(level, worldPosition);
    }

    public static void tick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state,
                             ExpanderBlockEntity be) {
        if (level.isClientSide) return;
        if (level.getGameTime() % 15 == 0 && be.getBase() == null) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("powerExpander", powerExpander);
        tag.putInt("expanderTier", tier);
        tag.putByte("direction", (byte) orientation.ordinal());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.powerExpander = tag.getBoolean("powerExpander");
        if (tag.contains("expanderTier")) {
            this.tier = tag.getInt("expanderTier");
        }
        if (tag.contains("direction")) {
            int ord = tag.getByte("direction");
            Direction[] dirs = Direction.values();
            if (ord >= 0 && ord < dirs.length) this.orientation = dirs[ord];
        }
    }
}
