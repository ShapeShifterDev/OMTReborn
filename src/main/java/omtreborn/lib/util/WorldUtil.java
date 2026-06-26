package omtreborn.lib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil {

    private WorldUtil() {}

    public static List<BlockEntity> getTouchingBlockEntities(Level level, BlockPos pos) {
        List<BlockEntity> result = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            BlockEntity be = level.getBlockEntity(pos.relative(dir));
            if (be != null) result.add(be);
        }
        return result;
    }

    public static <T extends BlockEntity> List<T> getTouchingBlockEntitiesByClass(Level level, BlockPos pos, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            BlockEntity be = level.getBlockEntity(pos.relative(dir));
            if (clazz.isInstance(be)) result.add(clazz.cast(be));
        }
        return result;
    }

    public static List<BlockState> getTouchingBlockStates(Level level, BlockPos pos) {
        List<BlockState> result = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            result.add(level.getBlockState(pos.relative(dir)));
        }
        return result;
    }
}
