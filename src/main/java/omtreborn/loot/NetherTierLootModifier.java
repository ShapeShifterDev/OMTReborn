package omtreborn.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import omtreborn.init.ModItems;
import omtreborn.init.ModWorldgen;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class NetherTierLootModifier extends LootModifier {

    public static final Codec<NetherTierLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            codecStart(inst).apply(inst, NetherTierLootModifier::new));

    private static final Set<ResourceLocation> TARGET_TABLES = Set.of(
            new ResourceLocation("minecraft", "chests/nether_bridge"),
            new ResourceLocation("minecraft", "chests/bastion_bridge"),
            new ResourceLocation("minecraft", "chests/bastion_hoglin_stable"),
            new ResourceLocation("minecraft", "chests/bastion_other"),
            new ResourceLocation("minecraft", "chests/bastion_treasure")
    );

    private static final float CHANCE = 0.25f;

    public NetherTierLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return ModWorldgen.NETHER_TIER_LOOT_MODIFIER.get();
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!TARGET_TABLES.contains(context.getQueriedLootTableId())) return generatedLoot;
        if (context.getRandom().nextFloat() >= CHANCE) return generatedLoot;

        Item[] eligible = {
                ModItems.BARREL_TIER_FOUR.get(),
                ModItems.CHAMBER_TIER_FOUR.get(),
                ModItems.SENSOR_TIER_FOUR.get(),
                ModItems.BARREL_TIER_FIVE.get(),
                ModItems.CHAMBER_TIER_FIVE.get(),
                ModItems.SENSOR_TIER_FIVE.get()
        };
        Item chosen = eligible[context.getRandom().nextInt(eligible.length)];
        generatedLoot.add(new ItemStack(chosen));
        return generatedLoot;
    }
}
