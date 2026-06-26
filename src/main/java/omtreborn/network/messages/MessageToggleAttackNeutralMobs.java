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

public class MessageToggleAttackNeutralMobs {
    private final BlockPos pos;
    private final boolean attackNeutrals;

    public MessageToggleAttackNeutralMobs(BlockPos pos, boolean attackNeutrals) {
        this.pos = pos;
        this.attackNeutrals = attackNeutrals;
    }

    public static void encode(MessageToggleAttackNeutralMobs msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.attackNeutrals);
    }

    public static MessageToggleAttackNeutralMobs decode(FriendlyByteBuf buf) {
        return new MessageToggleAttackNeutralMobs(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(MessageToggleAttackNeutralMobs msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            BlockEntity be = player.level().getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.canPlayerChangeSetting(base, player)) {
                base.setAttacksNeutrals(msg.attackNeutrals);
                base.informUpdate();
                OMTNetwork.sendToPlayer(player, new MessageTurretBase(base));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
