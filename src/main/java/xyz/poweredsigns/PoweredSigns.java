package xyz.poweredsigns;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.poweredsigns.config.ModConfig;
import xyz.poweredsigns.utils.SignUtils;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.PathUtil.validatePath;

public class PoweredSigns implements ModInitializer {
	public static String MODID = "poweredsigns";
	private static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static int ticksSinceStartup = 0;
	public static List<String> noPrintPlayers = new ArrayList<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Signs can now be powered.");
		ModConfig.init();
		noPrintPlayers = SignUtils.readToggleSigns();
		ServerTickEvents.END_SERVER_TICK.register(this::onEndTick);
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				dispatcher.register(CommandManager.literal("togglesigns")
						.then(CommandManager.argument("value", BoolArgumentType.bool())
								.executes(context -> toggleSigns(context, BoolArgumentType.getBool(context, "value"))))));

		FabricLoader.getInstance().getModContainer(MODID).ifPresent(container -> {
			ResourceManagerHelper.registerBuiltinResourcePack(asId("redstone_signs"), container, ResourcePackActivationType.NORMAL);
		});
	}

	public static Identifier asId(String path) {return new Identifier(MODID, path);}

	public void onEndTick(MinecraftServer server) {ticksSinceStartup = server.getTicks();}

	private int toggleSigns(CommandContext<ServerCommandSource> context, boolean value) {
		ServerCommandSource source = context.getSource();
		if (source.getPlayer() == null) {return 0;}
		String player = source.getPlayer().getName().getString();
		switch (FabricLoader.getInstance().getEnvironmentType()) {
			case CLIENT -> {
				if (value) {
					noPrintPlayers.remove(player);
					source.sendFeedback(() -> Text.translatable("text.poweredsigns.feedback.enabled"), false);
				} else {
					internalToggleSign(player);
					source.sendFeedback(() -> Text.translatable("text.poweredsigns.feedback.disabled"), false);
				}
			}
			case SERVER -> {
				if (value) {
					noPrintPlayers.remove(player);
					source.sendFeedback(() -> Text.literal("§ePowered signs will now send you messages."), false);
				} else {
					internalToggleSign(player);
					source.sendFeedback(() -> Text.literal("§ePowered signs will no longer send you messages."), false);
				}
			}
		}
		return 1;
	}

	private void internalToggleSign(String player) {
		if (!noPrintPlayers.contains(player)) {noPrintPlayers.add(player);}
	}

}