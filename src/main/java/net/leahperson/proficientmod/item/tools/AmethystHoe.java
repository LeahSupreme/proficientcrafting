package net.leahperson.proficientmod.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.item.ModTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AmethystHoe extends HoeItem {
    private static final UUID QUALITY_MODIFIER_UUID = UUID.nameUUIDFromBytes(
            "qualitycrafting:amethyst_hoe.quality".getBytes(StandardCharsets.UTF_8));

    public AmethystHoe() {
        super(ModTiers.AMETHYST, 1, -2.8f, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(ModAttributes.FARMING_QUALITY.get(),
                    new AttributeModifier(QUALITY_MODIFIER_UUID, "Amethyst Hoe quality", 5.0, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }
}
