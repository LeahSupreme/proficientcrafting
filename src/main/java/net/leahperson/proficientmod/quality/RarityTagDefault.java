package net.leahperson.proficientmod.quality;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.leahperson.proficientmod.attribute.AttributeAddition;

import java.util.List;

public record RarityTagDefault(String tag, List<AttributeAddition> attributes) {

    public static final Codec<RarityTagDefault> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("tag").forGetter(RarityTagDefault::tag),
            Codec.list(AttributeAddition.CODEC).fieldOf("attributes").forGetter(RarityTagDefault::attributes)
    ).apply(builder, RarityTagDefault::new));
}
