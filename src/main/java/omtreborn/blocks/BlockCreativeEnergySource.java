package omtreborn.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.init.ModBlockEntities;
import omtreborn.lib.block.OMLTileBlock;
import omtreborn.tileentity.CreativeEnergySourceBlockEntity;

import javax.annotation.Nullable;

public class BlockCreativeEnergySource extends OMLTileBlock {

    public BlockCreativeEnergySource(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CreativeEnergySourceBlockEntity(pos, state);
    }

    @Override
    @Nullable
    protected <T extends BlockEntity> BlockEntityTicker<T> createServerTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.CREATIVE_ENERGY_SOURCE.get(),
                CreativeEnergySourceBlockEntity::tick);
    }
}
