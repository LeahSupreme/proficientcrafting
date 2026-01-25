package net.leahperson.proficientmod.attribute;


import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

public record FoodAdditionData(String itemid, List<FoodAddition> rarities) {

    public static final ResourceKey<Registry<FoodAdditionData>> FOOD_QUALITY_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "rarityfood"));





    public static final Codec<FoodAdditionData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    Codec.STRING.fieldOf("itemid").forGetter(FoodAdditionData::itemid),
                    Codec.list(FoodAddition.CODEC).fieldOf("rarities").forGetter(FoodAdditionData::rarities)
            )
            .apply(builder, FoodAdditionData::new));

    public FoodAdditionData {
        itemid = itemid;
        rarities = rarities;
    }





}
