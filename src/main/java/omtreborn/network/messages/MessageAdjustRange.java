package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import omtreborn.lib.util.PlayerUtil;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.function.Supplier;

public class MessageAdjustRange {
    private final BlockPos pos;
    private final int range;

    public MessageAdjustRange(BlockPos pos, int range) {
        this.pos = pos;
        this.range = range;
    }

    public static void encode(MessageAdjustRange msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.range);
    }

    public static MessageAdjustRange decode(FriendlyByteBuf buf) {
        return new MessageAdjustRange(buf.readBlockPos(), buf.readInt());
    }

    public static void handle(MessageAdjustRange msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.isPlayerAdmin(base, player)) {
                base.updateMaxRange();
                base.setRange(Math.min(msg.range, base.getMaxRange()));
                base.informUpdate();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
