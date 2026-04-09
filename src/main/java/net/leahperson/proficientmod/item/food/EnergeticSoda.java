package net.leahperson.proficientmod.item.food;

import net.leahperson.proficientmod.effects.ModEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

import java.util.List;

public class EnergeticSoda extends QualityFoodItem {
    public EnergeticSoda() {
        super(null,
                List.of(),
            new Properties().stacksTo(1).food(new FoodProperties.Builder()
                    .nutrition(3)
                    .saturationMod(0.2f)
                    .build())
        );
    }
}