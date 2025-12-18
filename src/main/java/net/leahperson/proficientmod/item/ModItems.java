package net.leahperson.proficientmod.item;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.nbt.QualityType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ProficientMod.MOD_ID);

    public static final RegistryObject<Item> CRUDEHAMMER = ITEMS.register("crudeforgehammer",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IRONHAMMER = ITEMS.register("ironforgehammer",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RARITYITEM = ITEMS.register("rarityitem",
            () -> new RarityItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
