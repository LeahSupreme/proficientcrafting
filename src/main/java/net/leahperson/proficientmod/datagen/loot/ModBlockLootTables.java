package net.leahperson.proficientmod.datagen.loot;

import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.FORGING_TABLE.get());
        this.dropSelf(ModBlocks.COOKING_POT.get());
        this.dropSelf(ModBlocks.SCRIBING_TABLE.get());
        this.dropSelf(ModBlocks.JEWELCRAFTING_STATION.get());
        this.dropSelf(ModBlocks.WORKBENCH.get());
        this.dropSelf(ModBlocks.REFORGING_ALTAR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
