package net.leahperson.proficientmod.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.attribute.AttributeAdditionData;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.util.AttributeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;



@Mod.EventBusSubscriber(modid = ProficientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QualityItemAttributeEvent {




    @SubscribeEvent
    public static void addQualityCurioAttributes(CurioAttributeModifierEvent event){



        ItemStack stack = event.getItemStack();
        if(!QualityUtils.hasQuality(stack)){
            return;
        }

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();



        Optional<AttributeAdditionData> qualityEntry = Minecraft.getInstance().level.registryAccess().registryOrThrow(AttributeAdditionData.QUALITY_CURIOS_REGISTRY).stream().filter((elem)->{
            return elem.itemid().equals(itemId.getNamespace()+":"+itemId.getPath());
        }).findAny();

        if(qualityEntry.isEmpty()){
            return;
        }

        List<AttributeAddition> attributes = qualityEntry.get().rarities().get(QualityUtils.getQualityLevel(stack)-1);

        Multimap<Attribute, AttributeModifier> qualityModifiers = HashMultimap.create();

        for(int i = 0; i < attributes.size(); i++){
            String operation = attributes.get(i).operation();
            String attributeid = attributes.get(i).attributeid();;
            AtomicReference<Double> amount = new AtomicReference<>(attributes.get(i).amount());
            ResourceLocation attributeResource = ResourceLocation.parse(attributeid);
            Registry<Attribute> ar = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ATTRIBUTE);
            Attribute a = ar.get(attributeResource);
            AttributeModifier.Operation o = AttributeModifier.Operation.valueOf(operation);

            qualityModifiers.put(a,new AttributeModifier(Mth.createInsecureUUID(RandomSource.create((itemId.toString()+ProficientMod.MOD_ID+String.valueOf(QualityUtils.getQualityLevel(stack))).hashCode())),ProficientMod.MOD_ID+" "+a.getDescriptionId(), amount.get(),o));
        }
        Multimap<Attribute, AttributeModifier> combinedModifiers = AttributeUtils.combineAttributes(event.getOriginalModifiers(),qualityModifiers);
        event.clearModifiers();
        combinedModifiers.forEach((key,elem)->{
            event.addModifier(key,elem);
        });


    }

    @SubscribeEvent
    public static void addQualityToolAttributes(ItemAttributeModifierEvent event) {


        ItemStack stack = event.getItemStack();

        if(!QualityUtils.hasQuality(stack)){
            return;
        }

        if(!LivingEntity.getEquipmentSlotForItem(stack).equals(event.getSlotType())){
            return;
        }

        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();

        Optional<AttributeAdditionData> qualityEntry = Minecraft.getInstance().level.registryAccess().registryOrThrow(AttributeAdditionData.QUALITY_ATTRIBUTE_REGISTRY).stream().filter((elem)->{
            return elem.itemid().equals(itemId.getNamespace()+":"+itemId.getPath());
        }).findAny();

        if(qualityEntry.isEmpty()){
            return;
        }

        List<AttributeAddition> attributes = qualityEntry.get().rarities().get(QualityUtils.getQualityLevel(stack)-1);

        Multimap<Attribute, AttributeModifier> qualityModifiers = HashMultimap.create();

        for(int i = 0; i < attributes.size(); i++){
            String operation = attributes.get(i).operation();
            String attributeid = attributes.get(i).attributeid();;
            AtomicReference<Double> amount = new AtomicReference<>(attributes.get(i).amount());
            ResourceLocation attributeResource = ResourceLocation.parse(attributeid);
            Registry<Attribute> ar = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.ATTRIBUTE);
            Attribute a = ar.get(attributeResource);
            AttributeModifier.Operation o = AttributeModifier.Operation.valueOf(operation);

            qualityModifiers.put(a,new AttributeModifier(Mth.createInsecureUUID(RandomSource.create((itemId.toString()+ProficientMod.MOD_ID+String.valueOf(QualityUtils.getQualityLevel(stack))).hashCode())),ProficientMod.MOD_ID+" "+a.getDescriptionId(), amount.get(),o));
        }
        Multimap<Attribute, AttributeModifier> combinedModifiers = AttributeUtils.combineAttributes(event.getOriginalModifiers(),qualityModifiers);
        event.clearModifiers();
        combinedModifiers.forEach((key,elem)->{
            event.addModifier(key,elem);
        });




    }
}
