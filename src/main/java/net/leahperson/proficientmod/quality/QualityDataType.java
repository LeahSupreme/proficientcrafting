package net.leahperson.proficientmod.quality;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.attribute.FoodAddition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record QualityDataType(int index, double bonus, ResourceLocation icon, Optional<FoodAddition> food, Optional<List<RarityTagDefault>> tagDefaults) {

    public static final ResourceKey<Registry<QualityDataType>> RARITY_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "rarity"));

    public static final QualityDataType NONE = new QualityDataType(0, 1, ProficientMod.modLocation("none"), Optional.empty(), Optional.empty());

    public static final Codec<QualityDataType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("index").forGetter(QualityDataType::index),
                    Codec.DOUBLE.fieldOf("bonus").forGetter(QualityDataType::bonus),
                    ResourceLocation.CODEC.fieldOf("icon").forGetter(QualityDataType::icon),
                    FoodAddition.CODEC.optionalFieldOf("food").forGetter(QualityDataType::food),
                    Codec.list(RarityTagDefault.CODEC).optionalFieldOf("tagDefaults").forGetter(QualityDataType::tagDefaults))
            .apply(builder, QualityDataType::new));

    public QualityDataType {
        index = Math.max(0, index);
    }
}
