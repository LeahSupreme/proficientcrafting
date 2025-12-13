package net.leahperson.proficientmod.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.nbt.RarityNBT;
import net.leahperson.proficientmod.recipe.ForgingTableRecipe;
import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ForgingTableCategory implements IRecipeCategory<ForgingTableRecipe> {
    public static final ResourceLocation UID =  ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "forging_table");

    public static final RecipeType<ForgingTableRecipe> FORGING_TABLE_TYPE =
            new RecipeType<>(UID, ForgingTableRecipe.class);


    private final IDrawable background;
    private final IDrawable icon;

    private static final int DISTANCE_INPUT_X = 18;
    private static final int DISTANCE_INPUT_Y = 16;

    public ForgingTableCategory(IGuiHelper helper) {

        this.background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID,"textures/gui/forging_table_gui.png"), 0, 0, 246, 165);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FORGING_TABLE.get()));;
    }

    @Override
    public RecipeType<ForgingTableRecipe> getRecipeType() {
        return FORGING_TABLE_TYPE;
    }

    @Override
    public int getWidth() {
        return 180;
    }

    @Override
    public int getHeight() {
        return 130;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.qualitycrafting.forging_table");
    }



    @SuppressWarnings("removal")
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ForgingTableRecipe recipe, IFocusGroup focuses) {
        for(int i = 0; i < recipe.getIngredients().size(); i++){
            IRecipeSlotBuilder slotBuilder = builder
                    .addSlot(RecipeIngredientRole.INPUT, 5+((i%3)* DISTANCE_INPUT_X), (int) (35+(Math.floor(i/3)* DISTANCE_INPUT_Y)))
                    .addRichTooltipCallback(new QualityInputTooltip(recipe));

            slotBuilder.addIngredients(recipe.getIngredients().get(i));

            //builder.addSlot().addIngredients(recipe.getIngredients().get(i));
        }

        //============





            //=========

        builder.addSlot(RecipeIngredientRole.CATALYST,90,52).addItemStack(new ItemStack(ModBlocks.FORGING_TABLE.get()));

        builder.addSlot(RecipeIngredientRole.CATALYST,90,12).addIngredients(Ingredient.of(ModTags.Items.FORGING_HAMMER));




        builder.addSlot(RecipeIngredientRole.OUTPUT, 139, 52).addItemStack(recipe.getResultItem(null));


    }

    //static int ticks = 0;

    @Override
    public void draw(ForgingTableRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        //IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        //guiGraphics

        //guiGraphics.blit(0,0,0,180,130);


        //Xp color: 128 252 32


        /*if(mouseX >= 10 && mouseX <= 60 && mouseY >= 25 && mouseY <= 33){
            guiGraphics.renderTooltip(Minecraft.getInstance().font,Component.literal("Some Text"),(int)mouseX,(int)mouseY);
        }
        guiGraphics.drawString(Minecraft.getInstance().font,"Quality + ?",10,25,63*256*256+252*256+252,false);
        */


        //ticks++;

        guiGraphics.drawString(Minecraft.getInstance().font, "aaTypes: "+RarityNBT.getMaxRarityIndex(Minecraft.getInstance().level),40,40,0xFF80FC20,true);


        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("qualitycrafting.jei.levelcost").append(Integer.toString(recipe.getLevelCost())),5,20,0xFF80FC20,true);

        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("qualitycrafting.jei.proficiencycost").append(Integer.toString(recipe.getProficiencyRequired())),10,90,0xFF636363,false);
        int numRarities = recipe.getNumOutputs();
        //int rarityTick = ((ticks/120)%numRarities);
        //if(ticks % (120*numRarities) )


            List<Integer> allQualities = new ArrayList<Integer>();
            allQualities.add(0);
            allQualities.addAll(recipe.getQualityRequired());
            List<MutableComponent> qualityComponents = new ArrayList<MutableComponent>();
            for(int i = 0; i < allQualities.size();i++){
                qualityComponents.add(RarityNBT.styleRarity(i,Integer.toString(allQualities.get(i))));
                if(i != allQualities.size()-1){
                    qualityComponents.add(Component.literal("/"));
                }
            }
            MutableComponent allQualitiesComponent = qualityComponents.stream().reduce(Component.empty(),(component,element)->{
                return component.append(element);
            });

            /*MutableComponent mycom = Component.empty();
            for(int i = 0; i < numRarities; i++){
                mycom = mycom.append(RarityNBT.getRarityComponent(i));
            }*/


            String qualityString = String.join("/",allQualities.stream().map(elem->Integer.toString(elem)).toList());
            guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("qualitycrafting.jei.qualitycost").append(allQualitiesComponent),10,100,0xFF636363,false);



            //guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("for ").append(mycom).append(" Rarity"),10,110,0xFF636363,false);





        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("qualitycrafting.jei.yieldcost",recipe.getYieldAdded(),recipe.getYieldCost()),10,110,0xFF636363,false);




    }
}
