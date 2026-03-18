package net.leahperson.proficientmod;

import com.mojang.logging.LogUtils;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.attribute.ModAttributes;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.leahperson.proficientmod.command.ModCommands;
import net.leahperson.proficientmod.loot.ModLootModifiers;
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
        modEventBus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
            event.dataPackRegistry(QualityDataType.RARITY_REGISTRY, QualityDataType.CODEC, QualityDataType.CODEC);
            event.dataPackRegistry(ItemQualityData.REGISTRY, ItemQualityData.CODEC, ItemQualityData.CODEC);
        });
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModLootModifiers.register(modEventBus);

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
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(ModItems.CRUDEHAMMER);
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
    }

    public static ResourceLocation modLocation(final String path) {
        return ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, path);
    }
}