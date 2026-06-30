package omtreborn.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModBlockEntities;
import net.minecraftforge.network.NetworkHooks;
import omtreborn.lib.block.OMLTileBlock;
import omtreborn.lib.permission.EnumAccessLevel;
import omtreborn.lib.permission.TrustedPlayer;
import omtreborn.lib.tileentity.OMLOwnedBlockEntity;
import omtreborn.tileentity.TurretBaseBlockEntity;

import javax.annotation.Nullable;

public class BlockTurretBase extends OMLTileBlock {

    private final int tier;

    public BlockTurretBase(int tier, BlockBehaviour.Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }

    // --- Adjacency helpers used by attachment blocks ---

    @Nullable
    public static BlockPos findAdjacent(LevelReader level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos adj = pos.relative(dir);
            if (level.getBlockState(adj).getBlock() instanceof BlockTurretBase) {
                return adj;
            }
        }
        return null;
    }

    public static boolean hasAdjacent(LevelReader level, BlockPos pos) {
        return findAdjacent(level, pos) != null;
    }

    // --- Block behaviour ---

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return switch (tier) {
            case 1 -> SoundType.WOOD;
            case 5 -> SoundType.DEEPSLATE;
            default -> SoundType.STONE;
        };
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (!OMTConfig.BASES.baseBreakable.get()) return -1.0f;
        float hardness = tier == 5 ? 3.5f : 2.0f;
        int divisor = player.hasCorrectToolForDrops(state) ? 30 : 100;
        return player.getDestroySpeed(state) / hardness / divisor;
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        if (!OMTConfig.BASES.baseBreakable.get()) return Float.MAX_VALUE;
        return switch (tier) {
            case 1 -> OMTConfig.BASES.baseTierOne.baseBlastResistance.get().floatValue();
            case 2 -> OMTConfig.BASES.baseTierTwo.baseBlastResistance.get().floatValue();
            case 3 -> OMTConfig.BASES.baseTierThree.baseBlastResistance.get().floatValue();
            case 4 -> OMTConfig.BASES.baseTierFour.baseBlastResistance.get().floatValue();
            default -> OMTConfig.BASES.baseTierFive.baseBlastResistance.get().floatValue();
        };
    }

    // --- Placement ---

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(pos.relative(dir)).getBlock() instanceof BlockTurretBase) {
                return false;
            }
        }
        return true;
    }

    // --- Interaction ---

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TurretBaseBlockEntity base) {
                NetworkHooks.openScreen(sp, base, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // --- Owner ---

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                             @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide && placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TurretBaseBlockEntity base) {
                base.setOwnerUUID(player.getUUID());
                base.getTrustManager().setOwner(new TrustedPlayer(player, EnumAccessLevel.ADMIN));
                base.setChanged();
            }
        }
    }

    // --- Neighbor logic ---

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                 BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            // TODO Phase 6: ((TurretBase) level.getBlockEntity(pos)).updateExpanders();
        }
    }

    // --- Particles (client-side) ---

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (tier != 1) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof TurretBaseBlockEntity base) || !base.isBurning()) return;

        double cx = pos.getX() + 0.5;
        double cy = pos.getY();
        double cz = pos.getZ() + 0.5;

        if (random.nextDouble() < 0.1) {
            level.playLocalSound(cx, cy, cz, SoundEvents.FURNACE_FIRE_CRACKLE,
                    SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }

        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        double dx = facing.getStepX() * 0.52;
        double dz = facing.getStepZ() * 0.52;
        double dy = random.nextDouble() * 6.0 / 16.0;

        level.addParticle(ParticleTypes.SMOKE, cx + dx, cy + dy, cz + dz, 0, 0, 0);
        level.addParticle(ParticleTypes.FLAME, cx + dx, cy + dy, cz + dz, 0, 0, 0);
    }

    // --- Block entity ---

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TurretBaseBlockEntity(pos, state);
    }

    @Override
    protected <T extends BlockEntity> BlockEntityTicker<T> createServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.TURRET_BASE.get(), TurretBaseBlockEntity::tick);
    }
}
