package net.leahperson.proficientmod.client;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.entity.ModBlockEntities;
import net.leahperson.proficientmod.block.entity.renderer.CookingPotRenderer;
import net.leahperson.proficientmod.block.entity.renderer.ForgingTableRenderer;
import net.leahperson.proficientmod.block.entity.renderer.JewelcraftingStationRenderer;
import net.leahperson.proficientmod.block.entity.renderer.ReforgingAltarRenderer;
import net.leahperson.proficientmod.block.entity.renderer.ScribingTableRenderer;
import net.leahperson.proficientmod.block.entity.renderer.WorkbenchRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ProficientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.FORGING_STATION_BE.get(),      ForgingTableRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COOKING_POT_BE.get(),          CookingPotRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SCRIBING_TABLE_BE.get(),        ScribingTableRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WORKBENCH_BE.get(),             WorkbenchRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.JEWELCRAFTING_STATION_BE.get(), JewelcraftingStationRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.REFORGING_ALTAR_BE.get(),       ReforgingAltarRenderer::new);
    }
}
