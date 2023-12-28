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
import xyz.poweredsigns.utils.CooldownStatistics;

import static xyz.poweredsigns.PoweredSigns.MODID;
import static xyz.poweredsigns.PoweredSigns.ticksSinceStartup;
import static xyz.poweredsigns.utils.SignUtils.*;

@Mixin(SignBlockEntity.class)
public class SignEntityMixin extends BlockEntity {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public SignEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private static void tickMixin(World world, BlockPos pos, BlockState state, SignBlockEntity blockEntity, CallbackInfo ci) {

        if (blockEntity.isRemoved()) {getCooldownHashmap().remove(blockEntity);}

        if (!getCooldownHashmap().containsKey(blockEntity)) {getCooldownHashmap().put(blockEntity, new CooldownStatistics(ticksSinceStartup, ModConfig.getInstance().coolDownTicks));}

        BlockPos offsetPos = positionOffset(pos, state, blockEntity);
        if (!(isBlockPowered(world, offsetPos))) {return;}

        if ((ticksSinceStartup - getCooldownHashmap().get(blockEntity).getLastCall()) < getCooldownHashmap().get(blockEntity).getCustomCooldown()) {return;}
        else {getCooldownHashmap().put(blockEntity, new CooldownStatistics(ticksSinceStartup, getCooldownHashmap().get(blockEntity).getCustomCooldown()));}

        if (ModConfig.getInstance().logSignPositions) {LOGGER.info("Sign Position: "+pos);}

        aesthetics(world, pos);
        printToPlayers(world, pos, blockEntity);
    }
}
