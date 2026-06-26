package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import omtreborn.lib.util.PlayerUtil;
import omtreborn.network.OMTNetwork;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.function.Supplier;

public class MessageSetBaseTargetingType {
    private final BlockPos pos;

    public MessageSetBaseTargetingType(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageSetBaseTargetingType msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static MessageSetBaseTargetingType decode(FriendlyByteBuf buf) {
        return new MessageSetBaseTargetingType(buf.readBlockPos());
    }

    public static void handle(MessageSetBaseTargetingType msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.canPlayerChangeSetting(base, player)) {
                base.setMultiTargeting(!base.isMultiTargeting());
                base.informUpdate();
                OMTNetwork.sendToPlayer(player, new MessageTurretBase(base));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
