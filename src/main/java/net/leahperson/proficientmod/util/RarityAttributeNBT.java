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
    public static final String TAG_REFORGING_ATTRIBUTES = "reforging_attributes";
    private static final String TAG_ID = "id";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_OP = "operation";

    private RarityAttributeNBT() {}

    public static boolean hasAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        return modTag != null && modTag.contains(TAG_RARITY_ATTRIBUTES, Tag.TAG_LIST);
    }

    public static List<AttributeAddition> getAttributes(ItemStack stack) {
        return readList(stack, TAG_RARITY_ATTRIBUTES);
    }

    public static void setAttributes(ItemStack stack, List<AttributeAddition> additions) {
        writeList(stack, TAG_RARITY_ATTRIBUTES, additions);
    }

    public static void addAttribute(ItemStack stack, AttributeAddition addition) {
        List<AttributeAddition> current = new ArrayList<>(getAttributes(stack));
        current.add(addition);
        setAttributes(stack, current);
    }

    public static void clearAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        if (modTag != null) {
            modTag.remove(TAG_RARITY_ATTRIBUTES);
        }
    }

    public static boolean hasReforgingAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        return modTag != null && modTag.contains(TAG_REFORGING_ATTRIBUTES, Tag.TAG_LIST);
    }

    public static List<AttributeAddition> getReforgingAttributes(ItemStack stack) {
        return readList(stack, TAG_REFORGING_ATTRIBUTES);
    }

    public static void setReforgingAttributes(ItemStack stack, List<AttributeAddition> additions) {
        writeList(stack, TAG_REFORGING_ATTRIBUTES, additions);
    }

    public static void clearReforgingAttributes(ItemStack stack) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        if (modTag != null) {
            modTag.remove(TAG_REFORGING_ATTRIBUTES);
        }
    }

    private static List<AttributeAddition> readList(ItemStack stack, String key) {
        CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
        if (modTag == null || !modTag.contains(key, Tag.TAG_LIST)) {
            return Collections.emptyList();
        }
        ListTag list = modTag.getList(key, Tag.TAG_COMPOUND);
        List<AttributeAddition> result = new ArrayList<>(list.size());
        for (int index = 0; index < list.size(); index++) {
            CompoundTag entry = list.getCompound(index);
            result.add(new AttributeAddition(
                    entry.getString(TAG_ID),
                    entry.getDouble(TAG_AMOUNT),
                    entry.getString(TAG_OP)));
        }
        return result;
    }

    private static void writeList(ItemStack stack, String key, List<AttributeAddition> additions) {
        if (additions.isEmpty()) {
            CompoundTag modTag = stack.getTagElement(ProficientMod.MOD_ID);
            if (modTag != null) {
                modTag.remove(key);
            }
            return;
        }
        ListTag list = new ListTag();
        for (AttributeAddition addition : additions) {
            CompoundTag entry = new CompoundTag();
            entry.putString(TAG_ID, addition.attribute_id());
            entry.putDouble(TAG_AMOUNT, addition.amount());
            entry.putString(TAG_OP, addition.operation());
            list.add(entry);
        }
        stack.getOrCreateTagElement(ProficientMod.MOD_ID).put(key, list);
    }
}
