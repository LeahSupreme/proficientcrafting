package net.leahperson.proficientmod.block.entity;

import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.recipe.ForgingRecipe;
import net.leahperson.proficientmod.recipe.ForgingRecipeContainer;
import net.leahperson.proficientmod.recipe.ForgingTableRecipe;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import java.util.*;

public class ForgingTableBlockEntity extends BlockEntity {
    private static final int INPUT_SLOTS = 9;

    private final NonNullList<ItemStack> inputStacks = NonNullList.withSize(INPUT_SLOTS, ItemStack.EMPTY);
    private ItemStack outputStack = ItemStack.EMPTY;

    private int progress = 0;
    private int maxProgress = 0;
    private boolean crafting = false;

    public ForgingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FORGING_TABLE_BE.get(), pos, state);
    }

    public NonNullList<ItemStack> getInputStacks() {
        return inputStacks;
    }

    public ItemStack getOutputStack() {
        return outputStack;
    }

    public boolean hasOutput() {
        return !outputStack.isEmpty();
    }

    public boolean isCrafting() {
        return crafting;
    }

    public boolean addInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (hasOutput() || crafting) return false;

        for (int i = 0; i < inputStacks.size(); i++) {
            if (inputStacks.get(i).isEmpty()) {
                ItemStack one = stack.copy();
                one.setCount(1);
                inputStacks.set(i, one);
                setChangedAndSync();
                return true;
            }
        }

        return false;
    }

    public ItemStack removeOutput() {
        if (outputStack.isEmpty()) return ItemStack.EMPTY;
        ItemStack copy = outputStack.copy();
        outputStack = ItemStack.EMPTY;
        setChangedAndSync();
        return copy;
    }

    public ItemStack removeLastInput() {
        if (crafting) return ItemStack.EMPTY;

        for (int i = inputStacks.size() - 1; i >= 0; i--) {
            ItemStack stack = inputStacks.get(i);
            if (!stack.isEmpty()) {
                inputStacks.set(i, ItemStack.EMPTY);
                setChangedAndSync();
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    public boolean startCraft(Level level) {
        if (level == null || level.isClientSide) return false;
        if (crafting || !outputStack.isEmpty()) return false;

        ItemStack[] copy = new ItemStack[INPUT_SLOTS];
        for (int i = 0; i < INPUT_SLOTS; i++) {
            copy[i] = inputStacks.get(i).copy();
        }

        ForgingRecipeContainer container = new ForgingRecipeContainer(copy);

        Optional<ForgingRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(net.leahperson.proficientmod.registry.ModRecipeTypes.FORGING.get())
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();

        if (recipeOpt.isEmpty()) {
            return false;
        }

        ForgingRecipe recipe = recipeOpt.get();

        this.crafting = true;
        this.progress = 0;
        this.maxProgress = recipe.getCraftTime();

        setChangedAndSync();
        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ForgingTableBlockEntity be) {
        if (level.isClientSide) return;
        if (!be.crafting) return;

        be.progress++;

        if (be.progress >= be.maxProgress) {
            be.finishCraft((ServerLevel) level);
        }

        be.setChangedAndSync();
    }

    private void finishCraft(ServerLevel level) {
        ItemStack[] copy = new ItemStack[INPUT_SLOTS];
        for (int i = 0; i < INPUT_SLOTS; i++) {
            copy[i] = inputStacks.get(i).copy();
        }

        ForgingRecipeContainer container = new ForgingRecipeContainer(copy);

        Optional<ForgingRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(net.leahperson.proficientmod.registry.ModRecipeTypes.FORGING.get())
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();

        if (recipeOpt.isEmpty()) {
            resetCrafting();
            return;
        }

        ForgingRecipe recipe = recipeOpt.get();
        ItemStack result = recipe.assemble(container, level.registryAccess());

        int rarity = recipe.getMinQuality() > 0 ? 1 : 0;
        QualityDataUtil.setRarity(result, rarity);

        for (int i = 0; i < inputStacks.size(); i++) {
            inputStacks.set(i, ItemStack.EMPTY);
        }

        outputStack = result;

        level.playSound(null, worldPosition, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 1.1,
                worldPosition.getZ() + 0.5,
                10,
                0.25, 0.15, 0.25,
                0.01);

        resetCrafting();
        setChangedAndSync();
    }

    private void resetCrafting() {
        this.crafting = false;
        this.progress = 0;
        this.maxProgress = 0;
    }

    private void setChangedAndSync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        for (int i = 0; i < inputStacks.size(); i++) {
            CompoundTag stackTag = new CompoundTag();
            inputStacks.get(i).save(stackTag);
            tag.put("Input" + i, stackTag);
        }

        CompoundTag outputTag = new CompoundTag();
        outputStack.save(outputTag);
        tag.put("Output", outputTag);

        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putBoolean("Crafting", crafting);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        for (int i = 0; i < inputStacks.size(); i++) {
            inputStacks.set(i, ItemStack.of(tag.getCompound("Input" + i)));
        }

        outputStack = ItemStack.of(tag.getCompound("Output"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        crafting = tag.getBoolean("Crafting");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}