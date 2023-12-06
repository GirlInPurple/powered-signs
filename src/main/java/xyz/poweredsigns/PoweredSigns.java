package xyz.poweredsigns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.poweredsigns.config.ModConfig;

public class PoweredSigns implements ModInitializer {

	public static String MODID = "poweredsigns";
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static int ticksSinceStartup = 0;

	@Override
	public void onInitialize() {
		LOGGER.info("Signs can now be powered.");
		ModConfig.init();
		ServerTickEvents.END_SERVER_TICK.register(this::onEndTick);
	}

	public void onEndTick(MinecraftServer server) {
		ticksSinceStartup = server.getTicks();
	}

}