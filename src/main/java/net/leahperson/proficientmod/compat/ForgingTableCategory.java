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
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.util.ModTags;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class ForgingTableCategory implements IRecipeCategory<ForgingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "forging_table");
    public static final RecipeType<ForgingRecipe> FORGING_TABLE_TYPE = new RecipeType<>(UID, ForgingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    private static final int SLOT_SIZE = 18;
    private static final int INPUT_START_X = 5;
    private static final int INPUT_START_Y = 35;
    private static final int OUTPUT_X = 139;
    private static final int OUTPUT_START_Y = 15;

    public ForgingTableCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(
                ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID,"textures/gui/forging_table_gui.png"),
                0, 0, 246, 165);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FORGING_TABLE.get()));
    }

    @Override
    public RecipeType<ForgingRecipe> getRecipeType() {
        return FORGING_TABLE_TYPE;
    }

    @Override
    public int getWidth() {
        return 240;
    }

    @Override
    public int getHeight() {
        return 130;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.qualitycrafting.forging_table");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ForgingRecipe recipe, IFocusGroup focuses) {
        boolean hasQualityPerIngredient = !recipe.getQualityPerIngredient().isEmpty();
        for (int i = 0; i < recipe.getInputItems().size(); i++) {
            var slot = builder.addSlot(RecipeIngredientRole.INPUT,
                            INPUT_START_X + (i % 3) * SLOT_SIZE,
                            INPUT_START_Y + (i / 3) * SLOT_SIZE)
                   .addIngredients(recipe.getInputItems().get(i));
            if (hasQualityPerIngredient) {
                slot.addRichTooltipCallback(new QualityInputTooltip(recipe));
            }
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 90, 52)
               .addItemStack(new ItemStack(ModBlocks.FORGING_TABLE.get()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 90, 12)
               .addIngredients(Ingredient.of(ModTags.Items.FORGING_HAMMER));

        for (int i = 0; i < recipe.getOutputs().size(); i++) {
            ItemStack out = recipe.getOutputs().get(i).copy();
            if (i < recipe.getQualityDecoration().size()) {
                QualityDataUtil.setRarity(out, recipe.getQualityDecoration().get(i));
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_START_Y + (i * SLOT_SIZE))
                   .addItemStack(out);
        }
    }

    @Override
    public void draw(ForgingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.hasQualityOutputs()) {
            for (int i = 0; i < recipe.getOutputs().size(); i++) {
                int min = i > 0 ? recipe.getQualityRequired().get(i - 1) : 0;
                MutableComponent range;
                if (i < recipe.getOutputs().size() - 1) {
                    int max = recipe.getQualityRequired().get(i) - 1;
                    range = Component.literal(min + "-" + max + " ");
                } else {
                    range = Component.literal(min + "+ ");
                }
                guiGraphics.drawString(Minecraft.getInstance().font,
                        range.append(Component.translatable("qualitycrafting:quality")),
                        159, OUTPUT_START_Y + 5 + (SLOT_SIZE * i), 0xFF636363, false);
            }
        }

        if (recipe.getProficiencyRequired() > 0) {
            guiGraphics.drawString(Minecraft.getInstance().font,
                    Component.translatable("qualitycrafting.jei.proficiencycost").append(Integer.toString(recipe.getProficiencyRequired())),
                    10, 90, 0xFF636363, false);
        }
    }
}
