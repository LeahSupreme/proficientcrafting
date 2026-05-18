package net.leahperson.proficientmod.item.food;

import net.leahperson.proficientmod.effects.ModEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

import java.util.List;

public class CraftersStew extends QualityFoodItem {
    public CraftersStew() {
        super(Items.BOWL,
            List.of(),
            new Properties().food(new FoodProperties.Builder()
                    .nutrition(2)
                    .saturationMod(0.7f)
                    .build())
        );
    }
}
