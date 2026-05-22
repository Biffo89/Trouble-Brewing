package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameObject;

import java.awt.*;
import java.util.Arrays;

public class PipeRepairModel extends TeamImageModel {

    private static final int PIPE_ID = 8930;

    private static final int[] PIPE_BURN_STATE_1 = { 15843, 15841, 15869, 15867 };
    private static final int[] PIPE_BURN_STATE_2 = { 15842, 15840, 15868, 15866 };
    private static final int[] PIPE_BURN_STATE_3 = { 15844, 15870 };

    public PipeRepairModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
        imageId = PIPE_ID;
        outlineColor = Color.RED;
    }

    private static String getBurnState(int id) {
        if (Arrays.stream(PIPE_BURN_STATE_1).anyMatch(state -> state == id)) {
            return "1";
        } else if (Arrays.stream(PIPE_BURN_STATE_2).anyMatch(state -> state == id)) {
            return "2";
        } else if (Arrays.stream(PIPE_BURN_STATE_3).anyMatch(state -> state == id)) {
            return "3";
        }

        return null;
    }
}
