package omtreborn.init;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import omtreborn.OpenModularTurrets;
import omtreborn.loot.NetherTierLootModifier;
import omtreborn.worldgen.FerroniteOreBiomeModifier;

public class ModWorldgen {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, OpenModularTurrets.MODID);

    public static final RegistryObject<Codec<FerroniteOreBiomeModifier>> FERRONITE_ORE_MODIFIER =
            BIOME_MODIFIER_SERIALIZERS.register("ferronite_ore", () -> FerroniteOreBiomeModifier.CODEC);

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, OpenModularTurrets.MODID);

    public static final RegistryObject<Codec<NetherTierLootModifier>> NETHER_TIER_LOOT_MODIFIER =
            LOOT_MODIFIER_SERIALIZERS.register("nether_tier_components", () -> NetherTierLootModifier.CODEC);

    public static void register(IEventBus bus) {
        BIOME_MODIFIER_SERIALIZERS.register(bus);
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
