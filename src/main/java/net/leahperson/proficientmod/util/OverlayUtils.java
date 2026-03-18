package net.leahperson.proficientmod.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OverlayUtils {
    public static boolean isOverlay(final ItemStack stack) {
        if (Minecraft.getInstance().level != null) {
            return QualityUtils.isRarityItem(stack, Minecraft.getInstance().level);
        } else {
            return false;
        }
    }

    public static ItemStack getOverlay(final ItemStack stack) {
        if (Minecraft.getInstance().level != null) {
            return QualityUtils.getRarityItem(QualityUtils.getQualityLevel(stack), Minecraft.getInstance().level);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static void renderOverlay(ItemStack stack, int seed, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, Level level) {
        if (!QualityUtils.hasQuality(stack)) {
            return;
        }
        ItemStack overlay = getOverlay(stack);
        if (overlay.isEmpty()) {
            return;
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(overlay, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, level, seed);
    }
}