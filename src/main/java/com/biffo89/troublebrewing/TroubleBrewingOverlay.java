package com.biffo89.troublebrewing;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.util.Text;

import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class TroubleBrewingOverlay extends OverlayPanel
{

    private final Client client;
    private final TroubleBrewingPlugin plugin;

    private static final int POINTS_PER_RUM = 10;
    private static final int TROUBLE_BREWING_REGION_ID = 15150;

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
        if (!plugin.getGame().isInTroubleBrewing())
            return null;

        panelComponent.getChildren().add(LineComponent.builder()
                .left("      Pieces of eight")
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Contribution")
                .right("   "+plugin.getGame().getResourcePoints())
                .rightColor(plugin.getGame().getResourcePoints() >= 100 ? ColorScheme.PROGRESS_COMPLETE_COLOR : Color.white)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Rum")
                .right("   "+plugin.getGame().getBottles()*POINTS_PER_RUM)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Total")
                .right("   "+plugin.getGame().getPoints())
                .build());
        if (plugin.getConfig().brewstats()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("--------")
                    .right("--------")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Bitternuts")
                    .right("   "+plugin.getGame().getBitternuts())
                    .rightColor(plugin.getGame().getBitternutsComplete() ? ColorScheme.PROGRESS_COMPLETE_COLOR : Color.white)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Grubs")
                    .right("   "+plugin.getGame().getGrubs())
                    .rightColor(plugin.getGame().getGrubsComplete() ? ColorScheme.PROGRESS_COMPLETE_COLOR : Color.white)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Buckets")
                    .right("   "+plugin.getGame().getBuckets())
                    .rightColor(plugin.getGame().getBucketsComplete() ? ColorScheme.PROGRESS_COMPLETE_COLOR : Color.white)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Water")
                    .right("   "+plugin.getGame().getColouredWater())
                    .rightColor(plugin.getGame().getColouredWaterComplete() ? ColorScheme.PROGRESS_COMPLETE_COLOR : Color.white)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Bark")
                    .right("   "+plugin.getGame().getBark())
                    .rightColor(plugin.getGame().getBarkComplete() ? ColorScheme.PROGRESS_COMPLETE_COLOR : Color.white)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("--------")
                    .right("--------")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Rum Eligible Level")
                    .right("   "+plugin.getGame().getRumEligibility())
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Brew Time")
                    .right("   "+plugin.getGame().getBrewEndTime())
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Rum State")
                    .right("   "+plugin.getGame().getRumStateString())
                    .rightColor(plugin.getGame().isRumReady() ? Color.yellow : Color.white)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Game Time")
                    .right("   "+plugin.getGame().getGameEndTime())
                    .build());
        }
        return super.render(graphics);
    }
}
