package net.leahperson.proficientmod.item.food;

import net.leahperson.proficientmod.effects.ModEffects;
import net.minecraft.world.food.FoodProperties;

import java.util.List;

public class WeddingCake extends QualityFoodItem {
    public WeddingCake() {
        super(null,
            List.of(
                new EffectDefinition(ModEffects.QUALITY, 0, 3600, 1200),
                new EffectDefinition(ModEffects.PROFICIENCY, 0, 3600, 1200)
            ),
            new Properties().stacksTo(1).food(new FoodProperties.Builder()
                    .nutrition(10)
                    .saturationMod(1.2f)
                    .build())
        );
    }
}
