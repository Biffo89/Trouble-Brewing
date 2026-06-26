package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public abstract class HighlightModel {

    @Getter
    protected final GameObject gameObject;
    protected final Client client;
    protected final TroubleBrewingPlugin plugin;
    protected final int plane;

    public HighlightModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        this.gameObject = gameObject;
        this.client = client;
        this.plugin = plugin;
        this.plane = gameObject.getPlane();
    }

    public abstract void makeModelOutline(Graphics2D graphics, ModelOutlineRenderer modelOutlineRenderer, ItemManager itemManager);
}
