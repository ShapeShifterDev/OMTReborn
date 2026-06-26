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

public class MessageToggleAttackMobs {
    private final BlockPos pos;
    private final boolean attackMobs;

    public MessageToggleAttackMobs(BlockPos pos, boolean attackMobs) {
        this.pos = pos;
        this.attackMobs = attackMobs;
    }

    public static void encode(MessageToggleAttackMobs msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.attackMobs);
    }

    public static MessageToggleAttackMobs decode(FriendlyByteBuf buf) {
        return new MessageToggleAttackMobs(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(MessageToggleAttackMobs msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.canPlayerChangeSetting(base, player)) {
                base.setAttacksMobs(msg.attackMobs);
                base.informUpdate();
                OMTNetwork.sendToPlayer(player, new MessageTurretBase(base));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
