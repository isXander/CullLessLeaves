package dev.isxander.culllessleaves.mixins.sodiumcompat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(BlockOcclusionCache.class)
public class BlockOcclusionCacheMixin {
    /**
     * Sodium caches if the sides are visible which reimplements
     * vanilla code that this mod uses.
     */
    @ModifyExpressionValue(method = "shouldDrawSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isSideInvisible(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean shouldCullSide(boolean isSideInvisible, BlockState state, BlockView view, BlockPos pos, Direction facing) {
        if (CullLessLeaves.getConfig().enabled && state.getBlock() instanceof Cullable cullable) {
            return isSideInvisible || cullable.cll$shouldCullSide(state, view, pos, facing);
        }
        return isSideInvisible;
    }
}
