package com.mubioh.plexmate.settings;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

public class PlexmateSettingsScreen extends GameOptionsScreen {

    public PlexmateSettingsScreen(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.literal("Plexmate Settings"));
    }

    @Override
    protected void addOptions() {
        this.body.addAll(
                PlexmateOptions.CLICK_ACTION_MODE,
                PlexmateOptions.SHOW_OWN_NAMETAG
        );
        this.body.addSingleOptionEntry(PlexmateOptions.SELECTED_DOMAIN);
        this.body.addAll(
                PlexmateOptions.AUTO_GG,
                PlexmateOptions.AUTO_GG_COOLDOWN
        );
        this.body.addAll(
                PlexmateOptions.AUTO_ACCEPT_FRIENDS,
                PlexmateOptions.SELECTED_GAME
        );
    }
}
