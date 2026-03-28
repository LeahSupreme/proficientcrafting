package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.block.entity.ReforgingAltarBlockEntity;
import net.leahperson.proficientmod.particle.ModParticles;
import net.leahperson.proficientmod.util.OverlayUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ReforgingAltarRenderer implements BlockEntityRenderer<ReforgingAltarBlockEntity> {

    private static final ResourceLocation BEAM_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");
    private static final ResourceLocation GLYPH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID, "textures/block/reforging_altar_emissive.png");

    private static final float ITEM_SCALE = 0.25f;
    private static final float SIDE_ITEM_DEPTH = 1f / 16f;
    private static final float SIDE_ITEM_Y = 6f / 16f;
    private static final float FLOAT_HEIGHT = 1.1f;
    private static final float BOB_AMPLITUDE = 0.025f;
    private static final float BOB_SPEED = 0.1f;

    private static final float SPIN_BASE = 0.75f;
    private static final float SPIN_MAX = SPIN_BASE * 25.0f;
    private static final float SPIN_X_FACTOR = 0.63f;
    private static final float SPIN_Z_FACTOR = 0.375f;

    private static final float SIDE_DISAPPEAR_START = 0.80f;
    private static final float SIDE_DISAPPEAR_STEP = 0.05f;

    private static final float SIDE_GROW_END = 0.30f;
    private static final float SIDE_SCALE_MAX = 1.25f;
    private static final float SIDE_SCALE_MIN = 0.750f;
    private static final float SIDE_SPIN_MAX = SPIN_MAX;

    private static final float BEAM_RADIUS_MIN = 0.01f;
    private static final float BEAM_RADIUS_MAX = 0.25f;
    private static final float BEAM_GLOW_MIN = BEAM_RADIUS_MIN + 0.03f;
    private static final float BEAM_GLOW_MAX = BEAM_RADIUS_MAX + 0.10f;
    private static final float BEAM_SLOW_TICKS = 20f;
    private static final float BEAM_BURST_FRACTION = 0.2f;
    private static final float BEAM_PHASE_ONE_HEIGHT = 0.05f;
    private static final float BEAM_TARGET_HEIGHT = 256f;
    private static final float GLYPH_PULSE_SPEED = 0.08f;

    private final ItemRenderer itemRenderer;

    public ReforgingAltarRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean shouldRenderOffScreen(ReforgingAltarBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public void render(ReforgingAltarBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        float time = level.getGameTime() + partialTick;

        float craftProgress = 0f;
        if (blockEntity.isCrafting() && blockEntity.getMaxProgress() > 0) {
            craftProgress = Math.min((blockEntity.getProgress() + partialTick) / (float) blockEntity.getMaxProgress(), 1f);
        }

        boolean crafting = blockEntity.isCrafting();
        BlockPos blockPos = blockEntity.getBlockPos();

        float beamHeightFraction = getBeamHeightFraction(blockEntity, partialTick, crafting);

        float spinIntegral = (crafting && blockEntity.getMaxProgress() > 0)
                ? spinFractionIntegral(craftProgress) : 0f;
        int maxProgress = blockEntity.getMaxProgress();
        float craftBonus = crafting ? maxProgress * (SPIN_MAX - SPIN_BASE) * spinIntegral : 0f;

        float tumbleFade;
        if (blockEntity.isOutputReady()) {
            tumbleFade = 0f;
        } else if (!crafting || craftProgress < SIDE_DISAPPEAR_START) {
            tumbleFade = 1f;
        } else {
            tumbleFade = 1f - (craftProgress - SIDE_DISAPPEAR_START) / (1f - SIDE_DISAPPEAR_START);
        }

        float timeSincePlaced = (float)(level.getGameTime() + partialTick - blockEntity.getItemPlacedTime());

        float centerAngleY = time * SPIN_BASE + craftBonus;
        float centerAngleX = (timeSincePlaced * (SPIN_BASE * SPIN_X_FACTOR) + craftBonus * 0.5f) * tumbleFade;
        float centerAngleZ = (timeSincePlaced * (SPIN_BASE * SPIN_Z_FACTOR) + craftBonus * 0.3f) * tumbleFade;

        renderSideItem(blockEntity.getSlotForFace(Direction.NORTH), Direction.NORTH, 1,
                crafting, craftProgress, maxProgress, SIDE_DISAPPEAR_START,
                blockPos, poseStack, bufferSource, combinedLight, combinedOverlay, level);
        renderSideItem(blockEntity.getSlotForFace(Direction.SOUTH), Direction.SOUTH, 2,
                crafting, craftProgress, maxProgress, SIDE_DISAPPEAR_START + SIDE_DISAPPEAR_STEP,
                blockPos, poseStack, bufferSource, combinedLight, combinedOverlay, level);
        renderSideItem(blockEntity.getSlotForFace(Direction.EAST), Direction.EAST, 3,
                crafting, craftProgress, maxProgress, SIDE_DISAPPEAR_START + SIDE_DISAPPEAR_STEP * 2,
                blockPos, poseStack, bufferSource, combinedLight, combinedOverlay, level);
        renderSideItem(blockEntity.getSlotForFace(Direction.WEST), Direction.WEST, 4,
                crafting, craftProgress, maxProgress, SIDE_DISAPPEAR_START + SIDE_DISAPPEAR_STEP * 3,
                blockPos, poseStack, bufferSource, combinedLight, combinedOverlay, level);

        renderTopGlyph(craftProgress, time, crafting, poseStack, bufferSource);

        if (crafting && beamHeightFraction > 0f) {
            renderBeam(beamHeightFraction, craftProgress, time, partialTick, poseStack, bufferSource, level, blockPos);
        }

        ItemStack topStack = blockEntity.getSlotForFace(Direction.UP);
        if (topStack.isEmpty()) {
            return;
        }

        float bob = Mth.sin(time * BOB_SPEED) * BOB_AMPLITUDE;

        poseStack.pushPose();
        poseStack.translate(0.5, FLOAT_HEIGHT + bob, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(centerAngleY));
        poseStack.mulPose(Axis.XP.rotationDegrees(centerAngleX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(centerAngleZ));
        poseStack.scale(ITEM_SCALE * 2, ITEM_SCALE * 2, ITEM_SCALE * 2);

        OverlayUtils.renderOutputStack(topStack, 50, poseStack, bufferSource, combinedLight, combinedOverlay, level, itemRenderer);

        poseStack.popPose();
    }

    private void renderSideItem(ItemStack stack, Direction direction, int seed,
                                boolean crafting, float craftProgress, int maxProgress,
                                float disappearThreshold, BlockPos blockPos,
                                PoseStack poseStack, MultiBufferSource bufferSource,
                                int combinedLight, int combinedOverlay, Level level) {
        if (stack.isEmpty()) {
            return;
        }

        float x, z, yaw;
        switch (direction) {
            case NORTH -> { x = 0.5f; z = SIDE_ITEM_DEPTH;yaw = 180f; }
            case SOUTH -> { x = 0.5f; z = 1f - SIDE_ITEM_DEPTH; yaw = 0f;   }
            case EAST  -> { x = 1f - SIDE_ITEM_DEPTH; z = 0.5f; yaw = 270f; }
            case WEST  -> { x = SIDE_ITEM_DEPTH; z = 0.5f; yaw = 90f;  }
            default    -> { return; }
        }

        if (crafting && craftProgress >= disappearThreshold) {
            float windowEnd = disappearThreshold + SIDE_DISAPPEAR_STEP;
            if (craftProgress < windowEnd && level instanceof ClientLevel clientLevel) {
                double worldX = blockPos.getX() + x;
                double worldY = blockPos.getY() + SIDE_ITEM_Y + 0.1;
                double worldZ = blockPos.getZ() + z;
                RandomSource random = level.getRandom();
                if (random.nextFloat() < 0.6f) {
                    clientLevel.addParticle(ParticleTypes.ENCHANT, worldX, worldY, worldZ,
                            (random.nextDouble() - 0.5) * 0.4, random.nextDouble() * 0.4, (random.nextDouble() - 0.5) * 0.4);
                }
                if (random.nextFloat() < 0.3f) {
                    clientLevel.addParticle(ParticleTypes.END_ROD, worldX, worldY, worldZ,
                            (random.nextDouble() - 0.5) * 0.15, random.nextDouble() * 0.25, (random.nextDouble() - 0.5) * 0.15);
                }
            }
            return;
        }

        if (crafting && craftProgress > 0.05f && level instanceof ClientLevel clientLevel) {
            RandomSource random = level.getRandom();
            if (random.nextFloat() < craftProgress * 0.35f) {
                double perpX = (1 - Math.abs(direction.getStepX())) * (random.nextDouble() - 0.5) * 0.25;
                double perpZ = (1 - Math.abs(direction.getStepZ())) * (random.nextDouble() - 0.5) * 0.25;
                double worldX = blockPos.getX() + x + perpX;
                double worldY = blockPos.getY() + SIDE_ITEM_Y + (random.nextDouble() - 0.5) * 0.25;
                double worldZ = blockPos.getZ() + z + perpZ;
                clientLevel.addParticle(new ItemParticleOption(ModParticles.SMALL_ITEM.get(), stack),
                        worldX, worldY, worldZ,
                        direction.getStepX() * 0.15 + perpX * 0.2,
                        random.nextDouble() * 0.08 + 0.02,
                        direction.getStepZ() * 0.15 + perpZ * 0.2);
            }
        }

        float scale;
        float spinAngle = 0f;
        if (!crafting) {
            scale = ITEM_SCALE;
        } else if (craftProgress < SIDE_GROW_END) {
            scale = ITEM_SCALE * Mth.lerp(craftProgress / SIDE_GROW_END, 1.0f, SIDE_SCALE_MAX);
        } else {
            float shrinkFraction = (craftProgress - SIDE_GROW_END) / (SIDE_DISAPPEAR_START - SIDE_GROW_END);
            scale = ITEM_SCALE * Mth.lerp(shrinkFraction, SIDE_SCALE_MAX, SIDE_SCALE_MIN);
            spinAngle = -(SIDE_SPIN_MAX * maxProgress * shrinkFraction * shrinkFraction * 0.5f);
        }

        poseStack.pushPose();
        poseStack.translate(x, SIDE_ITEM_Y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.ZP.rotationDegrees(spinAngle));
        poseStack.scale(scale, scale, scale);

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED,
                combinedLight, combinedOverlay, poseStack, bufferSource, level, seed);
        OverlayUtils.renderOverlay(stack, seed + 50, poseStack, bufferSource, combinedLight, combinedOverlay, level);

        poseStack.popPose();
    }

    private static float getBeamHeightFraction(ReforgingAltarBlockEntity blockEntity, float partialTick, boolean crafting) {
        float beamHeightFraction = 0f;
        if (crafting && blockEntity.getMaxProgress() > 0) {
            float rawProgress = blockEntity.getProgress() + partialTick;
            float burstDuration = BEAM_BURST_FRACTION * blockEntity.getMaxProgress();
            if (rawProgress <= BEAM_SLOW_TICKS) {
                beamHeightFraction = (rawProgress / BEAM_SLOW_TICKS) * BEAM_PHASE_ONE_HEIGHT;
            } else if (rawProgress < BEAM_SLOW_TICKS + burstDuration) {
                float burstProgress = (rawProgress - BEAM_SLOW_TICKS) / burstDuration;
                float exponentialProgress = (float) ((Math.exp(4.0 * burstProgress) - 1.0) / (Math.exp(4.0) - 1.0));
                beamHeightFraction = BEAM_PHASE_ONE_HEIGHT + (1f - BEAM_PHASE_ONE_HEIGHT) * exponentialProgress;
            } else {
                beamHeightFraction = 1f;
            }
        }
        return beamHeightFraction;
    }

    private static float spinFractionIntegral(float progress) {
        if (progress <= 0f) {
            return 0f;
        }
        if (progress <= 0.3f) {
            return 0.25f * progress * progress;
        }
        float result = 0.0225f;
        float phase2Delta = Math.min(progress, 0.7f) - 0.3f;
        result += 0.15f * phase2Delta + 0.4375f * phase2Delta * phase2Delta;
        if (progress <= 0.7f) {
            return result;
        }
        float phase3Delta = Math.min(progress, 1.0f) - 0.7f;
        result += 0.50f * phase3Delta + (5f / 6f) * phase3Delta * phase3Delta;
        return result;
    }

    private static void renderTopGlyph(float craftProgress, float time, boolean crafting, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (!crafting || craftProgress <= 0f) {
            return;
        }

        float alpha = getAlpha(craftProgress, time);

        VertexConsumer consumer = bufferSource.getBuffer(
                RenderType.entityTranslucentEmissive(GLYPH_TEXTURE, false));

        PoseStack.Pose pose = poseStack.last();
        float glyphY = 0.752f;
        addGlyphVertex(consumer, pose, 0f, glyphY, 0f, alpha, 0f, 0f);
        addGlyphVertex(consumer, pose, 0f, glyphY, 1f, alpha, 0f, 1f);
        addGlyphVertex(consumer, pose, 1f, glyphY, 1f, alpha, 1f, 1f);
        addGlyphVertex(consumer, pose, 1f, glyphY, 0f, alpha, 1f, 0f);
    }

    private static float getAlpha(float craftProgress, float time) {
        if (craftProgress >= 0.9f) {
            return 1.0f;
        }

        float normalizedProgress = craftProgress / 0.9f;
        float minAlpha = 0.05f + normalizedProgress * 0.35f;
        float maxAlpha = 0.30f + normalizedProgress * 0.70f;

        if (craftProgress < 0.1f) {
            return (craftProgress / 0.1f) * minAlpha;
        }

        float pulse = (1f + Mth.sin(time * GLYPH_PULSE_SPEED)) * 0.5f;
        return Mth.lerp(pulse, minAlpha, maxAlpha);
    }

    private static void addGlyphVertex(VertexConsumer consumer, PoseStack.Pose pose,
                                       float x, float y, float z, float alpha,
                                       float u, float v) {
        consumer.vertex(pose.pose(), x, y, z)
                .color(1f, 1f, 1f, alpha)
                .uv(u, v)
                .overlayCoords(0, 10)
                .uv2(0xF000F0)
                .normal(pose.normal(), 0f, 1f, 0f)
                .endVertex();
    }

    private void renderBeam(float beamHeightFraction, float craftProgress, float time, float partialTick,
                            PoseStack poseStack, MultiBufferSource bufferSource,
                            Level level, BlockPos blockPos) {
        float maxPossibleHeight = Math.max(1f, level.getMaxBuildHeight() - blockPos.getY() - 1f);
        float height = Math.max(0.1f, Math.min(beamHeightFraction * BEAM_TARGET_HEIGHT, maxPossibleHeight));

        float innerRadius = BEAM_RADIUS_MIN + (BEAM_RADIUS_MAX - BEAM_RADIUS_MIN) * craftProgress;
        float glowRadius = BEAM_GLOW_MIN + (BEAM_GLOW_MAX - BEAM_GLOW_MIN) * craftProgress;

        float red = 0.4f + (1.0f - 0.4f) * craftProgress;
        float green = 0.2f + (0.85f - 0.2f) * craftProgress;
        float blue = 0.9f + (0.1f - 0.9f) * craftProgress;

        poseStack.pushPose();
        poseStack.translate(0.0, 0.75, 0.0);

        renderBeamSegment(poseStack, bufferSource, partialTick, (long) time, 0f, height,
                red, green, blue, 0.9f, innerRadius);
        renderBeamSegment(poseStack, bufferSource, partialTick, (long) time, 0f, height,
                1f, 1f, 1f, 0.125f, glowRadius);

        poseStack.popPose();
    }

    private static void renderBeamSegment(PoseStack poseStack, MultiBufferSource bufferSource,
                                          float partialTick, long gameTime, float yStart, float height,
                                          float red, float green, float blue, float alpha, float radius) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.beaconBeam(BEAM_TEXTURE, alpha < 1.0f));

        float scroll = (float) Math.floorMod(gameTime, 40) + partialTick;
        float uvTop = Mth.frac(-scroll * 0.2f - Mth.floor(-scroll * 0.1f));
        float uvBottom = uvTop - height;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(scroll * 2.25f - 45f));

        addBeamQuad(consumer, poseStack,  radius, -radius, yStart, height, red, green, blue, alpha, uvTop, uvBottom);
        addBeamQuad(consumer, poseStack, -radius, -radius, yStart, height, red, green, blue, alpha, uvTop, uvBottom);
        addBeamQuad(consumer, poseStack, -radius,  radius, yStart, height, red, green, blue, alpha, uvTop, uvBottom);
        addBeamQuad(consumer, poseStack,  radius,  radius, yStart, height, red, green, blue, alpha, uvTop, uvBottom);

        poseStack.popPose();
    }

    private static void addBeamQuad(VertexConsumer consumer, PoseStack poseStack,
                                    float cornerX, float cornerZ, float yStart, float height,
                                    float red, float green, float blue, float alpha,
                                    float uvTop, float uvBottom) {
        PoseStack.Pose pose = poseStack.last();
        addBeamVertex(consumer, pose, -cornerZ, yStart,          cornerX, red, green, blue, alpha, 0f, uvBottom);
        addBeamVertex(consumer, pose,  cornerX, yStart,          cornerZ, red, green, blue, alpha, 1f, uvBottom);
        addBeamVertex(consumer, pose,  cornerX, yStart + height, cornerZ, red, green, blue, alpha, 1f, uvTop);
        addBeamVertex(consumer, pose, -cornerZ, yStart + height, cornerX, red, green, blue, alpha, 0f, uvTop);
    }

    private static void addBeamVertex(VertexConsumer consumer, PoseStack.Pose pose,
                                      float x, float y, float z,
                                      float red, float green, float blue, float alpha,
                                      float u, float v) {
        consumer.vertex(pose.pose(), x, y, z)
                .color(red, green, blue, alpha)
                .uv(u, v)
                .overlayCoords(0, 10)
                .uv2(0xF000F0)
                .normal(pose.normal(), 0f, 1f, 0f)
                .endVertex();
    }
}
