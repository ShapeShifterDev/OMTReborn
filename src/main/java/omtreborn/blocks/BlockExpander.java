package omtreborn.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import omtreborn.init.ModBlockEntities;
import omtreborn.tileentity.ExpanderBlockEntity;

import javax.annotation.Nullable;

/**
 * Types 0-4: power storage expanders (tiers 1-5).
 * Types 5-9: inventory expanders (tiers 1-5).
 */
public class BlockExpander extends AbstractBaseAttachment {

    public static final IntegerProperty EXPANDER_TYPE = IntegerProperty.create("expander_type", 0, 9);

    public BlockExpander(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
                .setValue(EXPANDER_TYPE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXPANDER_TYPE);
    }

    public static boolean isInventoryExpander(BlockState state) {
        return state.getValue(EXPANDER_TYPE) >= 5;
    }

    public static int getExpanderTier(BlockState state) {
        int type = state.getValue(EXPANDER_TYPE);
        return (type % 5) + 1;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer sp && isInventoryExpander(state)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ExpanderBlockEntity expander) {
                NetworkHooks.openScreen(sp, expander, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ExpanderBlockEntity(pos, state);
    }

    @Override
    protected <T extends BlockEntity> BlockEntityTicker<T> createServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.EXPANDER.get(), ExpanderBlockEntity::tick);
    }
}
