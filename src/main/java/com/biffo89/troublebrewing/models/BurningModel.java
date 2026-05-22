package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameObject;

import java.awt.*;
import java.util.Arrays;

public class BurningModel extends TeamImageModel {

    private static final int BUCKET_OF_WATER_ID = 1929;

    private static final int[] BURNING_BRIDGES_OBJECT_IDS = { 15857, 15856, 15883, 15882 };

    public BurningModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
        imageId = BUCKET_OF_WATER_ID;
        outlineColor = Color.RED;

        if (Arrays.stream(BURNING_BRIDGES_OBJECT_IDS).anyMatch(id -> gameObject.getId() == id))
        {
            outlineIgnoreTeam = true;
        }
    }
}
