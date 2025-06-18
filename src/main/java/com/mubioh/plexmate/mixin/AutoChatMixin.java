package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.client.PlexMateClient;
import com.mubioh.plexmate.utils.ServerUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
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

    private void handleMessage(Text text) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (text == null || player == null || !PlexMateClient.config.autoGG) return;

        String message = text.getString();

        for (String trigger : GG_TRIGGERS) {
            if (message.contains(trigger) && (ServerUtils.isServerMessage(text) || message.replaceAll("§[0-9a-fk-or]", "").toLowerCase().contains(trigger.toLowerCase()))) {
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

    @Inject(method = "onGameMessage", at = @At("TAIL"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (!ServerUtils.isOnMineplex()) return;
        handleMessage(packet.content());
    }

    @Inject(method = "onChatMessage", at = @At("TAIL"))
    private void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        if (!ServerUtils.isOnMineplex()) return;
        handleMessage(packet.unsignedContent());
    }
}
