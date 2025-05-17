package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.settings.PlexmateOptions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow @Nullable private SplashTextRenderer splashText;

    @Shadow @Nullable protected abstract Text getMultiplayerDisabledText();

    protected TitleScreenMixin() {
        super(Text.literal("Title screen"));
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void overrideSplashText(CallbackInfo ci) {
        if (splashText == null) {
            String[] splashes = {
                    "Chiss is a Chiss",
                    "Knock knock..."
            };
            splashText = new SplashTextRenderer(splashes[new Random().nextInt(splashes.length)]);
        }
    }

    @Inject(method = "addNormalWidgets", at = @At("HEAD"), cancellable = true)
    private void overrideAddNormalWidgets(int y, int spacingY, CallbackInfoReturnable<Integer> cir) {
        Text text = this.getMultiplayerDisabledText();
        boolean multiplayerEnabled = text == null;
        Tooltip tooltip = multiplayerEnabled ? null : Tooltip.of(text);

        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("menu.singleplayer"),
                (button) -> this.client.setScreen(new SelectWorldScreen(this))
        ).dimensions(this.width / 2 - 100, y, 200, 20).build());

        int spacing = 4;
        int halfWidth = (200 - spacing) / 2;
        int xLeft = this.width / 2 - halfWidth - spacing / 2;
        int xRight = this.width / 2 + spacing / 2;

        y += spacingY;

        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("menu.multiplayer"),
                        (button) -> {
                            Screen screen = this.client.options.skipMultiplayerWarning
                                    ? new MultiplayerScreen(this)
                                    : new MultiplayerWarningScreen(this);
                            this.client.setScreen(screen);
                        })
                .dimensions(xLeft, y, halfWidth, 20)
                .tooltip(tooltip)
                .build()).active = multiplayerEnabled;

        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Join " + (PlexmateOptions.SELECTED_DOMAIN.getValue() ? "Clans" : "Mineplex")),
                        (button) -> connectToMineplex()
                ).dimensions(xRight, y, halfWidth, 20)
                .tooltip(tooltip)
                .build()).active = multiplayerEnabled;

        y += spacingY;
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("menu.online"),
                        (button) -> this.client.setScreen(new RealmsMainScreen(this))
                ).dimensions(this.width / 2 - 100, y, 200, 20)
                .tooltip(tooltip)
                .build()).active = multiplayerEnabled;

        cir.setReturnValue(y);
    }


    @Unique
    private void connectToMineplex() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) return;

        String domain = PlexmateOptions.SELECTED_DOMAIN.getValue() ? "clans.mineplex.com" : "mineplex.com";
        ServerInfo serverInfo = new ServerInfo("Mineplex Games", domain, ServerInfo.ServerType.OTHER);
        serverInfo.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED);

        ServerAddress serverAddress = ServerAddress.parse(domain);
        ConnectScreen.connect(client.currentScreen, client, serverAddress, serverInfo, false, null);
    }
}
