package xyz.poweredsigns.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.poweredsigns.SignUtils;

import static xyz.poweredsigns.PoweredSigns.noPrintPlayers;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    /**
     * On world save, the .csv file will be saved.
     * */
    @Inject(at = @At("HEAD"), method = "save")
    private void saveMixin(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {SignUtils.writeToggleSigns(noPrintPlayers);}
}