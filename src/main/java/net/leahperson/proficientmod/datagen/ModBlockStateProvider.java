package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ProficientMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile forgingTableModel = new ModelFile.UncheckedModelFile(modLoc("block/sepiaanvil"));
        ModelFile cauldronModel = new ModelFile.UncheckedModelFile(mcLoc("block/cauldron"));

        simpleBlock(ModBlocks.FORGING_TABLE.get(), forgingTableModel);
        simpleBlockItem(ModBlocks.FORGING_TABLE.get(), forgingTableModel);

        simpleBlock(ModBlocks.COOKING_POT.get(), cauldronModel);
        itemModels().withExistingParent("cooking_pot", mcLoc("item/cauldron"));

        // Plain cube placeholders until custom models are made
        simplePlaceholder(ModBlocks.SCRIBING_TABLE.get(), "scribing_table");
        simplePlaceholder(ModBlocks.JEWELCRAFTING_STATION.get(), "jewelcrafting_station");
        simplePlaceholder(ModBlocks.WORKBENCH.get(), "workbench");
        simplePlaceholder(ModBlocks.REFORGING_ALTAR.get(), "reforging_altar");
    }

    private void simplePlaceholder(net.minecraft.world.level.block.Block block, String name) {
        ModelFile model = models().cubeAll(name, mcLoc("block/smooth_stone"));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }
}