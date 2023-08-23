package dev.isxander.culllessleaves.mixins.blocks;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.MangroveRootsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MangroveRootsBlock.class)
public class MangroveRootsBlockMixin implements Cullable {
    @Override
    public boolean cll$shouldCullSide(BlockState state, BlockView view, BlockPos pos, Direction facing) {
        return CullLessLeaves.getConfig().fastMangroveRootsCulling && CullLessLeaves.shouldCullSide(
                1,
                view,
                pos,
                facing,
                block -> block instanceof MangroveRootsBlock
        );
    }
}
