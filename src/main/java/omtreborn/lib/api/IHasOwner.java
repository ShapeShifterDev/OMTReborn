package omtreborn.lib.api;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public interface IHasOwner {

    @Nullable
    UUID getOwnerUUID();

    void setOwnerUUID(@Nullable UUID uuid);

    default boolean isOwner(Player player) {
        UUID owner = getOwnerUUID();
        return owner != null && owner.equals(player.getUUID());
    }
}
