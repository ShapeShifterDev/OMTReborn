package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.function.Supplier;

public class MessageOpenTurretGui {

    private final BlockPos pos;

    public MessageOpenTurretGui(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageOpenTurretGui msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static MessageOpenTurretGui decode(FriendlyByteBuf buf) {
        return new MessageOpenTurretGui(buf.readBlockPos());
    }

    public static void handle(MessageOpenTurretGui msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base) {
                NetworkHooks.openScreen(player, base, buf -> buf.writeBlockPos(msg.pos));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
