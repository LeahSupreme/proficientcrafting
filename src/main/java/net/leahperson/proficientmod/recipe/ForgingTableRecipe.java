package net.leahperson.proficientmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class ForgingTableRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final NonNullList<ItemStack> outputs;
    private final ResourceLocation id;
    private final Integer proficiencyRequired;
    private final NonNullList<Integer> qualityRequired;
    private final NonNullList<Float> qualityPerIngredient;
    private final int levelCost;
    private final int yieldCost;
    private final int yieldAdded;



    public ForgingTableRecipe(ResourceLocation id, NonNullList<Ingredient> inputItems, ItemStack output, NonNullList<ItemStack> outputs, int proficiencyRequired, NonNullList<Integer> qualityRequired, NonNullList<Float> qualityPerIngredient, int levelCost, int yieldCost, int yieldAdded) {


        this.id = id;
        this.inputItems = inputItems;
        this.output = output;
        this.outputs = outputs;
        this.proficiencyRequired = proficiencyRequired;
        this.qualityRequired = qualityRequired;
        this.qualityPerIngredient = qualityPerIngredient;
        this.levelCost = levelCost;
        this.yieldCost = yieldCost;
        this.yieldAdded = yieldAdded;
    }

    
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {

        if(pLevel.isClientSide){
            return false;
        }

        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;

        for (int j = 0; j < pContainer.getContainerSize(); ++j) {
            ItemStack itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }
        return i == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;



        //return inputItems.get(0).test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    public ItemStack getResultItemFromQualuty(double playerQuality){
        int outputIndex = 0;
        for(int i = 0; i < qualityRequired.size();i++){
            if(playerQuality >= qualityRequired.get(i)){
                outputIndex++;
            }
        }
        return outputs.get(outputIndex).copy();
        //return output.copy();
    }



    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    
    public List<Float> getQualityPerIngredient(){
        return this.qualityPerIngredient;
    }

    public ItemStack getOutputOfRarity(int rarity){
        return this.outputs.get(rarity);
    }

    public int getNumOutputs(){
        return outputs.size();
    }

    public int getLevelCost(){
        return this.levelCost;
    }

    public Integer getProficiencyRequired() {
        return proficiencyRequired;
    }

    public NonNullList<Integer> getQualityRequired() {
        return qualityRequired;
    }

    public int getYieldCost() {
        return yieldCost;
    }

    public int getYieldAdded() {
        return yieldAdded;
    }

    public static class Type implements RecipeType<ForgingTableRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String id = "forging_table";
    }

    public static class Serializer implements  RecipeSerializer<ForgingTableRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID,"forging_table");

        @Override
        public ForgingTableRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }


            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));



            NonNullList<ItemStack> outputs = NonNullList.withSize(GsonHelper.getAsJsonArray(pSerializedRecipe,"qualityCosts").size()+1,ItemStack.EMPTY);
            outputs.set(0,output);

            if(GsonHelper.isArrayNode(pSerializedRecipe,"outputPerQuality")){
                JsonArray jsonOutputs = GsonHelper.getAsJsonArray(pSerializedRecipe,"outputPerQuality");
                for(int i = 1; i < outputs.size();i++){
                    JsonElement j = jsonOutputs.get(i-1);
                    outputs.set(i, ShapedRecipe.itemStackFromJson(GsonHelper.convertToJsonObject(j,"outputPerQuality")));

                }
            }else{

                for(int i = 1; i < outputs.size();i++) {

                    ItemStack newItem = output.copy();
                    CompoundTag qualityLevelNBT = new CompoundTag();
                    qualityLevelNBT.putInt("quality", i);
                    //CompoundTag qualityCraftingNBT =  new CompoundTag();
                    //qualityCraftingNBT.put(ProficientMod.MOD_ID,qualityLevelNBT);

                    newItem.addTagElement(ProficientMod.MOD_ID, qualityLevelNBT);
                    outputs.set(i, newItem);
                }

            }
                int proficiency = GsonHelper.getAsInt(pSerializedRecipe,"proficiency");
                int yieldCost = GsonHelper.getAsInt(pSerializedRecipe,"yieldCost");
                int yieldAdded = GsonHelper.getAsInt(pSerializedRecipe,"yieldAdded");
                int levelCost = GsonHelper.getAsInt(pSerializedRecipe,"levelCost");

                JsonArray qualityCostsJSON =  GsonHelper.getAsJsonArray(pSerializedRecipe,"qualityCosts");
                NonNullList<Integer> qualityCosts = NonNullList.withSize(qualityCostsJSON.size(),0);
                for(int i = 0; i < qualityCostsJSON.size();i++){
                    JsonElement e = qualityCostsJSON.get(i);
                    qualityCosts.set(i,e.getAsInt());
                }

                JsonArray qualityIngredientsJSON =  GsonHelper.getAsJsonArray(pSerializedRecipe,"qualityPerIngredient");
                NonNullList<Float> qualityIngredients = NonNullList.withSize(qualityIngredientsJSON.size(),(float)0);
                for(int i = 0; i < qualityIngredientsJSON.size();i++){
                    JsonElement e = qualityIngredientsJSON.get(i);
                    qualityIngredients.set(i,e.getAsFloat());
                }

                return new ForgingTableRecipe(pRecipeId,inputs,output,outputs,proficiency,qualityCosts,qualityIngredients,levelCost,yieldCost,yieldAdded);

                //return new ForgingTableRecipe(pRecipeId,inputs, output );
        }

        @Override
        public @Nullable ForgingTableRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack output = pBuffer.readItem();

            NonNullList<ItemStack> outputs = NonNullList.withSize(pBuffer.readInt(),ItemStack.EMPTY);
            for(int i = 0; i < outputs.size(); i++) {
                outputs.set(i, pBuffer.readItem());
            }
            int proficiency = pBuffer.readInt();
            NonNullList<Integer> qualityCosts = NonNullList.withSize(pBuffer.readInt(),0);
            for(int i = 0; i < qualityCosts.size(); i++) {
                qualityCosts.set(i, pBuffer.readInt());
            }
            NonNullList<Float> qualityPerItem = NonNullList.withSize(pBuffer.readInt(),(float)0);
            for(int i = 0; i < qualityCosts.size(); i++) {
                qualityPerItem.set(i, pBuffer.readFloat());
            }
            int yieldCost = pBuffer.readInt();
            int yieldAdded = pBuffer.readInt();
            int levelCost = pBuffer.readInt();



            return new ForgingTableRecipe(pRecipeId,inputs, output,outputs,proficiency,qualityCosts,qualityPerItem,levelCost,yieldCost,yieldAdded);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ForgingTableRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);

            pBuffer.writeInt(pRecipe.outputs.size());
            for (ItemStack items : pRecipe.outputs){
                pBuffer.writeItemStack(items,false);
            }
            pBuffer.writeInt(pRecipe.qualityRequired.size());
            for(Integer i : pRecipe.qualityRequired){
                pBuffer.writeInt(i);
            }

            pBuffer.writeInt(pRecipe.qualityPerIngredient.size());
            for(float f : pRecipe.qualityRequired){
                pBuffer.writeFloat(f);
            }
            pBuffer.writeInt(pRecipe.yieldCost);
            pBuffer.writeInt(pRecipe.yieldAdded);
            pBuffer.writeInt(pRecipe.levelCost);


        }

    }
}
