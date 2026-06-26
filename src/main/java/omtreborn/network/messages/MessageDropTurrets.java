package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import omtreborn.lib.util.PlayerUtil;
import omtreborn.tileentity.TurretBaseBlockEntity;
import omtreborn.tileentity.turrets.TurretHeadBlockEntity;

import java.util.function.Supplier;

public class MessageDropTurrets {
    private final BlockPos pos;

    public MessageDropTurrets(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageDropTurrets msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static MessageDropTurrets decode(FriendlyByteBuf buf) {
        return new MessageDropTurrets(buf.readBlockPos());
    }

    public static void handle(MessageDropTurrets msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level level = player.level();
            BlockEntity baseBe = level.getBlockEntity(msg.pos);
            if (!(baseBe instanceof TurretBaseBlockEntity base) || !PlayerUtil.isPlayerAdmin(base, player)) return;

            for (Direction dir : Direction.values()) {
                BlockPos checkPos = msg.pos.relative(dir);
                BlockEntity adj = level.getBlockEntity(checkPos);
                if (adj instanceof TurretHeadBlockEntity) {
                    Block.popResource(level, checkPos, new ItemStack(level.getBlockState(checkPos).getBlock().asItem()));
                    level.removeBlock(checkPos, false);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
