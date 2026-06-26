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

public class MessageToggleMode {
    private final BlockPos pos;

    public MessageToggleMode(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageToggleMode msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static MessageToggleMode decode(FriendlyByteBuf buf) {
        return new MessageToggleMode(buf.readBlockPos());
    }

    public static void handle(MessageToggleMode msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.isPlayerAdmin(base, player)) {
                base.toggleMode();
                base.informUpdate();
                OMTNetwork.sendToPlayer(player, new MessageTurretBase(base));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
