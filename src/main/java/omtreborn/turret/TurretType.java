package omtreborn.turret;

import omtreborn.config.TurretSetting;

public class TurretType {

    private final String internalName;
    private final TurretSetting settings;

    public TurretType(String internalName, TurretSetting settings) {
        this.internalName = internalName;
        this.settings = settings;
    }

    public static TurretType of(String internalName, TurretSetting settings) {
        return new TurretType(internalName, settings);
    }

    public String getInternalName() {
        return internalName;
    }

    public TurretSetting getSettings() {
        return settings;
    }
}
