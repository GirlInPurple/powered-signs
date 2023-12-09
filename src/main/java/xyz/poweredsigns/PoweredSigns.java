package xyz.poweredsigns;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.poweredsigns.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

public class PoweredSigns implements ModInitializer {

	public static String MODID = "poweredsigns";
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static int ticksSinceStartup = 0;
	public static List<PlayerEntity> noPrintPlayers = new ArrayList<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Signs can now be powered.");
		ModConfig.init();
		ServerTickEvents.END_SERVER_TICK.register(this::onEndTick);
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(CommandManager.literal("togglesigns")
						.then(CommandManager.argument("value", BoolArgumentType.bool())
								.executes(context -> toggleSigns(context, BoolArgumentType.getBool(context, "value"))))));
	}

	public void onEndTick(MinecraftServer server) {
		ticksSinceStartup = server.getTicks();
	}

	private int toggleSigns(CommandContext<ServerCommandSource> context, boolean value) {
		ServerCommandSource source = context.getSource();
		PlayerEntity player = source.getPlayer();

		if (value) {
			noPrintPlayers.remove(player);
			source.sendFeedback(() -> Text.translatable("text.poweredsigns.feedback.enabled"), false);
		} else {
			noPrintPlayers.add(player);
			source.sendFeedback(() -> Text.translatable("text.poweredsigns.feedback.disabled"), false);
		}

		return 1;
	}

}