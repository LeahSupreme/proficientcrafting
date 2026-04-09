package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.item.ModItems;
import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {

    public ModItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, CompletableFuture<TagLookup<Block>> blockTagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registries, blockTagLookup, ProficientMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Items.FORGING_HAMMER)
                .add(ModItems.STONE_FORGE_HAMMER.get())
                .add(ModItems.IRON_FORGE_HAMMER.get())
                .add(ModItems.DIAMOND_FORGE_HAMMER.get())
                .add(ModItems.GOLD_FORGE_HAMMER.get())
                .add(ModItems.AMETHYST_FORGE_HAMMER.get())
                .add(ModItems.NETHERITE_FORGE_HAMMER.get());

        this.tag(ModTags.Items.COOKING_LADLE)
                .add(ModItems.WOODEN_LADLE.get())
                .add(ModItems.IRON_LADLE.get())
                .add(ModItems.DIAMOND_LADLE.get())
                .add(ModItems.GOLD_LADLE.get())
                .add(ModItems.AMETHYST_LADLE.get())
                .add(ModItems.NETHERITE_LADLE.get());

        this.tag(ModTags.Items.SCRIBING_QUILL)
                .add(ModItems.IRON_TIPPED_QUILL.get())
                .add(ModItems.DIAMOND_TIPPED_QUILL.get())
                .add(ModItems.NETHERITE_TIPPED_QUILL.get());

        this.tag(ModTags.Items.JEWEL_CHISEL)
                .add(ModItems.IRON_CHISEL.get())
                .add(ModItems.DIAMOND_CHISEL.get())
                .add(ModItems.GOLD_CHISEL.get())
                .add(ModItems.AMETHYST_CHISEL.get())
                .add(ModItems.NETHERITE_CHISEL.get());

        this.tag(ModTags.Items.WOODWORKING_SAW)
                .add(ModItems.IRON_SAW.get())
                .add(ModItems.GOLD_SAW.get())
                .add(ModItems.DIAMOND_SAW.get())
                .add(ModItems.AMETHYST_SAW.get())
                .add(ModItems.NETHERITE_SAW.get());

        this.tag(ModTags.Items.REFORGING_SCEPTER)
                .add(ModItems.AMETHYST_SCEPTER.get())
                .add(ModItems.DIAMOND_SCEPTER.get())
                .add(ModItems.EMERALD_SCEPTER.get())
                .add(ModItems.NETHER_STAR_SCEPTER.get());

        this.tag(ModTags.Items.GOLDEN_SWORDS).add(Items.GOLDEN_SWORD);
        this.tag(ModTags.Items.GOLDEN_PICKAXES).add(Items.GOLDEN_PICKAXE);
        this.tag(ModTags.Items.GOLDEN_HOES).add(Items.GOLDEN_HOE);
    }
}
