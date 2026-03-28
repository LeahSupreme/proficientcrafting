package net.leahperson.proficientmod.enchantment;

import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class CraftingToolEnchantment extends Enchantment {
    private static final EnchantmentCategory CRAFTING_TOOL_CATEGORY = EnchantmentCategory.create(
            "crafting_tool", item -> false
    );

    protected CraftingToolEnchantment() {
        super(Rarity.UNCOMMON, CRAFTING_TOOL_CATEGORY, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 25;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(ModTags.Items.FORGING_HAMMER)
                || stack.is(ModTags.Items.COOKING_LADLE)
                || stack.is(ModTags.Items.SCRIBING_QUILL)
                || stack.is(ModTags.Items.JEWEL_CHISEL)
                || stack.is(ModTags.Items.WOODWORKING_SAW)
                || stack.is(ModTags.Items.REFORGING_SCEPTER);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canEnchant(stack);
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof CraftingToolEnchantment) || other.getClass() != this.getClass();
    }
}
