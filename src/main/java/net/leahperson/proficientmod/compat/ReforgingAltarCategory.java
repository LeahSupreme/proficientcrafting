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
import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.recipe.ReforgingAttributeDefinition;
import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.recipe.ReforgingRecipe;
import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReforgingAltarCategory implements IRecipeCategory<ReforgingRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "reforging_altar");
    public static final RecipeType<ReforgingRecipe> REFORGING_ALTAR_TYPE = new RecipeType<>(UID, ReforgingRecipe.class);

    private static final int SLOT_SIZE = 18;

    private final IDrawable background;
    private final IDrawable icon;

    public ReforgingAltarCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
                ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "textures/gui/station_gui.png"),
                0, 0, 246, 165);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.REFORGING_ALTAR.get()));
    }

    @Override
    public RecipeType<ReforgingRecipe> getRecipeType() {
        return REFORGING_ALTAR_TYPE;
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
        return Component.translatable("block.qualitycrafting.reforging_altar");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    private static final int[][] CATALYST_OFFSETS = {
        { 0, -SLOT_SIZE },
        { 0,  SLOT_SIZE },
        { SLOT_SIZE,  0 },
        { -SLOT_SIZE, 0 },
    };

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReforgingRecipe recipe, IFocusGroup focuses) {
        int centerX = 90;
        int centerY = 35;

        if (recipe.getTargetIngredient().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, centerX, centerY)
                   .addIngredients(recipe.getTargetIngredient().get());
        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, centerX, centerY)
                   .addItemStack(new ItemStack(net.minecraft.world.item.Items.IRON_SWORD));
        }

        List<Ingredient> catalysts = recipe.getCatalysts();
        for (int i = 0; i < catalysts.size() && i < CATALYST_OFFSETS.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT,
                            centerX + CATALYST_OFFSETS[i][0],
                            centerY + CATALYST_OFFSETS[i][1])
                   .addIngredients(catalysts.get(i));
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, 5, 5)
               .addItemStack(new ItemStack(ModBlocks.REFORGING_ALTAR.get()));
        builder.addSlot(RecipeIngredientRole.CATALYST, 5, 23)
               .addIngredients(Ingredient.of(ModTags.Items.REFORGING_SCEPTER));
    }

    @Override
    public void draw(ReforgingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        net.minecraft.client.gui.Font font = Minecraft.getInstance().font;

        int textStartX = 140;
        int textStartY = 8;
        int lineHeight = 10;
        int lineIndex  = 0;

        if (!recipe.getAttributeTiers().isEmpty()) {
            for (int tierIndex = 0; tierIndex < recipe.getAttributeTiers().size(); tierIndex++) {
                int qualityThreshold = tierIndex > 0 ? recipe.getQualityRequired().get(tierIndex - 1) : 0;
                MutableComponent tierLabel;
                if (tierIndex == 0 && recipe.getQualityRequired().isEmpty()) {
                    tierLabel = Component.literal("All quality:");
                } else {
                    String tierPrefix = tierIndex < recipe.getQualityRequired().size()
                            ? qualityThreshold + "-" + (recipe.getQualityRequired().get(tierIndex) - 1)
                            : qualityThreshold + "+";
                    tierLabel = Component.literal(tierPrefix + " quality:");
                }
                guiGraphics.drawString(font, tierLabel, textStartX, textStartY + lineIndex * lineHeight, 0xFF404040, false);
                lineIndex++;

                List<ReforgingAttributeDefinition> tierAttributes = recipe.getAttributeTiers().get(tierIndex);
                for (ReforgingAttributeDefinition def : tierAttributes) {
                    String shortAttributeName = def.attributeId()
                            .replaceFirst(".*:", "")
                            .replace("_", " ");
                    MutableComponent attributeLabel = Component.literal(
                            "  " + def.min() + "-" + def.max() + " " + shortAttributeName);
                    guiGraphics.drawString(font, attributeLabel, textStartX, textStartY + lineIndex * lineHeight, 0xFF606060, false);
                    lineIndex++;
                }
            }
        }

        guiGraphics.drawString(font,
                Component.translatable("qualitycrafting.jei.crafttime")
                        .append(Integer.toString(recipe.getCraftTime())),
                5, 90, 0xFF636363, false);

        if (recipe.getProficiencyRequired() > 0) {
            guiGraphics.drawString(font,
                    Component.translatable("qualitycrafting.jei.proficiencycost")
                            .append(Integer.toString(recipe.getProficiencyRequired())),
                    5, 101, 0xFF636363, false);
        }
    }
}
