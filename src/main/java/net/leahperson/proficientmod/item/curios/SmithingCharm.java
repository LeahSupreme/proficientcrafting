package net.leahperson.proficientmod.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class SmithingCharm extends Item implements ICurioItem {
    public SmithingCharm(Properties properties) {
        super(properties.stacksTo(1).defaultDurability(0));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.QUALITY.get(), new AttributeModifier(uuid, "Smithing Charm quality", 8.0, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.PROFICIENCY.get(), new AttributeModifier(uuid, "Smithing Charm proficiency", 8.0, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.YIELD.get(), new AttributeModifier(uuid, "Smithing Charm yield", 8.0, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
