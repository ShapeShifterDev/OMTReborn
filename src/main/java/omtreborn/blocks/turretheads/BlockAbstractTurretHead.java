package omtreborn.blocks.turretheads;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import omtreborn.blocks.BlockTurretBase;
import omtreborn.config.OMTConfig;
import omtreborn.lib.block.OMLTileBlock;
import omtreborn.lib.tileentity.OMLOwnedBlockEntity;

import javax.annotation.Nullable;

public abstract class BlockAbstractTurretHead extends OMLTileBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockAbstractTurretHead(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // --- Placement ---

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, Direction.UP);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        if (!(below.getBlock() instanceof BlockTurretBase)) return false;
        return ((BlockTurretBase) below.getBlock()).getTier() >= getMinimumTier();
    }

    public abstract int getMinimumTier();

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                             @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide && placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof OMLOwnedBlockEntity owned) {
                owned.setOwnerUUID(player.getUUID());
            }
            // TODO Phase 6: ((TurretHead) be).updateMaxRange() on adjacent TurretBase
        }
    }

    // --- Neighbor logic ---

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block,
                                 BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide && !(level.getBlockState(pos.below()).getBlock() instanceof BlockTurretBase)) {
            Block.popResource(level, pos, new ItemStack(this.asItem()));
            level.removeBlock(pos, false);
        }
    }

    // --- Interaction ---

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer) {
            // TODO Phase 9: open turret config GUI
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // --- Breaking ---

    @Override
    public float getDestroyProgress(BlockState state, Player player, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        if (!OMTConfig.TURRETS.turretBreakable.get()) return -1.0f;
        return super.getDestroyProgress(state, player, level, pos);
    }

    // --- Block entity (abstract — each concrete subclass provides its own) ---

    @Override
    @Nullable
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return getBlockEntityTicker(level, state, type);
    }

    @Nullable
    protected <T extends BlockEntity> BlockEntityTicker<T> getBlockEntityTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
