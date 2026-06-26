package omtreborn.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.blocks.BlockExpander;
import omtreborn.config.OMTConfig;

import javax.annotation.Nullable;
import java.util.List;

/**
 * BlockItem for expander blocks.
 * Types 0-4: power storage tiers 1-5.
 * Types 5-9: inventory tiers 1-5.
 * Type is stored in item NBT as "expander_type".
 */
public class ExpanderItem extends BlockItem {

    public static final String TAG_TYPE = "expander_type";

    public ExpanderItem(Block block, Properties props) {
        super(block, props);
    }

    public static int getExpanderType(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains(TAG_TYPE)) ? Mth.clamp(tag.getInt(TAG_TYPE), 0, 9) : 0;
    }

    /** Returns a copy of baseStack with the given expander type encoded in NBT. */
    public static ItemStack ofType(ItemStack baseStack, int type) {
        ItemStack copy = baseStack.copy();
        int t = Mth.clamp(type, 0, 9);
        copy.getOrCreateTag().putInt(TAG_TYPE, t);
        return copy;
    }

    @Override
    public Component getName(ItemStack stack) {
        int type = getExpanderType(stack);
        boolean isInv = type >= 5;
        int tier = (type % 5) + 1;
        String key = isInv
                ? "block.omtreborn.expander.inv.tier" + tier
                : "block.omtreborn.expander.power.tier" + tier;
        return Component.translatable(key);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state != null) {
            state = state.setValue(BlockExpander.EXPANDER_TYPE, getExpanderType(context.getItemInHand()));
        }
        return state;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.omtreborn.hold_shift").withStyle(ChatFormatting.GRAY));
            return;
        }
        int type = getExpanderType(stack);
        boolean isInv = type >= 5;
        int tier = (type % 5) + 1;

        tooltip.add(Component.empty());
        if (isInv) {
            int slots = switch (tier) { case 1 -> 4; case 2 -> 8; case 3 -> 16; case 4 -> 32; default -> 64; };
            tooltip.add(Component.translatable("tooltip.omtreborn.expander.inv.header").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltip.omtreborn.expander.inv.desc",
                    Component.literal(String.valueOf(slots)).withStyle(ChatFormatting.WHITE)));
        } else {
            int capacity = switch (tier) {
                case 1 -> OMTConfig.MISCELLANEOUS.expanderPowerTierOneCapacity.get();
                case 2 -> OMTConfig.MISCELLANEOUS.expanderPowerTierTwoCapacity.get();
                case 3 -> OMTConfig.MISCELLANEOUS.expanderPowerTierThreeCapacity.get();
                case 4 -> OMTConfig.MISCELLANEOUS.expanderPowerTierFourCapacity.get();
                default -> OMTConfig.MISCELLANEOUS.expanderPowerTierFiveCapacity.get();
            };
            tooltip.add(Component.translatable("tooltip.omtreborn.expander.power.header").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltip.omtreborn.expander.power.desc",
                    Component.literal(String.valueOf(capacity)).withStyle(ChatFormatting.WHITE)));
        }
    }
}
