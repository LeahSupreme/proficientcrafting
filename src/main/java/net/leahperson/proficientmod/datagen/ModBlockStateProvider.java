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
        ModelFile forgingStationModel      = new ModelFile.UncheckedModelFile(modLoc("block/forging_station"));
        ModelFile cookingPotModel          = new ModelFile.UncheckedModelFile(modLoc("block/cooking_pot"));
        ModelFile reforgingAltarModel      = new ModelFile.UncheckedModelFile(modLoc("block/reforging_altar"));
        ModelFile scribingTableModel       = new ModelFile.UncheckedModelFile(modLoc("block/scribing_table"));
        ModelFile jewelcraftingStationModel = new ModelFile.UncheckedModelFile(modLoc("block/jewelcrafting_station"));
        ModelFile workbenchModel           = new ModelFile.UncheckedModelFile(modLoc("block/workbench"));

        horizontalBlock(ModBlocks.FORGING_STATION.get(), forgingStationModel, 90);
        simpleBlockItem(ModBlocks.FORGING_STATION.get(), forgingStationModel);

        horizontalBlock(ModBlocks.COOKING_POT.get(), cookingPotModel);
        simpleBlockItem(ModBlocks.COOKING_POT.get(), cookingPotModel);

        horizontalBlock(ModBlocks.REFORGING_ALTAR.get(), reforgingAltarModel);
        simpleBlockItem(ModBlocks.REFORGING_ALTAR.get(), reforgingAltarModel);

        horizontalBlock(ModBlocks.SCRIBING_TABLE.get(), scribingTableModel);
        simpleBlockItem(ModBlocks.SCRIBING_TABLE.get(), scribingTableModel);

        horizontalBlock(ModBlocks.JEWELCRAFTING_STATION.get(), jewelcraftingStationModel);
        simpleBlockItem(ModBlocks.JEWELCRAFTING_STATION.get(), jewelcraftingStationModel);

        horizontalBlock(ModBlocks.WORKBENCH.get(), workbenchModel);
        simpleBlockItem(ModBlocks.WORKBENCH.get(), workbenchModel);
    }
}
