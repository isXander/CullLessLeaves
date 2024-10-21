package dev.isxander.culllessleaves.compat;

import dev.isxander.culllessleaves.compat.sodium.SodiumCompat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class Compat {
    public static final boolean SODIUM = FabricLoader.getInstance().isModLoaded("sodium");

    public static boolean isFancyLeaves() {
        return SODIUM ? SodiumCompat.isFancyLeaves() : Minecraft.useFancyGraphics();
    }
}