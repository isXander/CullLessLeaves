package dev.isxander.culllessleaves.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import dev.isxander.yacl3.gui.controllers.TickBoxController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CullLessLeavesConfig {
    public static final ConfigInstance<CullLessLeavesConfig> INSTANCE = GsonConfigInstance.createBuilder(CullLessLeavesConfig.class)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("cull-less-leaves.json"))
            .build();

    @ConfigEntry public boolean enabled = true;
    @ConfigEntry public int depth = 2;

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
                                 .flag(OptionFlag.RELOAD_CHUNKS)
                                 .build())
                         .option(Option.<Integer>createBuilder()
                                 .name(Text.translatable("cull-less-leaves.option.depth"))
                                 .description(OptionDescription.of(Text.translatable("cull-less-leaves.option.depth.tooltip")))
                                 .binding(
                                         defaults.depth,
                                         () -> config.depth,
                                         value -> config.depth = value
                                 )
                                 .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                         .range(1, 4)
                                         .step(1))
                                 .flag(OptionFlag.RELOAD_CHUNKS)
                                 .build())
                         .build()))
                .generateScreen(parent);
    }
}
