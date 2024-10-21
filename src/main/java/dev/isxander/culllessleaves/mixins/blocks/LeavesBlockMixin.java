package dev.isxander.culllessleaves.mixins.blocks;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import dev.isxander.culllessleaves.compat.Compat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin implements Cullable {
    @Override
    public boolean cll$shouldCullSide(BlockState state, BlockGetter view, BlockPos pos, Direction facing) {
        return CullLessLeaves.shouldCullSide(
                Compat.isFancyLeaves() ? CullLessLeaves.getConfig().depth : 1,
                view,
                pos,
                facing,
                block -> block instanceof LeavesBlock
        );
    }
}
