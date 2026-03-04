package net.leahperson.proficientmod.compat;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.recipe.ForgingTableRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class QualityInputTooltip implements IRecipeSlotRichTooltipCallback {

    private List<Float> rarityBonus;
    public QualityInputTooltip(ForgingTableRecipe recipe) {
        rarityBonus = recipe.getQualityPerIngredient();
    }

    @Override
    public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {

        for(int i = 1; i <= QualityUtils.getMaxRarityIndex(Minecraft.getInstance().level); i++){
            tooltip.add(Component.translatable("qualitycrafting.jei.rarityinputtooltip",rarityBonus.get(i-1), QualityUtils.getRarityComponent(i)));
        }

        //tooltip.add(Component.literal("+10 Quality if Uncommon\n+15 Quality if Rare\n+25 Quality if Legendary\n"+RarityNBT.getMaxRarityIndex(Minecraft.getInstance().level)));


    }


}
