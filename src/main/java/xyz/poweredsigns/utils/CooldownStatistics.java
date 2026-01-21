package xyz.poweredsigns.utils;

import xyz.poweredsigns.mixin.SignEntityMixin;

/**
 * A subclass used by {@link SignEntityMixin} to hold custom cooldown data.
 * */
public class CooldownStatistics {
    /** The tick when the sign last printed */
    int lastCall;
    /** A custom cooldown, or how long the sign should not print. */
    int customCooldown;

    public CooldownStatistics(int lastCall, int customCooldown) {
        this.lastCall = lastCall;
        this.customCooldown = customCooldown;
    }

    /** @see #lastCall */
    public int getLastCall() {
        return lastCall;
    }

    /** @see #customCooldown */
    public int getCustomCooldown() {
        return customCooldown;
    }
}
