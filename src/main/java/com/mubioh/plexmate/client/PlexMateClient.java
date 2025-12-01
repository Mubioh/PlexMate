package com.mubioh.plexmate.client;

import com.mubioh.plexmate.features.FeatureRegistry;
import com.mubioh.plexmate.mixin.KeyBindingAccessor;
import com.mubioh.plexmate.settings.PlexmateSettingsScreen;
import com.mubioh.plexmate.settings.config.Config;
import com.mubioh.plexmate.settings.PlexmateOptions;
import com.mubioh.plexmate.utils.KeybindUtils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PlexMateClient implements ClientModInitializer {

    public static Config config;

    @Override
    public void onInitializeClient() {
        config = Config.load();
        PlexmateOptions.applyConfig(config);

        ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
            private boolean configApplied = false;

            @Override
            public void onEndTick(MinecraftClient client) {
                if (!configApplied && client.options != null) {
                    PlexmateOptions.applyConfig(config);
                    configApplied = true;
                }
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.getWindow() == null) return;

            int keyCode = ((KeyBindingAccessor) KeybindUtils.SETTINGS_SCREEN_KEY)
                    .getBoundKey()
                    .getCode();

            boolean isPressed = InputUtil.isKeyPressed(client.getWindow(), keyCode);
            boolean isGameKeyPress = KeybindUtils.SETTINGS_SCREEN_KEY.wasPressed();

            if (client.player == null) {
                if (isPressed) {
                    client.setScreen(new PlexmateSettingsScreen(null, client.options));
                }
                return;
            }

            if (isGameKeyPress) {
                client.setScreen(new PlexmateSettingsScreen(null, client.options));
            }
        });

//        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if (InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_RIGHT_CONTROL)) {
//                client.setScreen(new com.mubioh.plexmate.settings.PlexmateSettingsScreen(null, client.options));
//            }
//        });

        KeybindUtils.registerAll();
        FeatureRegistry.initialize();
    }
}
