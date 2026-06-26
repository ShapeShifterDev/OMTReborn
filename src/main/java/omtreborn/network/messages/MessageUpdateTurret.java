package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import omtreborn.tileentity.turrets.AbstractDirectedTurretBlockEntity;

import java.util.function.Supplier;

public class MessageUpdateTurret {
    final BlockPos pos;
    final float yaw;
    final float pitch;

    public MessageUpdateTurret(AbstractDirectedTurretBlockEntity turret) {
        this.pos = turret.getBlockPos();
        this.yaw = turret.getYaw();
        this.pitch = turret.getPitch();
    }

    private MessageUpdateTurret(BlockPos pos, float yaw, float pitch) {
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static void encode(MessageUpdateTurret msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeFloat(msg.yaw);
        buf.writeFloat(msg.pitch);
    }

    public static MessageUpdateTurret decode(FriendlyByteBuf buf) {
        return new MessageUpdateTurret(buf.readBlockPos(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(MessageUpdateTurret msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient(msg)));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(MessageUpdateTurret msg) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level == null) return;
        BlockEntity be = mc.level.getBlockEntity(msg.pos);
        if (be instanceof AbstractDirectedTurretBlockEntity turret) {
            turret.setYaw(msg.yaw);
            turret.setPitch(msg.pitch);
        }
    }
}
