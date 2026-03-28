package net.leahperson.proficientmod.block.custom;

import net.leahperson.proficientmod.block.entity.ModBlockEntities;
import net.leahperson.proficientmod.block.entity.ReforgingAltarBlockEntity;
import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ReforgingAltarBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE = box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    public ReforgingAltarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReforgingAltarBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.REFORGING_ALTAR_BE.get(), ReforgingAltarBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ReforgingAltarBlockEntity altar)) {
            return InteractionResult.PASS;
        }

        Direction hitFace = hit.getDirection();
        int hitSlot = ReforgingAltarBlockEntity.slotIndexForFace(hitFace);

        if (hitSlot < 0) {
            return InteractionResult.PASS;
        }

        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.is(ModTags.Items.REFORGING_SCEPTER)) {
            boolean craftStarted = altar.startCraft(level, player);
            if (craftStarted && !player.getAbilities().instabuild) {
                heldItem.hurtAndBreak(1, player, brokenPlayer -> brokenPlayer.broadcastBreakEvent(hand));
            }
            return craftStarted ? InteractionResult.CONSUME : InteractionResult.FAIL;
        }

        if (heldItem.isEmpty()) {
            ItemStack removedItem = altar.removeItemFromFace(hitFace);
            if (!removedItem.isEmpty()) {
                player.addItem(removedItem);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        ItemStack singleItem = heldItem.copy();
        singleItem.setCount(1);
        boolean placed = altar.addItemToFace(singleItem, hitFace);
        if (placed) {
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }
}
