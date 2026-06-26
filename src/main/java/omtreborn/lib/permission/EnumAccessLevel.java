package omtreborn.lib.permission;

import net.minecraft.network.chat.Component;

public enum EnumAccessLevel {
    NONE("none", "omtreborn.accessLevel.none"),
    OPEN_GUI("open_gui", "omtreborn.accessLevel.open_gui"),
    CHANGE_SETTINGS("change_settings", "omtreborn.accessLevel.change_settings"),
    ADMIN("admin", "omtreborn.accessLevel.admin");

    private final String name;
    private final String translationKey;

    EnumAccessLevel(String name, String translationKey) {
        this.name = name;
        this.translationKey = translationKey;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getLocalizedName() {
        return Component.translatable(translationKey).getString();
    }

    public static EnumAccessLevel fromName(String name) {
        for (EnumAccessLevel level : values()) {
            if (level.name.equals(name)) return level;
        }
        return NONE;
    }
}
