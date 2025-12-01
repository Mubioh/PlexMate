package com.mubioh.plexmate.utils;

import com.mubioh.plexmate.features.clickaction.ClickActionFeature;
import com.mubioh.plexmate.features.gamequeue.GameQueueFeature;
import com.mubioh.plexmate.settings.PlexmateOptions;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeybindUtils {
    private static final Map<KeyBinding, Consumer<MinecraftClient>> BINDINGS = new LinkedHashMap<>();
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of("plexmate.mineplex_games"));

    public static KeyBinding SETTINGS_SCREEN_KEY;

    public static void registerAll() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Map.Entry<KeyBinding, Consumer<MinecraftClient>> entry : BINDINGS.entrySet()) {
                if (entry.getKey().wasPressed()) {
                    entry.getValue().accept(client);
                }
            }
        });

        SETTINGS_SCREEN_KEY = addKeybind(
                "key.plexmate.open_settings",
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                client -> {}
        );

        addMouseKeybind("key.plexmate.player_action", GLFW.GLFW_MOUSE_BUTTON_MIDDLE, client -> {
            if (client.player != null && client.getNetworkHandler() != null && ServerUtils.isOnMineplex()) {
                ClickActionFeature.onClick(client);
            }
        });

        addMouseKeybind("key.plexmate.queue_unqueue", GLFW.GLFW_MOUSE_BUTTON_5, client -> {
            if (client.player != null && client.getNetworkHandler() != null && ServerUtils.isOnMineplex()) {
                String selectedCommand = GameQueueFeature.GAME_MODE_COMMANDS.get(PlexmateOptions.SELECTED_GAME.getValue());
                String command = ServerUtils.isQueued() ? "unqueue" : "queue " + selectedCommand;

                client.player.networkHandler.sendChatCommand(command);
                ServerUtils.setQueued(!ServerUtils.isQueued());
            }
        });
    }

    public static KeyBinding addKeybind(String name, int glfwKey, Consumer<MinecraftClient> callback) {
        return addKeybind(name, InputUtil.Type.KEYSYM, glfwKey, callback);
    }

    public static KeyBinding addMouseKeybind(String name, int mouseButton, Consumer<MinecraftClient> callback) {
        return addKeybind(name, InputUtil.Type.MOUSE, mouseButton, callback);
    }

    public static KeyBinding addKeybind(String name, InputUtil.Type type, int code, Consumer<MinecraftClient> callback) {
        KeyBinding keyBind = new KeyBinding(name, type, code, CATEGORY);
        KeyBindingHelper.registerKeyBinding(keyBind);
        BINDINGS.put(keyBind, callback);
        return keyBind;
    }
}
