package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.client.PlexMateClient;
import com.mubioh.plexmate.utils.ServerUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class AutoFriendMixin {

    @Inject(method = "onGameMessage", at = @At("TAIL"))
    private void onFriendRequestMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!ServerUtils.isOnMineplex()) return;
        if (!PlexMateClient.config.autoAcceptFriendRequests) return;

        Text text = packet.content();
        if (text == null || !ServerUtils.isServerMessage(text)) return;

        String msg = text.getString();
        if (msg.contains("has sent you a friend request")) {
            String[] parts = msg.split(" ");
            if (parts.length >= 2) {
                String name = parts[1];
                MinecraftClient.getInstance().player.networkHandler.sendChatCommand("friend add " + name);
            }
        }
    }
}
