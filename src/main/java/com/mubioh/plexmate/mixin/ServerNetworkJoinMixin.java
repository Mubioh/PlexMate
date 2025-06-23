package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.utils.ServerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ServerNetworkJoinMixin {

    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (ServerUtils.getShouldQueueClans()) {
            ServerUtils.setShouldQueueClans(false);
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.player != null) {
                client.player.networkHandler.sendChatCommand("queue clans");
            }
        }
    }
}
