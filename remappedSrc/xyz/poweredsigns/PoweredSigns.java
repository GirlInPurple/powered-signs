package xyz.poweredsigns;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.SignBlock;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoweredSigns implements ModInitializer {

	public static String MODID = "poweredsigns";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		LOGGER.info("Signs can now be powered!");
	}

	@SubscribeEvent
	public void onBlockNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
		Block block = event.getState().getBlock();
		if (block instanceof SignBlock) {
			BlockPos signPos = event.getPos();
			if (block.isPowered()) {
				List<PlayerEntity> players = event.getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(signPos).grow(32));
				for (PlayerEntity player : players) {
					player.sendMessage(new TextComponentString(block.getText().get(0).getString()), Util.DUMMY_UUID);
				}
			}
		}
	}
}