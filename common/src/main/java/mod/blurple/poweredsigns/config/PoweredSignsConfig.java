package mod.blurple.poweredsigns.config;

public class PoweredSignsConfig {

  /** How far away */
  public int playerDistance = 48;

  /** Log positions of signs that are printed to the console.
   * Set to false on boot to reduce log spam. */
  public boolean logSignPositionsToConsole = false;

  /** Log positions of signs that are printed to a CSV file.
   * Generally advised over logging to the console. */
  public boolean logSignPositionsToFile = false;

  /** Create redstone particle on print. */
  public boolean particles = true;

  /** Create a click sound on print. */
  public boolean audio = true;

  /**  */
  public boolean strongPowerOnly = true;

  /** Refuses to print the content of signs where the text wouldn't be visible.
   * The only case where this applies is wall-mounted signs,
   * where the side of the sign facing the wall will not be printed. */
  public boolean strictSignContent = true;
}
