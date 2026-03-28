package net.leahperson.proficientmod.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.leahperson.proficientmod.Config;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.event.GatheringAttributeEvent;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GatheringLootModifier extends LootModifier {
    public static final Supplier<Codec<GatheringLootModifier>> CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.create(inst ->
                    LootModifier.codecStart(inst).apply(inst, GatheringLootModifier::new)));

    public GatheringLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state == null) return generatedLoot;

        RegistryObject<Attribute> qualityAttribute;
        RegistryObject<Attribute> yieldAttribute;

        if (state.is(BlockTags.CROPS)) {
            qualityAttribute = ModAttributes.FARMING_QUALITY;
            yieldAttribute = ModAttributes.FARMING_YIELD;
        } else if (state.is(Tags.Blocks.ORES)) {
            qualityAttribute = ModAttributes.MINING_QUALITY;
            yieldAttribute = ModAttributes.MINING_YIELD;
        } else {
            return generatedLoot;
        }

        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Player player)) return generatedLoot;

        int quality = (int) player.getAttributeValue(qualityAttribute.get());
        int yield = (int) player.getAttributeValue(yieldAttribute.get());

        net.minecraft.resources.ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        int extraQuality = blockId != null ? Config.qualityThresholds.getOrDefault(blockId.toString(), 0) : 0;
        int effectiveQuality = quality - extraQuality;

        if (effectiveQuality > 0) {
            int rarity = GatheringAttributeEvent.computeRarity(effectiveQuality, context.getRandom());
            if (rarity > 0) {
                for (ItemStack stack : generatedLoot) {
                    QualityDataUtil.setRarity(stack, rarity);
                }
            }
        }

        if (yield > 0) {
            int bonus = GatheringAttributeEvent.computeBonus(yield, context.getRandom());
            if (bonus > 0) {
                ObjectArrayList<ItemStack> extraDrops = new ObjectArrayList<>();
                for (ItemStack itemStack : generatedLoot) {
                    ItemStack extraDrop = itemStack.copy();
                    extraDrop.setCount(bonus);
                    extraDrops.add(extraDrop);
                }
                generatedLoot.addAll(extraDrops);
            }
        }

        return generatedLoot;
    }
}
