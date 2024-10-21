package dev.isxander.culllessleaves.compat.sodium;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.config.CullLessLeavesConfig;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.client.Minecraft;

public class SodiumCompat {
    private static final OptionStorage<CullLessLeavesConfig> OPTION_STORAGE = new OptionStorage<>() {
        @Override
        public CullLessLeavesConfig getData() {
            return CullLessLeaves.getConfig();
        }

        @Override
        public void save() {
            CullLessLeavesConfig.INSTANCE.save();
        }
    };

    public static boolean isFancyLeaves() {
        return SodiumClientMod.options().quality.leavesQuality
                .isFancy(Minecraft.getInstance().options.graphicsMode().get());
    }

    public static OptionStorage<CullLessLeavesConfig> getOptionStorage() {
        return OPTION_STORAGE;
    }
}