package net.leahperson.proficientmod.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ForgingRecipeContainer implements Container {
    private final ItemStack[] stacks;

    public ForgingRecipeContainer(ItemStack[] stacks) {
        this.stacks = stacks;
    }

    @Override
    public int getContainerSize() {
        return stacks.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return stacks[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = stacks[slot];
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack split = stack.split(amount);
        if (stack.getCount() <= 0) {
            stacks[slot] = ItemStack.EMPTY;
        }
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = stacks[slot];
        stacks[slot] = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        stacks[slot] = stack;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = ItemStack.EMPTY;
        }
    }
}