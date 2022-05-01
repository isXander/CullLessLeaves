package dev.isxander.culllessleaves;

import dev.isxander.culllessleaves.compat.Compat;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CullLessLeaves {
    public static boolean shouldCullSide(BlockState state, BlockView view, BlockPos pos, Direction facing) {
        var depth = 2;

        // if not fancy leaves, cull everything apart from outside
        if (!Compat.isFancyLeaves())
            depth = 1;

        var vec = facing.getVector();
        var cull = true;
        for (int i = 1; i <= depth; i++) {
            cull &= view.getBlockState(pos.add(vec.multiply(i))).getBlock() instanceof LeavesBlock;
        }
        return cull;
    }
}
