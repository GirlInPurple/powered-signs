package xyz.poweredsigns.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.poweredsigns.config.ModConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.poweredsigns.PoweredSigns.*;
import static xyz.poweredsigns.SignUtils.*;

@Mixin(SignBlockEntity.class)
public class SignEntityServerMixin extends BlockEntity {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public SignEntityServerMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Unique
    private static final Map<SignBlockEntity, Integer> cooldownTicksMap = new HashMap<>();

    @Inject(at = @At("HEAD"), method = "tick")
    private static void tickMixin(World world, BlockPos pos, BlockState state, SignBlockEntity blockEntity, CallbackInfo ci) {

        // Initialize Needed Variables
        if (!cooldownTicksMap.containsKey(blockEntity)) {cooldownTicksMap.put(blockEntity, 0);}
        int cooldownTicks = cooldownTicksMap.get(blockEntity);

        BlockPos underPos = positionOffset(pos);

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

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(ModConfig.getInstance().playerDistance),player -> true);
        for (PlayerEntity player : players) {
            if (!(noPrintPlayers.contains(player))) {
                for (int index = 0; index < 8; index++) {
                    int lineIndex = index % 4; // 0 1 2 3 0 1 2 3
                    String tempChatString;
                    if (index >= 4) {tempChatString = blockEntity.getText(false).getMessage(lineIndex, false).getString();}// front side
                    else {tempChatString = blockEntity.getText(true).getMessage(lineIndex, false).getString();}// back side
                    if (!(tempChatString.equals(""))) {player.sendMessage(Text.literal(tempChatString), false);}
                }
            }
        }
    }
}
