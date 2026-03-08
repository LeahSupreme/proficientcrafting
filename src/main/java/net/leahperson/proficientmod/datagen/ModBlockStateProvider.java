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

        simpleBlock(ModBlocks.FORGING_TABLE.get(), forgingTableModel);
        simpleBlockItem(ModBlocks.FORGING_TABLE.get(), forgingTableModel);
    }
}