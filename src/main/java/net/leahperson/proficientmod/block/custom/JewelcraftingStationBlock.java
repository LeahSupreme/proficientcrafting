package net.leahperson.proficientmod.block.custom;

import net.leahperson.proficientmod.block.entity.JewelcraftingStationBlockEntity;
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

public class JewelcraftingStationBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // Pedestal base → narrow neck → full-width display top
    private static final VoxelShape BOTTOM = box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
    private static final VoxelShape NECK   = box(4.0, 4.0, 4.0, 12.0, 8.0, 12.0);
    private static final VoxelShape TOP    = box(0.0, 8.0, 0.0, 16.0, 12.0, 16.0);
    private static final VoxelShape SHAPE  = Shapes.or(BOTTOM, NECK, TOP);

    public JewelcraftingStationBlock(BlockBehaviour.Properties props) {
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
        return new JewelcraftingStationBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.JEWELCRAFTING_STATION_BE.get(), JewelcraftingStationBlockEntity::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof JewelcraftingStationBlockEntity table)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (held.is(ModTags.Items.JEWEL_CHISEL)) {
            boolean started = table.startCraft(level, player);
            if (started && !player.getAbilities().instabuild) {
                held.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));
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
