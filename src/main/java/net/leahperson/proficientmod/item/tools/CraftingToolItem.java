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

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CraftingToolItem extends TieredItem implements Vanishable {
    @Nullable
    private final Attribute bonusAttribute;
    @Nullable
    private final UUID bonusModifierUUID;
    private final double bonusAttributeAmount;

    public CraftingToolItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.bonusAttribute = null;
        this.bonusModifierUUID = null;
        this.bonusAttributeAmount = 0;
    }

    public CraftingToolItem(Tier tier, Attribute bonusAttribute, String modifierName, double bonusAmount, Properties properties) {
        super(tier, properties);
        this.bonusAttribute = bonusAttribute;
        this.bonusModifierUUID = UUID.nameUUIDFromBytes(modifierName.getBytes(StandardCharsets.UTF_8));
        this.bonusAttributeAmount = bonusAmount;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot != EquipmentSlot.MAINHAND || bonusAttribute == null || bonusModifierUUID == null) {
            return super.getDefaultAttributeModifiers(slot);
        }
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(slot));
        builder.put(bonusAttribute, new AttributeModifier(
                bonusModifierUUID, "Tool bonus",
                bonusAttributeAmount,
                AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
