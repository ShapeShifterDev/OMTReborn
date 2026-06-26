package omtreborn.lib.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.lib.api.IHasOwner;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class OMLOwnedBlockEntity extends OMLBlockEntity implements IHasOwner {

    @Nullable
    private UUID ownerUUID;

    public OMLOwnedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (ownerUUID != null) {
            tag.putUUID("ownerUUID", ownerUUID);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ownerUUID = tag.hasUUID("ownerUUID") ? tag.getUUID("ownerUUID") : null;
    }
}
