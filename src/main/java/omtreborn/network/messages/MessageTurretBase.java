package omtreborn.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import omtreborn.gui.ConfigureScreen;
import omtreborn.gui.TurretBaseScreen;
import omtreborn.lib.permission.EnumAccessLevel;
import omtreborn.lib.permission.TrustedPlayer;
import omtreborn.lib.permission.TrustedPlayersManager;
import omtreborn.tileentity.TurretBaseBlockEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class MessageTurretBase {

    final BlockPos pos;
    final int tier;
    final int rfStorageCurrent;
    final int range;
    final int maxRange;
    final int kills;
    final int playerKills;
    final boolean active;
    final boolean multiTargeting;
    final boolean attacksMobs;
    final boolean attacksNeutrals;
    final boolean attacksPlayers;
    @Nullable final String ownerName;
    @Nullable final UUID ownerUUID;
    final List<TrustedPlayer> trustedPlayers;

    public MessageTurretBase(TurretBaseBlockEntity base) {
        this.pos = base.getBlockPos();
        this.tier = base.getTier();
        this.rfStorageCurrent = base.getEnergyStorage().getEnergyStored();
        this.range = base.getRange();
        this.maxRange = base.getMaxRange();
        this.kills = base.getKills();
        this.playerKills = base.getPlayerKills();
        this.active = base.isActive();
        this.multiTargeting = base.isMultiTargeting();
        this.attacksMobs = base.isAttacksMobs();
        this.attacksNeutrals = base.isAttacksNeutrals();
        this.attacksPlayers = base.isAttacksPlayers();
        TrustedPlayer owner = base.getTrustManager().getOwner();
        this.ownerName = owner != null ? owner.getName() : null;
        this.ownerUUID = owner != null ? owner.getUuid() : null;
        this.trustedPlayers = new ArrayList<>(base.getTrustManager().getTrustedPlayers());
    }

    private MessageTurretBase(BlockPos pos, int tier, int rfStorageCurrent, int range, int maxRange,
                               int kills, int playerKills, boolean active, boolean multiTargeting,
                               boolean attacksMobs, boolean attacksNeutrals, boolean attacksPlayers,
                               @Nullable String ownerName, @Nullable UUID ownerUUID,
                               List<TrustedPlayer> trustedPlayers) {
        this.pos = pos;
        this.tier = tier;
        this.rfStorageCurrent = rfStorageCurrent;
        this.range = range;
        this.maxRange = maxRange;
        this.kills = kills;
        this.playerKills = playerKills;
        this.active = active;
        this.multiTargeting = multiTargeting;
        this.attacksMobs = attacksMobs;
        this.attacksNeutrals = attacksNeutrals;
        this.attacksPlayers = attacksPlayers;
        this.ownerName = ownerName;
        this.ownerUUID = ownerUUID;
        this.trustedPlayers = trustedPlayers;
    }

    public static void encode(MessageTurretBase msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeInt(msg.tier);
        buf.writeInt(msg.rfStorageCurrent);
        buf.writeInt(msg.range);
        buf.writeInt(msg.maxRange);
        buf.writeInt(msg.kills);
        buf.writeInt(msg.playerKills);
        buf.writeBoolean(msg.active);
        buf.writeBoolean(msg.multiTargeting);
        buf.writeBoolean(msg.attacksMobs);
        buf.writeBoolean(msg.attacksNeutrals);
        buf.writeBoolean(msg.attacksPlayers);
        boolean hasOwner = msg.ownerName != null && msg.ownerUUID != null;
        buf.writeBoolean(hasOwner);
        if (hasOwner) {
            buf.writeUtf(msg.ownerName);
            buf.writeUUID(msg.ownerUUID);
        }
        buf.writeInt(msg.trustedPlayers.size());
        for (TrustedPlayer tp : msg.trustedPlayers) {
            buf.writeUtf(tp.getName());
            buf.writeUUID(tp.getUuid() != null ? tp.getUuid() : new UUID(0, 0));
            buf.writeInt(tp.getAccessLevel().ordinal());
            buf.writeBoolean(tp.isHacked());
        }
    }

    public static MessageTurretBase decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int tier = buf.readInt();
        int rfStorageCurrent = buf.readInt();
        int range = buf.readInt();
        int maxRange = buf.readInt();
        int kills = buf.readInt();
        int playerKills = buf.readInt();
        boolean active = buf.readBoolean();
        boolean multiTargeting = buf.readBoolean();
        boolean attacksMobs = buf.readBoolean();
        boolean attacksNeutrals = buf.readBoolean();
        boolean attacksPlayers = buf.readBoolean();
        boolean hasOwner = buf.readBoolean();
        String ownerName = null;
        UUID ownerUUID = null;
        if (hasOwner) {
            ownerName = buf.readUtf();
            ownerUUID = buf.readUUID();
        }
        int tpCount = buf.readInt();
        List<TrustedPlayer> tpList = new ArrayList<>();
        for (int i = 0; i < tpCount; i++) {
            String name = buf.readUtf();
            UUID uuid = buf.readUUID();
            EnumAccessLevel level = EnumAccessLevel.values()[buf.readInt()];
            boolean hacked = buf.readBoolean();
            TrustedPlayer tp = new TrustedPlayer(name);
            tp.setUuid(uuid);
            tp.setAccessLevel(level);
            tp.setHacked(hacked);
            tpList.add(tp);
        }
        return new MessageTurretBase(pos, tier, rfStorageCurrent, range, maxRange, kills, playerKills,
                active, multiTargeting, attacksMobs, attacksNeutrals, attacksPlayers,
                ownerName, ownerUUID, tpList);
    }

    public static void handle(MessageTurretBase msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient(msg)));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(MessageTurretBase msg) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.level == null) return;
        net.minecraft.world.level.block.entity.BlockEntity be = mc.level.getBlockEntity(msg.pos);
        if (!(be instanceof TurretBaseBlockEntity base)) return;

        base.setActive(msg.active);
        base.setMultiTargeting(msg.multiTargeting);
        base.setAttacksMobs(msg.attacksMobs);
        base.setAttacksNeutrals(msg.attacksNeutrals);
        base.setAttacksPlayers(msg.attacksPlayers);
        base.setKills(msg.kills);
        base.setPlayerKills(msg.playerKills);
        base.setRange(msg.range);
        base.setEnergyStored(msg.rfStorageCurrent);

        TrustedPlayersManager tm = base.getTrustManager();
        if (msg.ownerName != null && msg.ownerUUID != null) {
            TrustedPlayer owner = new TrustedPlayer(msg.ownerName);
            owner.setUuid(msg.ownerUUID);
            tm.setOwner(owner);
        }
        tm.setTrustedPlayers(msg.trustedPlayers);

        if (mc.screen instanceof TurretBaseScreen tbs) {
            tbs.onServerSync();
        } else if (mc.screen instanceof ConfigureScreen cs) {
            cs.onServerSync();
        }
    }
}
