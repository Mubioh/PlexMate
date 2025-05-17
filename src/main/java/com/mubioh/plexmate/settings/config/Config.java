package com.mubioh.plexmate.settings.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mubioh.plexmate.features.clickaction.ClickActionFeature;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {

    private static final File FILE = new File("config/plexmate.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ClickActionFeature.ClickMode clickMode = ClickActionFeature.ClickMode.FRIEND;
    public boolean showOwnNametag = true;
    public boolean useClansDomain = false;
    public boolean autoGG = true;
    public int autoGGCooldown = 1;
    public String selectedGame = "CakeWars";
    public boolean autoAcceptFriendRequests = false;

    public static Config load() {
        if (!FILE.exists()) return new Config();
        try (FileReader reader = new FileReader(FILE)) {
            return GSON.fromJson(reader, Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Config();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
