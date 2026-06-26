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
import omtreborn.tileentity.turrets.IncendiaryTurretBlockEntity;

import javax.annotation.Nullable;

public class BlockIncendiaryTurret extends BlockAbstractTurretHead {

    public BlockIncendiaryTurret(BlockBehaviour.Properties properties) {
        super(properties);
        TurretList.addTurret(OpenModularTurrets.MODID + ":incendiary_turret");
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IncendiaryTurretBlockEntity(pos, state);
    }

    @Override
    protected <T extends BlockEntity> BlockEntityTicker<T> getBlockEntityTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.TURRET_INCENDIARY.get(), IncendiaryTurretBlockEntity::tick);
    }

    @Override
    public int getMinimumTier() { return 3; }
}
