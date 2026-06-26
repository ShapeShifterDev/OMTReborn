package omtreborn.network;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import omtreborn.network.messages.*;

import java.util.Optional;

public class OMTNetwork {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("omtreborn", "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals);

    private static int id = 0;

    public static void init() {
        CHANNEL.registerMessage(id++, MessageToggleAttackMobs.class,
                MessageToggleAttackMobs::encode, MessageToggleAttackMobs::decode,
                MessageToggleAttackMobs::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageToggleAttackNeutralMobs.class,
                MessageToggleAttackNeutralMobs::encode, MessageToggleAttackNeutralMobs::decode,
                MessageToggleAttackNeutralMobs::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageToggleAttackPlayers.class,
                MessageToggleAttackPlayers::encode, MessageToggleAttackPlayers::decode,
                MessageToggleAttackPlayers::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageAdjustRange.class,
                MessageAdjustRange::encode, MessageAdjustRange::decode,
                MessageAdjustRange::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageDropTurrets.class,
                MessageDropTurrets::encode, MessageDropTurrets::decode,
                MessageDropTurrets::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageDropBase.class,
                MessageDropBase::encode, MessageDropBase::decode,
                MessageDropBase::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageSetBaseTargetingType.class,
                MessageSetBaseTargetingType::encode, MessageSetBaseTargetingType::decode,
                MessageSetBaseTargetingType::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageToggleMode.class,
                MessageToggleMode::encode, MessageToggleMode::decode,
                MessageToggleMode::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, MessageTurretBase.class,
                MessageTurretBase::encode, MessageTurretBase::decode,
                MessageTurretBase::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, MessageUpdateTurret.class,
                MessageUpdateTurret::encode, MessageUpdateTurret::decode,
                MessageUpdateTurret::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, MessageOpenTurretGui.class,
                MessageOpenTurretGui::encode, MessageOpenTurretGui::decode,
                MessageOpenTurretGui::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void sendToPlayer(ServerPlayer player, Object msg) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToNearby(BlockPos pos, ServerLevel level, Object msg) {
        CHANNEL.send(PacketDistributor.NEAR.with(
                PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 64, level.dimension())), msg);
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }
}
