package net.leahperson.proficientmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class JewelcraftingStationBlockEntity extends BlockEntity {

    public JewelcraftingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.JEWELCRAFTING_STATION_BE.get(), pos, state);
    }
}
