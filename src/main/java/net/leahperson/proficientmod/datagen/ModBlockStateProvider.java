package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ProficientMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //simpleBlockWithItem(ModBlocks.FORGING_TABLE.get(), models().getBuilder("minecraft:anvil"));
        //blockWithItem(ModBlocks.FORGING_TABLE);


        horizontalBlock(ModBlocks.FORGING_TABLE.get(),new ModelFile.UncheckedModelFile(modLoc("block/sepiaanvil")));
        simpleBlockItem(ModBlocks.FORGING_TABLE.get(),new ModelFile.UncheckedModelFile(modLoc("block/sepiaanvil")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(modLoc("block/sepiaanvil")));
    }
}
