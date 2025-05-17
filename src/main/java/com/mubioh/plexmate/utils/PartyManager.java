package com.mubioh.plexmate.utils;

import net.minecraft.text.Text;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PartyManager {

    private static final Set<String> members = new HashSet<>();

    public static void addMember(String name) {
        members.add(name.toLowerCase());
    }

    public static void removeMember(String name) {
        members.remove(name.toLowerCase());
    }

    public static boolean isMember(String name) {
        return members.contains(name.toLowerCase());
    }

    public static void clear() {
        members.clear();
    }

    public static Set<String> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    public static void updateFromMessage(Text text) {
        if (text == null || !ServerUtils.isServerMessage(text)) return;

        String msg = text.getString().toLowerCase();

        if (msg.contains("has joined the party")) {
            // Example: "Party> Mubioh has joined the party."
            String[] parts = msg.split(" ");
            if (parts.length >= 2) {
                String name = parts[1];
                addMember(name);
            }
        }

        if (msg.contains("has left the party")) {
            // Example: "Party> Mubioh has left the party."
            String[] parts = msg.split(" ");
            if (parts.length >= 2) {
                String name = parts[1];
                removeMember(name);
            }
        }

        if (msg.contains("you have kicked")) {
            // Example: "Command> You have kicked Mubioh from the party."
            String[] parts = msg.split(" ");
            if (parts.length >= 4) {
                String name = parts[4];
                removeMember(name);
            }
        }

        if (msg.contains("you have disbanded the party")) {
            // Example: "Command> You have disbanded the party."
            clear();
        }
    }
}
