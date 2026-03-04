package net.leahperson.proficientmod.item;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ProficientMod.MOD_ID);

    public static final RegistryObject<Item> CRUDEHAMMER = ITEMS.register("crudeforgehammer",
            () -> new Item(new Item.Properties().stacksTo(1).defaultDurability(32).durability(32)));

    public static final RegistryObject<Item> IRONHAMMER = ITEMS.register("ironforgehammer",
            () -> new Item(new Item.Properties().stacksTo(1).defaultDurability(64).durability(64)));

    public static final RegistryObject<Item> FUNNYRING = ITEMS.register("funnyring",
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
