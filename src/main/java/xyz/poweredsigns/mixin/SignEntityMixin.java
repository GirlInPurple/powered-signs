package xyz.poweredsigns.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.poweredsigns.config.ModConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.poweredsigns.PoweredSigns.MODID;
import static xyz.poweredsigns.PoweredSigns.ticksSinceStartup;

@Mixin(SignBlockEntity.class)
public class SignEntityMixin extends BlockEntity {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public SignEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Unique
    private static final Map<SignBlockEntity, Integer> cooldownTicksMap = new HashMap<>();

    @Inject(at = @At("HEAD"), method = "tick")
    private static void tickMixin(World world, BlockPos pos, BlockState state, SignBlockEntity blockEntity, CallbackInfo ci) {

        // Initialize Needed Variables
        if (!cooldownTicksMap.containsKey(blockEntity)) {cooldownTicksMap.put(blockEntity, 0);}
        int cooldownTicks = cooldownTicksMap.get(blockEntity);

        BlockPos underPos = posOffset(pos);

        // Checks
        if (!(isBlockPowered(world, underPos))) {return;}

        if ((ticksSinceStartup - cooldownTicks) < ModConfig.getInstance().coolDownTicks) {return;}
        else {cooldownTicksMap.put(blockEntity, ticksSinceStartup);}

        if (ModConfig.getInstance().logSignPositions) {LOGGER.info("Sign Position: "+pos);}

        if (ModConfig.getInstance().particles) {
            world.addParticle(
                    new DustParticleEffect(DustParticleEffect.RED, 1),
                    pos.getX() + 0.5,
                    pos.getY() + 0.75,
                    pos.getZ() + 0.5,
                    0,
                    0.25,
                    0
            );
        }
        // Particles are rendered on the server, sorry about that, nothing I can really do about it

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(ModConfig.getInstance().playerDistance),player -> true);
        for (PlayerEntity player : players) {
            if (!(player instanceof ServerPlayerEntity)) {
                // ServerPlayerEntity is the player entity for LAN and SP worlds
                // Skip it, or it will send to host player twice
                for (int index = 0; index < 8; index++) {
                    int lineIndex = index % 4; // 0 1 2 3 0 1 2 3
                    String tempChatString;
                    if (index >= 4) {tempChatString = blockEntity.getText(false).getMessage(lineIndex, false).getString();}
                    // front side
                    else {tempChatString = blockEntity.getText(true).getMessage(lineIndex, false).getString();}
                    // back side
                    if (!(tempChatString.equals(""))) {player.sendMessage(Text.literal(tempChatString), false);}
                }
            }
        }
    }

    @Unique
    private static boolean isBlockPowered(World world, BlockPos pos) {
        return world.getReceivedRedstonePower(pos) > 0;
        /*
        Disabled this section due to it not working
        I will fix it at a later date

        if (ModConfig.getInstance().powerMode) {
            // Both Strong and Weak power
            return world.getReceivedRedstonePower(pos) > 0;
        }
        else {
            // Only Strong power
            BlockState state = world.getBlockState(pos);
            for (Direction direction : Direction.values()) {
                if (state.getStrongRedstonePower(world, pos, direction) > 0) {
                    return true;
                }
            }
            return false;
        }
        */
    }

    @Unique
    private static BlockPos posOffset(BlockPos pos) {
        return new BlockPos((pos.getX()), (pos.getY() - 1), (pos.getZ()));
    }
}
