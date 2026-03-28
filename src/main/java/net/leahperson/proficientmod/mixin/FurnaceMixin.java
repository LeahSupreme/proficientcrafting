package net.leahperson.proficientmod.mixin;

import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class FurnaceMixin {
    @Unique
    private static final ThreadLocal<Integer> PENDING_RARITY = ThreadLocal.withInitial(() -> 0);

    @Inject(method = "burn", at = @At("HEAD"))
    private void captureInputRarity(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        PENDING_RARITY.set(QualityDataUtil.getRarity(items.get(0)));
    }

    @Inject(method = "burn", at = @At("TAIL"))
    private void applyRarityToOutput(RegistryAccess registryAccess, Recipe<?> recipe, NonNullList<ItemStack> items, int maxStackSize, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        int rarity = PENDING_RARITY.get();
        PENDING_RARITY.set(0);
        if (callbackInfoReturnable.getReturnValue() && rarity > 0) {
            QualityDataUtil.setRarity(items.get(2), rarity);
        }
    }
}
