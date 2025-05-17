package com.mubioh.plexmate.settings;

import com.mojang.serialization.Codec;
import com.mubioh.plexmate.client.PlexMateClient;
import com.mubioh.plexmate.features.clickaction.ClickActionFeature;
import com.mubioh.plexmate.features.gamequeue.GameQueueFeature;
import com.mubioh.plexmate.settings.config.Config;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static java.util.List.of;

public class PlexmateOptions {

    public static final SimpleOption<ClickActionFeature.ClickMode> CLICK_ACTION_MODE = new SimpleOption<>(
            "Click Action",
            option -> Tooltip.of(Text.of("Middle-click a player to friend or party them. Hold Shift to remove them instead.")),
            (option, value) -> Text.literal(String.valueOf(value)),
            new SimpleOption.PotentialValuesBasedCallbacks<>(
                    of(ClickActionFeature.ClickMode.FRIEND, ClickActionFeature.ClickMode.PARTY, ClickActionFeature.ClickMode.DISABLED),
                    ClickActionFeature.ClickMode.CODEC
            ),
            ClickActionFeature.ClickMode.FRIEND,
            newValue -> {
                ClickActionFeature.setMode(newValue);
                PlexMateClient.config.clickMode = newValue;
                PlexMateClient.config.save();
            }
    );

    public static final SimpleOption<Boolean> SHOW_OWN_NAMETAG = new SimpleOption<>(
            "Nametag",
            option -> Tooltip.of(Text.of("Show your own nameplate in third person view.")),
            (option, value) -> Text.literal(value ? "Shown" : "Hidden"),
            SimpleOption.BOOLEAN,
            true,
            newValue -> {
                PlexMateClient.config.showOwnNametag = newValue;
                PlexMateClient.config.save();
            }
    );

    public static final SimpleOption<Boolean> SELECTED_DOMAIN = new SimpleOption<>(
            "Home Server",
            option -> Tooltip.of(Text.of("Choose which server to join from the main menu.")),
            (option, value) -> Text.literal("§e" + (value ? "clans.mineplex.com" : "mineplex.com")),
            SimpleOption.BOOLEAN,
            false,
            newValue -> {
                PlexMateClient.config.useClansDomain = newValue;
                PlexMateClient.config.save();
            }
    );

    public static final SimpleOption<Boolean> AUTO_GG = new SimpleOption<>(
            "AutoGG",
            option -> Tooltip.of(Text.of("Automatically says GG when a game ends.")),
            (option, value) -> Text.literal(value ? "Enabled" : "Disabled"),
            SimpleOption.BOOLEAN,
            true,
            newValue -> {
                PlexMateClient.config.autoGG = newValue;
                PlexMateClient.config.save();
            }
    );

    public static final SimpleOption<Integer> AUTO_GG_COOLDOWN = new SimpleOption<>(
            "AutoGG Cooldown",
            option -> Tooltip.of(Text.literal("Delay before AutoGG message is sent (if enabled).")),
            (option, value) -> Text.literal("AutoGG delay: " + value + " seconds"),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 5),
            1,
            value -> {
                PlexMateClient.config.autoGGCooldown = value;
                PlexMateClient.config.save();
            }
    );

    public static final SimpleOption<String> SELECTED_GAME = new SimpleOption<>(
            "Game Mode",
            option -> Tooltip.of(Text.of("Select which Mineplex game to queue when pressing the key.")),
            (option, value) -> Text.literal(value),
            new SimpleOption.PotentialValuesBasedCallbacks<>(
                    new ArrayList<>(GameQueueFeature.GAME_MODE_COMMANDS.keySet()),
                    Codec.STRING
            ),
            "CakeWars",
            value -> {
                PlexMateClient.config.selectedGame = value;
                PlexMateClient.config.save();
            }
    );

    public static final SimpleOption<Boolean> AUTO_ACCEPT_FRIENDS = new SimpleOption<>(
            "AutoFriend",
            option -> Tooltip.of(Text.of("Automatically accept friend requests from other players.")),
            (option, value) -> Text.literal(value ? "Enabled" : "Disabled"),
            SimpleOption.BOOLEAN,
            false,
            newValue -> {
                PlexMateClient.config.autoAcceptFriendRequests = newValue;
                PlexMateClient.config.save();
            }
    );

    public static void applyConfig(Config config) {
        CLICK_ACTION_MODE.setValue(config.clickMode);
        ClickActionFeature.setMode(config.clickMode);
        SHOW_OWN_NAMETAG.setValue(config.showOwnNametag);
        SELECTED_DOMAIN.setValue(config.useClansDomain);
        AUTO_GG.setValue(config.autoGG);
        AUTO_GG_COOLDOWN.setValue(config.autoGGCooldown);
        SELECTED_GAME.setValue(config.selectedGame);
        AUTO_ACCEPT_FRIENDS.setValue(config.autoAcceptFriendRequests);
    }
}
