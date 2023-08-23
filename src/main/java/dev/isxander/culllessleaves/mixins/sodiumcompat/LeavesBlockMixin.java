package dev.isxander.culllessleaves.mixins.sodiumcompat;

import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LeavesBlock.class, priority = 1100)
public class LeavesBlockMixin extends Block {
    public LeavesBlockMixin(Settings settings) {
        super(settings);
    }

    /**
     * sodium implements custom logic for
     * culling leaves when set to fast
     *
     * this mixin simply reverts to vanilla behaviour
     * @see me.jellysquid.mods.sodium.mixin.features.render_layer.leaves.MixinLeavesBlock
     */
//    @Override
//    @SuppressWarnings("deprecation")
//    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
//        return super.isSideInvisible(state, stateFrom, direction);
//    }
}
