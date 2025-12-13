package net.leahperson.proficientmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import net.leahperson.proficientmod.block.entity.ForgingTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.Properties;

public class ForgingTableBlockEntityRenderer implements BlockEntityRenderer<ForgingTableBlockEntity> {

    public ForgingTableBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    static final float DIST = 0.2f;
    static final float BLOCK_HEIGHT = 0.13f;
    static final float ITEM_HEIGHT = 0.0225f;

    static final float[] POS_ARRAY = {
            0.5f,1.025f,0.5f,
            1-DIST,1.025f,0.5f,
            DIST,1.025f,0.5f,
            0.5f,1.025f,1-DIST,
            0.5f,1.025f,DIST,
            DIST,1.025f,DIST,
            DIST,1.025f,1-DIST,
            1-DIST,1.025f,DIST,
            1-DIST,1.025f,1-DIST,
    };

    @Override
    public void render(ForgingTableBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        List<ItemStack> items = pBlockEntity.getRenderItems();



        float yRot = pBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING).toYRot();
        float heightStacked = 0;

        for(int i = 0; i < items.size();i++){

            ItemStack itemStack = items.get(i);

                //BlockItem b = (BlockItem)itemStack.getItem();
            boolean isBlock = (itemStack.getItem() instanceof  BlockItem);




                pPoseStack.pushPose();
                //pPoseStack.translate(POS_ARRAY[i*3],POS_ARRAY[i*3+1],POS_ARRAY[i*3+2]);
                pPoseStack.translate(0.5f, 1.0f+(heightStacked)+(isBlock?BLOCK_HEIGHT/2:ITEM_HEIGHT/2), 0.5f);

                if(isBlock){
                    pPoseStack.scale(0.3f, 0.3f, 0.3f);
                }else{

                    pPoseStack.scale(0.3f, 0.3f, 0.3f);
                }
                pPoseStack.mulPose(Axis.XP.rotationDegrees(90));

                pPoseStack.mulPose(Axis.ZP.rotationDegrees(270+yRot-(30*i)));
                itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                        OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
                pPoseStack.popPose();

                if(isBlock){
                    heightStacked+=BLOCK_HEIGHT*1.1f;
                }else{
                    heightStacked+=ITEM_HEIGHT;
                }




        }





    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
