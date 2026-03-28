package net.leahperson.proficientmod.compat;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.recipe.JewelcraftingRecipe;
import net.leahperson.proficientmod.recipe.ScribingRecipe;
import net.leahperson.proficientmod.recipe.WorkbenchRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;

public class QualityInputTooltip implements IRecipeSlotRichTooltipCallback {

    private final NonNullList<Float> qualityPerIngredient;

    public QualityInputTooltip(ForgingRecipe recipe) {
        this.qualityPerIngredient = recipe.getQualityPerIngredient();
    }

    public QualityInputTooltip(ScribingRecipe recipe) {
        this.qualityPerIngredient = recipe.getQualityPerIngredient();
    }

    public QualityInputTooltip(JewelcraftingRecipe recipe) {
        this.qualityPerIngredient = recipe.getQualityPerIngredient();
    }

    public QualityInputTooltip(WorkbenchRecipe recipe) {
        this.qualityPerIngredient = recipe.getQualityPerIngredient();
    }

    @Override
    public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
        int maxRarity = QualityUtils.getMaxRarityIndex(Minecraft.getInstance().level);
        for (int i = 1; i <= maxRarity && i <= qualityPerIngredient.size(); i++) {
            tooltip.add(Component.translatable("qualitycrafting.jei.rarityinputtooltip",
                    qualityPerIngredient.get(i - 1),
                    QualityUtils.getRarityComponent(i)));
        }
    }
}
