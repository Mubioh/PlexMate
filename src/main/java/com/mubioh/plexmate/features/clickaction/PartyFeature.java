package com.mubioh.plexmate.features.clickaction;

import com.mubioh.plexmate.features.Feature;
import com.mubioh.plexmate.utils.PartyManager;
import com.mubioh.plexmate.utils.ServerUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class PartyFeature implements Feature {
    @Override
    public void initialize() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (!ServerUtils.isOnMineplex()) return;
            PartyManager.clear();
        });
    }
}
