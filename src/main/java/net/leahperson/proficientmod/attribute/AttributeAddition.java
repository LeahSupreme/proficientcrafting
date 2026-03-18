package net.leahperson.proficientmod.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record AttributeAddition(String attribute_id, double amount, String operation) {
    public static final Codec<AttributeAddition> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("attribute_id").forGetter(AttributeAddition::attribute_id),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeAddition::amount),
                    Codec.STRING.fieldOf("operation").forGetter(AttributeAddition::operation)
            )
            .apply(builder, AttributeAddition::new));

    public AttributeAddition {
        attribute_id = attribute_id;
        amount = amount;
        operation = operation;
    }
}
