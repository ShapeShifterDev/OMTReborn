package omtreborn.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import omtreborn.init.ModBlockEntities;

public class CreativeEnergySourceBlockEntity extends BlockEntity {

    public CreativeEnergySourceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_ENERGY_SOURCE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CreativeEnergySourceBlockEntity be) {
        if (level.isClientSide) return;
        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(dir));
            if (neighbor == null) continue;
            neighbor.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(storage -> {
                if (storage.canReceive()) {
                    storage.receiveEnergy(Integer.MAX_VALUE, false);
                }
            });
        }
    }
}
