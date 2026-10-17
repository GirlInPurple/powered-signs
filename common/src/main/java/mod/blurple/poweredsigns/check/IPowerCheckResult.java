package mod.blurple.poweredsigns.check;

import mod.blurple.poweredsigns.config.PoweredSignsConfig;

public sealed interface IPowerCheckResult permits NoResult, PowerCheckResult {
  boolean canPrint(PoweredSignsConfig config);
}
