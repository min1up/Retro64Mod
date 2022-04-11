package com.dylanpdx.retro64.mixin;

import com.dylanpdx.retro64.RemoteMCharHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class MixinCamera {

    @Inject(at=@At("HEAD"), method="Lnet/minecraft/client/Camera;getMaxZoom(D)D", cancellable = true)
    private void getMaxZoom(double pStartingDistance, CallbackInfoReturnable<Double> callback){
        if (RemoteMCharHandler.getIsMChar(MinecraftClient.getInstance().player))
            callback.setReturnValue(pStartingDistance);
    }
}
