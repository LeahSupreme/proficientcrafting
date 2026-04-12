package net.leahperson.proficientmod.mixin;

import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class FurnaceMixin {

    @Inject(method = "canBurn", at = @At("RETURN"), cancellable = true)
    private void preventQualityMismatch(RegistryAccess registryAccess, Recipe<?> recipe,
            NonNullList<ItemStack> items, int maxStackSize,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!callbackInfoReturnable.getReturnValue()) {
            return;
        }
        ItemStack inputStack = items.get(0);
        ItemStack outputStack = items.get(2);
        if (!outputStack.isEmpty() && QualityDataUtil.getRarity(inputStack) != QualityDataUtil.getRarity(outputStack)) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "serverTick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;setRecipeUsed(Lnet/minecraft/world/item/crafting/Recipe;)V"
    ))
    private static void applyQualityOnSmeltComplete(
            Level level, BlockPos pos, BlockState state,
            AbstractFurnaceBlockEntity furnace, CallbackInfo callbackInfo) {
        int inputRarity = QualityDataUtil.getRarity(furnace.getItem(0));
        if (inputRarity > 0) {
            QualityDataUtil.setRarity(furnace.getItem(2), inputRarity);
        }
    }
}
