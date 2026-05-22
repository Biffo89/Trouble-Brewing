package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameObject;

import java.awt.*;
import java.util.Arrays;

public class BridgeRepairModel extends TeamImageModel {

    private static final int BRIDGE_ID = 8979;

    private static final int[] BRIDGE_BURN_STATE_1 = { 15861, 15859, 15887, 15885 };
    private static final int[] BRIDGE_BURN_STATE_2 = { 15860, 15858, 15886, 15884 };
    private static final int[] BRIDGE_BURN_STATE_3 = { 15862, 15888 };

    public BridgeRepairModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
        imageId = BRIDGE_ID;
        outlineColor = Color.RED;
        outlineIgnoreTeam = true;
    }

    private static String getBurnState(int id) {
        if (Arrays.stream(BRIDGE_BURN_STATE_1).anyMatch(state -> state == id)) {
            return "1";
        } else if (Arrays.stream(BRIDGE_BURN_STATE_2).anyMatch(state -> state == id)) {
            return "2";
        } else if (Arrays.stream(BRIDGE_BURN_STATE_3).anyMatch(state -> state == id)) {
            return "3";
        }

        return null;
    }
}
