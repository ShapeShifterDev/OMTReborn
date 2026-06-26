package omtreborn.lib.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class OMLElectricBlockEntity extends OMLOwnedBlockEntity {

    protected EnergyStorage energyStorage;
    private LazyOptional<IEnergyStorage> energyOpt;

    public OMLElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity) {
        this(type, pos, state, capacity, capacity, capacity);
    }

    public OMLElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                   int capacity, int maxReceive, int maxExtract) {
        super(type, pos, state);
        this.energyStorage = new EnergyStorage(capacity, maxReceive, maxExtract) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int received = super.receiveEnergy(maxReceive, simulate);
                if (!simulate && received > 0) setChanged();
                return received;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int extracted = super.extractEnergy(maxExtract, simulate);
                if (!simulate && extracted > 0) setChanged();
                return extracted;
            }
        };
        this.energyOpt = LazyOptional.of(() -> this.energyStorage);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void setEnergyStored(int stored) {
        energyStorage.deserializeNBT(net.minecraft.nbt.IntTag.valueOf(stored));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyOpt.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyOpt.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("energy", energyStorage.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("energy", Tag.TAG_INT)) {
            energyStorage.deserializeNBT((IntTag) tag.get("energy"));
        }
    }
}
