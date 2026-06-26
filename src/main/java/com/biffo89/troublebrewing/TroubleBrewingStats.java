package com.biffo89.troublebrewing;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.util.Text;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TroubleBrewingStats {

    private final Client client;
    private final TroubleBrewingPlugin plugin;

    private static final int POINTS_PER_RUM = 10;
    private static final int CONTRIBUTION_VARBIT_ID = 2290;
    private static final int BLUE_BANDANA_ID = 8949;
    private static final int TROUBLE_BREWING_REGION_ID = 15150;
    private static final int RED_TEAM_RUM_BOTTLES_ID = 27197512;

    private static final int BREW_OVERLAY_GROUP_ID = 415;
    private static final int BITTERNUT_COUNT_INDEX = 47;
    private static final int SWEETGRUB_COUNT_INDEX = 48;
    private static final int BUCKET_COUNT_INDEX = 49;
    private static final int COLOURWATER_COUNT_INDEX = 50;
    private static final int BARK_COUNT_INDEX = 51;
    private static final int BREW_TIME_INDEX = 74;
    private static final int BOILER_1_INDEX = 44;
    private static final int BOILER_2_INDEX = 45;
    private static final int BOILER_3_INDEX = 46;

    private static final int BUCKETS_PER_RUM = 5;
    private static final int WATER_PER_RUM = 3;
    private static final int BREWING_TIME_IN_SECONDS = 38;

    private static final int RUM_READY_STATE = 2;

    private int teamBitternuts = 0;
    private int teamGrubs = 0;
    private int teamBuckets = 0;
    private int teamColouredWater = 0;
    private int teamBark = 0;

    private String timeRemainingLabel = "";
    private LocalDateTime gameEndTime;
    private LocalDateTime brewEndTime;

    @Getter
    private boolean rumReady;

    @Getter
    private int brewsAttemptsRemaining = 0;

    @Getter
    private int resourcePoints = 0;

    @Getter
    private int bottles = 0;

    @Getter
    private int rumEligibility = 0;

    @Getter
    private int boiler1Logs = 0;

    @Getter
    private int boiler2Logs = 0;

    @Getter
    private int boiler3Logs = 0;

    public TroubleBrewingStats(TroubleBrewingPlugin plugin, Client client) {
        this.client = client;
        this.plugin = plugin;
    }

    public void update() {

        if (!isInTroubleBrewing()) {
            return;
        }

        updatePoints();
    }

    public void updateAvailableLoads(int availableLoads) {
        rumEligibility = availableLoads;
    }

    public void updatePlayerContrib(int playerContribution) {
        resourcePoints = playerContribution;

        // the game sets the player contribution to 0 on game end.
        if (resourcePoints == 0) {
            brewEndTime = null;
            rumReady = false;
            teamBitternuts = 0;
            teamGrubs = 0;
            teamBuckets = 0;
            teamColouredWater = 0;
            teamBark = 0;
            boiler1Logs = 0;
            boiler2Logs = 0;
            boiler3Logs = 0;
        }
    }

    public void updateRedRumState(int redRumState) {
        if (isBlueTeam()) {
            return;
        }

        if (redRumState == RUM_READY_STATE) {
            rumReady = true;
        }
        else {
            rumReady = false;
        }
    }

    public void updateBlueRumState(int blueRumState) {
        if (!isBlueTeam()) {
            return;
        }

        if (blueRumState == RUM_READY_STATE) {
            rumReady = true;
        }
        else {
            rumReady = false;
        }
    }

    public boolean isInTroubleBrewing()
    {
        return client.getLocalPlayer() != null
                && client.getLocalPlayer().getWorldLocation().getRegionID() == TROUBLE_BREWING_REGION_ID;
    }

    public boolean isBlueTeam() {
        return client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.HEAD.getSlotIdx()).getId() == BLUE_BANDANA_ID;
    }

    public int getPoints()
    {
        return resourcePoints + bottles * POINTS_PER_RUM;
    }

    public String getBrewEndTime() {
        if (brewEndTime == null) {
            return "-";
        }

        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), brewEndTime);

        if (seconds < -30) {
            return "-";
        }

        return String.valueOf(seconds) + "s";
    }

    public String getGameEndTime() {
        if (gameEndTime == null) {
            return "-";
        }

        Duration duration = Duration.between(LocalDateTime.now(), gameEndTime);

        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;

        return minutes + ":" + String.format("%02d", seconds);
    }

    public String getBitternuts() {
        return teamBitternuts + " (" + brewsAttemptsRemaining + ")";
    }

    public boolean getBitternutsComplete() {
        return teamBitternuts >= brewsAttemptsRemaining;
    }

    public String getGrubs() {
        return teamGrubs + " (" + brewsAttemptsRemaining + ")";
    }

    public boolean getGrubsComplete() {
        return teamGrubs >= brewsAttemptsRemaining;
    }

    public String getBuckets() {
        if (teamBuckets > 200) {
            // lots of AFKers, so need to show the absurd value
            return "* (" + brewsAttemptsRemaining * BUCKETS_PER_RUM + ")";
        }
        return teamBuckets + " (" + brewsAttemptsRemaining * BUCKETS_PER_RUM + ")";
    }

    public boolean getBucketsComplete() {
        return teamBuckets >= brewsAttemptsRemaining * BUCKETS_PER_RUM;
    }

    public String getColouredWater() {
        return teamColouredWater + " (" + brewsAttemptsRemaining * WATER_PER_RUM + ")";
    }

    public boolean getColouredWaterComplete() {
        return teamColouredWater >= brewsAttemptsRemaining * WATER_PER_RUM;
    }

    public String getBark() {
        return teamBark + " (" + brewsAttemptsRemaining + ")";
    }

    public boolean getBarkComplete() {
        return teamBark >= brewsAttemptsRemaining;
    }

    public String getRumStateString() {
        if (rumReady) {
            return "Ready";
        }
        else {
            return "-";
        }
    }

    private void updatePoints()
    {
        resourcePoints = client.getVarbitValue(CONTRIBUTION_VARBIT_ID);
        Widget scoreWidget = client.getWidget(RED_TEAM_RUM_BOTTLES_ID+(isBlueTeam() ? 1 : 0));
        if (scoreWidget == null) return;
        bottles = Integer.parseInt(scoreWidget.getText());

        Widget baseOverlay = client.getWidget(BREW_OVERLAY_GROUP_ID, 0);
        if (baseOverlay == null || baseOverlay.isHidden())
        {
            return;
        }

        updateMaterials();
        updateTimeRemaining();

    }

    private void updateMaterials() {

        int bitternuts = extractNumericValue(BITTERNUT_COUNT_INDEX);
        int grubs = extractNumericValue(SWEETGRUB_COUNT_INDEX);
        int buckets = extractNumericValue(BUCKET_COUNT_INDEX);
        int colouredWater = extractNumericValue(COLOURWATER_COUNT_INDEX);
        int bark = extractNumericValue(BARK_COUNT_INDEX);

        boiler1Logs = extractNumericValue(BOILER_1_INDEX);
        boiler2Logs = extractNumericValue(BOILER_2_INDEX);
        boiler3Logs = extractNumericValue(BOILER_3_INDEX);

        boolean bottleStarted = false;
        if (bitternuts < teamBitternuts) {
            bottleStarted = true;
        }
        teamBitternuts = bitternuts;

        if (grubs < teamGrubs) {
            bottleStarted = true;
        }
        teamGrubs = grubs;

        if (buckets < teamBuckets) {
            bottleStarted = true;
        }
        teamBuckets = buckets;

        if (colouredWater < teamColouredWater) {
            bottleStarted = true;
        }
        teamColouredWater = colouredWater;

        if (bark < teamBark) {
            bottleStarted = true;
        }
        teamBark = bark;

        if (bottleStarted) {
            brewEndTime = LocalDateTime.now().plusSeconds(BREWING_TIME_IN_SECONDS);
        }
    }

    private int extractNumericValue(int childId) {
        Widget resourceWidget = client.getWidget(BREW_OVERLAY_GROUP_ID, childId);
        if (resourceWidget == null) {
            return 0;
        }

        String rawText = resourceWidget.getText();
        if (rawText == null || rawText.isEmpty()) {
            return 0;
        }

        try {
            String cleanText = Text.removeTags(rawText).trim();
            return Integer.parseInt(cleanText);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void updateTimeRemaining() {

        Widget resourceWidget = client.getWidget(BREW_OVERLAY_GROUP_ID, BREW_TIME_INDEX);
        if (resourceWidget == null) {
            return;
        }

        String rawText = resourceWidget.getText();
        if (rawText == null || rawText.isEmpty()) {
            return;
        }

        if (!rawText.equals(timeRemainingLabel))
        {
            try {
                timeRemainingLabel = rawText;
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(rawText);

                if (matcher.find()) {
                    int minutes = Integer.parseInt(matcher.group());
                    gameEndTime = LocalDateTime.now().plusMinutes(minutes);
                }
            } catch(Exception ex) {
                return;
            }
        }

        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), gameEndTime);

        if (brewEndTime != null && LocalDateTime.now().isBefore(brewEndTime))
        {
            seconds = ChronoUnit.SECONDS.between(brewEndTime, gameEndTime);
        }

        brewsAttemptsRemaining = (int) Math.floor((double)seconds / BREWING_TIME_IN_SECONDS);
    }
}
