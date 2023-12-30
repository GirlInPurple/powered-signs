package xyz.poweredsigns.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.poweredsigns.config.ModConfig;
import xyz.poweredsigns.mixin.SignEntityMixin;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.poweredsigns.PoweredSigns.*;
import static xyz.poweredsigns.utils.SignColorEnum.ColorFromString;

public class SignUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    private static final String PlayerJsonPath = "./config/poweredsignsplayers.json";

    /**
     * This hashmap is used to store the data of the sign.
     * Due to how static classes work, we cant use the "{@code this}" keyword, so this is a workaround.
     * It stores the specific entity and cooldown data for that entity.
     * Look at {@link CooldownStatistics} for more context.
     * */
    private static final Map<SignBlockEntity, CooldownStatistics> CooldownHashmap = new WeakHashMap<>();
    public static Map<SignBlockEntity, CooldownStatistics> getCooldownHashmap() {return CooldownHashmap;}

    /**
     * Checks if the block below is powered.
     * If the block is transparent, it returns false no matter what.
     *
     * @param pos Position of the block to be checked
     * @param world World to referenced
     * @return A boolean value of if the block is powered
     * */
    public static boolean isBlockPowered(World world, BlockPos pos) {
        boolean transparentBlock = world.getBlockState(pos).getBlock().isTransparent(world.getBlockState(pos), world, pos);
        if (!ModConfig.getInstance().strongPowerOnly) {
            // Both Strong and Weak power
            if (!transparentBlock) {return world.getReceivedRedstonePower(pos) > 0;}
            else {return false;}
        } else {
            // Only Strong power
            if (!transparentBlock) {return world.getReceivedStrongRedstonePower(pos) > 0;}
            else {return false;}
        }
    }

    /**
     * Checks if {@code ModConfig.getInstance().legacyPoweringSystem} is true, if it is, skip this method completely.
     * Then checks what kind of sign it is, and gives a specific offset depending on the type and angle of the sign.
     *
     * @param pos The position of the sign.
     * @param blockEntity The internal data of the sign.
     * @param state The block state of the sign.
     * @return Returns a {@link net.minecraft.util.math.BlockPos} that is offset by the wanted amount.
     */
    public static BlockPos positionOffset(BlockPos pos, BlockState state, SignBlockEntity blockEntity) {
        if (ModConfig.getInstance().legacyPoweringSystem) {return new BlockPos((pos.getX()), (pos.getY() - 1), (pos.getZ()));}

        Direction signDirection = getSignFacing(state);
        if (blockEntity instanceof HangingSignBlockEntity) {
            if (signDirection == null) {return new BlockPos((pos.getX()), (pos.getY()+1), (pos.getZ()));}
            if (signDirection == Direction.NORTH) {return new BlockPos((pos.getX()+1), (pos.getY()), (pos.getZ()));}
            if (signDirection == Direction.SOUTH) {return new BlockPos((pos.getX()-1), (pos.getY()), (pos.getZ()));}
            if (signDirection == Direction.EAST) {return new BlockPos((pos.getX()), (pos.getY()), (pos.getZ()+1));}
            if (signDirection == Direction.WEST) {return new BlockPos((pos.getX()), (pos.getY()), (pos.getZ()-1));}
        }
        if (blockEntity instanceof SignBlockEntity) {
            if (signDirection == null) {return new BlockPos((pos.getX()), (pos.getY()-1), (pos.getZ()));}
            if (signDirection == Direction.NORTH) {return new BlockPos((pos.getX()), (pos.getY()), (pos.getZ()-1));}
            if (signDirection == Direction.SOUTH) {return new BlockPos((pos.getX()), (pos.getY()), (pos.getZ()+1));}
            if (signDirection == Direction.EAST) {return new BlockPos((pos.getX()-1), (pos.getY()), (pos.getZ()));}
            if (signDirection == Direction.WEST) {return new BlockPos((pos.getX()+1), (pos.getY()), (pos.getZ()));}
        }
        return new BlockPos((pos.getX()), (pos.getY() - 1), (pos.getZ()));
    }

    /**
     * A public method used by {@link SignEntityMixin} to print to the chat.
     * Calls {@link #innerPrint(SignBlockEntity, PlayerEntity)} repeatedly.
     *
     * @param world The current World.
     * @param pos The position of the sign. Can be offset.
     * @param blockEntity The internal data of the sign.
     */
    public static void printToPlayers(World world, BlockPos pos, SignBlockEntity blockEntity) {
        int customBox = ModConfig.getInstance().playerDistance;
        Matcher matcher = SignRegex(blockEntity);

        if (matcher.matches()) {
            if (!Objects.equals(matcher.group(1), "-") && Integer.parseInt(matcher.group(1)) < ModConfig.getInstance().playerDistance) {
                customBox = Integer.parseInt(matcher.group(1));
            }

            if (!Objects.equals(matcher.group(2), null) && Integer.parseInt(matcher.group(2)) > ModConfig.getInstance().coolDownTicks) {
                getCooldownHashmap().put(blockEntity, new CooldownStatistics(ticksSinceStartup, Integer.parseInt(matcher.group(2))));
            }
        }

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(customBox), player -> true);
        for (PlayerEntity player : players) {
            if (noPrintPlayers != null && !(noPrintPlayers.contains(player.getName().getString()))) {
                switch (FabricLoader.getInstance().getEnvironmentType()) {
                    case CLIENT -> {if (!(player instanceof ClientPlayerEntity)) {innerPrint(blockEntity, player);}}
                    case SERVER -> {innerPrint(blockEntity, player);}
                }
            }
        }
    }

    /**
     * A method used internally to print out to players.
     * This will be called multiple times per tick, so be careful modifying it.
     *
     * @param blockEntity The sign in question
     * @param player A PlayerEntity to target
     */
    private static void innerPrint(SignBlockEntity blockEntity, PlayerEntity player) {
        for (int index = 0; index < 8; index++) {
            int lineIndex = index % 4; // 0 1 2 3 0 1 2 3
            String tempChatString = "";
            boolean RegexCheck;
            boolean sideIndex;
            String boldText;
            if (index >= 4) {sideIndex = false;} else {sideIndex = true;}
            if (blockEntity.getText(sideIndex).isGlowing()) {boldText = "§l";} else {boldText = "";}
            if (SignRegex(blockEntity).matches() && index == 0) {RegexCheck = false;} else {RegexCheck = true;}

            tempChatString = tempChatString + ColorFromString(blockEntity.getText(sideIndex).getColor().toString()).getValue();
            tempChatString = tempChatString + boldText;
            tempChatString = tempChatString + blockEntity.getText(sideIndex).getMessage(lineIndex, false).getString();
            tempChatString = tempChatString + ColorFromString("RESET").getValue();

            if (!(blockEntity.getText(sideIndex).getMessage(lineIndex, false).getString().equals("")) && RegexCheck) {
                player.sendMessage(Text.literal(tempChatString), false);
            }
        }
    }

    /**
     * Applies particles and audio to the sign block.
     * Particles are client-side only.
     *
     * @param pos Position of the sign to be acted apon
     * @param world World to referenced
     * */
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

    /**
     * Checks the Horizontal Facing of a blockentity
     *
     * @param state The blockstate needed to check.
     * */
    public static Direction getSignFacing(BlockState state) {
        try {
            return state.get(Properties.HORIZONTAL_FACING);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * @param blockEntity A sign to check the first line of
     * @return A Matcher for the blockEntity given
     * */
    public static Matcher SignRegex(SignBlockEntity blockEntity) {
        Pattern pattern = Pattern.compile("\\[PS\\] (-?\\d*)\\s*(\\d*)");
        return pattern.matcher(blockEntity.getText(true).getMessage(0, false).getString());
    }

    public static List<String> readToggleSigns() {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> TemporaryJson = gson.fromJson(new FileReader(PlayerJsonPath), type);
            if (TemporaryJson == null) {
                LOGGER.info("The /togglesigns list has been reset due to a possible error, as the output was null.");
                return new ArrayList<>();
            }
            else {return TemporaryJson;}
        } catch (Exception e) {
            LOGGER.info("The /togglesigns list has been reset due to an error: "+e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void writeToggleSigns(List<String> ToggleSigns) {
        try (FileWriter writer = new FileWriter(PlayerJsonPath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(ToggleSigns, writer);
            LOGGER.info("Saving list for /togglesigns in " + PlayerJsonPath);
        } catch (Exception e) {
            LOGGER.info(String.valueOf(e));
        }
    }
}
