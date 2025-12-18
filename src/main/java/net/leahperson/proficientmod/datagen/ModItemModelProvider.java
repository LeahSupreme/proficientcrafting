package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, ProficientMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.CRUDEHAMMER);
        simpleItem(ModItems.IRONHAMMER);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated")).texture("layer0",
                ResourceLocation.tryBuild(ProficientMod.MOD_ID, "item/" + item.getId().getPath()));
    }
}
