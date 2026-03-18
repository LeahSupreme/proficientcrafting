package net.leahperson.proficientmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ReforgingAltarBlockEntity extends BlockEntity {

    public ReforgingAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REFORGING_ALTAR_BE.get(), pos, state);
    }
}
