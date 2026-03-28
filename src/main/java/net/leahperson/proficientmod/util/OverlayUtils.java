package net.leahperson.proficientmod.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
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

    public static int outputVisualCount(int stackCount) {
        if (stackCount <= 1) {
            return 1;
        }
        if (stackCount <= 16) {
            return 2;
        }
        if (stackCount <= 48) {
            return 3;
        }
        return 4;
    }

    public static void renderOutputStack(ItemStack stack, int seedBase, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, Level level, ItemRenderer itemRenderer) {
        int visualCount = outputVisualCount(stack.getCount());
        float halfSpan = (visualCount - 1) / 2.0f;
        for (int layer = 0; layer < visualCount; layer++) {
            float centeredLayer = layer - halfSpan;
            poseStack.pushPose();
            poseStack.translate(centeredLayer * -0.125f, centeredLayer * 0.1875f, layer * 0.001f);
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, level, seedBase + layer);
            if (layer == 0) {
                renderOverlay(stack, seedBase + 50, poseStack, bufferSource, combinedLight, combinedOverlay, level);
            }
            poseStack.popPose();
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