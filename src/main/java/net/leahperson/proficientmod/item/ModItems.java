package net.leahperson.proficientmod.item;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ProficientMod.MOD_ID);

    public static final RegistryObject<Item> CRUDEHAMMER = ITEMS.register("crude_forge_hammer",
            () -> new Item(new Item.Properties().stacksTo(1).defaultDurability(32).durability(32)));

    public static final RegistryObject<Item> FORGING_TABLE_ITEM = ITEMS.register("forging_table",
            () -> new BlockItem(ModBlocks.FORGING_TABLE.get(), new Item.Properties()));

    public static final RegistryObject<Item> IRONHAMMER = ITEMS.register("iron_forge_hammer",
            () -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .durability(250)));

    public static final RegistryObject<Item> FUNNYRING = ITEMS.register("funny_ring",
            () -> new FunnyRing(new Item.Properties(),FunnyRing.funnyringAttributes()));





    public static final RegistryObject<Item> UNCOMMON_RARITY = ITEMS.register("rarity1",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RARE_RARITY = ITEMS.register("rarity2",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LEGENDARY_RARITY = ITEMS.register("rarity3",
            () -> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
