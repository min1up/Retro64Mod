package com.dylanpdx.retro64.mixin;

import com.dylanpdx.retro64.RemoteMCharHandler;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;

import static com.dylanpdx.retro64.mappingsConvert.m_tryCheckInsideBlocks;

@Mixin(Player.class)
public class MixinPlayerTravel {

    @Inject(at=@At("HEAD"),method="Lnet/minecraft/entity/player/PlayerEntity;travel(Lnet/minecraft/util/math/Vec3d;)V", cancellable = true)
    private void plrTravel(CallbackInfo ci){
        var thisPlr = ((PlayerEntity)(Object)this);
        if (!thisPlr.isMainPlayer())   //Is this even right?????
            return;
        if (RemoteMCharHandler.getIsMChar(thisPlr)){
            try {
                m_tryCheckInsideBlocks.invoke(thisPlr);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            ci.cancel();
        }

    }

}
