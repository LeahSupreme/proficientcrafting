package net.leahperson.proficientmod.util;

import net.leahperson.proficientmod.ProficientMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class QualityDataUtil {
    public static final String TAG_QUALITY = "quality";

    private QualityDataUtil() {
    }

    public static int getRarity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        if (!tag.contains(ProficientMod.MOD_ID)) return 0;
        return tag.getCompound(ProficientMod.MOD_ID).getInt(TAG_QUALITY);
    }

    public static void setRarity(ItemStack stack, int rarity) {
        stack.getOrCreateTagElement(ProficientMod.MOD_ID).putInt(TAG_QUALITY, rarity);
    }
}
