package net.leahperson.proficientmod.block.custom;

import net.leahperson.proficientmod.block.entity.ForgingTableBlockEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ForgingStationBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // North/South facing — anvil top extends along Z axis
    private static final VoxelShape BASE    = box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    private static final VoxelShape LEG1_NS = box(4.0, 4.0, 3.0, 12.0, 5.0, 13.0);
    private static final VoxelShape LEG2_NS = box(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);
    private static final VoxelShape TOP_NS  = box(3.0, 10.0, 0.0, 13.0, 16.0, 16.0);
    private static final VoxelShape SHAPE_NS = Shapes.or(BASE, LEG1_NS, LEG2_NS, TOP_NS);

    // East/West facing — anvil top extends along X axis (90° rotation of NS)
    private static final VoxelShape LEG1_EW = box(3.0, 4.0, 4.0, 13.0, 5.0, 12.0);
    private static final VoxelShape LEG2_EW = box(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);
    private static final VoxelShape TOP_EW  = box(0.0, 10.0, 3.0, 16.0, 16.0, 13.0);
    private static final VoxelShape SHAPE_EW = Shapes.or(BASE, LEG1_EW, LEG2_EW, TOP_EW);

    public ForgingStationBlock(BlockBehaviour.Properties props) {
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
        Direction facing = state.getValue(FACING);
        // 90° model offset means N/S facing → model rotated 90°/270° → long axis runs E/W
        return (facing == Direction.NORTH || facing == Direction.SOUTH) ? SHAPE_EW : SHAPE_NS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ForgingTableBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.FORGING_STATION_BE.get(), ForgingTableBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ForgingTableBlockEntity table)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (held.is(ModTags.Items.FORGING_HAMMER)) {
            boolean started = table.startCraft(level, player);
            if (started && !player.getAbilities().instabuild) {
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return started ? InteractionResult.CONSUME : InteractionResult.FAIL;
        }

        if (held.isEmpty()) {
            ItemStack out = table.removeOutput();
            if (!out.isEmpty()) {
                player.addItem(out);
                return InteractionResult.CONSUME;
            }

            ItemStack removed = table.removeLastInput();
            if (!removed.isEmpty()) {
                player.addItem(removed);
                return InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        }

        if (table.hasOutput()) {
            player.addItem(table.removeOutput());
        }

        ItemStack insertOne = held.copy();
        insertOne.setCount(1);

        boolean inserted = table.addInput(insertOne);
        if (inserted) {
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }
}
