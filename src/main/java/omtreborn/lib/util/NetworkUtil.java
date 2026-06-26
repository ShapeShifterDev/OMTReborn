package omtreborn.lib.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class NetworkUtil {

    private NetworkUtil() {}

    public static PacketDistributor.PacketTarget targetFromBE(BlockEntity be, double range) {
        if (be.getLevel() instanceof ServerLevel serverLevel) {
            return PacketDistributor.NEAR.with(() ->
                new PacketDistributor.TargetPoint(
                    be.getBlockPos().getX(),
                    be.getBlockPos().getY(),
                    be.getBlockPos().getZ(),
                    range,
                    serverLevel.dimension()
                )
            );
        }
        return PacketDistributor.ALL.noArg();
    }

    public static PacketDistributor.PacketTarget targetFromPos(ServerLevel level, BlockPos pos, double range) {
        return PacketDistributor.NEAR.with(() ->
            new PacketDistributor.TargetPoint(
                pos.getX(), pos.getY(), pos.getZ(), range, level.dimension()
            )
        );
    }
}
