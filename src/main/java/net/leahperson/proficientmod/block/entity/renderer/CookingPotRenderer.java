package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.leahperson.proficientmod.block.entity.CookingPotBlockEntity;
import net.leahperson.proficientmod.util.OverlayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class CookingPotRenderer implements BlockEntityRenderer<CookingPotBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CookingPotRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CookingPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        float animTime = blockEntity.getLevel().getGameTime() + partialTick;

        renderLiquid(poseStack, bufferSource, combinedLight, combinedOverlay);

        List<ItemStack> items = new ArrayList<>();
        List<Long> placedTimes = new ArrayList<>();
        long[] inputPlacedTimes = blockEntity.getInputPlacedTimes();
        NonNullList<ItemStack> inputStacks = blockEntity.getInputStacks();
        for (int slot = 0; slot < inputStacks.size(); slot++) {
            if (!inputStacks.get(slot).isEmpty()) {
                items.add(inputStacks.get(slot));
                placedTimes.add(inputPlacedTimes[slot]);
            }
        }

        int count = items.size();
        if (count > 0) {
            float spinOffset = 0f;
            float convergence = 0f;
            if (blockEntity.isCrafting() && blockEntity.getMaxProgress() > 0) {
                float craftElapsed = animTime - (float) blockEntity.getCraftStartTime();
                spinOffset = (float) Math.toRadians(craftElapsed * 4f);
                convergence = Math.min((blockEntity.getProgress() + partialTick) / (float) blockEntity.getMaxProgress(), 1f);
            }

            float baseRadius = count == 1 ? 0f : 0.22f;
            float radius = baseRadius * (1f - convergence);

            for (int i = 0; i < count; i++) {
                float angle = (float) (2.0 * Math.PI * i / count) + spinOffset;
                float x = 0.5f + radius * (float) Math.sin(angle);
                float z = 0.5f + radius * (float) Math.cos(angle);
                float elapsed = animTime - (float) placedTimes.get(i);
                float bobY = 0.675f + (float) Math.sin(elapsed * 0.1f) * 0.03f;

                poseStack.pushPose();
                poseStack.translate(x, bobY, z);
                poseStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle) + 90f));
                poseStack.scale(0.35f, 0.35f, 0.35f);

                itemRenderer.renderStatic(
                        items.get(i),
                        ItemDisplayContext.FIXED,
                        combinedLight,
                        combinedOverlay,
                        poseStack,
                        bufferSource,
                        blockEntity.getLevel(),
                        i
                );

                OverlayUtils.renderOverlay(
                        items.get(i),
                        i + 500,
                        poseStack,
                        bufferSource,
                        combinedLight,
                        combinedOverlay,
                        blockEntity.getLevel()
                );

                poseStack.popPose();
            }
        }

        ItemStack output = blockEntity.getOutputStack();
        if (!output.isEmpty()) {
            float outputElapsed = animTime - (float) blockEntity.getOutputAppearTime();
            float bobY = 1.2f + (float) Math.sin(outputElapsed * 0.08f) * 0.05f;
            float outputRotation = (outputElapsed * 2f) % 360f;

            poseStack.pushPose();
            poseStack.translate(0.5, bobY, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(outputRotation));
            poseStack.scale(0.67f, 0.67f, 0.67f);

            OverlayUtils.renderOutputStack(
                    output,
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

    private void renderLiquid(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        TextureAtlasSprite waterSprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ResourceLocation.withDefaultNamespace("block/water_still"));

        VertexConsumer consumer = bufferSource.getBuffer(Sheets.translucentCullBlockSheet());
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();

        float liquidY = 0.625f;
        float minX = 0.125f;
        float maxX = 0.875f;
        float minZ = 0.125f;
        float maxZ = 0.875f;

        float minU = waterSprite.getU0();
        float maxU = waterSprite.getU1();
        float minV = waterSprite.getV0();
        float maxV = waterSprite.getV1();

        consumer.vertex(matrix, minX, liquidY, minZ).color(63, 118, 228, 180).uv(minU, minV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMatrix, 0, 1, 0).endVertex();
        consumer.vertex(matrix, minX, liquidY, maxZ).color(63, 118, 228, 180).uv(minU, maxV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMatrix, 0, 1, 0).endVertex();
        consumer.vertex(matrix, maxX, liquidY, maxZ).color(63, 118, 228, 180).uv(maxU, maxV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMatrix, 0, 1, 0).endVertex();
        consumer.vertex(matrix, maxX, liquidY, minZ).color(63, 118, 228, 180).uv(maxU, minV).overlayCoords(combinedOverlay).uv2(combinedLight).normal(normalMatrix, 0, 1, 0).endVertex();
    }
}
