package dev.isxander.culllessleaves.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CullLessLeavesConfig {
    public static final ConfigInstance<CullLessLeavesConfig> INSTANCE = GsonConfigInstance.createBuilder(CullLessLeavesConfig.class)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("cull-less-leaves.json"))
            .overrideGsonBuilder(new GsonBuilder().setPrettyPrinting())
            .build();

    @ConfigEntry public boolean enabled = true;
    @ConfigEntry public int depth = 2;
    @ConfigEntry public float randomRejection = 0.2f;

    @ConfigEntry public boolean fastMangroveRootsCulling = false;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
                .title(Text.translatable("cull-less-leaves.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("cull-less-leaves.title"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("cull-less-leaves.option.enabled"))
                                .binding(
                                        defaults.enabled,
                                        () -> config.enabled,
                                        value -> config.enabled = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("cull-less-leaves.option.depth"))
                                .description(OptionDescription.of(Text.translatable("cull-less-leaves.option.depth.tooltip")))
                                .binding(
                                        defaults.depth,
                                        () -> config.depth,
                                        v -> config.depth = v
                                )
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1, 4)
                                        .step(1))
                                .flag(OptionFlag.RELOAD_CHUNKS)
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Text.translatable("cull-less-leaves.option.random_rejection"))
                                .description(OptionDescription.of(Text.translatable("cull-less-leaves.option.random_rejection.tooltip")))
                                .binding(defaults.randomRejection, () -> config.randomRejection, v -> config.randomRejection = v)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0f, 1f)
                                        .step(0.1f)
                                        .valueFormatter(v -> Text.literal(String.format("%.0f%%", v*100))))
                                .flag(OptionFlag.RELOAD_CHUNKS)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("cull-less-leaves.option.fast_mangrove_roots_culling"))
                                .description(OptionDescription.of(Text.translatable("cull-less-leaves.option.fast_mangrove_roots_culling.tooltip")))
                                .binding(
                                        defaults.fastMangroveRootsCulling,
                                        () -> config.fastMangroveRootsCulling,
                                        value -> config.fastMangroveRootsCulling = value
                                )
                                .controller(TickBoxControllerBuilder::create)
                                .flag(OptionFlag.RELOAD_CHUNKS)
                                .build())
                        .build()))
                .generateScreen(parent);
    }
}
