package dev.isxander.culllessleaves;

import dev.isxander.culllessleaves.config.CullLessLeavesConfig;
import dev.isxander.culllessleaves.util.BlockConstantRandom;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class CullLessLeaves implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CullLessLeavesConfig.INSTANCE.load();
    }

    public static CullLessLeavesConfig getConfig() {
        return CullLessLeavesConfig.INSTANCE.instance();
    }

    public static boolean shouldCullSide(int depth, BlockGetter view, BlockPos pos, Direction facing, Function<Block, Boolean> blockCheck) {
        float rejectionChance = getConfig().randomRejection;

        Vec3i vec = facing.getNormal();
        boolean cull = true;

        boolean outerBlock = false;
        for (int i = 1; i <= depth; i++) {
            BlockState state = view.getBlockState(pos.offset(vec.multiply(i)));
            cull &= state != null && blockCheck.apply(state.getBlock());

            if (!cull && i == 1)
                outerBlock = true;
        }

        if (!outerBlock && !cull && BlockConstantRandom.getConstantRandomSeeded(pos) <= rejectionChance)
            cull = true;

        return cull;
    }
}