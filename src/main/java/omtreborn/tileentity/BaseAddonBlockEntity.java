package omtreborn.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.init.ModBlockEntities;
import omtreborn.lib.tileentity.OMLBlockEntity;
import omtreborn.turret.TurretHeadUtil;

public class BaseAddonBlockEntity extends OMLBlockEntity {

    private Direction orientation = Direction.NORTH;

    public BaseAddonBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASE_ADDON.get(), pos, state);
    }

    public Direction getOrientation() {
        return orientation;
    }

    public void setSide() {
        if (level != null) {
            Direction facing = TurretHeadUtil.getTurretBaseFacing(level, worldPosition);
            if (facing != null) this.orientation = facing;
        }
    }

    public TurretBaseBlockEntity getBase() {
        return TurretHeadUtil.getTurretBase(level, worldPosition);
    }

    public static void tick(net.minecraft.world.level.Level level, BlockPos pos, BlockState state,
                             BaseAddonBlockEntity be) {
        if (level.isClientSide) return;
        if (level.getGameTime() % 15 == 0 && be.getBase() == null) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putByte("direction", (byte) orientation.ordinal());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("direction")) {
            int ord = tag.getByte("direction");
            Direction[] dirs = Direction.values();
            if (ord >= 0 && ord < dirs.length) this.orientation = dirs[ord];
        }
    }
}
