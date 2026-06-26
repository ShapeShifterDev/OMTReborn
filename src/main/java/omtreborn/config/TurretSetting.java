package omtreborn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TurretSetting {

    public final ForgeConfigSpec.BooleanValue enabled;
    public final ForgeConfigSpec.IntValue baseRange;
    public final ForgeConfigSpec.IntValue baseMinRange;
    public final ForgeConfigSpec.IntValue baseFireRate;
    public final ForgeConfigSpec.IntValue baseDamage;
    public final ForgeConfigSpec.IntValue powerUsage;
    public final ForgeConfigSpec.DoubleValue baseAccuracyDeviation;
    public final ForgeConfigSpec.IntValue maxSimultaneous;
    public final ForgeConfigSpec.DoubleValue damageAmp;
    public final ForgeConfigSpec.DoubleValue fireRateUpgrade;
    public final ForgeConfigSpec.IntValue rangeUpgrade;
    public final ForgeConfigSpec.DoubleValue accuracyUpgrade;
    public final ForgeConfigSpec.DoubleValue efficiencyUpgrade;
    public final ForgeConfigSpec.DoubleValue recyclerNegateChance;
    public final ForgeConfigSpec.DoubleValue recyclerAddChance;

    public TurretSetting(ForgeConfigSpec.Builder builder, String name,
                          boolean enabled, int baseRange, int baseMinRange, int baseFireRate, int baseDamage,
                          int powerUsage, double baseAccuracyDeviation, int maxSimultaneous,
                          double damageAmp, double fireRateUpgrade, int rangeUpgrade,
                          double accuracyUpgrade, double efficiencyUpgrade,
                          double recyclerNegateChance, double recyclerAddChance) {
        builder.push(name);
        this.enabled = builder.comment("Whether this turret type is enabled").define("enabled", enabled);
        this.baseRange = builder.defineInRange("baseRange", baseRange, 1, 200);
        this.baseMinRange = builder.defineInRange("baseMinRange", baseMinRange, 0, 200);
        this.baseFireRate = builder.comment("Ticks between shots (higher = slower)").defineInRange("baseFireRate", baseFireRate, 1, 200);
        this.baseDamage = builder.defineInRange("baseDamage", baseDamage, 0, Integer.MAX_VALUE);
        this.powerUsage = builder.defineInRange("powerUsage", powerUsage, 0, Integer.MAX_VALUE);
        this.baseAccuracyDeviation = builder.defineInRange("baseAccuracyDeviation", baseAccuracyDeviation, 0.0, 200.0);
        this.maxSimultaneous = builder.defineInRange("maxSimultaneous", maxSimultaneous, 1, 20);
        this.damageAmp = builder.defineInRange("damageAmp", damageAmp, 0.0, 1.0);
        this.fireRateUpgrade = builder.defineInRange("fireRateUpgrade", fireRateUpgrade, 0.0, 1.0);
        this.rangeUpgrade = builder.defineInRange("rangeUpgrade", rangeUpgrade, 0, 10);
        this.accuracyUpgrade = builder.defineInRange("accuracyUpgrade", accuracyUpgrade, 0.0, 1.0);
        this.efficiencyUpgrade = builder.defineInRange("efficiencyUpgrade", efficiencyUpgrade, 0.0, 1.0);
        this.recyclerNegateChance = builder.defineInRange("recyclerNegateChance", recyclerNegateChance, 0.0, 1.0);
        this.recyclerAddChance = builder.defineInRange("recyclerAddChance", recyclerAddChance, 0.0, 1.0);
        builder.pop();
    }
}
