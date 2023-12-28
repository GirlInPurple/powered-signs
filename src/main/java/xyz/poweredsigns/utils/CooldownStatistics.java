package xyz.poweredsigns.utils;

import xyz.poweredsigns.mixin.SignEntityMixin;

/**
 * A subclass used by {@link SignEntityMixin} to hold custom cooldown data.
 * */
public class CooldownStatistics {
    int lastCall;
    int customCooldown;

    public CooldownStatistics(int lastCall, int customCooldown) {
        this.lastCall = lastCall;
        this.customCooldown = customCooldown;
    }

    public int getLastCall() {
        return lastCall;
    }

    public int getCustomCooldown() {
        return customCooldown;
    }
}
