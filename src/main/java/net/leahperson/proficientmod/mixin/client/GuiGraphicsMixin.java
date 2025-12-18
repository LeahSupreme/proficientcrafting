package net.leahperson.proficientmod.mixin.client;


import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.item.ModItems;
import net.leahperson.proficientmod.nbt.QualityType;
import net.leahperson.proficientmod.nbt.RarityNBT;
import net.leahperson.proficientmod.util.OverlayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow @Final private PoseStack pose;

    @Inject(method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.AFTER))
    private void qualitycrafting$renderIcon(final LivingEntity entity, final Level level, final ItemStack stack, final int x, final int y, final int seed, final int guiOffset, final CallbackInfo callback, @Local final BakedModel model) {

        GuiGraphics instance = (GuiGraphics) (Object) this;
        instance.pose().pushPose();
        instance.pose().translate(0, 0, 200 + (model.isGui3d() ? guiOffset : 0));

        //ResourceLocation myResource = ResourceLocation.tryBuild(ProficientMod.MOD_ID, "item/" + ModItems.RARITYITEM.getId().getPath());

        /*ResourceLocation myResource = level.registryAccess().registryOrThrow(QualityType.RARITY_REGISTRY).stream().min((elema,elemb)->{
            return elema.index()-elemb.index();
        }).get().icon();*/

        ResourceLocation resourceLocation = new ResourceLocation(ProficientMod.MOD_ID,"item/rarity1");
        //ResourceLocation myResource = ResourceLocation.fromNamespaceAndPath(ProficientMod.MOD_ID,"item/rarity1");

        //LogUtils.getLogger().info(myResource.toString());

        //instance.blit(x,y,0,16,16,Minecraft.getInstance().getModelManager().getAtlas(myResource).getSprite(myResource));

        instance.blit(resourceLocation,x,y,0,0,16,16);
        instance.pose().popPose();
    }

    @Shadow
    protected abstract void renderItem(@Nullable LivingEntity entity, @Nullable Level level, ItemStack stack, int x, int y, int seed, int guiOffset);
}
