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
import omtreborn.blocks.BlockTurretBase;
import omtreborn.config.BaseSetting;
import omtreborn.config.OMTConfig;

import javax.annotation.Nullable;
import java.util.List;

public class TurretBaseItem extends BlockItem {

    public static final String TAG_TIER = "tier";

    public TurretBaseItem(Block block, Properties props) {
        super(block, props);
    }

    public static int getTier(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains(TAG_TIER)) ? Mth.clamp(tag.getInt(TAG_TIER), 1, 5) : 1;
    }

    /** Returns a copy of baseStack with the given tier encoded in NBT. */
    public static ItemStack ofTier(ItemStack baseStack, int tier) {
        ItemStack copy = baseStack.copy();
        int t = Mth.clamp(tier, 1, 5);
        CompoundTag tag = copy.getOrCreateTag();
        tag.putInt(TAG_TIER, t);
        tag.putInt("CustomModelData", t);
        return copy;
    }

    @Override
    public Component getName(ItemStack stack) {
        int tier = getTier(stack);
        return Component.translatable("block.omtreborn.turret_base.tier" + tier);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        if (state != null) {
            state = state.setValue(BlockTurretBase.TIER, getTier(context.getItemInHand()));
        }
        return state;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.omtreborn.hold_shift").withStyle(ChatFormatting.GRAY));
            return;
        }
        int tier = getTier(stack);
        BaseSetting s = switch (tier) {
            case 1 -> OMTConfig.BASES.baseTierOne;
            case 2 -> OMTConfig.BASES.baseTierTwo;
            case 3 -> OMTConfig.BASES.baseTierThree;
            case 4 -> OMTConfig.BASES.baseTierFour;
            default -> OMTConfig.BASES.baseTierFive;
        };
        tooltip.add(Component.translatable("tooltip.omtreborn.turret_base.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.omtreborn.energy.header").withStyle(ChatFormatting.AQUA));
        if (tier == 1) {
            tooltip.add(Component.translatable("tooltip.omtreborn.energy.fuel_powered").withStyle(ChatFormatting.WHITE));
        } else {
            tooltip.add(Component.translatable("tooltip.omtreborn.energy.max",
                    Component.literal(String.valueOf(s.baseMaxCharge.get())).withStyle(ChatFormatting.WHITE)));
        }
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.omtreborn.extras.header").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("tooltip.omtreborn.extras.turret_limit",
                Component.literal(String.valueOf(s.baseMaxTurrets.get())).withStyle(ChatFormatting.WHITE)));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.omtreborn.turret_base.tier" + tier + ".flavour")
                .withStyle(ChatFormatting.DARK_GRAY));
    }
}
