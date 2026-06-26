package omtreborn.lib.util;

import net.minecraft.network.chat.Component;

public class GeneralUtil {

    private GeneralUtil() {}

    public static String safeLocalize(String key) {
        return Component.translatable(key).getString();
    }

    public static String safeLocalize(String key, Object... args) {
        return Component.translatable(key, args).getString();
    }
}
