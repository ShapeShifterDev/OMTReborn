package omtreborn.lib.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import omtreborn.lib.api.IHasTrustManager;
import omtreborn.lib.permission.TrustedPlayer;
import omtreborn.lib.permission.TrustedPlayersManager;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class OMLTrustedMachineBlockEntity extends OMLContainerElectricBlockEntity
        implements IHasTrustManager {

    protected TrustedPlayersManager trustManager;

    public OMLTrustedMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity) {
        this(type, pos, state, capacity, capacity, capacity);
    }

    public OMLTrustedMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                         int capacity, int maxReceive, int maxExtract) {
        super(type, pos, state, capacity, maxReceive, maxExtract);
        this.trustManager = new TrustedPlayersManager(null);
    }

    @Override
    public TrustedPlayersManager getTrustManager() {
        return trustManager;
    }

    public void setOwner(Player player) {
        TrustedPlayer owner = new TrustedPlayer(player);
        trustManager.setOwner(owner);
        setOwnerUUID(player.getUUID());
    }

    @Override
    public void setOwnerUUID(@Nullable UUID uuid) {
        super.setOwnerUUID(uuid);
        if (trustManager != null && trustManager.getOwner() != null && uuid != null) {
            trustManager.getOwner().setUuid(uuid);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        trustManager.writeToNBT(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        trustManager.readFromNBT(tag);
        if (trustManager.getOwner() != null) {
            setOwnerUUID(trustManager.getOwner().getUuid());
        }
    }
}
