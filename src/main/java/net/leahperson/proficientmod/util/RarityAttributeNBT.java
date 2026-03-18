package net.leahperson.proficientmod.util;

import net.leahperson.proficientmod.ProficientMod;
import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RarityAttributeNBT {
    public static final String TAG_RARITY_ATTRIBUTES = "rarity_attributes";
    private static final String TAG_ID = "id";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_OP = "operation";

    private RarityAttributeNBT() {}

    public static boolean hasAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        return modTag != null && modTag.contains(TAG_RARITY_ATTRIBUTES, Tag.TAG_LIST);
    }

    public static List<AttributeAddition> getAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        if (modTag == null || !modTag.contains(TAG_RARITY_ATTRIBUTES, Tag.TAG_LIST)) return Collections.emptyList();
        ListTag list = modTag.getList(TAG_RARITY_ATTRIBUTES, Tag.TAG_COMPOUND);
        List<AttributeAddition> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            result.add(new AttributeAddition(
                    entry.getString(TAG_ID),
                    entry.getDouble(TAG_AMOUNT),
                    entry.getString(TAG_OP)));
        }
        return result;
    }

    public static void setAttributes(ItemStack stack, List<AttributeAddition> additions) {
        if (additions.isEmpty()) {
            clearAttributes(stack);
            return;
        }
        ListTag list = new ListTag();
        for (AttributeAddition a : additions) {
            CompoundTag entry = new CompoundTag();
            entry.putString(TAG_ID, a.attribute_id());
            entry.putDouble(TAG_AMOUNT, a.amount());
            entry.putString(TAG_OP, a.operation());
            list.add(entry);
        }
        stack.getOrCreateTagElement(ProficientMod.MOD_ID).put(TAG_RARITY_ATTRIBUTES, list);
    }

    public static void addAttribute(ItemStack stack, AttributeAddition addition) {
        List<AttributeAddition> current = new ArrayList<>(getAttributes(stack));
        current.add(addition);
        setAttributes(stack, current);
    }

    public static void clearAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        if (modTag != null) modTag.remove(TAG_RARITY_ATTRIBUTES);
    }
}
