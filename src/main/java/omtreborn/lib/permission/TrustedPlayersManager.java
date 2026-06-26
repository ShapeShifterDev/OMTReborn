package omtreborn.lib.permission;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrustedPlayersManager {

    private final List<TrustedPlayer> trustedPlayers = new ArrayList<>();
    @Nullable
    private TrustedPlayer owner;

    public TrustedPlayersManager(@Nullable TrustedPlayer owner) {
        this.owner = owner;
    }

    @Nullable
    public TrustedPlayer getOwner() {
        return owner;
    }

    public void setOwner(@Nullable TrustedPlayer owner) {
        this.owner = owner;
    }

    public List<TrustedPlayer> getTrustedPlayers() {
        return trustedPlayers;
    }

    public void addTrustedPlayer(TrustedPlayer player) {
        trustedPlayers.removeIf(tp -> Objects.equals(tp.getUuid(), player.getUuid()));
        trustedPlayers.add(player);
    }

    public void removeTrustedPlayer(TrustedPlayer player) {
        trustedPlayers.removeIf(tp -> Objects.equals(tp.getUuid(), player.getUuid()));
    }

    public void setTrustedPlayers(List<TrustedPlayer> players) {
        trustedPlayers.clear();
        trustedPlayers.addAll(players);
    }

    public boolean isOwner(Player player) {
        return owner != null && player.getUUID().equals(owner.getUuid());
    }

    public boolean isTrusted(Player player, EnumAccessLevel requiredLevel) {
        if (isOwner(player)) return true;
        for (TrustedPlayer tp : trustedPlayers) {
            if (player.getUUID().equals(tp.getUuid()) &&
                tp.getAccessLevel().ordinal() >= requiredLevel.ordinal()) {
                return true;
            }
        }
        return false;
    }

    public void writeToNBT(CompoundTag tag) {
        if (owner != null) {
            CompoundTag ownerTag = new CompoundTag();
            ownerTag.putString("name", owner.getName());
            if (owner.getUuid() != null) ownerTag.putUUID("uuid", owner.getUuid());
            ownerTag.putString("accessLevel", owner.getAccessLevel().getName());
            tag.put("trustOwner", ownerTag);
        }
        ListTag list = new ListTag();
        for (TrustedPlayer tp : trustedPlayers) {
            CompoundTag tpTag = new CompoundTag();
            tpTag.putString("name", tp.getName());
            if (tp.getUuid() != null) tpTag.putUUID("uuid", tp.getUuid());
            tpTag.putString("accessLevel", tp.getAccessLevel().getName());
            tpTag.putBoolean("hacked", tp.isHacked());
            list.add(tpTag);
        }
        tag.put("trustedPlayers", list);
    }

    public void readFromNBT(CompoundTag tag) {
        if (tag.contains("trustOwner")) {
            CompoundTag ownerTag = tag.getCompound("trustOwner");
            TrustedPlayer tp = new TrustedPlayer(ownerTag.getString("name"));
            if (ownerTag.hasUUID("uuid")) tp.setUuid(ownerTag.getUUID("uuid"));
            tp.setAccessLevel(EnumAccessLevel.fromName(ownerTag.getString("accessLevel")));
            owner = tp;
        }
        trustedPlayers.clear();
        ListTag list = tag.getList("trustedPlayers", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag tpTag = list.getCompound(i);
            TrustedPlayer tp = new TrustedPlayer(tpTag.getString("name"));
            if (tpTag.hasUUID("uuid")) tp.setUuid(tpTag.getUUID("uuid"));
            tp.setAccessLevel(EnumAccessLevel.fromName(tpTag.getString("accessLevel")));
            tp.setHacked(tpTag.getBoolean("hacked"));
            trustedPlayers.add(tp);
        }
    }
}
