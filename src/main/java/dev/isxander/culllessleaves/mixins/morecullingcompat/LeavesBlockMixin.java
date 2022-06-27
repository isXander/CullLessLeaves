package dev.isxander.culllessleaves.mixins.morecullingcompat;

import ca.fxco.moreculling.api.block.MoreBlockCulling;
import dev.isxander.culllessleaves.CullLessLeaves;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Restriction(require = @Condition("moreculling"))
@Mixin(LeavesBlock.class)
public class LeavesBlockMixin implements MoreBlockCulling {
    @Override
    public boolean usesCustomShouldDrawFace(BlockState state) {
        return CullLessLeaves.getConfig().enabled;
    }

    @Override
    public Optional<Boolean> customShouldDrawFace(BlockView view, BlockState thisState, BlockState sideState, BlockPos thisPos, BlockPos sidePos, Direction side) {
        if (sideState.isAir()) return Optional.of(true);
        if (CullLessLeaves.shouldCullSide(view, sidePos, side))
            return Optional.of(false);
        else
            return Optional.empty();
    }
}
