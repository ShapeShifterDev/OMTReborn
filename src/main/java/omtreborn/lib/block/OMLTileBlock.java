package omtreborn.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nullable;

public abstract class OMLTileBlock extends OMLBlock implements EntityBlock {

    public OMLTileBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createServerTicker(level, state, type);
    }

    @Nullable
    protected <T extends BlockEntity> BlockEntityTicker<T> createServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> actualType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == actualType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be != null) {
                be.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        var stack = handler.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            popResource(level, pos, stack);
                        }
                    }
                });
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
