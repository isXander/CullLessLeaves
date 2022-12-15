package dev.isxander.culllessleaves.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.compat.Compat;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin {
    @ModifyExpressionValue(method = "shouldDrawSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSideInvisible(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"))
    private static boolean shouldCullLeafSide(boolean isSideInvisible, BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos) {
        if (!(state.getBlock() instanceof LeavesBlock) || !CullLessLeaves.getConfig().enabled || Compat.SODIUM)
            return isSideInvisible;

        return CullLessLeaves.shouldCullSide(world, pos, side);
    }
}
