package net.leahperson.proficientmod;

import com.mojang.logging.LogUtils;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.effects.ModEffects;
import net.leahperson.proficientmod.enchantment.ModEnchantments;
import net.leahperson.proficientmod.potion.ModPotions;
import net.leahperson.proficientmod.block.ModBlocks;
import net.leahperson.proficientmod.block.entity.ModBlockEntities;
import net.leahperson.proficientmod.item.ModCreativeModeTabs;
import net.leahperson.proficientmod.item.ModItems;
import net.leahperson.proficientmod.quality.QualityDataType;
import net.leahperson.proficientmod.recipe.ModRecipes;
import net.leahperson.proficientmod.registry.ModRecipeSerializers;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.leahperson.proficientmod.command.ModCommands;
import net.leahperson.proficientmod.loot.ModLootModifiers;
import net.leahperson.proficientmod.particle.ModParticles;
import net.leahperson.proficientmod.particle.SmallItemParticle;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ProficientMod.MOD_ID)
public class ProficientMod
{
    public static final String MOD_ID = "qualitycrafting";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ProficientMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus(); //FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModAttributes.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        modEventBus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
            event.dataPackRegistry(QualityDataType.RARITY_REGISTRY, QualityDataType.CODEC, QualityDataType.CODEC);
            event.dataPackRegistry(ItemQualityData.REGISTRY, ItemQualityData.CODEC, ItemQualityData.CODEC);
        });
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModParticles.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::applyAttributes);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        AttributeSupplier playerAttribs = DefaultAttributes.getSupplier(EntityType.PLAYER);
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES.getValues()) {
            if (playerAttribs.hasAttribute(attribute)) attribute.setSyncable(true);
        }
        event.enqueueWork(ProficientMod::registerBrewingRecipes);
    }

    private static void registerBrewingRecipes() {
        //Ingredient awkwardPotion = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        //Ingredient redstone = Ingredient.of(Items.REDSTONE);
        //Ingredient glowstone = Ingredient.of(Items.GLOWSTONE_DUST);

        //addBrewingSet(awkwardPotion, Ingredient.of(Items.DIAMOND), ModPotions.QUALITY, ModPotions.LONG_QUALITY, ModPotions.STRONG_QUALITY, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.BOOK), ModPotions.PROFICIENCY, ModPotions.LONG_PROFICIENCY, ModPotions.STRONG_PROFICIENCY, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.EMERALD), ModPotions.YIELD, ModPotions.LONG_YIELD, ModPotions.STRONG_YIELD, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.SPIDER_EYE), ModPotions.MOB_DROP_QUALITY, ModPotions.LONG_MOB_DROP_QUALITY, ModPotions.STRONG_MOB_DROP_QUALITY, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.BONE), ModPotions.MOB_DROP_YIELD, ModPotions.LONG_MOB_DROP_YIELD, ModPotions.STRONG_MOB_DROP_YIELD, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.WHEAT_SEEDS), ModPotions.FARMING_QUALITY, ModPotions.LONG_FARMING_QUALITY, ModPotions.STRONG_FARMING_QUALITY, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.BONE_MEAL), ModPotions.FARMING_YIELD, ModPotions.LONG_FARMING_YIELD, ModPotions.STRONG_FARMING_YIELD, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.QUARTZ), ModPotions.MINING_QUALITY, ModPotions.LONG_MINING_QUALITY, ModPotions.STRONG_MINING_QUALITY, redstone, glowstone);
        //addBrewingSet(awkwardPotion, Ingredient.of(Items.COAL), ModPotions.MINING_YIELD, ModPotions.LONG_MINING_YIELD, ModPotions.STRONG_MINING_YIELD, redstone, glowstone);
    }

    private static void addBrewingSet(
            Ingredient awkwardPotion,
            Ingredient baseIngredient,
            RegistryObject<Potion> base,
            RegistryObject<Potion> longVariant,
            RegistryObject<Potion> strongVariant,
            Ingredient redstone,
            Ingredient glowstone) {
        ItemStack baseOutput = PotionUtils.setPotion(new ItemStack(Items.POTION), base.get());
        BrewingRecipeRegistry.addRecipe(awkwardPotion, baseIngredient, baseOutput);
        BrewingRecipeRegistry.addRecipe(Ingredient.of(baseOutput), redstone, PotionUtils.setPotion(new ItemStack(Items.POTION), longVariant.get()));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(baseOutput), glowstone, PotionUtils.setPotion(new ItemStack(Items.POTION), strongVariant.get()));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(ModItems.STONE_FORGE_HAMMER);
        }
    }

    @SubscribeEvent
    public void applyAttributes(EntityAttributeModificationEvent attributeModificationEvent) {
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.PROFICIENCY.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.QUALITY.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.YIELD.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.FARMING_QUALITY.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.FARMING_YIELD.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.MINING_QUALITY.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.MINING_YIELD.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.MOB_DROP_QUALITY.get());
        attributeModificationEvent.add(EntityType.PLAYER, ModAttributes.MOB_DROP_YIELD.get());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.registerSpecial(ModParticles.SMALL_ITEM.get(), new SmallItemParticle.Provider());
        }
    }

    public static ResourceLocation modLocation(final String path) {
        return ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, path);
    }
}