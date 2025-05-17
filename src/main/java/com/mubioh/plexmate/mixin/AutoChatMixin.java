package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.client.PlexMateClient;
import com.mubioh.plexmate.utils.ServerUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayNetworkHandler.class)
public class AutoChatMixin {

    private static final List<String> GG_TRIGGERS = List.of(
            "has won the game!",
            "You have defeated",
            "1st Place",
            "Game over! The",
            "Won the game"
    );

    @Inject(method = "onGameMessage", at = @At("TAIL"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!ServerUtils.isOnMineplex()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (!PlexMateClient.config.autoGG || player == null) return;

        Text text = packet.content();
        if (text == null) return;

        for (String trigger : GG_TRIGGERS) {
            if (text.getString().contains(trigger) && ServerUtils.isServerMessage(text)) {
                int delaySeconds = PlexMateClient.config.autoGGCooldown;

                new Thread(() -> {
                    try {
                        Thread.sleep(delaySeconds * 1000L);
                        client.execute(() -> {
                            ClientPlayerEntity delayedPlayer = client.player;
                            if (delayedPlayer != null) {
                                delayedPlayer.networkHandler.sendChatMessage("gg");
                            }
                        });
                    } catch (InterruptedException ignored) {
                    }
                }).start();

                break;
            }
        }
    }
}
