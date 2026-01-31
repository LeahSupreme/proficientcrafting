package net.leahperson.proficientmod.attribute;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AttributeAdditionData(String itemid, List<List<AttributeAddition>> rarities) {

    public static final ResourceKey<Registry<AttributeAdditionData>> QUALITY_ATTRIBUTE_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "raritystats"));
    public static final ResourceKey<Registry<AttributeAdditionData>> QUALITY_CURIOS_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "raritycurios"));


    //public static final AttributeAdditionData NONE = new AttributeAdditionData(0, 1, ProficientMod.location("none"));



    public static final Codec<AttributeAdditionData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("itemid").forGetter(AttributeAdditionData::itemid),
                    Codec.list(Codec.list(AttributeAddition.CODEC)).fieldOf("rarities").forGetter(AttributeAdditionData::rarities)
            )
            .apply(builder, AttributeAdditionData::new));

    public AttributeAdditionData {
        itemid = itemid;
        rarities = rarities;
    }





}
