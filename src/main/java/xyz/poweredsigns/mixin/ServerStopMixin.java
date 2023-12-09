package xyz.poweredsigns.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerStopMixin {
    @Inject(at = @At("HEAD"), method = "shutdown")
    private void init(CallbackInfo info) {
        // Save noPrintPlayers to a file in the active world directory so the configs save after restart
        // Not sure how to do this, will be in next commit
    }
}
