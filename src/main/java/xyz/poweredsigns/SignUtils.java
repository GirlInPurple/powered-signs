package xyz.poweredsigns;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.poweredsigns.config.ModConfig;

public class SignUtils {

    public static boolean isBlockPowered(World world, BlockPos pos) {
        if (ModConfig.getInstance().strongPowerOnly) {
            // Both Strong and Weak power
            return world.getReceivedRedstonePower(pos) > 0;
        } else {
            // Only Strong power
            return world.getReceivedStrongRedstonePower(pos) > 0;
        }
    }

    public static BlockPos positionOffset(BlockPos pos) {
        return new BlockPos((pos.getX()), (pos.getY() - 1), (pos.getZ()));
    }


    public static BlockPos newPosOffset(BlockPos pos, SignBlockEntity blockEntity, int amount) {
        // fix for wall signs
        return null;
    }
}
