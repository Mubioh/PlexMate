package com.mubioh.plexmate.mixin;

import com.mubioh.plexmate.settings.PlexmateOptions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true)
    private void viewOwnLabel(LivingEntity entity, double squaredDistanceToCamera, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (entity == client.player) {
            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }

        if (entity.equals(MinecraftClient.getInstance().player)) {
            if (!PlexmateOptions.SHOW_OWN_NAMETAG.getValue()) {
                cir.setReturnValue(false);
                return;
            }
            cir.setReturnValue(MinecraftClient.isHudEnabled());
        }
    }
}
