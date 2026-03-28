package net.leahperson.proficientmod.block.custom;

import net.leahperson.proficientmod.block.entity.CookingPotBlockEntity;
import net.leahperson.proficientmod.block.entity.ModBlockEntities;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CookingPotBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape INSIDE = box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(
                    box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0),
                    box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0),
                    box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
                    INSIDE),
            BooleanOp.ONLY_FIRST);

    public CookingPotBlock(BlockBehaviour.Properties props) {
        super(props);
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
        return new CookingPotBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.COOKING_POT_BE.get(), CookingPotBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof CookingPotBlockEntity cookingPot)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (held.is(ModTags.Items.COOKING_LADLE)) {
            boolean started = cookingPot.startCraft(level, player);
            if (started && !player.getAbilities().instabuild) {
                held.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));
            }
            return started ? InteractionResult.CONSUME : InteractionResult.FAIL;
        }

        if (held.isEmpty()) {
            ItemStack outputItem = cookingPot.removeOutput();
            if (!outputItem.isEmpty()) {
                player.addItem(outputItem);
                return InteractionResult.CONSUME;
            }
            ItemStack removed = cookingPot.removeLastInput();
            if (!removed.isEmpty()) {
                player.addItem(removed);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        if (cookingPot.hasOutput()) {
            player.addItem(cookingPot.removeOutput());
        }

        ItemStack insertOne = held.copy();
        insertOne.setCount(1);
        boolean inserted = cookingPot.addInput(insertOne);
        if (inserted) {
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }
}
