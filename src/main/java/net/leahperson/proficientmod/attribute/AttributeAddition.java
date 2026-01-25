package net.leahperson.proficientmod.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record AttributeAddition(String attributeid, double amount, String operation) {
    public static final Codec<AttributeAddition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("attributeid").forGetter(AttributeAddition::attributeid),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeAddition::amount),
                    Codec.STRING.fieldOf("operation").forGetter(AttributeAddition::operation)
            )
            .apply(builder, AttributeAddition::new));

    public AttributeAddition {
        attributeid = attributeid;
        amount = amount;
        operation = operation;
    }
}
