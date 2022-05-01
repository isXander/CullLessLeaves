package dev.isxander.culllessleaves.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class Compat {
    public static final boolean SODIUM = FabricLoader.getInstance().isModLoaded("sodium");

    public static boolean isFancyLeaves() {
        return SODIUM ? SodiumCompat.isFancyLeaves() : MinecraftClient.isFancyGraphicsOrBetter();
    }
}
