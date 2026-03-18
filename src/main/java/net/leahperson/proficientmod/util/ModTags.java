package net.leahperson.proficientmod.util;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        //public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
        //public static final TagKey<Block> NEEDS_SAPPHIRE_TOOL = tag("needs_sapphire_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> FORGING_HAMMER = tag("forging_hammer");
        public static final TagKey<Item> GOLDEN_SWORDS = tag("golden_swords");
        public static final TagKey<Item> GOLDEN_PICKAXES = tag("golden_pickaxes");
        public static final TagKey<Item> GOLDEN_HOES = tag("golden_hoes");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, name));
        }
    }
}