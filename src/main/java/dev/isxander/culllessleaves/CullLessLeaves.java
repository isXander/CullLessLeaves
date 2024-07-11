package dev.isxander.culllessleaves;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import dev.isxander.culllessleaves.compat.Compat;
import dev.isxander.culllessleaves.config.CullLessLeavesConfig;
import dev.isxander.culllessleaves.util.BlockConstantRandom;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;

import java.util.function.Function;

public class CullLessLeaves implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CullLessLeavesConfig.INSTANCE.load();
    }

    public static CullLessLeavesConfig getConfig() {
        return CullLessLeavesConfig.INSTANCE.instance();
    }

    public static boolean shouldCullSide(int depth, BlockView view, BlockPos pos, Direction facing, Function<Block, Boolean> blockCheck) {
        float rejectionChance = getConfig().randomRejection;

        Vec3i vec = facing.getVector();
        boolean cull = true;

        boolean outerBlock = false;
        for (int i = 1; i <= depth; i++) {
            BlockState state = view.getBlockState(pos.add(vec.multiply(i)));
            cull &= state != null && blockCheck.apply(state.getBlock());

            if (!cull && i == 1)
                outerBlock = true;
        }

        if (!outerBlock && !cull && BlockConstantRandom.getConstantRandomSeeded(pos) <= rejectionChance)
            cull = true;

        return cull;
    }
}
