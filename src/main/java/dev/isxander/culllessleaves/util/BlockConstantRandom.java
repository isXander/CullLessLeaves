package dev.isxander.culllessleaves.util;

import net.minecraft.util.math.BlockPos;

public class BlockConstantRandom {
    // GitHub Copilot suggested this code whilst writing the name of the method lol
    public static float getConstantRandomHashed(BlockPos pos) {
        int i = pos.getX() * 3129871 ^ pos.getZ() * 116129781 ^ pos.getY();
        i = i * i * 42317861 + i * 11;
        return ((float) ((i >> 16 & 15) + 0.5D) / 16.0F);
    }

    // This code is like setting up a random instance seeded with the block position
    public static float getConstantRandomSeeded(BlockPos pos) {
        long seed = (pos.asLong() ^ 25214903917L) & 281474976710655L;
        seed = seed * 25214903917L + 11L & 281474976710655L;
        return ((int) (seed >> 48 - 24)) * 5.9604645E-8F;
    }
}
