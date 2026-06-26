package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.util.Arrays;

public class SweetgrubMound extends HighlightModel {

    private static final int INVENTORY_ID = 93;
    private static final int RAW_RAT_MEAT_ID = 2134;

    public SweetgrubMound(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
    }

    @Override
    public void makeModelOutline(Graphics2D graphics, ModelOutlineRenderer modelOutlineRenderer, ItemManager itemManager) {

        if (gameObject == null || gameObject.getLocalLocation() == null) {
            return;
        }

        if (client.getTopLevelWorldView().getPlane() != plane) {
            return;
        }

        if (gameObject.getRenderable() == null) {
            return;
        }

        ItemContainer inventory = client.getItemContainer(INVENTORY_ID);
        boolean hasMeat = inventory != null && inventory.contains(RAW_RAT_MEAT_ID);

        if (!hasMeat) {
            return;
        }

        final int borderThickness = 2;
        final Color outlineColor = Color.YELLOW;

        modelOutlineRenderer.drawOutline(gameObject, borderThickness, outlineColor, 0);
    }
}
