package com.biffo89.troublebrewing;

import com.biffo89.troublebrewing.models.HighlightModel;
import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class ModelOutlineOverlay extends Overlay {

    private final Client client;
    private final TroubleBrewingPlugin plugin;
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    public ModelOutlineOverlay(Client client, TroubleBrewingPlugin plugin, ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.plugin = plugin;
        this.modelOutlineRenderer = modelOutlineRenderer;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        for (HighlightModel model : plugin.getHighlightModels()) {
            model.makeModelOutline(graphics, modelOutlineRenderer);
        }

        return null;
    }
}
