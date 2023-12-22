package xyz.poweredsigns.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
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
import java.util.Map;

import static xyz.poweredsigns.PoweredSigns.MODID;
import static xyz.poweredsigns.PoweredSigns.ticksSinceStartup;
import static xyz.poweredsigns.SignUtils.*;

@Mixin(SignBlockEntity.class)
public class SignEntityClientMixin extends BlockEntity {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public SignEntityClientMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Unique
    private static final Map<SignBlockEntity, Integer> cooldownTicksMap = new HashMap<>();

    @Inject(at = @At("HEAD"), method = "tick")
    private static void tickMixin(World world, BlockPos pos, BlockState state, SignBlockEntity blockEntity, CallbackInfo ci) {

        // Initialize Needed Variables
        if (!cooldownTicksMap.containsKey(blockEntity)) {cooldownTicksMap.put(blockEntity, 0);}
        int cooldownTicks = cooldownTicksMap.get(blockEntity);

        BlockPos offsetPos = positionOffset(pos);

        // Checks
        if (!(isBlockPowered(world, offsetPos))) {return;}
        if ((ticksSinceStartup - cooldownTicks) < ModConfig.getInstance().coolDownTicks) {return;}
        else {cooldownTicksMap.put(blockEntity, ticksSinceStartup);}
        if (ModConfig.getInstance().logSignPositions) {LOGGER.info("Sign Position: "+pos);}

        aesthetics(world, pos);
        printToPlayers(world, pos, blockEntity);
        LOGGER.info("This is a "+state);
    }
}
