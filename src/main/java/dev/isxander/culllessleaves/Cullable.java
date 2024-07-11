package dev.isxander.culllessleaves;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface Cullable {
    boolean cll$shouldCullSide(BlockState state, BlockView view, BlockPos pos, Direction facing);
}
