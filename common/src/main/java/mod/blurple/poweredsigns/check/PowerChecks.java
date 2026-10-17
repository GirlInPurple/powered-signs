package mod.blurple.poweredsigns.check;

import mod.blurple.poweredsigns.config.PoweredSignsConfig;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;

@FunctionalInterface
public interface PowerChecks<SignType extends SignBlock> {

  PowerChecks<WallSignBlock> WALL = new PowerChecks<>() {
    @Override
    public PowerCheckResult findPoweredBlock(WallSignBlock sign, PoweredSignsConfig config) {
      return ;
    }
  };
  PowerChecks<StandingSignBlock> STANDING = new PowerChecks<StandingSignBlock>() {
    @Override
    public PowerCheckResult findPoweredBlock(StandingSignBlock sign, PoweredSignsConfig config) {
      return null;
    }
  };

  PowerChecks<WallHangingSignBlock> WALL_HANGING = new PowerChecks<WallHangingSignBlock>() {
    @Override
    public PowerCheckResult findPoweredBlock(WallHangingSignBlock sign,
      PoweredSignsConfig config) {
      return null;
    }
  };
  PowerChecks<CeilingHangingSignBlock> CEILING_HANGING = new PowerChecks<CeilingHangingSignBlock>() {
    @Override
    public PowerCheckResult findPoweredBlock(CeilingHangingSignBlock sign,
      PoweredSignsConfig config) {
      return null;
    }
  };

  PowerCheckResult findPoweredBlock(
    SignType sign,
    PoweredSignsConfig config
  );
}
