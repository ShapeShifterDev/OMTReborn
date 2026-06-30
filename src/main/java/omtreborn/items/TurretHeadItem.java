package omtreborn.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import omtreborn.blocks.BlockTurretBase;
import omtreborn.blocks.turretheads.BlockAbstractTurretHead;
import omtreborn.client.render.TurretItemRenderers;
import omtreborn.config.TurretSetting;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TurretHeadItem extends BlockItem {

    private static final DecimalFormat RATE_FORMAT = new DecimalFormat("0.0");

    private final Supplier<TurretSetting> setting;
    private final String descKey;
    private final String damageDescKey;
    @Nullable private final String ammoKey;
    private final boolean isActivation;

    public TurretHeadItem(Block block, Properties props, Supplier<TurretSetting> setting,
                           String descKey, String damageDescKey, @Nullable String ammoKey, boolean isActivation) {
        super(block, props);
        this.setting = setting;
        this.descKey = descKey;
        this.damageDescKey = damageDescKey;
        this.ammoKey = ammoKey;
        this.isActivation = isActivation;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    Block block = TurretHeadItem.this.getBlock();
                    if (block != null) {
                        renderer = TurretItemRenderers.get(block);
                    }
                }
                return renderer != null ? renderer : IClientItemExtensions.super.getCustomRenderer();
            }
        });
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        BlockPos placePos = context.getClickedPos();
        BlockState below = context.getLevel().getBlockState(placePos.below());
        if (below.getBlock() instanceof BlockTurretBase) {
            int baseTier = ((BlockTurretBase) below.getBlock()).getTier();
            int required = ((BlockAbstractTurretHead) getBlock()).getMinimumTier();
            if (baseTier < required) {
                Player player = context.getPlayer();
                if (player != null) {
                    player.displayClientMessage(
                        Component.translatable("message.omtreborn.turret.tier_too_low", required),
                        true
                    );
                }
                return InteractionResult.FAIL;
            }
        }
        return super.place(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.omtreborn.hold_shift").withStyle(ChatFormatting.GRAY));
            return;
        }
        TurretSetting s = setting.get();
        tooltip.add(Component.translatable(descKey).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.info.header").withStyle(ChatFormatting.GOLD));

        tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.range",
                Component.literal(s.baseMinRange.get() + "-" + s.baseRange.get()).withStyle(ChatFormatting.WHITE)));

        if (isActivation) {
            tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.effect",
                    Component.translatable(damageDescKey).withStyle(ChatFormatting.WHITE)));
            tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.activation_rate",
                    Component.literal(RATE_FORMAT.format(20.0 / s.baseFireRate.get())).withStyle(ChatFormatting.WHITE)));
            tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.power.activation",
                    Component.literal(String.valueOf(s.powerUsage.get())).withStyle(ChatFormatting.WHITE)));
        } else {
            tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.damage",
                    Component.translatable(damageDescKey).withStyle(ChatFormatting.WHITE)));
            tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.fire_rate",
                    Component.literal(RATE_FORMAT.format(20.0 / s.baseFireRate.get())).withStyle(ChatFormatting.WHITE)));
            tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.power",
                    Component.literal(String.valueOf(s.powerUsage.get())).withStyle(ChatFormatting.WHITE)));
            if (ammoKey != null) {
                tooltip.add(Component.translatable("tooltip.omtreborn.turret_head.ammo",
                        Component.translatable(ammoKey).withStyle(ChatFormatting.WHITE)));
            }
        }
    }
}
