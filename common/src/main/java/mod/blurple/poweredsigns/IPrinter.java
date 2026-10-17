package mod.blurple.poweredsigns;

import static mod.blurple.poweredsigns.Constants.CLICK_SOUND;

import net.kyori.adventure.audience.Audience;

public interface IPrinter() {

  default void print(Audience audience) {
    audience.playSound(CLICK_SOUND);
    audience.
  }
}
