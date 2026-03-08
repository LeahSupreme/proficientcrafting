package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.leahperson.proficientmod.block.entity.ForgingTableBlockEntity;
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

        // Render 9 input slots in a 3x3 grid on top of the table
        for (int i = 0; i < blockEntity.getInputStacks().size(); i++) {
            ItemStack stack = blockEntity.getInputStacks().get(i);
            if (stack.isEmpty()) continue;

            int row = i / 3;
            int col = i % 3;

            poseStack.pushPose();

            // Centered roughly on the top face of the block
            poseStack.translate(0.2 + (col * 0.3), 1.02, 0.2 + (row * 0.3));

            // Lay flat on the table
            poseStack.scale(0.25f, 0.25f, 0.25f);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    0
            );

            poseStack.popPose();
        }

        // Render output slightly above the center
        ItemStack output = blockEntity.getOutputStack();
        if (!output.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.5, 1.08, 0.5);
            poseStack.scale(0.35f, 0.35f, 0.35f);

            itemRenderer.renderStatic(
                    output,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    1
            );

            poseStack.popPose();
        }
    }
}