package net.leahperson.proficientmod.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class CraftingStatEffect extends MobEffect {
    protected CraftingStatEffect(int color, Attribute... affectedAttributes) {
        super(MobEffectCategory.BENEFICIAL, color);
        for (Attribute attribute : affectedAttributes) {
            ResourceLocation attributeRegistryName = ForgeRegistries.ATTRIBUTES.getKey(attribute);
            UUID modifierUUID = UUID.nameUUIDFromBytes(attributeRegistryName.toString().getBytes(StandardCharsets.UTF_8));
            this.addAttributeModifier(attribute, modifierUUID.toString(), 10.0, AttributeModifier.Operation.ADDITION);
        }
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (amplifier + 1);
    }
}
