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

public class MessageToggleAttackPlayers {
    private final BlockPos pos;
    private final boolean attackPlayers;

    public MessageToggleAttackPlayers(BlockPos pos, boolean attackPlayers) {
        this.pos = pos;
        this.attackPlayers = attackPlayers;
    }

    public static void encode(MessageToggleAttackPlayers msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.attackPlayers);
    }

    public static MessageToggleAttackPlayers decode(FriendlyByteBuf buf) {
        return new MessageToggleAttackPlayers(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(MessageToggleAttackPlayers msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.canPlayerChangeSetting(base, player)) {
                base.setAttacksPlayers(msg.attackPlayers);
                base.informUpdate();
                OMTNetwork.sendToPlayer(player, new MessageTurretBase(base));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
