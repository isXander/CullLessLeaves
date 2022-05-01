package dev.isxander.culllessleaves.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "cull-less-leaves")
public class CullLessLeavesConfig implements ConfigData {
    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(min = 1L, max = 4L)
    @ConfigEntry.Gui.Tooltip(count = 4)
    @Comment("amount of layers of leaves before the inside is culled")
    public int depth = 2;
}
