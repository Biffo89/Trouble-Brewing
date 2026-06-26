package com.biffo89.troublebrewing.models;

import com.biffo89.troublebrewing.TroubleBrewingPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class TeamImageModel extends HighlightModel {

    private static final int IMAGE_Z_OFFSET = 150;
    private static final int TEXT_Z_OFFSET = 80;
    private static final int MIDWAY_POINT_Y_COORD = 2975;

    protected int imageId = 0;
    protected Color outlineColor = Color.RED;
    protected String message = null;
    protected boolean outlineIgnoreTeam = false;

    public TeamImageModel(TroubleBrewingPlugin plugin, Client client, GameObject gameObject) {
        super(plugin, client, gameObject);
    }

    protected void preprocess() { }

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

        preprocess();

        boolean oppositeTeam = false;
        // Ignore burning objects on the opposite team
        if (plugin.getGame().isBlueTeam()) {
            if (gameObject.getWorldLocation().getY() > MIDWAY_POINT_Y_COORD) {
                oppositeTeam = true;
            }
        }
        else {
            if (gameObject.getWorldLocation().getY() < MIDWAY_POINT_Y_COORD) {
                oppositeTeam = true;
            }
        }

        if (oppositeTeam && !outlineIgnoreTeam) {
            return;
        }

        final int borderThickness = 2;

        modelOutlineRenderer.drawOutline(gameObject, borderThickness, outlineColor, 0);

        if (oppositeTeam) {
            return;
        }

        if (imageId == 0) {
            return;
        }

        BufferedImage image = itemManager.getImage(imageId);
        if (image == null) {
            return;
        }

        Point imageLocation = Perspective.getCanvasImageLocation(
                client,
                gameObject.getLocalLocation(),
                image,
                IMAGE_Z_OFFSET
        );

        if (imageLocation != null) {
            graphics.drawImage(image, imageLocation.getX(), imageLocation.getY(), null);
        }

        if (message == null) {
            return;
        }

        Point textLocation = Perspective.getCanvasTextLocation(
                client,
                graphics,
                gameObject.getLocalLocation(),
                message,
                TEXT_Z_OFFSET
        );

        if (textLocation != null) {
            graphics.setFont(new Font("Arial", Font.BOLD, 14));

            OverlayUtil.renderTextLocation(graphics, textLocation, message, Color.WHITE);
        }

    }
}
