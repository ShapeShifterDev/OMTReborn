package omtreborn.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import omtreborn.config.OMTConfig;
import omtreborn.init.ModBlocks;

import java.util.List;

public enum FerroniteOreBiomeModifier implements BiomeModifier {
    INSTANCE;

    public static final Codec<FerroniteOreBiomeModifier> CODEC = Codec.unit(INSTANCE);

    private static final RuleTest STONE_TEST = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    private static final RuleTest DEEPSLATE_TEST = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !biome.is(BiomeTags.IS_OVERWORLD)) return;

        double mult = OMTConfig.MISCELLANEOUS.ferroniteOreFrequency.get();
        if (mult <= 0) return;

        Block stoneOre = ModBlocks.FERRONITE_ORE.get();
        Block deepslateOre = ModBlocks.DEEPSLATE_FERRONITE_ORE.get();

        // mirrors ore_iron_middle: count 10, size 9, trapezoid(-24, 56)
        int middleCount = (int) Math.round(10 * mult);
        if (middleCount > 0)
            addOre(builder, stoneOre, deepslateOre, 9, middleCount,
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)));

        // mirrors ore_iron_upper: count 90, size 9, trapezoid(80, 384)
        int upperCount = (int) Math.round(90 * mult);
        if (upperCount > 0)
            addOre(builder, stoneOre, deepslateOre, 9, upperCount,
                    HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)));

        // mirrors ore_iron_small: count 10, size 4, uniform(above_bottom, 72)
        int smallCount = (int) Math.round(10 * mult);
        if (smallCount > 0)
            addOre(builder, stoneOre, deepslateOre, 4, smallCount,
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(72)));
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    private static void addOre(ModifiableBiomeInfo.BiomeInfo.Builder builder, Block stoneOre, Block deepslateOre,
                                int veinSize, int count, PlacementModifier heightPlacement) {
        List<OreConfiguration.TargetBlockState> targets = List.of(
                OreConfiguration.target(STONE_TEST, stoneOre.defaultBlockState()),
                OreConfiguration.target(DEEPSLATE_TEST, deepslateOre.defaultBlockState())
        );
        ConfiguredFeature<?, ?> cf = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, veinSize));
        PlacedFeature placed = new PlacedFeature(
                Holder.direct(cf),
                List.of(CountPlacement.of(count), InSquarePlacement.spread(), heightPlacement, BiomeFilter.biome())
        );
        builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Holder.direct(placed));
    }
}
