package omtreborn.lib.api;

import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public interface IOwnedBlockAddon {

    @Nullable
    BlockPos getBasePos();

    void setBasePos(@Nullable BlockPos pos);
}
