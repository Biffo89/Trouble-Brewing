package com.biffo89.troublebrewing;

import com.biffo89.troublebrewing.models.HighlightModel;
import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

public class ModelOutlineOverlay extends Overlay {

    private final TroubleBrewingPlugin plugin;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final ItemManager itemManager;

    @Inject
    public ModelOutlineOverlay(TroubleBrewingPlugin plugin, ModelOutlineRenderer modelOutlineRenderer, ItemManager itemManager) {
        this.plugin = plugin;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.itemManager = itemManager;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        if (!plugin.getGame().isInTroubleBrewing()) {
            return null;
        }

        if (!plugin.getConfig().highlightItems()) {
            return null;
        }

        for (HighlightModel model : plugin.getHighlightModels()) {
            model.makeModelOutline(graphics, modelOutlineRenderer, itemManager);
        }

        return null;
    }
}
