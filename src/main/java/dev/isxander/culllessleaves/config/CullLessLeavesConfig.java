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
        return INSTANCE.buildConfig((config, builder) -> builder
                .title(Text.translatable("cull-less-leaves.title"))
                .category(ConfigCategory.createBuilder()
                         .name(Text.translatable("cull-less-leaves.title"))
                         .option(Option.createBuilder(boolean.class)
                                 .name(Text.translatable("cull-less-leaves.option.enabled"))
                                 .binding(
                                         config.getDefaults().enabled,
                                         () -> config.getConfig().enabled,
                                         value -> config.getConfig().enabled = value
                                 )
                                 .controller(TickBoxController::new)
                                 .build())
                         .option(Option.createBuilder(int.class)
                                 .name(Text.translatable("cull-less-leaves.option.depth"))
                                 .tooltip(Text.translatable("cull-less-leaves.option.depth.tooltip"))
                                 .binding(
                                         config.getDefaults().depth,
                                         () -> config.getConfig().depth,
                                         value -> config.getConfig().depth = value
                                 )
                                 .controller(yacl -> new IntegerSliderController(yacl, 1, 4, 1))
                                 .flag(OptionFlag.RELOAD_CHUNKS)
                                 .build())
                         .build()))
                .generateScreen(parent);
    }
}
