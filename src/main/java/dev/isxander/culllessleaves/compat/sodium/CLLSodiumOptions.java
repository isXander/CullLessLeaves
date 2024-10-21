package dev.isxander.culllessleaves.compat.sodium;

import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CLLSodiumOptions {
    public static void addGroupsToPerformance(List<OptionGroup> groups) {
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, SodiumCompat.getOptionStorage())
                        .setName(Component.translatable("cull-less-leaves.sodium.option.enabled"))
                        .setTooltip(Component.translatable("cull-less-leaves.sodium.option.enabled.description"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.enabled = value, opts -> opts.enabled)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, SodiumCompat.getOptionStorage())
                        .setName(Component.translatable("cull-less-leaves.sodium.option.depth"))
                        .setTooltip(Component.translatable("cull-less-leaves.option.depth.tooltip"))
                        .setControl(o -> new SliderControl(o, 1, 4, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.depth = value, opts -> opts.depth)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, SodiumCompat.getOptionStorage())
                        .setName(Component.translatable("cull-less-leaves.option.random_rejection"))
                        .setTooltip(Component.translatable("cull-less-leaves.option.random_rejection.tooltip"))
                        .setControl(o -> new SliderControl(o, 0, 100, 10, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.randomRejection = value / 100f, opts -> (int)(opts.randomRejection * 100f))
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, SodiumCompat.getOptionStorage())
                        .setName(Component.translatable("cull-less-leaves.option.fast_mangrove_roots_culling"))
                        .setTooltip(Component.translatable("cull-less-leaves.option.fast_mangrove_roots_culling.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.fastMangroveRootsCulling = value, opts -> opts.fastMangroveRootsCulling)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build()
        );
    }
}
