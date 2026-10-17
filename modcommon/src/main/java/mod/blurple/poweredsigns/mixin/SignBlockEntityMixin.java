package mod.blurple.poweredsigns.mixin;

import static mod.blurple.poweredsigns.Constants.PROJECT_ID;
import static mod.blurple.poweredsigns.check.PowerChecks.*;

import java.util.LinkedList;
import java.util.List;
import mod.blurple.poweredsigns.IPrinter;
import mod.blurple.poweredsigns.config.PoweredSignsConfig;
import net.kyori.adventure.audience.Audience;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin {

  @Inject(at = @At("HEAD"), method = "tick")
  private static void tick$poweredsigns(
    Level level,
    BlockPos blockPos,
    BlockState blockState,
    SignBlockEntity signBlockEntity,
    CallbackInfo ci
  ) {
    if (level instanceof ClientLevel) return;
    ServerLevel serverLevel = (ServerLevel) level;

    Block probableSign = blockState.getBlock();
    boolean canPrint = false;

    // TODO everything below here
    // placeholders for syntax completion,
    // all of these need to find homes somewhere else.

    // also configs are platform-specific:
    // paper and neoforge have their own systems but are incredibly different,
    // and fabric doesnt really have one.
    // cloth config says to not use it in server environs, might do anyway.
    // why hasnt a truely universal config api been mode yet??
    PoweredSignsConfig config = null;

    // adding net.kyori.adventure.audience.ForwardingAudience to a sign mixin is probably a good idea
    // took a quick look at the docs and there doesnt seem to be any way to
    // do something like Audience.inRadius(BlockPos, int)
    // so this is a basic reimpl of it until i can get around to making it better.
    List<Audience> withinRadius = new LinkedList<>();
    serverLevel.getAllEntities()
      .forEach(entity -> {
        if (entity.distanceToSqr(Vec3.atLowerCornerOf(blockPos)) <= config.playerDistance)
          withinRadius.add((Audience) entity);
      });
    Audience audience = Audience.audience(withinRadius);

    // the logger needs to be pulled from .platform.PlatformImpl via a ServiceLoader,
    Logger logger = LoggerFactory.getLogger(PROJECT_ID);
    // and the printer from probably the same way.
    IPrinter printer = new IPrinter() { /* TODO */ };

    switch (probableSign) {
      case WallSignBlock sign -> canPrint = WALL.findPoweredBlock(sign, config).canPrint(config);
      case StandingSignBlock sign -> canPrint = STANDING.findPoweredBlock(sign, config).canPrint(config);
      case WallHangingSignBlock sign -> canPrint = WALL_HANGING.findPoweredBlock(sign, config).canPrint(config);
      case CeilingHangingSignBlock sign -> canPrint = CEILING_HANGING.findPoweredBlock(sign, config).canPrint(config);
      default -> logger.warn("Somehow found a block entity that extends SignBlockEntity, but was not one of the main sign types. Almost guaranteed to be a mod/plugin conflict.")
    }

    if (canPrint) {
      printer.print(audience);
    }
  }
}
