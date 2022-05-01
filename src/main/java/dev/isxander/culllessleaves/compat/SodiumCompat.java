package dev.isxander.culllessleaves.compat;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.config.CullLessLeavesConfig;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;

public class SodiumCompat {
    public static boolean isFancyLeaves() {
        return SodiumClientMod.options().quality.leavesQuality.isFancy(MinecraftClient.getInstance().options.graphicsMode);
    }

    public static class CullLeavesOptionStorage implements OptionStorage<CullLessLeavesConfig> {
        public static final CullLeavesOptionStorage INSTANCE = new CullLeavesOptionStorage();

        @Override
        public CullLessLeavesConfig getData() {
            return CullLessLeaves.getConfig();
        }

        @Override
        public void save() {
            AutoConfig.getConfigHolder(CullLessLeavesConfig.class).save();
        }
    }
}
