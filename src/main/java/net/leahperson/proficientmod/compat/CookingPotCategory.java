package net.leahperson.proficientmod.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.recipe.CookingRecipe;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CookingPotCategory implements IRecipeCategory<CookingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "cooking_pot");
    public static final RecipeType<CookingRecipe> COOKING_POT_TYPE = new RecipeType<>(UID, CookingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    private static final int SLOT_SIZE = 18;
    private static final int INPUT_START_X = 5;
    private static final int INPUT_START_Y = 20;
    private static final int INPUT_COLUMNS = 4;
    private static final int OUTPUT_X = 139;
    private static final int OUTPUT_START_Y = 5;

    public CookingPotCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(240, 110);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COOKING_POT.get()));
    }

    @Override
    public RecipeType<CookingRecipe> getRecipeType() {
        return COOKING_POT_TYPE;
    }

    @Override
    public int getWidth() {
        return 240;
    }

    @Override
    public int getHeight() {
        return 110;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.qualitycrafting.cooking_pot");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CookingRecipe recipe, IFocusGroup focuses) {
        boolean hasQualityPerIngredient = !recipe.getQualityPerIngredient().isEmpty();
        NonNullList<Float> qualityPerIngredient = recipe.getQualityPerIngredient();

        for (int i = 0; i < recipe.getInputItems().size(); i++) {
            int column = i % INPUT_COLUMNS;
            int row = i / INPUT_COLUMNS;
            var slot = builder.addSlot(RecipeIngredientRole.INPUT,
                            INPUT_START_X + column * SLOT_SIZE,
                            INPUT_START_Y + row * SLOT_SIZE)
                    .addIngredients(recipe.getInputItems().get(i));
            if (hasQualityPerIngredient) {
                slot.addRichTooltipCallback((slotView, tooltipBuilder) -> {
                    int maxRarity = QualityUtils.getMaxRarityIndex(Minecraft.getInstance().level);
                    for (int rarityIndex = 1; rarityIndex <= maxRarity && rarityIndex <= qualityPerIngredient.size(); rarityIndex++) {
                        tooltipBuilder.add(Component.translatable("qualitycrafting.jei.rarityinputtooltip",
                                qualityPerIngredient.get(rarityIndex - 1),
                                QualityUtils.getRarityComponent(rarityIndex)));
                    }
                });
            }
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 95, 28)
                .addItemStack(new ItemStack(ModBlocks.COOKING_POT.get()));

        for (int i = 0; i < recipe.getOutputs().size(); i++) {
            ItemStack outputStack = recipe.getOutputs().get(i).copy();
            if (i < recipe.getQualityDecoration().size()) {
                QualityDataUtil.setRarity(outputStack, recipe.getQualityDecoration().get(i));
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_START_Y + (i * SLOT_SIZE))
                    .addItemStack(outputStack);
        }
    }

    @Override
    public void draw(CookingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.hasQualityOutputs()) {
            for (int i = 0; i < recipe.getOutputs().size(); i++) {
                int minimum = i > 0 ? recipe.getQualityRequired().get(i - 1) : 0;
                MutableComponent range;
                if (i < recipe.getOutputs().size() - 1) {
                    int maximum = recipe.getQualityRequired().get(i) - 1;
                    range = Component.literal(minimum + "-" + maximum + " ");
                } else {
                    range = Component.literal(minimum + "+ ");
                }
                guiGraphics.drawString(Minecraft.getInstance().font,
                        range.append(Component.translatable("qualitycrafting:quality")),
                        OUTPUT_X + SLOT_SIZE + 2, OUTPUT_START_Y + 5 + (SLOT_SIZE * i), 0xFF636363, false);
            }
        }

        guiGraphics.drawString(Minecraft.getInstance().font,
                Component.translatable("qualitycrafting.jei.cooktime").append(Integer.toString(recipe.getCookTime())),
                10, 90, 0xFF636363, false);
    }
}
