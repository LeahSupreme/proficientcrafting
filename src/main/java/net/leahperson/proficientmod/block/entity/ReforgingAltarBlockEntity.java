package net.leahperson.proficientmod.block.entity;

import net.leahperson.proficientmod.attribute.AttributeAddition;
import net.leahperson.proficientmod.attribute.ModAttributes;
import net.leahperson.proficientmod.recipe.util.SimpleItemContainer;
import net.leahperson.proficientmod.recipe.ReforgingRecipe;
import net.leahperson.proficientmod.registry.ModRecipeTypes;
import net.leahperson.proficientmod.util.QualityDataUtil;
import net.leahperson.proficientmod.util.RarityAttributeNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReforgingAltarBlockEntity extends BlockEntity {

    private static final int TOP_SLOT   = 0;
    private static final int NORTH_SLOT = 1;
    private static final int SOUTH_SLOT = 2;
    private static final int EAST_SLOT  = 3;
    private static final int WEST_SLOT  = 4;
    private static final int TOTAL_SLOTS = 5;

    private final NonNullList<ItemStack> slots = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);

    private int progress = 0;
    private int maxProgress = 0;
    private boolean crafting = false;
    private boolean outputReady = false;
    private int capturedQuality = 0;
    private UUID capturedPlayerUUID = null;
    private long itemPlacedTime = 0L;

    public ReforgingAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFORGING_ALTAR_BE.get(), pos, state);
    }

    /**
     * Expands the frustum-culling AABB to cover the full beam height while crafting,
     * so the renderer is not skipped when only the beam tip is on screen.
     */
    @Override
    public AABB getRenderBoundingBox() {
        if (!crafting) return super.getRenderBoundingBox();
        int top = level != null ? level.getMaxBuildHeight() : worldPosition.getY() + 256;
        return new AABB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                        worldPosition.getX() + 1, top, worldPosition.getZ() + 1);
    }

    public NonNullList<ItemStack> getSlots() { return slots; }

    public ItemStack getSlotForFace(Direction face) {
        int slotIndex = slotIndexForFace(face);
        return slotIndex >= 0 ? slots.get(slotIndex) : ItemStack.EMPTY;
    }

    public boolean isCrafting() { return crafting; }
    public boolean isOutputReady() { return outputReady; }
    public int getProgress() { return progress; }
    public int getMaxProgress() { return maxProgress; }
    public long getItemPlacedTime() { return itemPlacedTime; }

    public static int slotIndexForFace(Direction face) {
        return switch (face) {
            case UP    -> TOP_SLOT;
            case NORTH -> NORTH_SLOT;
            case SOUTH -> SOUTH_SLOT;
            case EAST  -> EAST_SLOT;
            case WEST  -> WEST_SLOT;
            default    -> -1;
        };
    }

    public boolean addItemToFace(ItemStack stack, Direction face) {
        if (crafting || outputReady || stack.isEmpty()) {
            return false;
        }
        int slotIndex = slotIndexForFace(face);
        if (slotIndex < 0 || !slots.get(slotIndex).isEmpty()) {
            return false;
        }
        ItemStack singleItem = stack.copy();
        singleItem.setCount(1);
        slots.set(slotIndex, singleItem);
        if (face == Direction.UP && level != null) {
            itemPlacedTime = level.getGameTime();
        }
        setChangedAndSync();
        return true;
    }

    public ItemStack removeItemFromFace(Direction face) {
        if (crafting) {
            return ItemStack.EMPTY;
        }
        if (outputReady && face == Direction.UP) {
            ItemStack result = slots.get(TOP_SLOT).copy();
            slots.set(TOP_SLOT, ItemStack.EMPTY);
            outputReady = false;
            setChangedAndSync();
            return result;
        }
        int slotIndex = slotIndexForFace(face);
        if (slotIndex < 0 || slots.get(slotIndex).isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = slots.get(slotIndex).copy();
        slots.set(slotIndex, ItemStack.EMPTY);
        setChangedAndSync();
        return result;
    }

    public boolean startCraft(Level level, Player player) {
        if (level == null || level.isClientSide || crafting || outputReady) {
            return false;
        }
        if (slots.get(TOP_SLOT).isEmpty()) {
            player.displayClientMessage(
                    Component.translatable("qualitycrafting.reforging.noitem"), true);
            return false;
        }

        Optional<ReforgingRecipe> matchedRecipe = findMatchingRecipe(level);
        if (matchedRecipe.isEmpty()) {
            return false;
        }

        ReforgingRecipe recipe = matchedRecipe.get();

        if (recipe.getProficiencyRequired() > 0) {
            int playerProficiency = (int) player.getAttributeValue(ModAttributes.PROFICIENCY.get());
            if (playerProficiency < recipe.getProficiencyRequired()) {
                player.displayClientMessage(Component.translatable(
                        "qualitycrafting.station.notproficient",
                        recipe.getProficiencyRequired(), playerProficiency), true);
                return false;
            }
        }

        capturedQuality     = (int) player.getAttributeValue(ModAttributes.QUALITY.get());
        capturedPlayerUUID  = player.getUUID();
        maxProgress         = recipe.getCraftTime();

        if (maxProgress == 0) {
            finishCraft((ServerLevel) level);
            return true;
        }

        crafting = true;
        progress = 0;
        setChangedAndSync();
        return true;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ReforgingAltarBlockEntity blockEntity) {
        if (level.isClientSide || !blockEntity.crafting) {
            return;
        }
        blockEntity.progress++;
        if (blockEntity.progress >= blockEntity.maxProgress) {
            blockEntity.finishCraft((ServerLevel) level);
        }
        blockEntity.setChangedAndSync();
    }

    private void finishCraft(ServerLevel level) {
        Optional<ReforgingRecipe> matchedRecipe = findMatchingRecipe(level);
        if (matchedRecipe.isEmpty()) {
            resetCraftingState();
            return;
        }

        ReforgingRecipe recipe = matchedRecipe.get();

        List<AttributeAddition> newAttributes = recipe.getAttributesForQuality(capturedQuality, level.getRandom());

        ItemStack reforgedItem = slots.get(TOP_SLOT).copy();
        RarityAttributeNBT.clearAttributes(reforgedItem);
        if (!newAttributes.isEmpty()) {
            RarityAttributeNBT.setAttributes(reforgedItem, newAttributes);
        }

        for (int catalogSlot = NORTH_SLOT; catalogSlot < TOTAL_SLOTS; catalogSlot++) {
            slots.set(catalogSlot, ItemStack.EMPTY);
        }
        slots.set(TOP_SLOT, reforgedItem);

        if (capturedPlayerUUID != null && recipe.getLevelCost() > 0) {
            Player player = level.getPlayerByUUID(capturedPlayerUUID);
            if (player != null && !player.isCreative()) {
                player.giveExperienceLevels(-recipe.getLevelCost());
            }
        }

        level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.2F);

        // Burst of enchanting glyphs + sparkle end-rod particles at completion
        double cx = worldPosition.getX() + 0.5;
        double cy = worldPosition.getY() + 1.1;
        double cz = worldPosition.getZ() + 0.5;
        level.sendParticles(ParticleTypes.ENCHANT,  cx, cy, cz, 80, 0.5, 0.4, 0.5, 0.6);
        level.sendParticles(ParticleTypes.END_ROD,  cx, cy, cz, 30, 0.4, 0.4, 0.4, 0.4);
        level.sendParticles(ParticleTypes.WITCH,    cx, cy, cz, 20, 0.3, 0.3, 0.3, 0.2);

        crafting    = false;
        progress    = 0;
        maxProgress = 0;
        outputReady = true;
        setChangedAndSync();
    }

    private SimpleItemContainer buildContainerForMatching() {
        ItemStack[] containerSlots = new ItemStack[TOTAL_SLOTS];
        for (int slotIndex = 0; slotIndex < TOTAL_SLOTS; slotIndex++) {
            containerSlots[slotIndex] = slots.get(slotIndex).copy();
        }
        return new SimpleItemContainer(containerSlots);
    }

    private Optional<ReforgingRecipe> findMatchingRecipe(Level level) {
        SimpleItemContainer container = buildContainerForMatching();
        return level.getRecipeManager()
                .getAllRecipesFor(ModRecipeTypes.REFORGING.get())
                .stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }

    private void resetCraftingState() {
        crafting    = false;
        progress    = 0;
        maxProgress = 0;
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
        for (int slotIndex = 0; slotIndex < slots.size(); slotIndex++) {
            CompoundTag stackTag = new CompoundTag();
            slots.get(slotIndex).save(stackTag);
            tag.put("Slot" + slotIndex, stackTag);
        }
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putBoolean("Crafting", crafting);
        tag.putBoolean("OutputReady", outputReady);
        tag.putInt("CapturedQuality", capturedQuality);
        if (capturedPlayerUUID != null) {
            tag.putUUID("PlayerUUID", capturedPlayerUUID);
        }
        tag.putLong("ItemPlacedTime", itemPlacedTime);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        for (int slotIndex = 0; slotIndex < slots.size(); slotIndex++) {
            slots.set(slotIndex, ItemStack.of(tag.getCompound("Slot" + slotIndex)));
        }
        progress            = tag.getInt("Progress");
        maxProgress         = tag.getInt("MaxProgress");
        crafting            = tag.getBoolean("Crafting");
        outputReady         = tag.getBoolean("OutputReady");
        capturedQuality     = tag.getInt("CapturedQuality");
        capturedPlayerUUID  = tag.hasUUID("PlayerUUID") ? tag.getUUID("PlayerUUID") : null;
        itemPlacedTime      = tag.getLong("ItemPlacedTime");
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
