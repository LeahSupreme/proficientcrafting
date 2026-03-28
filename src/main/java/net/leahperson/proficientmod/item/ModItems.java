package net.leahperson.proficientmod.item;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.item.curios.Ring;
import net.leahperson.proficientmod.item.curios.SmithingCharm;
import net.leahperson.proficientmod.item.food.CraftersStew;
import net.leahperson.proficientmod.item.food.EnergeticSoda;
import net.leahperson.proficientmod.item.food.WeddingCake;
import net.leahperson.proficientmod.item.tools.AmethystPickaxe;
import net.leahperson.proficientmod.item.tools.CraftingToolItem;
import net.leahperson.proficientmod.item.tools.ForgeHammerItem;
import net.leahperson.proficientmod.item.tools.SawItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ProficientMod.MOD_ID);

    public static final RegistryObject<Item> AMETHYST_PICKAXE = ITEMS.register("amethyst_pickaxe",
            AmethystPickaxe::new);

    public static final RegistryObject<Item> STONE_FORGE_HAMMER = ITEMS.register("stone_forge_hammer",
            () -> new ForgeHammerItem(Tiers.STONE,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:stone_forge_hammer.proficiency", 5.0,
                    new Item.Properties()));

    public static final RegistryObject<Item> IRON_FORGE_HAMMER = ITEMS.register("iron_forge_hammer",
            () -> new ForgeHammerItem(Tiers.IRON,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:iron_forge_hammer.proficiency", 10.0,
                    new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_FORGE_HAMMER = ITEMS.register("diamond_forge_hammer",
            () -> new ForgeHammerItem(Tiers.DIAMOND,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:diamond_forge_hammer.proficiency", 20.0,
                    new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> GOLD_FORGE_HAMMER = ITEMS.register("gold_forge_hammer",
            () -> new ForgeHammerItem(Tiers.GOLD,
                    ModAttributes.YIELD.get(), "qualitycrafting:gold_forge_hammer.yield", 10.0,
                    new Item.Properties()));

    public static final RegistryObject<Item> AMETHYST_FORGE_HAMMER = ITEMS.register("amethyst_forge_hammer",
            () -> new ForgeHammerItem(ModTiers.AMETHYST,
                    ModAttributes.QUALITY.get(), "qualitycrafting:amethyst_forge_hammer.quality", 10.0,
                    new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> NETHERITE_FORGE_HAMMER = ITEMS.register("netherite_forge_hammer",
            () -> new ForgeHammerItem(Tiers.NETHERITE,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:netherite_forge_hammer.proficiency", 30.0,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> WOODEN_LADLE = ITEMS.register("wooden_ladle",
            () -> new CraftingToolItem(Tiers.WOOD,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:wooden_ladle.proficiency", 5.0,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> IRON_LADLE = ITEMS.register("iron_ladle",
            () -> new CraftingToolItem(Tiers.IRON,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:iron_ladle.proficiency", 10.0,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DIAMOND_LADLE = ITEMS.register("diamond_ladle",
            () -> new CraftingToolItem(Tiers.DIAMOND,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:diamond_ladle.proficiency", 20.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> GOLD_LADLE = ITEMS.register("gold_ladle",
            () -> new CraftingToolItem(Tiers.GOLD,
                    ModAttributes.YIELD.get(), "qualitycrafting:gold_ladle.yield", 10.0,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> AMETHYST_LADLE = ITEMS.register("amethyst_ladle",
            () -> new CraftingToolItem(ModTiers.AMETHYST,
                    ModAttributes.QUALITY.get(), "qualitycrafting:amethyst_ladle.quality", 10.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> NETHERITE_LADLE = ITEMS.register("netherite_ladle",
            () -> new CraftingToolItem(Tiers.NETHERITE,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:netherite_ladle.proficiency", 30.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> IRON_CHISEL = ITEMS.register("iron_chisel",
            () -> new CraftingToolItem(Tiers.IRON,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:iron_chisel.proficiency", 10.0,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DIAMOND_CHISEL = ITEMS.register("diamond_chisel",
            () -> new CraftingToolItem(Tiers.DIAMOND,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:diamond_chisel.proficiency", 20.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> GOLD_CHISEL = ITEMS.register("gold_chisel",
            () -> new CraftingToolItem(Tiers.GOLD,
                    ModAttributes.YIELD.get(), "qualitycrafting:gold_chisel.yield", 10.0,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> NETHERITE_CHISEL = ITEMS.register("netherite_chisel",
            () -> new CraftingToolItem(Tiers.NETHERITE,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:netherite_chisel.proficiency", 30.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> IRON_SAW = ITEMS.register("iron_saw",
            () -> new SawItem(Tiers.IRON,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:iron_saw.proficiency", 10.0,
                    new Item.Properties()));

    public static final RegistryObject<Item> GOLD_SAW = ITEMS.register("gold_saw",
            () -> new SawItem(Tiers.GOLD,
                    ModAttributes.YIELD.get(), "qualitycrafting:gold_saw.yield", 10.0,
                    new Item.Properties()));

    public static final RegistryObject<Item> DIAMOND_SAW = ITEMS.register("diamond_saw",
            () -> new SawItem(Tiers.DIAMOND,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:diamond_saw.proficiency", 20.0,
                    new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> AMETHYST_SAW = ITEMS.register("amethyst_saw",
            () -> new SawItem(ModTiers.AMETHYST,
                    ModAttributes.QUALITY.get(), "qualitycrafting:amethyst_saw.quality", 10.0,
                    new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> NETHERITE_SAW = ITEMS.register("netherite_saw",
            () -> new SawItem(Tiers.NETHERITE,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:netherite_saw.proficiency", 30.0,
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> IRON_TIPPED_QUILL = ITEMS.register("iron_tipped_quill",
            () -> new CraftingToolItem(Tiers.IRON,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:iron_tipped_quill.proficiency", 10.0,
                    new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DIAMOND_TIPPED_QUILL = ITEMS.register("diamond_tipped_quill",
            () -> new CraftingToolItem(Tiers.DIAMOND,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:diamond_tipped_quill.proficiency", 20.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> NETHERITE_TIPPED_QUILL = ITEMS.register("netherite_tipped_quill",
            () -> new CraftingToolItem(Tiers.NETHERITE,
                    ModAttributes.PROFICIENCY.get(), "qualitycrafting:netherite_tipped_quill.proficiency", 30.0,
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> AMETHYST_SCEPTER = ITEMS.register("amethyst_scepter",
            () -> new CraftingToolItem(ModTiers.AMETHYST,
                    ModAttributes.QUALITY.get(), "qualitycrafting:amethyst_scepter.quality", 10.0,
                    new Item.Properties().stacksTo(1).durability(600)));

    public static final RegistryObject<Item> DIAMOND_SCEPTER = ITEMS.register("diamond_scepter",
            () -> new CraftingToolItem(Tiers.DIAMOND,
                    ModAttributes.QUALITY.get(), "qualitycrafting:diamond_scepter.quality", 20.0,
                    new Item.Properties().stacksTo(1).durability(1000).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> EMERALD_SCEPTER = ITEMS.register("emerald_scepter",
            () -> new CraftingToolItem(Tiers.IRON,
                    ModAttributes.QUALITY.get(), "qualitycrafting:emerald_scepter.quality", 30.0,
                    new Item.Properties().stacksTo(1).durability(1400).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> NETHER_STAR_SCEPTER = ITEMS.register("nether_star_scepter",
            () -> new CraftingToolItem(Tiers.GOLD,
                    ModAttributes.QUALITY.get(), "qualitycrafting:nether_star_scepter.quality", 50.0,
                    new Item.Properties().stacksTo(1).durability(2500).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> LAPIS_RING = ITEMS.register("lapis_ring",
            () -> new Ring(new Item.Properties(), 5.0));

    public static final RegistryObject<Item> DIAMOND_RING = ITEMS.register("diamond_ring",
            () -> new Ring(new Item.Properties().rarity(Rarity.UNCOMMON), 10.0));

    public static final RegistryObject<Item> EMERALD_RING = ITEMS.register("emerald_ring",
            () -> new Ring(new Item.Properties().rarity(Rarity.UNCOMMON), 15.0));

    public static final RegistryObject<Item> AMETHYST_RING = ITEMS.register("amethyst_ring",
            () -> new Ring(new Item.Properties().rarity(Rarity.RARE), 20.0));

    public static final RegistryObject<Item> NETHER_STAR_RING = ITEMS.register("nether_star_ring",
            () -> new Ring(new Item.Properties().rarity(Rarity.EPIC), 30.0));

    public static final RegistryObject<Item> SMITHING_CHARM = ITEMS.register("smithing_charm",
            () -> new SmithingCharm(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> CRAFTERS_STEW = ITEMS.register("crafters_stew",
            CraftersStew::new);

    public static final RegistryObject<Item> ENERGETIC_SODA = ITEMS.register("energetic_soda",
            EnergeticSoda::new);

    public static final RegistryObject<Item> WEDDING_CAKE = ITEMS.register("wedding_cake",
            WeddingCake::new);

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
