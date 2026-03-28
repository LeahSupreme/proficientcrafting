package net.leahperson.proficientmod.enchantment;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ProficientMod.MOD_ID);

    public static final RegistryObject<Enchantment> CRAFTING_QUALITY =
            ENCHANTMENTS.register("crafting_quality", () -> new CraftingToolEnchantment() {});

    public static final RegistryObject<Enchantment> CRAFTING_PROFICIENCY =
            ENCHANTMENTS.register("crafting_proficiency", () -> new CraftingToolEnchantment() {});

    public static final RegistryObject<Enchantment> CRAFTING_YIELD =
            ENCHANTMENTS.register("crafting_yield", () -> new CraftingToolEnchantment() {});

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
