package dev.isxander.culllessleaves;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface Cullable {
    boolean cll$shouldCullSide(BlockState state, BlockGetter view, BlockPos pos, Direction facing);
}