package com.biffo89.troublebrewing.models;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public abstract class HighlightModel {

    @Getter
    protected final GameObject gameObject;
    protected final Client client;

    public HighlightModel(Client client, GameObject gameObject) {
        this.gameObject = gameObject;
        this.client = client;
    }

    public abstract void makeModelOutline(Graphics2D graphics, ModelOutlineRenderer modelOutlineRenderer);
}
