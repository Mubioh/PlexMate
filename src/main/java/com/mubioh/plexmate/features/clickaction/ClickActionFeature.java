package com.mubioh.plexmate.features.clickaction;

import com.mojang.serialization.Codec;
import com.mubioh.plexmate.features.Feature;
import com.mubioh.plexmate.settings.PlexmateOptions;
import com.mubioh.plexmate.utils.PartyManager;
import com.mubioh.plexmate.utils.ServerUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;

public class ClickActionFeature implements Feature {

    public enum ClickMode {
        FRIEND,
        PARTY,
        DISABLED;

        public static final Codec<ClickMode> CODEC = Codec.STRING.xmap(
                str -> {
                    try {
                        return ClickMode.valueOf(str.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return FRIEND;
                    }
                },
                ClickMode::name
        );

        @Override
        public String toString() {
            return switch (this) {
                case FRIEND -> "Friend";
                case PARTY -> "Party";
                case DISABLED -> "Disabled";
            };
        }
    }

    private static ClickMode mode = PlexmateOptions.CLICK_ACTION_MODE.getValue();

    public static void onClick(MinecraftClient client) {
        if (client.player == null || client.getNetworkHandler() == null) return;
        if (!ServerUtils.isOnMineplex()) return;
        if (mode == ClickMode.DISABLED) return;

        if (client.crosshairTarget instanceof EntityHitResult entityHit &&
                entityHit.getEntity() instanceof PlayerEntity targetPlayer &&
                !targetPlayer.equals(client.player)) {

            String name = targetPlayer.getGameProfile().getName();
            boolean shift = client.options.sneakKey.isPressed();
            String baseCommand = mode == ClickMode.FRIEND ? "friend" : "party";

            String subCommand;
            if (mode == ClickMode.FRIEND) {
                subCommand = shift ? "remove" : "add";
            } else {
                if (shift) {
                    subCommand = PartyManager.isMember(name) ? "kick" : "revoke";
                } else {
                    subCommand = "invite";
                }
            }

            client.player.networkHandler.sendChatCommand(baseCommand + " " + subCommand + " " + name);
        }
    }

    public static void setMode(ClickMode newMode) {
        mode = newMode;
    }

    public static ClickMode getMode() {
        return mode;
    }

    @Override
    public void initialize() {}
}
