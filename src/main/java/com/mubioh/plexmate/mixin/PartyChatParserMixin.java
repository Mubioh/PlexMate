package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.utils.PartyManager;
import com.mubioh.plexmate.utils.ServerUtils;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class PartyChatParserMixin {

    @Inject(method = "onGameMessage", at = @At("TAIL"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!ServerUtils.isOnMineplex()) return;

        Text text = packet.content();
        PartyManager.updateFromMessage(text);
    }
}
