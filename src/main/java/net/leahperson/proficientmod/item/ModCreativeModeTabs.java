package net.leahperson.proficientmod.item;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProficientMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> PROFICIENT_TAB = CREATIVE_MODE_TABS.register("proficient_tab",()-> CreativeModeTab.builder()
            .icon(()-> new ItemStack(ModItems.CRUDEHAMMER.get()))
            .title(Component.translatable("creativetab.proficient_tab"))
            .displayItems((pParameters,pOutput)->{
                pOutput.accept(ModItems.CRUDEHAMMER.get());
                pOutput.accept(ModItems.IRONHAMMER.get());
                pOutput.accept(ModBlocks.FORGING_TABLE.get());
                pOutput.accept(ModItems.FUNNYRING.get());
            })
            .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
