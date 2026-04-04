package net.leahperson.proficientmod.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Vanishable;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class CraftingToolItem extends TieredItem implements Vanishable {

    private final List<AttributeBonus> bonusAttributes;

    public CraftingToolItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.bonusAttributes = List.of();
    }

    public CraftingToolItem(Tier tier, List<AttributeBonus> bonusAttributes, Properties properties) {
        super(tier, properties);
        this.bonusAttributes = bonusAttributes;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND || bonusAttributes.isEmpty()) {
            return super.getDefaultAttributeModifiers(slot);
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));
        for (AttributeBonus attributeBonus : bonusAttributes) {
            UUID modifierUUID = UUID.nameUUIDFromBytes(attributeBonus.modifierName().getBytes(StandardCharsets.UTF_8));
            builder.put(attributeBonus.attribute(), new AttributeModifier(
                    modifierUUID,
                    "Tool bonus",
                    attributeBonus.amount(),
                    AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }
}
