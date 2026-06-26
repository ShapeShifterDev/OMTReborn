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
import omtreborn.tileentity.turrets.RailGunTurretBlockEntity;

import javax.annotation.Nullable;

public class BlockRailGunTurret extends BlockAbstractTurretHead {

    public BlockRailGunTurret(BlockBehaviour.Properties properties) {
        super(properties);
        TurretList.addTurret(OpenModularTurrets.MODID + ":rail_gun_turret");
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RailGunTurretBlockEntity(pos, state);
    }

    @Override
    protected <T extends BlockEntity> BlockEntityTicker<T> getBlockEntityTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.TURRET_RAIL_GUN.get(), RailGunTurretBlockEntity::tick);
    }

    @Override
    public int getMinimumTier() { return 5; }
}
