package net.leahperson.proficientmod.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.nbt.QualityType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeHelper;

import java.rmi.registry.Registry;

public class RarityBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final RarityBEWLR INSTANCE = new RarityBEWLR();
    private static final String DEFAULT_RARITY = "qualitycrafting:rarity2";

    public RarityBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        int rarityIndex = RarityItem.getRarityType(stack);
        BakedModel rarityModel = getRarityModel(rarityIndex);
        // render the item using the computed model
        renderItem(rarityModel, stack, itemDisplayContext, poseStack, buffer, packedLight, packedOverlay);
    }

    public static BakedModel getRarityModel(int rarityIndex) {

        //ModelResourceLocation textureLocation = (ModelResourceLocation) Minecraft.getInstance().level.registryAccess().registryOrThrow(QualityType.RARITY_REGISTRY).stream().filter(elem -> elem.index() == rarityIndex).findFirst().get().icon();

        ModelResourceLocation textureLocation = ModelResourceLocation.vanilla(ProficientMod.MOD_ID,"item/rarity3");
        //textureLocation = ResourceLocation.withDefaultNamespace("item/rarity3");
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(textureLocation);
        if (model == null) {
            // model not found, defaults to the missing model
            model = Minecraft.getInstance().getModelManager().getMissingModel();
        }
        return model;
    }

    public void renderItem(BakedModel model, ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        // https://gist.github.com/XFactHD/11ccae6a54da62909caf6d555cd4d8b9
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        poseStack.popPose();
        poseStack.pushPose();

        model = model.applyTransform(itemDisplayContext, poseStack, isLeftHand(itemDisplayContext));
        poseStack.translate(-.5, -.5, -.5);

        boolean glint = stack.hasFoil();
        for (RenderType type : model.getRenderTypes(stack, true)) {
            type = RenderTypeHelper.getEntityRenderType(type, true);
            VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, type, true, glint);
            renderer.renderModelLists(model, stack, packedLight, packedOverlay, poseStack, consumer);
        }

    }
    private static boolean isLeftHand(ItemDisplayContext itemDisplayContext) {
        return itemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || itemDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }
}
