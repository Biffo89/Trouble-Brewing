package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class BoilerModel extends TeamImageModel {

    private static final int TINDERBOX_ID = 590;
    private static final int LOGS_ID = 1511;

    private static final int BOILER_1_EMPTY = 15912;
    private static final int BOILER_1_UNLIT = 15913;
    private static final int BOILER_1_LIT = 15914;
    private static final int BOILER_2_EMPTY = 15909;
    private static final int BOILER_2_UNLIT = 15910;
    private static final int BOILER_2_LIT = 15911;
    private static final int BOILER_3_EMPTY = 15903;
    private static final int BOILER_3_UNLIT = 15904;
    private static final int BOILER_3_LIT = 15905;


    public BoilerModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
    }

    @Override
    protected void preprocess() {
        if (gameObject.getId() == BOILER_1_EMPTY || gameObject.getId() == BOILER_2_EMPTY || gameObject.getId() == BOILER_3_EMPTY) {
            imageId = LOGS_ID;
            outlineColor = Color.RED;
        } else if (gameObject.getId() == BOILER_1_UNLIT || gameObject.getId() == BOILER_2_UNLIT || gameObject.getId() == BOILER_3_UNLIT) {
            imageId = TINDERBOX_ID;
            outlineColor = Color.RED;
        } else if (gameObject.getId() == BOILER_1_LIT) {
            setLogs(plugin.getGame().getBoiler1Logs());
        } else if (gameObject.getId() == BOILER_2_LIT) {
            setLogs(plugin.getGame().getBoiler2Logs());
        } else if (gameObject.getId() == BOILER_3_LIT) {
            setLogs(plugin.getGame().getBoiler3Logs());
        }
    }

    private void setLogs(int logCount) {
        if (logCount < 3) {
            imageId = LOGS_ID;
            outlineColor = Color.RED;
            message = String.valueOf(logCount);
        } else if (logCount < 7) {
            imageId = LOGS_ID;
            outlineColor = Color.YELLOW;
            message = String.valueOf(logCount);
        } else if (logCount < 10) {
            imageId = LOGS_ID;
            outlineColor = Color.GREEN;
            message = String.valueOf(logCount);
        } else {
            imageId = 0;
            outlineColor = Color.GREEN;
            message = null;
        }
    }
}
