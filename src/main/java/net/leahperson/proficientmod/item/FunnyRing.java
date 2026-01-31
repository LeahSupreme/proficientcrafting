package net.leahperson.proficientmod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class FunnyRing  extends Item implements ICurioItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;


    public FunnyRing(Properties pProperties, Multimap<Attribute, AttributeModifier> defaultModifiers) {

        super(new Item.Properties().stacksTo(1).defaultDurability(0));
        this.defaultModifiers = defaultModifiers;
    }



    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
        for (Attribute attribute : defaultModifiers.keySet()) {
            var modifiers = defaultModifiers.get(attribute);
            for (AttributeModifier attributeModifier : modifiers) {
                attributeBuilder.put(attribute, new AttributeModifier(uuid, attributeModifier.getName(), attributeModifier.getAmount(), attributeModifier.getOperation()));
            }
        }
        return attributeBuilder.build();

        //return ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack);
    }
    private static Multimap<Attribute, AttributeModifier> createMultimap(Attribute attribute, AttributeModifier modifier){
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        map.put(attribute,modifier);
        return map;
    }

    public static Multimap<Attribute, AttributeModifier> funnyringAttributes(){
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        map.put(Attributes.MAX_HEALTH,new AttributeModifier("somedesc",10, AttributeModifier.Operation.ADDITION));
        return map;
    }
}
