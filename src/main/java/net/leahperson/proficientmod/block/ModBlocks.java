package net.leahperson.proficientmod.block;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.custom.CookingPotBlock;
import net.leahperson.proficientmod.block.custom.ForgingStationBlock;
import net.leahperson.proficientmod.block.custom.JewelcraftingStationBlock;
import net.leahperson.proficientmod.block.custom.ReforgingAltarBlock;
import net.leahperson.proficientmod.block.custom.ScribingTableBlock;
import net.leahperson.proficientmod.block.custom.WorkbenchBlock;
import net.leahperson.proficientmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ProficientMod.MOD_ID);

    public static final RegistryObject<Block> FORGING_STATION = registerBlock("forging_station",
            () -> new ForgingStationBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> COOKING_POT = registerBlock("cooking_pot",
            () -> new CookingPotBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> SCRIBING_TABLE = registerBlock("scribing_table",
            () -> new ScribingTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final RegistryObject<Block> JEWELCRAFTING_STATION = registerBlock("jewelcrafting_station",
            () -> new JewelcraftingStationBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> WORKBENCH = registerBlock("workbench",
            () -> new WorkbenchBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final RegistryObject<Block> REFORGING_ALTAR = registerBlock("reforging_altar",
            () -> new ReforgingAltarBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(4.0F)
                    .sound(SoundType.STONE)
                    .noOcclusion()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }


    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name, ()->new BlockItem(block.get(),new Item.Properties()));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
