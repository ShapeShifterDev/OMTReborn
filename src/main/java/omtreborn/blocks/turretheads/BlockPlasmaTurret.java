package omtreborn.blocks.turretheads;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.OpenModularTurrets;
import omtreborn.api.lists.TurretList;
import omtreborn.init.ModBlockEntities;
import omtreborn.tileentity.turrets.PlasmaLauncherBlockEntity;

import javax.annotation.Nullable;

public class BlockPlasmaTurret extends BlockAbstractTurretHead {

    public BlockPlasmaTurret(BlockBehaviour.Properties properties) {
        super(properties);
        TurretList.addTurret(OpenModularTurrets.MODID + ":plasma_turret");
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlasmaLauncherBlockEntity(pos, state);
    }

    @Override
    protected <T extends BlockEntity> BlockEntityTicker<T> getBlockEntityTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.TURRET_PLASMA_LAUNCHER.get(), PlasmaLauncherBlockEntity::tick);
    }

    @Override
    public int getMinimumTier() { return 5; }
}
