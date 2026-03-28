package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.leahperson.proficientmod.block.custom.ForgingStationBlock;
import net.leahperson.proficientmod.block.custom.ScribingTableBlock;
import net.leahperson.proficientmod.block.entity.ScribingTableBlockEntity;
import net.leahperson.proficientmod.util.OverlayUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ScribingTableRenderer implements BlockEntityRenderer<ScribingTableBlockEntity> {
    private final ItemRenderer itemRenderer;
    private static final float TABLE_TOP_HEIGHT = 1.02f;

    private static final float[][] CROSS_SLOT_POSITIONS = {
        {0.5f, 0.5f},
        {0.5f, 0.2f},
        {0.5f, 0.8f},
        {0.8f, 0.5f},
        {0.2f, 0.5f},
    };

    public ScribingTableRenderer(BlockEntityRendererProvider.Context context) {
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
    public void render(ScribingTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        float facingYaw = getFacingYaw(blockEntity.getBlockState());

        float craftProgress = 0f;
        if (blockEntity.isCrafting() && blockEntity.getMaxProgress() > 0) {
            craftProgress = Math.min((blockEntity.getProgress() + partialTick) / (float) blockEntity.getMaxProgress(), 1f);
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(facingYaw));
        poseStack.translate(-0.5, 0, -0.5);

        for (int slotIndex = 0; slotIndex < blockEntity.getInputStacks().size(); slotIndex++) {
            ItemStack inputStack = blockEntity.getInputStacks().get(slotIndex);
            if (inputStack.isEmpty()) continue;

            float slotX = CROSS_SLOT_POSITIONS[slotIndex][0];
            float slotZ = CROSS_SLOT_POSITIONS[slotIndex][1];
            float itemX = slotX + (0.5f - slotX) * craftProgress;
            float itemZ = slotZ + (0.5f - slotZ) * craftProgress;
            float itemHeight = TABLE_TOP_HEIGHT + (slotIndex * 0.001f);

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
                    slotIndex + 300,
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
            poseStack.translate(0.5, TABLE_TOP_HEIGHT, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(facingYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));
            poseStack.scale(0.35f, 0.35f, 0.35f);

            OverlayUtils.renderOutputStack(
                    outputStack,
                    400,
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
