package net.leahperson.proficientmod.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FoodAddition(float nutritionadd, float saturationmodifieradd, List<EffectAddition> effectsadd) {

    public static final Codec<FoodAddition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.FLOAT.fieldOf("nutritionadd").forGetter(FoodAddition::nutritionadd),
                    Codec.FLOAT.fieldOf("saturationmodifieradd").forGetter(FoodAddition::saturationmodifieradd),
                    Codec.list(EffectAddition.CODEC).fieldOf("effectsadd").forGetter(FoodAddition::effectsadd)
            )
            .apply(builder, FoodAddition::new));

    public FoodAddition {
        nutritionadd = nutritionadd;
        saturationmodifieradd = saturationmodifieradd;
        effectsadd = effectsadd;
    }
}
