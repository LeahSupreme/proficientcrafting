package net.leahperson.proficientmod.block.entity;

import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.quality.QualityDataType;
import net.leahperson.proficientmod.quality.RarityTagDefault;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.recipe.CookingRecipe;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.leahperson.proficientmod.util.RarityAttributeNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.RecipeMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CookingPotBlockEntity extends BlockEntity {
    private static final int MAX_INPUTS = 8;

    private final NonNullList<ItemStack> inputStacks = NonNullList.withSize(MAX_INPUTS, ItemStack.EMPTY);
    private ItemStack outputStack = ItemStack.EMPTY;

    private int progress = 0;
    private int maxProgress = 0;
    private boolean crafting = false;
    private int capturedQuality = 0;
    private int capturedYield = 0;
    private UUID capturedPlayerUUID = null;
    private long[] inputPlacedTimes = new long[MAX_INPUTS];
    private long outputAppearTime = 0;
    private long craftStartTime = 0;

    public CookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COOKING_POT_BE.get(), pos, state);
    }

    public NonNullList<ItemStack> getInputStacks() {
        return inputStacks;
    }

    public long[] getInputPlacedTimes() {
        return inputPlacedTimes;
    }

    public long getOutputAppearTime() {
        return outputAppearTime;
    }

    public long getCraftStartTime() {
        return craftStartTime;
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

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public boolean addInput(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (hasOutput() || crafting) {
            return false;
        }

        for (int i = 0; i < inputStacks.size(); i++) {
            if (inputStacks.get(i).isEmpty()) {
                ItemStack singleStack = stack.copy();
                singleStack.setCount(1);
                inputStacks.set(i, singleStack);
                inputPlacedTimes[i] = getLevel().getGameTime();
                setChangedAndSync();
                return true;
            }
        }
        return false;
    }

    public ItemStack removeOutput() {
        if (outputStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = outputStack.copy();
        outputStack = ItemStack.EMPTY;
        outputAppearTime = 0;
        setChangedAndSync();
        return copy;
    }

    public ItemStack removeLastInput() {
        if (crafting) {
            return ItemStack.EMPTY;
        }

        for (int i = inputStacks.size() - 1; i >= 0; i--) {
            ItemStack stack = inputStacks.get(i);
            if (!stack.isEmpty()) {
                inputStacks.set(i, ItemStack.EMPTY);
                inputPlacedTimes[i] = 0;
                setChangedAndSync();
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean startCraft(Level level, Player player) {
        if (crafting || !outputStack.isEmpty()) {
            return false;
        }
        Optional<CookingRecipe> recipeOpt = findMatchingRecipe(level, inputStacks);
        if (recipeOpt.isEmpty()) {
            return false;
        }

        CookingRecipe recipe = recipeOpt.get();

        int requiredProficiency = recipe.getProficiencyRequired();
        if (requiredProficiency > 0) {
            int playerProficiency = (int) player.getAttributeValue(ModAttributes.PROFICIENCY.get());
            if (playerProficiency < requiredProficiency) {
                player.displayClientMessage(Component.translatable(
                        "qualitycrafting.station.notproficient",
                        requiredProficiency, playerProficiency), true);
                return false;
            }
        }

        capturedQuality = (int) player.getAttributeValue(ModAttributes.QUALITY.get());
        capturedYield = (int) player.getAttributeValue(ModAttributes.YIELD.get());
        capturedPlayerUUID = player.getUUID();

        if (!recipe.getQualityPerIngredient().isEmpty()) {
            List<ItemStack> inputs = new ArrayList<>();
            for (int i = 0; i < MAX_INPUTS; i++) {
                if (!inputStacks.get(i).isEmpty()) {
                    inputs.add(inputStacks.get(i));
                }
            }
            int[] matches = RecipeMatcher.findMatches(inputs, recipe.getInputItems());
            if (matches != null) {
                for (int inputIndex : matches) {
                    if (inputIndex < 0 || inputIndex >= inputs.size()) {
                        continue;
                    }
                    int itemQuality = QualityUtils.getQualityLevel(inputs.get(inputIndex));
                    if (itemQuality <= 0) {
                        continue;
                    }
                    int rarityIndex = itemQuality - 1;
                    if (rarityIndex >= recipe.getQualityPerIngredient().size()) {
                        continue;
                    }
                    capturedQuality += recipe.getQualityPerIngredient().get(rarityIndex);
                }
            }
        }

        maxProgress = recipe.getCookTime();
        crafting = true;
        progress = 0;
        craftStartTime = level.getGameTime();
        setChangedAndSync();
        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CookingPotBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }
        if (!blockEntity.crafting) {
            return;
        }

        blockEntity.progress++;
        if (blockEntity.progress >= blockEntity.maxProgress) {
            blockEntity.finishCraft(level);
        }
        blockEntity.setChangedAndSync();
    }

    private void finishCraft(Level level) {
        Optional<CookingRecipe> recipeOpt = findMatchingRecipe(level, inputStacks);
        if (recipeOpt.isEmpty()) {
            resetCrafting();
            return;
        }

        CookingRecipe recipe = recipeOpt.get();
        ItemStack result;
        if (recipe.hasQualityOutputs()) {
            result = recipe.getOutputForQuality(capturedQuality, level.getRandom());
        } else {
            result = recipe.assemble(makeContainer(), level.registryAccess());
        }

        if (capturedPlayerUUID != null && recipe.getLevelCost() > 0) {
            Player player = level.getPlayerByUUID(capturedPlayerUUID);
            if (player != null && !player.isCreative()) {
                player.giveExperienceLevels(-recipe.getLevelCost());
            }
        }

        int rarity = QualityDataUtil.getRarity(result);
        if (rarity > 0) {
            List<AttributeAddition> additions = resolveAttributes(result, rarity, level);
            if (!additions.isEmpty()) {
                RarityAttributeNBT.setAttributes(result, additions);
            }
        }

        if (recipe.getYieldCost() > 0 && recipe.getYieldAdded() > 0) {
            int bonusCount = (capturedYield / recipe.getYieldCost()) * recipe.getYieldAdded();
            if (bonusCount > 0) {
                result.grow(Math.min(bonusCount, result.getMaxStackSize() - result.getCount()));
            }
        }

        Collections.fill(inputStacks, ItemStack.EMPTY);
        Arrays.fill(inputPlacedTimes, 0);
        outputStack = result;
        outputAppearTime = level.getGameTime();

        level.playSound(null, worldPosition, SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, SoundSource.BLOCKS, 1.0F, 1.2F);
        level.playSound(null, worldPosition, SoundEvents.GENERIC_BURN, SoundSource.BLOCKS, 1.0F, 1.2F);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 1.25, worldPosition.getZ() + 0.5,
                    12, 0.2, 0.1, 0.2, 0.02);
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 1.25, worldPosition.getZ() + 0.5,
                    6, 0.15, 0.05, 0.15, 0.01);
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 1.25, worldPosition.getZ() + 0.5,
                    10, 0.25, 0.15, 0.25, 0.01);
        }

        resetCrafting();
        setChangedAndSync();
    }

    private static List<AttributeAddition> resolveAttributes(ItemStack stack, int rarity, Level level) {
        RegistryAccess registryAccess = level.registryAccess();
        ResourceLocation itemId = stack.getItem().builtInRegistryHolder().key().location();
        String itemIdStr = itemId.getNamespace() + ":" + itemId.getPath();
        int rarityIndex = rarity - 1;

        Optional<ItemQualityData> itemEntry = registryAccess
                .registry(ItemQualityData.REGISTRY)
                .flatMap(registry -> registry.stream()
                        .filter(entry -> entry.item_id().equals(itemIdStr))
                        .findAny());
        if (itemEntry.isPresent() && itemEntry.get().rarities().isPresent()) {
            List<List<AttributeAddition>> rarities = itemEntry.get().rarities().get();
            if (rarityIndex < rarities.size()) {
                return rarities.get(rarityIndex);
            }
        }

        Optional<net.minecraft.core.Registry<QualityDataType>> rarityRegistry =
                registryAccess.registry(QualityDataType.RARITY_REGISTRY);
        if (rarityRegistry.isPresent()) {
            Optional<QualityDataType> tier = rarityRegistry.get().stream()
                    .filter(qualityDataType -> qualityDataType.index() == rarity)
                    .findFirst();
            if (tier.isPresent() && tier.get().tagDefaults().isPresent()) {
                Set<TagKey<Item>> itemTags = stack.getTags().collect(Collectors.toSet());
                for (RarityTagDefault tagDefault : tier.get().tagDefaults().get()) {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, ResourceLocation.parse(tagDefault.tag()));
                    if (itemTags.contains(tagKey)) {
                        return tagDefault.attributes();
                    }
                }
            }
        }

        return List.of();
    }

    private static Optional<CookingRecipe> findMatchingRecipe(Level level, NonNullList<ItemStack> stacks) {
        final ItemStack[] inputArray = stacks.toArray(new ItemStack[0]);
        Container container = new Container() {
            @Override
            public int getContainerSize() {
                return inputArray.length;
            }

            @Override
            public boolean isEmpty() {
                for (ItemStack inputStack : inputArray) {
                    if (!inputStack.isEmpty()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public ItemStack getItem(int slot) {
                return inputArray[slot];
            }

            @Override
            public ItemStack removeItem(int slot, int amount) {
                return ItemStack.EMPTY;
            }

            @Override
            public ItemStack removeItemNoUpdate(int slot) {
                return ItemStack.EMPTY;
            }

            @Override
            public void setItem(int slot, ItemStack stack) {
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
            }
        };

        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.COOKING.get())
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    private Container makeContainer() {
        final ItemStack[] inputArray = inputStacks.toArray(new ItemStack[0]);
        return new Container() {
            @Override
            public int getContainerSize() {
                return inputArray.length;
            }

            @Override
            public boolean isEmpty() {
                for (ItemStack inputStack : inputArray) {
                    if (!inputStack.isEmpty()) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public ItemStack getItem(int slot) {
                return inputArray[slot];
            }

            @Override
            public ItemStack removeItem(int slot, int amount) {
                return ItemStack.EMPTY;
            }

            @Override
            public ItemStack removeItemNoUpdate(int slot) {
                return ItemStack.EMPTY;
            }

            @Override
            public void setItem(int slot, ItemStack stack) {
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
            }
        };
    }

    private void resetCrafting() {
        crafting = false;
        progress = 0;
        maxProgress = 0;
        capturedQuality = 0;
        capturedYield = 0;
        capturedPlayerUUID = null;
        craftStartTime = 0;
    }

    private void setChangedAndSync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
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
        tag.putInt("CapturedQuality", capturedQuality);
        tag.putInt("CapturedYield", capturedYield);
        if (capturedPlayerUUID != null) {
            tag.putUUID("PlayerUUID", capturedPlayerUUID);
        }
        tag.putLongArray("InputPlacedTimes", inputPlacedTimes);
        tag.putLong("OutputAppearTime", outputAppearTime);
        tag.putLong("CraftStartTime", craftStartTime);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        for (int i = 0; i < inputStacks.size(); i++) {
            inputStacks.set(i, ItemStack.of(tag.getCompound("Input" + i)));
        }
        outputStack = ItemStack.of(tag.getCompound("Output"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        crafting = tag.getBoolean("Crafting");
        capturedQuality = tag.getInt("CapturedQuality");
        capturedYield = tag.getInt("CapturedYield");
        capturedPlayerUUID = tag.hasUUID("PlayerUUID") ? tag.getUUID("PlayerUUID") : null;
        if (tag.contains("InputPlacedTimes")) {
            long[] loaded = tag.getLongArray("InputPlacedTimes");
            System.arraycopy(loaded, 0, inputPlacedTimes, 0, Math.min(loaded.length, MAX_INPUTS));
        }
        outputAppearTime = tag.getLong("OutputAppearTime");
        craftStartTime = tag.getLong("CraftStartTime");
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
