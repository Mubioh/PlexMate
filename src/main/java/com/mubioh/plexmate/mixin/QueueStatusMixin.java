package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.utils.ServerUtils;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class QueueStatusMixin {

    @Inject(method = "onGameMessage", at = @At("TAIL"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!ServerUtils.isOnMineplex()) return;
        ServerUtils.updateQueueStatusFromMessage(packet.content());
    }
}
