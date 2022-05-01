package dev.isxander.culllessleaves.mixins.sodiumcompat;

import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.compat.SodiumCompat;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
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
                .add(OptionImpl.createBuilder(boolean.class, SodiumCompat.CullLeavesOptionStorage.INSTANCE)
                        .setName(new TranslatableText("text.autoconfig.cull-less-leaves.title"))
                        .setTooltip(new TranslatableText("text.cull-less-leaves.description"))
                        .setControl(TickBoxControl::new)
                        .setBinding((opts, value) -> opts.enabled = value, opts -> opts.enabled)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .add(OptionImpl.createBuilder(int.class, SodiumCompat.CullLeavesOptionStorage.INSTANCE)
                        .setName(new TranslatableText("text.cull-less-leaves.sodium.option.depth"))
                        .setTooltip(new TranslatableText("text.autoconfig.cull-less-leaves.option.depth.@Tooltip"))
                        .setControl(o -> new SliderControl(o, 1, 4, 1, ControlValueFormatter.number()))
                        .setBinding((opts, value) -> opts.depth = value, opts -> opts.depth)
                        .setImpact(OptionImpact.MEDIUM)
                        .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                        .build()
                )
                .build()
        );

        return groups;
    }
}
