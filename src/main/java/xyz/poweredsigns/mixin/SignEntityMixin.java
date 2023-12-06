package xyz.poweredsigns.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.List;

import static xyz.poweredsigns.PoweredSigns.MODID;
import static xyz.poweredsigns.PoweredSigns.ticksSinceStartup;

@Mixin(SignBlockEntity.class)
public class SignEntityMixin extends BlockEntity {
    public SignEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Unique
    private static int cooldownTicks = 0;

    @Inject(at = @At("HEAD"), method = "tick")
    private static void tickMixin(World world, BlockPos pos, BlockState state, SignBlockEntity blockEntity, CallbackInfo ci) {
        BlockPos underPos = posOffset(pos, 1); // Grab position of block in need of powering

        if (!(isBlockPowered(world, underPos))) {return;} // If block is not powered, exit
        if ((ticksSinceStartup - cooldownTicks) < ModConfig.getInstance().coolDownTicks) {return;} // If the nbt value is less than 10, exit
        cooldownTicks = ticksSinceStartup;

        List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, new Box(pos).expand(ModConfig.getInstance().playerDistance),player -> true);
        for (PlayerEntity player : players) {
            for (int index = 0; index < 8; index++) {
                int lineIndex = index % 4;

                String tempChatString;
                if (index >= 4) {tempChatString = blockEntity.getText(false).getMessage(lineIndex, false).getString();}
                else {tempChatString = blockEntity.getText(true).getMessage(lineIndex, false).getString();}

                if (!(tempChatString == "")) {player.sendMessage(Text.literal(tempChatString), false);}

                if (ModConfig.getInstance().logSignPositions) {LOGGER.info("Sign Position: "+pos);}
            }
        }
        //world.addParticle(/*particle here*/, pos.getX(), pos.getY(), pos.getZ(), 0, 0.25, 0);
    }

    @Unique
    private static boolean isBlockPowered(World world, BlockPos pos) {
        return world.getReceivedRedstonePower(pos) > 0;
    }

    @Unique
    private static BlockPos posOffset(BlockPos pos, int minus) {
        return new BlockPos((pos.getX()), (pos.getY() - minus), (pos.getZ()));
    }
}
