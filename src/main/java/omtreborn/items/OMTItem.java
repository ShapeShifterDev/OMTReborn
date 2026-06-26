package omtreborn.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Base class for items that show a shift-to-reveal tooltip.
 * descKey maps to two lang entries: descKey (body) and descKey + ".flavour" (flavor line).
 */
public class OMTItem extends Item {

    private final String labelKey;
    private final ChatFormatting labelColor;
    private final String descKey;

    public OMTItem(Properties props, String labelKey, ChatFormatting labelColor, String descKey) {
        super(props);
        this.labelKey = labelKey;
        this.labelColor = labelColor;
        this.descKey = descKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable(labelKey).withStyle(labelColor));
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable(descKey));
        } else {
            tooltip.add(Component.translatable("tooltip.omtreborn.hold_shift").withStyle(ChatFormatting.GRAY));
        }
    }
}
