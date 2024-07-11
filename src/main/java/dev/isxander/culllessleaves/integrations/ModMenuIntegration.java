package dev.isxander.culllessleaves.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.culllessleaves.CullLessLeaves;
import dev.isxander.culllessleaves.config.CullLessLeavesConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CullLessLeavesConfig::makeScreen;
    }
}tat
