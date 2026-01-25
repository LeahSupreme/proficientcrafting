package net.leahperson.proficientmod.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EffectAddition(String effectid, int duration, int amplifier) {
    public static final Codec<EffectAddition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("effectid").forGetter(EffectAddition::effectid),
                    Codec.INT.fieldOf("duration").forGetter(EffectAddition::duration),
                    Codec.INT.fieldOf("amplifier").forGetter(EffectAddition::amplifier)
            )
            .apply(builder, EffectAddition::new));

    public EffectAddition {
        effectid = effectid;
        duration = duration;
        amplifier = amplifier;
    }
}
