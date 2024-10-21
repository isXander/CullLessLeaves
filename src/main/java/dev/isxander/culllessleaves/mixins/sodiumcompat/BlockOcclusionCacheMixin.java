package dev.isxander.culllessleaves.mixins.sodiumcompat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.Cullable;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
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
    @ModifyExpressionValue(
            method = "shouldDrawSide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean shouldCullSide(boolean isSideInvisible, BlockState state, BlockGetter view, BlockPos pos, Direction facing) {
        if (CullLessLeaves.getConfig().enabled && state.getBlock() instanceof Cullable cullable) {
            return isSideInvisible || cullable.cll$shouldCullSide(state, view, pos, facing);
        }
        return isSideInvisible;
    }
}