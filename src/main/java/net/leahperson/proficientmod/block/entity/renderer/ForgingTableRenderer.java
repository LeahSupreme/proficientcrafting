package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.leahperson.proficientmod.block.custom.ForgingStationBlock;
import net.leahperson.proficientmod.block.entity.ForgingTableBlockEntity;
import net.leahperson.proficientmod.util.OverlayUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ForgingTableRenderer implements BlockEntityRenderer<ForgingTableBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ForgingTableRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    private static float getFacingYaw(BlockState blockState) {
        Direction blockFacing = blockState.getValue(ForgingStationBlock.FACING);
        return switch (blockFacing) {
            case EAST -> 90f;
            case SOUTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };
    }

    @Override
    public void render(ForgingTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        float facingYaw = getFacingYaw(blockEntity.getBlockState());

        float craftProgress = 0f;
        if (blockEntity.isCrafting() && blockEntity.getMaxProgress() > 0) {
            craftProgress = Math.min((blockEntity.getProgress() + partialTick) / (float) blockEntity.getMaxProgress(), 1f);
        }

        int rowCount = 0;
        for (int slotIndex = 0; slotIndex < blockEntity.getInputStacks().size(); slotIndex++) {
            if (!blockEntity.getInputStacks().get(slotIndex).isEmpty()) {
                rowCount = Math.max(rowCount, slotIndex / 3 + 1);
            }
        }
        if (rowCount == 0) rowCount = 1;

        float gridOriginZ = 0.5f - (rowCount - 1) * 0.15f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(facingYaw));
        poseStack.translate(-0.5, 0, -0.5);

        for (int slotIndex = 0; slotIndex < blockEntity.getInputStacks().size(); slotIndex++) {
            ItemStack inputStack = blockEntity.getInputStacks().get(slotIndex);
            if (inputStack.isEmpty()) continue;

            int row = slotIndex / 3;
            int column = slotIndex % 3;

            float gridX = 0.2f + (column * 0.3f);
            float gridZ = gridOriginZ + (row * 0.3f);
            float itemX = gridX + (0.5f - gridX) * craftProgress;
            float itemZ = gridZ + (0.5f - gridZ) * craftProgress;
            float itemHeight = 1.02f + (slotIndex * 0.001f);

            poseStack.pushPose();
            poseStack.translate(itemX, itemHeight, itemZ);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.scale(0.25f, 0.25f, 0.25f);

            itemRenderer.renderStatic(
                    inputStack,
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    bufferSource,
                    blockEntity.getLevel(),
                    slotIndex
            );

            OverlayUtils.renderOverlay(
                    inputStack,
                    slotIndex + 100,
                    poseStack,
                    bufferSource,
                    combinedLight,
                    combinedOverlay,
                    blockEntity.getLevel()
            );

            poseStack.popPose();
        }

        poseStack.popPose();

        ItemStack outputStack = blockEntity.getOutputStack();
        if (!outputStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.02, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(facingYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));
            poseStack.scale(0.35f, 0.35f, 0.35f);

            OverlayUtils.renderOutputStack(
                    outputStack,
                    200,
                    poseStack,
                    bufferSource,
                    combinedLight,
                    combinedOverlay,
                    blockEntity.getLevel(),
                    itemRenderer
            );

            poseStack.popPose();
        }
    }
}
