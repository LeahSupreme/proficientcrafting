package net.leahperson.proficientmod.util;

import net.leahperson.proficientmod.quality.QualityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class OverlayUtils {


    public static ItemStack getOverlay(final ItemStack stack) {




        return QualityUtils.getRarityItem(QualityUtils.getQualityLevel(stack), Minecraft.getInstance().level);


        //return ItemStack.EMPTY;
    }

    /*public static ItemStack getOverlay(final int ordinal) {
        return getOverlay(Quality.get(ordinal));
    }

    public static ItemStack getOverlay(final Quality quality) {
        return switch (quality) {
            case IRON -> {
                if (IRON_OVERLAY == null) {
                    IRON_OVERLAY = new ItemStack(QFItems.IRON_OVERLAY.get());
                }

                yield IRON_OVERLAY;
            }
            case GOLD -> {
                if (GOLD_OVERLAY == null) {
                    GOLD_OVERLAY = new ItemStack(QFItems.GOLD_OVERLAY.get());
                }

                yield GOLD_OVERLAY;
            }
            case DIAMOND -> {
                if (DIAMOND_OVERLAY == null) {
                    DIAMOND_OVERLAY = new ItemStack(QFItems.DIAMOND_OVERLAY.get());
                }

                yield DIAMOND_OVERLAY;
            }
            default -> ItemStack.EMPTY;
        };
    }*/

    public static boolean isOverlay(final ItemStack stack) {
        //return false;
        return QualityUtils.isRarityItem(stack,Minecraft.getInstance().level);
    }
}