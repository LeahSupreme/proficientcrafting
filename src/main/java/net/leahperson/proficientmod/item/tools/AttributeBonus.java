package net.leahperson.proficientmod.item.tools;

import net.minecraft.world.entity.ai.attributes.Attribute;

public record AttributeBonus(Attribute attribute, String modifierName, double amount) {
}
