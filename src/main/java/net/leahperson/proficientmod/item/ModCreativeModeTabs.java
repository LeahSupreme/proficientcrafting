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
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProficientMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> PROFICIENT_TAB = CREATIVE_MODE_TABS.register("proficient_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.IRON_FORGE_HAMMER.get()))
                    .title(Component.translatable("creativetab.proficient_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.AMETHYST_PICKAXE.get());
                        output.accept(ModItems.AMETHYST_HOE.get());

                        output.accept(ModItems.STONE_FORGE_HAMMER.get());
                        output.accept(ModItems.IRON_FORGE_HAMMER.get());
                        output.accept(ModItems.GOLD_FORGE_HAMMER.get());
                        output.accept(ModItems.DIAMOND_FORGE_HAMMER.get());
                        output.accept(ModItems.AMETHYST_FORGE_HAMMER.get());
                        output.accept(ModItems.NETHERITE_FORGE_HAMMER.get());

                        output.accept(ModItems.WOODEN_LADLE.get());
                        output.accept(ModItems.IRON_LADLE.get());
                        output.accept(ModItems.GOLD_LADLE.get());
                        output.accept(ModItems.DIAMOND_LADLE.get());
                        output.accept(ModItems.AMETHYST_LADLE.get());
                        output.accept(ModItems.NETHERITE_LADLE.get());

                        output.accept(ModItems.IRON_CHISEL.get());
                       // output.accept(ModItems.GOLD_CHISEL.get());
                        output.accept(ModItems.AMETHYST_CHISEL.get());
                        output.accept(ModItems.DIAMOND_CHISEL.get());
                        output.accept(ModItems.NETHERITE_CHISEL.get());

                        output.accept(ModItems.IRON_SAW.get());
                        output.accept(ModItems.GOLD_SAW.get());
                        output.accept(ModItems.DIAMOND_SAW.get());
                        output.accept(ModItems.AMETHYST_SAW.get());
                        output.accept(ModItems.NETHERITE_SAW.get());

                        output.accept(ModItems.IRON_TIPPED_QUILL.get());
                        output.accept(ModItems.DIAMOND_TIPPED_QUILL.get());
                        output.accept(ModItems.NETHERITE_TIPPED_QUILL.get());

                        output.accept(ModItems.AMETHYST_SCEPTER.get());
                        output.accept(ModItems.DIAMOND_SCEPTER.get());
                        output.accept(ModItems.EMERALD_SCEPTER.get());
                        output.accept(ModItems.NETHER_STAR_SCEPTER.get());

                        output.accept(ModItems.LAPIS_RING.get());
                        output.accept(ModItems.DIAMOND_RING.get());
                        output.accept(ModItems.EMERALD_RING.get());
                        output.accept(ModItems.AMETHYST_RING.get());
                        output.accept(ModItems.NETHER_STAR_RING.get());

                        output.accept(ModItems.SMITHING_CHARM.get());

                        output.accept(ModItems.CRAFTERS_STEW.get());
                        output.accept(ModItems.ENERGETIC_SODA.get());
                        output.accept(ModItems.WEDDING_CAKE.get());

                        output.accept(ModBlocks.FORGING_STATION.get());
                        output.accept(ModBlocks.COOKING_POT.get());
                        output.accept(ModBlocks.SCRIBING_TABLE.get());
                        output.accept(ModBlocks.JEWELCRAFTING_STATION.get());
                        output.accept(ModBlocks.WORKBENCH.get());
                        output.accept(ModBlocks.REFORGING_ALTAR.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
