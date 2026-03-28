package net.leahperson.proficientmod.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.item.ModTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AmethystPickaxe extends PickaxeItem {
    private static final UUID QUALITY_MODIFIER_UUID = UUID.nameUUIDFromBytes(
            "qualitycrafting:amethyst_pickaxe.quality".getBytes(StandardCharsets.UTF_8));

    public AmethystPickaxe() {
        super(ModTiers.AMETHYST, 1, -2.8f, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(slot, stack));
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(ModAttributes.QUALITY.get(),
                    new AttributeModifier(QUALITY_MODIFIER_UUID, "Amethyst Pickaxe quality", 10.0, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }
}
