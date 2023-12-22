package xyz.poweredsigns.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.poweredsigns.SignUtils;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(at = @At("HEAD"), method = "shutdown")
    private void shutdownMixin(CallbackInfo info) {
        SignUtils.writeCSV();
    }
}
