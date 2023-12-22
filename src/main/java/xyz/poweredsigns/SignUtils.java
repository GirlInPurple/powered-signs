package xyz.poweredsigns;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.poweredsigns.config.ModConfig;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static xyz.poweredsigns.PoweredSigns.MODID;
import static xyz.poweredsigns.PoweredSigns.noPrintPlayers;

public class SignUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static boolean isBlockPowered(World world, BlockPos pos) {
        if (!ModConfig.getInstance().strongPowerOnly) {
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

    public static void printToPlayers(World world, BlockPos pos, SignBlockEntity blockEntity) {
        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(ModConfig.getInstance().playerDistance), player -> true);
        for (PlayerEntity player : players) {
            if (!(player instanceof ClientPlayerEntity) && !(noPrintPlayers.contains(player.getName().toString()))) {
                for (int index = 0; index < 8; index++) {
                    int lineIndex = index % 4; // 0 1 2 3 0 1 2 3
                    String tempChatString;
                    if (index >= 4) {tempChatString = blockEntity.getText(false).getMessage(lineIndex, false).getString();} // front side
                    else {tempChatString = blockEntity.getText(true).getMessage(lineIndex, false).getString();} // back side
                    if (!(tempChatString.equals(""))) {player.sendMessage(Text.literal(tempChatString), false);}
                }
            }
        }
    }

    public static void aesthetics(World world, BlockPos pos) {
        if (ModConfig.getInstance().particles) { // Client Exclusive
            world.addParticle(
                    new DustParticleEffect(DustParticleEffect.RED, 1),
                    pos.getX() + 0.5,
                    pos.getY() + 0.75,
                    pos.getZ() + 0.5,
                    0,
                    0,
                    0
            );
        }
        if (ModConfig.getInstance().audio) {
            world.playSound(
                    null,
                    pos,
                    SoundEvents.BLOCK_LEVER_CLICK,
                    SoundCategory.BLOCKS,
                    0.5f,
                    1f
            );
        }
    }

    public static void readCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("./config/players.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                noPrintPlayers.add(Arrays.toString(values));
            }
            LOGGER.info("Set noPrintPlayers to: "+noPrintPlayers);
        } catch (IOException e) {
            LOGGER.error(String.valueOf(e));
        }
    }

    public static void writeCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./config/players.csv"))) {
            for (String row : noPrintPlayers) {
                bw.write(row +", false");
                bw.newLine();
            }
            LOGGER.info("Set ./config/players.csv to: "+noPrintPlayers);
        } catch (IOException e) {
            LOGGER.error(String.valueOf(e));
        }
    }
}
