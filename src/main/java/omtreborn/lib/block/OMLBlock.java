package omtreborn.lib.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class OMLBlock extends Block {

    public OMLBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos,
                                 SpawnPlacements.Type type, EntityType<?> entityType) {
        return false;
    }
}
