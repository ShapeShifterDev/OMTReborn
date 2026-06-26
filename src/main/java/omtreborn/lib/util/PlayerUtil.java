package omtreborn.lib.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import omtreborn.lib.api.IHasOwner;
import omtreborn.lib.api.IHasTrustManager;
import omtreborn.lib.permission.EnumAccessLevel;

public class PlayerUtil {

    private PlayerUtil() {}

    public static boolean isOwner(IHasOwner tile, Player player) {
        return tile.isOwner(player);
    }

    public static boolean hasTrustLevel(IHasTrustManager tile, Player player, EnumAccessLevel required) {
        return tile.getTrustManager().isTrusted(player, required);
    }

    public static boolean isOp(Player player) {
        if (player instanceof ServerPlayer sp) {
            return sp.getServer() != null && sp.getServer().getPlayerList().isOp(sp.getGameProfile());
        }
        return false;
    }

    public static boolean canPlayerChangeSetting(IHasTrustManager tile, Player player) {
        return tile.getTrustManager().isTrusted(player, EnumAccessLevel.CHANGE_SETTINGS) || isOp(player);
    }

    public static boolean isPlayerAdmin(IHasTrustManager tile, Player player) {
        return tile.getTrustManager().isTrusted(player, EnumAccessLevel.ADMIN) || isOp(player);
    }
}
