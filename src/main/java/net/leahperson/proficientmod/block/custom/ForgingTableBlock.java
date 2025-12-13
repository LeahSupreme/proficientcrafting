package net.leahperson.proficientmod.block.custom;

import net.leahperson.proficientmod.block.entity.ForgingTableBlockEntity;
import net.leahperson.proficientmod.util.ModTags;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Predicate;

public class ForgingTableBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape SHAPE = Block.box(0,0,0,16,16,16);
    private static final Logger log = LoggerFactory.getLogger(ForgingTableBlock.class);

    public ForgingTableBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getClockWise());
    }

    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ForgingTableBlockEntity(pPos,pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ForgingTableBlockEntity) {
                ((ForgingTableBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity forgingTableEntity = pLevel.getBlockEntity(pPos);
        if (forgingTableEntity instanceof ForgingTableBlockEntity forgingTableBlockEntity) {
            if(pPlayer.getItemInHand(pHand).isEmpty()){
                //Handslot is empty, try to remove from the anvil
                if(forgingTableBlockEntity.isEmpty()){
                    //pPlayer.displayClientMessage(Component.literal("Nothing in table"),false);

                    return InteractionResult.CONSUME;
                }else{
                    forgingTableBlockEntity.removeLatestItem(pLevel,pPos,pState,pPlayer);
                    return InteractionResult.sidedSuccess(!pLevel.isClientSide);
                }
            }else{
                //Handslot has something in it, try to add to the anvil or craft
                if(pPlayer.getItemInHand(pHand).getTags().anyMatch(Predicate.isEqual(ModTags.Items.FORGING_HAMMER))){
                    //Craft

                    boolean result = forgingTableBlockEntity.attemptCraft(pLevel,pPos,pState,pPlayer);
                    if(!result){
                        //pPlayer.displayClientMessage(Component.literal("Craft Failed"),false);

                        return InteractionResult.CONSUME;
                    }else{




                        //pPlayer.displayClientMessage(Component.literal("Craft Succeeded"),false);

                        return InteractionResult.sidedSuccess(!pLevel.isClientSide);
                    }



                }else{
                    //Attempt to insert item.
                    if(forgingTableBlockEntity.isFull()){
                        //pPlayer.displayClientMessage(Component.literal("Table is full"),false);
                        return InteractionResult.CONSUME;
                    }else{
                        forgingTableBlockEntity.insertItem(pLevel,pPos,pState,pPlayer.getItemInHand(pHand));
                        return InteractionResult.sidedSuccess(!pLevel.isClientSide);
                    }
                }

            }

        }
        return InteractionResult.PASS;
    }

    public void addItem(Level pLevel, BlockPos pPos, Player pPlayer, ChiseledBookShelfBlockEntity pBlockEntity, ItemStack pItemStack){

    }

    public Item removeItem(){
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return super.getTicker(pLevel, pState, pBlockEntityType);
    }
}
