package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import omtreborn.lib.util.PlayerUtil;
import omtreborn.tileentity.TurretBaseBlockEntity;

import java.util.function.Supplier;

public class MessageDropBase {
    private final BlockPos pos;

    public MessageDropBase(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(MessageDropBase msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static MessageDropBase decode(FriendlyByteBuf buf) {
        return new MessageDropBase(buf.readBlockPos());
    }

    public static void handle(MessageDropBase msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level level = player.level();
            BlockEntity be = level.getBlockEntity(msg.pos);
            if (be instanceof TurretBaseBlockEntity base && PlayerUtil.isPlayerAdmin(base, player)) {
                // Pop the base item, then removeBlock (which triggers onRemove → drops inventory)
                Block.popResource(level, msg.pos, new ItemStack(level.getBlockState(msg.pos).getBlock().asItem()));
                level.removeBlock(msg.pos, false);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
