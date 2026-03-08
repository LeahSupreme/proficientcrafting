package net.leahperson.proficientmod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class QualityDataUtil {
    public static final String TAG_RARITY = "qc_rarity";

    private QualityDataUtil() {
    }

    public static int getRarity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        return tag.getInt(TAG_RARITY);
    }

    public static void setRarity(ItemStack stack, int rarity) {
        stack.getOrCreateTag().putInt(TAG_RARITY, rarity);
    }
}