package com.mubioh.plexmate.features.gamequeue;

import com.mubioh.plexmate.features.Feature;
import com.mubioh.plexmate.utils.ServerUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;

public class QueueStateFeature implements Feature {

    @Override
    public void initialize() {
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> {
            if (!ServerUtils.isOnMineplex()) return;
            ServerUtils.setQueued(false);
        });
    }
}
