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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReforgingAltarCategory implements IRecipeCategory<ReforgingRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "reforging_altar");
    public static final RecipeType<ReforgingRecipe> REFORGING_ALTAR_TYPE = new RecipeType<>(UID, ReforgingRecipe.class);

    private static final int STATION_X = 90;
    private static final int STATION_Y = 52;
    private static final int SCEPTER_Y = 12;

    private static final int[][] ITEM_SLOT_POSITIONS = {
        {41, 53},
        {41, 35},
        {41, 71},
        {59, 53},
        {23, 53},
    };

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

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReforgingRecipe recipe, IFocusGroup focuses) {
        if (recipe.getTargetIngredient().isPresent()) {
            builder.addSlot(RecipeIngredientRole.INPUT, ITEM_SLOT_POSITIONS[0][0], ITEM_SLOT_POSITIONS[0][1])
                    .addIngredients(recipe.getTargetIngredient().get());
        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, ITEM_SLOT_POSITIONS[0][0], ITEM_SLOT_POSITIONS[0][1])
                    .addItemStack(new ItemStack(net.minecraft.world.item.Items.IRON_SWORD));
        }



        List<Ingredient> catalysts = recipe.getCatalysts();
        for (int catalystIndex = 0; catalystIndex < catalysts.size() && catalystIndex + 1 < ITEM_SLOT_POSITIONS.length; catalystIndex++) {
            int[] position = ITEM_SLOT_POSITIONS[catalystIndex + 1];
            builder.addSlot(RecipeIngredientRole.INPUT, position[0], position[1])
                    .addIngredients(catalysts.get(catalystIndex));
        }

        builder.addSlot(RecipeIngredientRole.CATALYST, STATION_X, STATION_Y)
                .addItemStack(new ItemStack(ModBlocks.REFORGING_ALTAR.get()));
        builder.addSlot(RecipeIngredientRole.CATALYST, STATION_X, SCEPTER_Y)
                .addIngredients(Ingredient.of(ModTags.Items.REFORGING_SCEPTER));
    }

    @Override
    public void draw(ReforgingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        net.minecraft.client.gui.Font font = Minecraft.getInstance().font;

        int textStartX = 140;
        int textStartY = 8;
        int lineHeight = 10;
        int lineIndex = 0;

        if (!recipe.getAttributes().isEmpty()) {
            guiGraphics.drawString(font, Component.literal("Reforge bonuses:"),
                    textStartX, textStartY + lineIndex * lineHeight, 0xFF404040, false);
            lineIndex++;

            for (ReforgingAttributeDefinition definition : recipe.getAttributes()) {
                MutableComponent attributeLabel = getMutableComponent(definition);
                guiGraphics.drawString(font, attributeLabel, textStartX, textStartY + lineIndex * lineHeight, 0xFF606060, false);
                lineIndex++;
            }
        }

        if(recipe.getLevelCost() > 0){
            guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("qualitycrafting.jei.levelcost").append(Integer.toString(recipe.getLevelCost())),5,20,0xFF80FC20,true);

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

    private static @NotNull MutableComponent getMutableComponent(ReforgingAttributeDefinition definition) {
        String attributeShortName = definition.attributeId()
                .replaceFirst(".*:", "")
                .replace("_", " ");
        String avg0Display = definition.avg0() == Math.floor(definition.avg0())
                ? String.valueOf((int) definition.avg0())
                : String.valueOf(definition.avg0());
        String avg100Display = definition.avg100() == Math.floor(definition.avg100())
                ? String.valueOf((int) definition.avg100())
                : String.valueOf(definition.avg100());
        MutableComponent attributeLabel = Component.literal(
                "  " + attributeShortName + ": " + avg0Display + " \u2192 " + avg100Display);
        return attributeLabel;
    }
}
