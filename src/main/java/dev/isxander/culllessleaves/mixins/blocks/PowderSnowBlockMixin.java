package dev.isxander.culllessleaves.mixins.blocks;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin implements Cullable {
    @Override
    public boolean cll$shouldCullSide(BlockState state, BlockGetter view, BlockPos pos, Direction facing) {
        return CullLessLeaves.shouldCullSide(
                1,
                view,
                pos,
                facing,
                block -> block instanceof PowderSnowBlock
        );
    }
}
