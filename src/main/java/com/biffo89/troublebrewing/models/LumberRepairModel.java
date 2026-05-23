package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameObject;

import java.awt.*;
import java.util.Arrays;

public class LumberRepairModel extends TeamImageModel {

    private static final int LUMBER_ID = 8932;

    private static final int[] HOPPER_BURN_STATE_1 = { 15853, 15851, 15879, 15877 };
    private static final int[] HOPPER_BURN_STATE_2 = { 15852, 15850, 15878, 15876 };
    private static final int[] HOPPER_BURN_STATE_3 = { 15854, 15880 };

    public LumberRepairModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
        imageId = LUMBER_ID;
        outlineColor = Color.RED;
        message = getBurnState(gameObject.getId());
    }

    private static String getBurnState(int id) {
        if (Arrays.stream(HOPPER_BURN_STATE_1).anyMatch(state -> state == id)) {
            return "1";
        } else if (Arrays.stream(HOPPER_BURN_STATE_2).anyMatch(state -> state == id)) {
            return "2";
        } else if (Arrays.stream(HOPPER_BURN_STATE_3).anyMatch(state -> state == id)) {
            return "3";
        }

        return null;
    }
}
