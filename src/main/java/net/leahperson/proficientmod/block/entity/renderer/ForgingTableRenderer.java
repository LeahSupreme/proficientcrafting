package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.leahperson.proficientmod.block.entity.ForgingTableBlockEntity;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.util.OverlayUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ForgingTableRenderer implements BlockEntityRenderer<ForgingTableBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ForgingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ForgingTableBlockEntity blockEntity,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int combinedLight,
                       int combinedOverlay) {

        float t = 0f;
        if (blockEntity.isCrafting() && blockEntity.getMaxProgress() > 0) {
            t = Math.min((blockEntity.getProgress() + partialTick) / (float) blockEntity.getMaxProgress(), 1f);
        }

        int numRows = 0;
        for (int i = 0; i < blockEntity.getInputStacks().size(); i++) {
            if (!blockEntity.getInputStacks().get(i).isEmpty()) {
                numRows = Math.max(numRows, i / 3 + 1);
            }
        }
        if (numRows == 0) {
            numRows = 1;
        }

        float gridStartZ = 0.5f - (numRows - 1) * 0.15f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(90f));
        poseStack.translate(-0.5, 0, -0.5);

        for (int i = 0; i < blockEntity.getInputStacks().size(); i++) {
            ItemStack stack = blockEntity.getInputStacks().get(i);
            if (stack.isEmpty()) {
                continue;
            }

            int row = i / 3;
            int col = i % 3;

            float startX = 0.2f + (col * 0.3f);
            float startZ = gridStartZ + (row * 0.3f);
            float x = startX + (0.5f - startX) * t;
            float z = startZ + (0.5f - startZ) * t;
            float itemY = 1.02f + (i * 0.001f);

            poseStack.pushPose();
            poseStack.translate(x, itemY, z);
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
            poseStack.scale(0.25f, 0.25f, 0.25f);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    i
            );

            if (QualityUtils.hasQuality(stack)) {
                ItemStack overlay = OverlayUtils.getOverlay(stack);
                if (!overlay.isEmpty()) {
                    itemRenderer.renderStatic(
                            overlay,
                            ItemDisplayContext.FIXED,
                            combinedLight,
                            combinedOverlay,
                            poseStack,
                            bufferSource,
                            blockEntity.getLevel(),
                            i + 100
                    );
                }
            }

            poseStack.popPose();
        }

        poseStack.popPose();

        ItemStack output = blockEntity.getOutputStack();
        if (!output.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.02, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(270));
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));
            poseStack.scale(0.35f, 0.35f, 0.35f);

            itemRenderer.renderStatic(
                    output,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    200
            );

            if (QualityUtils.hasQuality(output)) {
                ItemStack overlay = OverlayUtils.getOverlay(output);
                if (!overlay.isEmpty()) {
                    itemRenderer.renderStatic(
                            overlay,
                            ItemDisplayContext.FIXED,
                            combinedLight,
                            combinedOverlay,
                            poseStack,
                            bufferSource,
                            blockEntity.getLevel(),
                            201
                    );
                }
            }

            poseStack.popPose();
        }
    }
}
