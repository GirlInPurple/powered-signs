package mod.blurple.poweredsigns;

import net.kyori.adventure.sound.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A class containing all the constants used in the project. */
public class Constants {
  /** Referred to as a MOD_ID in both the Fabric and Neoforge docs */
  public static final String PROJECT_ID = "powered_signs";

  /** A basic click sound used in  */
  public static final Sound CLICK_SOUND = Sound
    .sound()
    .build();
}
