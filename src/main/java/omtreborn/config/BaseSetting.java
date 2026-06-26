package omtreborn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BaseSetting {

    public final ForgeConfigSpec.IntValue baseMaxCharge;
    public final ForgeConfigSpec.IntValue baseMaxIo;
    public final ForgeConfigSpec.IntValue baseMaxTurrets;
    public final ForgeConfigSpec.IntValue baseBlastResistance;
    public final ForgeConfigSpec.IntValue baseHardness;

    public BaseSetting(ForgeConfigSpec.Builder builder, String name,
                        int baseMaxCharge, int baseMaxIo, int baseMaxTurrets,
                        int baseBlastResistance, int baseHardness) {
        builder.push(name);
        this.baseMaxCharge = builder.defineInRange("baseMaxCharge", baseMaxCharge, 1, Integer.MAX_VALUE);
        this.baseMaxIo = builder.defineInRange("baseMaxIo", baseMaxIo, 1, Integer.MAX_VALUE);
        this.baseMaxTurrets = builder.defineInRange("baseMaxTurrets", baseMaxTurrets, 1, Integer.MAX_VALUE);
        this.baseBlastResistance = builder.defineInRange("baseBlastResistance", baseBlastResistance, 1, Integer.MAX_VALUE);
        this.baseHardness = builder.defineInRange("baseHardness", baseHardness, 1, Integer.MAX_VALUE);
        builder.pop();
    }
}
