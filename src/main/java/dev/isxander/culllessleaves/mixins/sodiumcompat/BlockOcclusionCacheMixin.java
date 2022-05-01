package dev.isxander.culllessleaves.mixins.sodiumcompat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.culllessleaves.CullLessLeaves;
import me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(BlockOcclusionCache.class)
public class BlockOcclusionCacheMixin {
    @ModifyExpressionValue(method = "shouldDrawSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSideInvisible(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean shouldCullSide(boolean isSideInvisible, BlockState state, BlockView view, BlockPos pos, Direction facing) {
        if (!(state.getBlock() instanceof LeavesBlock) || !CullLessLeaves.getConfig().enabled)
            return isSideInvisible;

        return CullLessLeaves.shouldCullSide(view, pos, facing);
    }
}
