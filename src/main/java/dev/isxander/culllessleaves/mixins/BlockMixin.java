package dev.isxander.culllessleaves.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin {
    @ModifyExpressionValue(
            method = "shouldRenderFace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private static boolean shouldCullLeafSide(boolean isSideInvisible, BlockState state, BlockGetter view, BlockPos pos, Direction side, BlockPos blockPos) {
        if (CullLessLeaves.getConfig().enabled && state.getBlock() instanceof Cullable cullable) {
            return isSideInvisible || cullable.cll$shouldCullSide(state, view, pos, side);
        }
        return isSideInvisible;
    }
}
