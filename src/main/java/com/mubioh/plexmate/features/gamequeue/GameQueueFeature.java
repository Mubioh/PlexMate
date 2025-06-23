package com.mubioh.plexmate.features.gamequeue;

import com.mubioh.plexmate.features.Feature;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameQueueFeature implements Feature {

    public static final Map<String, String> GAME_MODE_COMMANDS = new LinkedHashMap<>();

    static {
        GAME_MODE_COMMANDS.put("CakeWars", "cakewars");
        GAME_MODE_COMMANDS.put("Champions", "champions");
        GAME_MODE_COMMANDS.put("Clans", "clans");
        GAME_MODE_COMMANDS.put("Disaster Survival", "disastersurvival");
        GAME_MODE_COMMANDS.put("Dragons", "dragons");
        GAME_MODE_COMMANDS.put("King of the Hill", "koth");
        GAME_MODE_COMMANDS.put("Micro Battles", "microbattles");
        GAME_MODE_COMMANDS.put("Minestrike", "minestrike");
        GAME_MODE_COMMANDS.put("One in the Quiver", "oitq");
        GAME_MODE_COMMANDS.put("Bomb Lobbers", "lob");
        GAME_MODE_COMMANDS.put("Skywars", "skywars");
        GAME_MODE_COMMANDS.put("Speed Builders", "speedbuilders");
        GAME_MODE_COMMANDS.put("Survival Games", "survivalgames");
    }

    @Override
    public void initialize() {}
}
