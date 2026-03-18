package net.leahperson.proficientmod.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record ItemQualityData(String item_id, Optional<List<List<AttributeAddition>>> rarities, Optional<List<FoodAddition>> food) {

    public static final ResourceKey<Registry<ItemQualityData>> REGISTRY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "raritystats"));

    public static final Codec<ItemQualityData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("item_id").forGetter(ItemQualityData::item_id),
                    Codec.list(Codec.list(AttributeAddition.CODEC)).optionalFieldOf("rarities").forGetter(ItemQualityData::rarities),
                    Codec.list(FoodAddition.CODEC).optionalFieldOf("food").forGetter(ItemQualityData::food)
            )
            .apply(builder, ItemQualityData::new));
}
