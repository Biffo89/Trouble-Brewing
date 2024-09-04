package com.biffo89.troublebrewing;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;

import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class TroubleBrewingOverlay extends OverlayPanel
{

    private final Client client;
    private final TroubleBrewingPlugin plugin;

    private static final int POINTS_PER_RUM = 10;
    private static final int CONTRIBUTION_VARBIT_ID = 2290;
    private static final int BLUE_BANDANA_ID = 8949;
    private static final int TROUBLE_BREWING_REGION_ID = 15150;
    private static final int RED_TEAM_RUM_BOTTLES_ID = 27197512;

    @Inject
    private TroubleBrewingOverlay(TroubleBrewingPlugin plugin, Client client)
    {
        super(plugin);
        setPosition(OverlayPosition.TOP_RIGHT);
        setPriority(OverlayPriority.LOW);
        this.client = client;
        this.plugin = plugin;
        getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Trouble Brewing overlay"));
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!isInTroubleBrewing())
            return null;
        updatePoints();
        panelComponent.getChildren().add(LineComponent.builder()
                .left("      Pieces of eight")
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Contribution")
                .right("   "+plugin.getResourcePoints())
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Rum")
                .right("   "+plugin.getBottles()*POINTS_PER_RUM)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Total")
                .right("   "+getPoints())
                .build());
        return super.render(graphics);
    }

    protected boolean isInTroubleBrewing()
    {
        return client.getLocalPlayer() != null
                && client.getLocalPlayer().getWorldLocation().getRegionID() == TROUBLE_BREWING_REGION_ID;
    }

    private int getPoints()
    {
        return plugin.getResourcePoints() + plugin.getBottles() * POINTS_PER_RUM;
    }

    private void updatePoints()
    {
        plugin.setResourcePoints(client.getVarbitValue(CONTRIBUTION_VARBIT_ID));
        boolean isBlueTeam = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.HEAD.getSlotIdx()).getId() == BLUE_BANDANA_ID;
        Widget scoreWidget = client.getWidget(RED_TEAM_RUM_BOTTLES_ID+(isBlueTeam?1:0));
        if (scoreWidget == null) return;
        plugin.setBottles(Integer.parseInt(scoreWidget.getText()));
    }
}
