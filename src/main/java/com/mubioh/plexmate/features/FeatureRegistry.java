package com.mubioh.plexmate.features;

import com.mubioh.plexmate.features.clickaction.ClickActionFeature;
import com.mubioh.plexmate.features.gamequeue.GameQueueFeature;
import com.mubioh.plexmate.features.clickaction.PartyFeature;
import com.mubioh.plexmate.features.gamequeue.QueueStateFeature;

import java.util.List;

public class FeatureRegistry {

    private static final List<Feature> FEATURES = List.of(
            new ClickActionFeature(),
            new GameQueueFeature(),
            new QueueStateFeature(),
            new PartyFeature()
    );

    public static void initialize() {
        FEATURES.forEach(Feature::initialize);
    }
}
