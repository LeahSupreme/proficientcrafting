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

public class Ring extends Item implements ICurioItem {
    private final double qualityAmount;

    public Ring(Properties properties, double qualityAmount) {
        super(properties.stacksTo(1).defaultDurability(0));
        this.qualityAmount = qualityAmount;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(ModAttributes.QUALITY.get(), new AttributeModifier(uuid, "Ring quality", qualityAmount, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }
}
