package dev.isxander.culllessleaves.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionFlag;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CullLessLeavesConfig {
    public static final CullLessLeavesConfig INSTANCE = new CullLessLeavesConfig();

    public final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("cull-less-leaves.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean enabled = true;
    public int depth = 2;

    public void save() {
        try {
            Files.deleteIfExists(configFile);

            JsonObject json = new JsonObject();
            json.addProperty("enabled", enabled);
            json.addProperty("depth", depth);

            Files.writeString(configFile, gson.toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            if (Files.notExists(configFile)) {
                save();
                return;
            }

            JsonObject json = gson.fromJson(Files.readString(configFile), JsonObject.class);

            if (json.has("enabled"))
                enabled = json.getAsJsonPrimitive("enabled").getAsBoolean();
            if (json.has("depth"))
                depth = json.getAsJsonPrimitive("depth").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Screen makeScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("cull-less-leaves.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("cull-less-leaves.title"))
                        .option(Option.createBuilder(boolean.class)
                                .name(Text.translatable("cull-less-leaves.option.enabled"))
                                .binding(
                                        true,
                                        () -> enabled,
                                        value -> enabled = value
                                )
                                .controller(TickBoxController::new)
                                .build())
                        .option(Option.createBuilder(int.class)
                                .name(Text.translatable("cull-less-leaves.option.depth"))
                                .tooltip(Text.translatable("cull-less-leaves.option.depth.tooltip"))
                                .binding(
                                        2,
                                        () -> depth,
                                        value -> depth = value
                                )
                                .controller(yacl -> new IntegerSliderController(yacl, 1, 4, 1))
                                .flag(OptionFlag.RELOAD_CHUNKS)
                                .build())
                        .build())
                .save(this::save)
                .build()
                .generateScreen(parent);
    }
}
