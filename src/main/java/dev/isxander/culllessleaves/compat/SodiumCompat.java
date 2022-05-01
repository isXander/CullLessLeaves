package dev.isxander.culllessleaves.compat;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.MinecraftClient;

public class SodiumCompat {
    public static boolean isFancyLeaves() {
        return SodiumClientMod.options().quality.leavesQuality.isFancy(MinecraftClient.getInstance().options.graphicsMode);
    }
}
