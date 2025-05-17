package com.mubioh.plexmate.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class ServerUtils {

    private static boolean isQueued = false;

    public static boolean isOnMineplex() {
        MinecraftClient client = MinecraftClient.getInstance();
        ServerInfo serverInfo = client.getCurrentServerEntry();
        if (serverInfo == null) return false;

        String ip = serverInfo.address.toLowerCase();
        return ip.endsWith(".mineplex.com") || ip.equals("mineplex.com");
    }

    public static boolean isServerMessage(Text text) {
        if (text == null) return false;

        TextColor color = text.getStyle().getColor();
        if (color != null && !color.equals(TextColor.fromFormatting(Formatting.WHITE))) {
            return true;
        }

        for (Text sibling : text.getSiblings()) {
            if (isServerMessage(sibling)) return true;
        }

        return false;
    }

    public static boolean isQueued() {
        return isQueued;
    }

    public static void setQueued(boolean queued) {
        isQueued = queued;
    }

    public static void updateQueueStatusFromMessage(Text message) {
        String msg = message.getString().toLowerCase();

        if (msg.contains("joined queue") && isServerMessage(message)) {
            setQueued(true);
        } else if (msg.contains("removed you from game queues.") && isServerMessage(message)) {
            setQueued(false);
        }
    }

}
