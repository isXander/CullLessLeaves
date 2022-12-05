package dev.isxander.culllessleaves.config;

import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionFlag;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.ConfigInstance;
import dev.isxander.yacl.config.GsonConfigInstance;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CullLessLeavesConfig {
    public static final ConfigInstance<CullLessLeavesConfig> INSTANCE = new GsonConfigInstance<>(CullLessLeavesConfig.class, FabricLoader.getInstance().getConfigDir().resolve("cull-less-leaves.json"));

    @ConfigEntry public boolean enabled = true;
    @ConfigEntry public int depth = 2;

    public static Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, (defaults, config, builder) -> builder
                .title(Text.translatable("cull-less-leaves.title"))
                .category(ConfigCategory.createBuilder()
                         .name(Text.translatable("cull-less-leaves.title"))
                         .option(Option.createBuilder(boolean.class)
                                 .name(Text.translatable("cull-less-leaves.option.enabled"))
                                 .binding(
                                         defaults.enabled,
                                         () -> config.enabled,
                                         value -> config.enabled = value
                                 )
                                 .controller(TickBoxController::new)
                                 .flag(OptionFlag.RELOAD_CHUNKS)
                                 .build())
                         .option(Option.createBuilder(int.class)
                                 .name(Text.translatable("cull-less-leaves.option.depth"))
                                 .tooltip(Text.translatable("cull-less-leaves.option.depth.tooltip"))
                                 .binding(
                                         defaults.depth,
                                         () -> config.depth,
                                         value -> config.depth = value
                                 )
                                 .controller(opt -> new IntegerSliderController(opt, 1, 4, 1))
                                 .flag(OptionFlag.RELOAD_CHUNKS)
                                 .build())
                         .build()))
                .generateScreen(parent);
    }
}
