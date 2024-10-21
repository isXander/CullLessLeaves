package dev.isxander.culllessleaves.mixins.blocks;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MangroveRootsBlock.class)
public class MangroveRootsBlockMixin implements Cullable {
    @Override
    public boolean cll$shouldCullSide(BlockState state, BlockGetter view, BlockPos pos, Direction facing) {
        return CullLessLeaves.getConfig().fastMangroveRootsCulling && CullLessLeaves.shouldCullSide(
                1,
                view,
                pos,
                facing,
                block -> block instanceof MangroveRootsBlock
        );
    }
}
