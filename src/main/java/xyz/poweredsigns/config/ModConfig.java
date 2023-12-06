package xyz.poweredsigns.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.*;

@SuppressWarnings("CanBeFinal")
@Config(name = "poweredsigns")
public class ModConfig implements ConfigData{

    @ConfigEntry.Category("poweredsigns")
    @ConfigEntry.Gui.Tooltip
    public int playerDistance = 32;

    @ConfigEntry.Category("poweredsigns")
    @ConfigEntry.Gui.Tooltip
    public int coolDownTicks = 10;

    @ConfigEntry.Category("poweredsigns")
    @ConfigEntry.Gui.Tooltip
    public boolean logSignPositions = false;

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }
    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}