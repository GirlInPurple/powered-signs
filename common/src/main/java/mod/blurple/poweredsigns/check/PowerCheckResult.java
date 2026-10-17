package mod.blurple.poweredsigns.check;

import mod.blurple.poweredsigns.config.PoweredSignsConfig;
import net.minecraft.core.BlockPos;

public record PowerCheckResult(
  BlockPos pos,
  boolean fromHangingChain,
  boolean fromHangingLine
) implements IPowerCheckResult {

  @Override
  public boolean canPrint(PoweredSignsConfig config) {
    return true; // TODO
  }
}
