package mod.blurple.poweredsigns.check;

import mod.blurple.poweredsigns.config.PoweredSignsConfig;

public enum NoResult implements IPowerCheckResult {
  INSTANCE;
  @Override public boolean canPrint(PoweredSignsConfig config) { return false; }
}
