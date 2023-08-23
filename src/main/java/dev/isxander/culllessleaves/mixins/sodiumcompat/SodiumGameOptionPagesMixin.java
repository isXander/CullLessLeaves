package dev.isxander.culllessleaves.mixins.sodiumcompat;

import dev.isxander.culllessleaves.compat.SodiumCompat;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Pseudo
@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGameOptionPagesMixin {
    @ModifyVariable(method = "performance", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static List<OptionGroup> addLeavesCulling(List<OptionGroup> groups) {
        groups.add(OptionGroup.createBuilder()
                .add(OptionImpl.createBuilder(boolean.class, SodiumCompat.getOptionStorage())
                        .setName(Text.translatable("cull-less-leaves.sodium.option.enabled"))
                        .setTooltip(Text.translatable("cull-less-leaves.sodium.option.enabled.description"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.enabled = value, opts -> opts.enabled)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, SodiumCompat.getOptionStorage())
                        .setName(Text.translatable("cull-less-leaves.sodium.option.depth"))
                        .setTooltip(Text.translatable("cull-less-leaves.option.depth.tooltip"))
                        .setControl(o -> new SliderControl(o, 1, 4, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.depth = value, opts -> opts.depth)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, SodiumCompat.getOptionStorage())
                        .setName(Text.translatable("cull-less-leaves.option.random_rejection"))
                        .setTooltip(Text.translatable("cull-less-leaves.option.random_rejection.tooltip"))
                        .setControl(o -> new SliderControl(o, 0, 100, 10, ControlValueFormatter.percentage()))
                        .setBinding((opts, value) -> opts.randomRejection = value / 100f, opts -> (int)(opts.randomRejection * 100f))
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(boolean.class, SodiumCompat.getOptionStorage())
                        .setName(Text.translatable("cull-less-leaves.option.fast_mangrove_roots_culling"))
                        .setTooltip(Text.translatable("cull-less-leaves.option.fast_mangrove_roots_culling.tooltip"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.fastMangroveRootsCulling = value, opts -> opts.fastMangroveRootsCulling)
                        .setImpact(OptionImpact.LOW)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build()
        );

        return groups;
    }
}
