package net.leahperson.proficientmod.nbt;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.recipe.ForgingTableRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record QualityType(int index, double bonus, ResourceLocation icon) {

    public static final ResourceKey<Registry<QualityType>> RARITY_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "rarity"));


    public static final QualityType NONE = new QualityType(0, 1, ProficientMod.location("none"));



    public static final Codec<QualityType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("index").forGetter(QualityType::index),
                    Codec.DOUBLE.fieldOf("bonus").forGetter(QualityType::bonus),
                    ResourceLocation.CODEC.fieldOf("icon").forGetter(QualityType::icon))
            .apply(builder, QualityType::new));

    public QualityType {
        index = Math.max(0, index);
        bonus = bonus;
        icon = icon;
    }

    public static Quality createQuality(final Holder<QualityType> holder, final ItemStack stack) {

        QualityType type = holder.value();







        return Quality.NONE;

    }


}
