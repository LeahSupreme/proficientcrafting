package net.leahperson.proficientmod.block.entity;

import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.attribute.ItemQualityData;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.quality.QualityDataType;
import net.leahperson.proficientmod.quality.RarityTagDefault;
import net.leahperson.proficientmod.quality.QualityUtils;
import net.leahperson.proficientmod.recipe.util.SimpleItemContainer;
import net.leahperson.proficientmod.recipe.JewelcraftingRecipe;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.leahperson.proficientmod.util.RarityAttributeNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.RecipeMatcher;
import java.util.*;
import java.util.stream.Collectors;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class JewelcraftingStationBlockEntity extends BlockEntity {
    private static final int INPUT_SLOTS = 9;
    private final NonNullList<ItemStack> inputStacks = NonNullList.withSize(INPUT_SLOTS, ItemStack.EMPTY);
    private ItemStack outputStack = ItemStack.EMPTY;
    private int progress = 0;
    private int maxProgress = 0;
    private boolean crafting = false;
    private int capturedQuality = 0;
    private int capturedYield = 0;
    private UUID capturedPlayerUUID = null;

    public JewelcraftingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JEWELCRAFTING_STATION_BE.get(), pos, state);
    }

    public NonNullList<ItemStack> getInputStacks() { return inputStacks; }
    public ItemStack getOutputStack() { return outputStack; }
    public boolean hasOutput() { return !outputStack.isEmpty(); }
    public boolean isCrafting() { return crafting; }
    public int getProgress() { return progress; }
    public int getMaxProgress() { return maxProgress; }

    public boolean addInput(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (hasOutput() || crafting) {
            return false;
        }
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
        if (outputStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = outputStack.copy();
        outputStack = ItemStack.EMPTY;
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
                setChangedAndSync();
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean startCraft(Level level, Player player) {
        if (level == null || level.isClientSide) {
            return false;
        }
        if (crafting || !outputStack.isEmpty()) {
            return false;
        }
        ItemStack[] copy = new ItemStack[INPUT_SLOTS];
        for (int i = 0; i < INPUT_SLOTS; i++) {
            copy[i] = inputStacks.get(i).copy();
        }
        SimpleItemContainer container = new SimpleItemContainer(copy);
        Optional<JewelcraftingRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.JEWELCRAFTING.get())
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
        if (recipeOpt.isEmpty()) {
            return false;
        }
        JewelcraftingRecipe recipe = recipeOpt.get();
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
        this.capturedQuality = (int) player.getAttributeValue(ModAttributes.QUALITY.get());
        this.capturedYield = (int) player.getAttributeValue(ModAttributes.YIELD.get());
        this.capturedPlayerUUID = player.getUUID();
        if (!recipe.getQualityPerIngredient().isEmpty()) {
            List<ItemStack> inputs = new ArrayList<>();
            for (int i = 0; i < INPUT_SLOTS; i++) {
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
                    this.capturedQuality += recipe.getQualityPerIngredient().get(rarityIndex);
                }
            }
        }
        this.maxProgress = recipe.getCraftTime();
        if (this.maxProgress == 0) {
            finishCraft((ServerLevel) level);
            return true;
        }
        this.crafting = true;
        this.progress = 0;
        setChangedAndSync();
        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, JewelcraftingStationBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }
        if (!blockEntity.crafting) {
            return;
        }
        blockEntity.progress++;
        if (blockEntity.progress >= blockEntity.maxProgress) {
            blockEntity.finishCraft((ServerLevel) level);
        }
        blockEntity.setChangedAndSync();
    }

    private void finishCraft(ServerLevel level) {
        ItemStack[] copy = new ItemStack[INPUT_SLOTS];
        for (int i = 0; i < INPUT_SLOTS; i++) {
            copy[i] = inputStacks.get(i).copy();
        }
        SimpleItemContainer container = new SimpleItemContainer(copy);
        Optional<JewelcraftingRecipe> recipeOpt = level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.JEWELCRAFTING.get())
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
        if (recipeOpt.isEmpty()) {
            resetCrafting();
            return;
        }
        JewelcraftingRecipe recipe = recipeOpt.get();
        ItemStack result;
        if (recipe.hasQualityOutputs()) {
            result = recipe.getOutputForQuality(capturedQuality, level.getRandom());
        } else {
            result = recipe.assemble(container, level.registryAccess());
            int rarity = recipe.getProficiencyRequired() > 0 ? 1 : 0;
            QualityDataUtil.setRarity(result, rarity);
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
        outputStack = result;
        level.playSound(null, worldPosition, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                worldPosition.getX() + 0.5,
                worldPosition.getY() + 1.1,
                worldPosition.getZ() + 0.5,
                10, 0.25, 0.15, 0.25, 0.01);
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
                .flatMap(reg -> reg.stream()
                        .filter(e -> e.item_id().equals(itemIdStr))
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
