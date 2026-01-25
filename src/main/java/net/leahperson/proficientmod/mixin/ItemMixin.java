package net.leahperson.proficientmod.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.attribute.AttributeAdditionData;
import net.leahperson.proficientmod.nbt.RarityNBT;
import net.leahperson.proficientmod.util.AttributeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

//@Mixin(Item.class)
public abstract class ItemMixin implements IForgeItem {
    //@Override
    public @Nullable Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {

        Multimap<Attribute, AttributeModifier> baseModifiers = IForgeItem.super.getAttributeModifiers(slot,stack);

        return baseModifiers;

        /*if(!RarityNBT.hasQuality(stack)){
            return baseModifiers;
        }

        if(!LivingEntity.getEquipmentSlotForItem(stack).equals(slot)){
            return baseModifiers;
        }


        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();



        Optional<AttributeAdditionData> qualityEntry = Minecraft.getInstance().level.registryAccess().registryOrThrow(AttributeAdditionData.ADDITIONAL_ATTRIBUTE_REGISTRY).stream().filter((elem)->{
            return elem.itemid().equals(itemId.getNamespace()+":"+itemId.getPath());
        }).findAny();

        if(qualityEntry.isEmpty()){
            return baseModifiers;
        }

        List<AttributeAddition> attributes = qualityEntry.get().rarities().get(RarityNBT.getQualityLevel(stack)-1);


        Multimap<Attribute, AttributeModifier> newModifiers = HashMultimap.create();

        for(int i = 0; i < attributes.size(); i++){
            String operation = attributes.get(i).operation();
            String attributeid = attributes.get(i).attributeid();;
            AtomicReference<Double> amount = new AtomicReference<>(attributes.get(i).amount());
            ResourceLocation attributeResource = ResourceLocation.parse(attributeid);
            Registry<Attribute> ar = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ATTRIBUTE);
            Attribute a = ar.get(attributeResource);
            AttributeModifier.Operation o = AttributeModifier.Operation.valueOf(operation);
            newModifiers.put(a,new AttributeModifier(Mth.createInsecureUUID(RandomSource.create((itemId.toString()+String.valueOf(RarityNBT.getQualityLevel(stack))).hashCode())),a.getDescriptionId(), amount.get(),o));


        }



        return AttributeUtils.combineAttributes(baseModifiers,newModifiers);*/
    }


}
