package dev.isxander.culllessleaves.mixins.blocks;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import dev.isxander.culllessleaves.compat.Compat;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin implements Cullable {
    @Override
    public boolean cll$shouldCullSide(BlockState state, BlockView view, BlockPos pos, Direction facing) {
        return CullLessLeaves.shouldCullSide(
                Compat.isFancyLeaves() ? CullLessLeaves.getConfig().depth : 1,
                view,
                pos,
                facing,
                block -> block instanceof LeavesBlock
        );
    }
}
