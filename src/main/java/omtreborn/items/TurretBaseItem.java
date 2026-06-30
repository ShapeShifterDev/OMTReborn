package omtreborn.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import omtreborn.blocks.BlockTurretBase;
import omtreborn.config.BaseSetting;
import omtreborn.config.OMTConfig;
import net.minecraft.world.item.BlockItem;

import javax.annotation.Nullable;
import java.util.List;

public class TurretBaseItem extends BlockItem {

    public TurretBaseItem(BlockTurretBase block, Properties props) {
        super(block, props);
    }

    public int getTier() {
        return ((BlockTurretBase) getBlock()).getTier();
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("block.omtreborn.turret_base.tier" + getTier());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.omtreborn.hold_shift").withStyle(ChatFormatting.GRAY));
            return;
        }
        int tier = getTier();
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
