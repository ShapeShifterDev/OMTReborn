package omtreborn.lib.permission;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

public class TrustedPlayer {

    private String name;
    private EnumAccessLevel accessLevel = EnumAccessLevel.NONE;
    @Nullable
    private UUID uuid;
    private boolean hacked = false;

    public TrustedPlayer(String name) {
        this.name = name;
    }

    public TrustedPlayer(Player player) {
        this.name = player.getName().getString();
        this.uuid = player.getUUID();
    }

    public TrustedPlayer(Player player, EnumAccessLevel accessLevel) {
        this(player);
        this.accessLevel = accessLevel;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EnumAccessLevel getAccessLevel() { return accessLevel; }
    public void setAccessLevel(EnumAccessLevel level) { this.accessLevel = level; }

    @Nullable
    public UUID getUuid() { return uuid; }
    public void setUuid(@Nullable UUID uuid) { this.uuid = uuid; }

    public boolean isHacked() { return hacked; }
    public void setHacked(boolean hacked) { this.hacked = hacked; }

    public OMLPlayer getPlayer() {
        return new OMLPlayer(name, uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrustedPlayer)) return false;
        TrustedPlayer other = (TrustedPlayer) o;
        return Objects.equals(name, other.name) &&
               accessLevel == other.accessLevel &&
               Objects.equals(uuid, other.uuid) &&
               hacked == other.hacked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accessLevel, uuid, hacked);
    }
}
