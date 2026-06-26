package omtreborn.lib.api;

import net.minecraft.core.BlockPos;

import java.util.List;

public interface INetworkTile {

    BlockPos getNetworkPos();

    List<BlockPos> getAdjacentAddonPositions();

    void onAddonAttached(BlockPos addonPos);

    void onAddonDetached(BlockPos addonPos);
}
