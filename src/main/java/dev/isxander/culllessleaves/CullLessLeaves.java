package dev.isxander.culllessleaves;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import dev.isxander.culllessleaves.compat.Compat;
import dev.isxander.culllessleaves.config.CullLessLeavesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CullLessLeaves implements ClientModInitializer, PreLaunchEntrypoint {
    @Override
    public void onInitializeClient() {
        FabricLoader.getInstance().getModContainer("moreculling").ifPresent((mod) -> {
            try {
                var predicate = VersionPredicateParser.parse(">=0.5.0");
                if (!predicate.test(mod.getMetadata().getVersion())) {
                    throw new FormattedException("Incompatible mod set!", "moreculling is present but does not match " + predicate);
                }
            } catch (VersionParsingException e) {
                e.printStackTrace();
            }

        });

        AutoConfig.register(CullLessLeavesConfig.class, Toml4jConfigSerializer::new);
    }

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }

    public static CullLessLeavesConfig getConfig() {
        return AutoConfig.getConfigHolder(CullLessLeavesConfig.class).getConfig();
    }

    public static boolean shouldCullSide(BlockView view, BlockPos pos, Direction facing) {
        var depth = getConfig().depth;

        // if not fancy leaves, cull everything apart from outside
        if (!Compat.isFancyLeaves())
            depth = 1;

        var vec = facing.getVector();
        var cull = true;
        for (int i = 1; i <= depth; i++) {
            var state = view.getBlockState(pos.add(vec.multiply(i)));
            cull &= state != null && state.getBlock() instanceof LeavesBlock;
        }
        return cull;
    }
}
