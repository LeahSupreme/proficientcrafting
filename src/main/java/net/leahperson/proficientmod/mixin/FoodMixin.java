package net.leahperson.proficientmod.mixin;


import net.leahperson.proficientmod.attribute.FoodAddition;
import net.leahperson.proficientmod.attribute.FoodAdditionData;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;

import java.util.Optional;

@Mixin(Item.class)
public abstract class FoodMixin implements IForgeItem {

    @Override
    public FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity)
    {
        if(!QualityUtils.hasQuality(stack)){
            return IForgeItem.super.getFoodProperties(stack,entity);
        }



        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();

        Registry<FoodAdditionData> foodRarityRegistry = Minecraft.getInstance().level.registryAccess().registryOrThrow(FoodAdditionData.FOOD_QUALITY_REGISTRY);
        Optional<FoodAdditionData> data = foodRarityRegistry.stream().filter(elem->{
            return elem.itemid().equals(itemId.getNamespace()+":"+itemId.getPath());
        }).findAny();
        if(data.isEmpty()){
            return IForgeItem.super.getFoodProperties(stack,entity);
        }

        FoodAddition food = data.get().rarities().get(QualityUtils.getQualityLevel(stack)-1);

        FoodProperties baseFoodProperties = IForgeItem.super.getFoodProperties(stack,entity);



        //baseFoodProperties.getEffects().get(0).getFirst();



        //Supplier<MobEffectInstance> a = ()-> {return baseFoodProperties.getEffects().get(0).getFirst();};

        FoodProperties.Builder newFoodProperties = new FoodProperties.Builder();

        newFoodProperties.nutrition((int) (baseFoodProperties.getNutrition()+food.nutritionadd()))
                .saturationMod(baseFoodProperties.getSaturationModifier()+food.saturationmodifieradd());

        if(baseFoodProperties.isFastFood()){
            newFoodProperties.fast();
        }
        if(baseFoodProperties.canAlwaysEat()){
            newFoodProperties.alwaysEat();
        }
        if(baseFoodProperties.isMeat()){
            newFoodProperties.meat();
        }




        baseFoodProperties.getEffects().forEach((elem)->{
            newFoodProperties.effect(elem.getFirst(),elem.getSecond());
        });
        food.effectsadd().forEach(elem->{
            newFoodProperties.effect(new MobEffectInstance( Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.MOB_EFFECT).get(ResourceLocation.parse(elem.effectid())),elem.duration()*20,elem.amplifier()),1.0f);
        });

        return newFoodProperties.build();




        //return IForgeItem.super.getFoodProperties(stack,entity);
    }
}
