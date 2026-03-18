package net.leahperson.proficientmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ScribingTableBlockEntity extends BlockEntity {

    public ScribingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCRIBING_TABLE_BE.get(), pos, state);
    }
}
