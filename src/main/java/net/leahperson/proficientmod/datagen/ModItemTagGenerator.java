package net.leahperson.proficientmod.datagen;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.item.ModItems;
import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.world.item.Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, ProficientMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //Todo: Crafting station tag, Proficient Tool tag, Hammer etc tag

        this.tag(ModTags.Items.FORGING_HAMMER).add(ModItems.CRUDEHAMMER.get());
        this.tag(ModTags.Items.FORGING_HAMMER).add(ModItems.IRONHAMMER.get());

        this.tag(ModTags.Items.GOLDEN_SWORDS).add(Items.GOLDEN_SWORD);
        this.tag(ModTags.Items.GOLDEN_PICKAXES).add(Items.GOLDEN_PICKAXE);
        this.tag(ModTags.Items.GOLDEN_HOES).add(Items.GOLDEN_HOE);
        //this.tag(new TagKey<Item>(ResourceKey.createRegistryKey(ResourceLocation.parse("curios:ring")),ResourceLocation.parse("curios:ring"))).add(ModItems.FUNNYRING.get());


    }
}
