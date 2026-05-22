package com.biffo89.troublebrewing;

import com.biffo89.troublebrewing.models.*;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Notification;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Trouble Brewing"
)
public class TroubleBrewingPlugin extends Plugin
{

	private static final int BREW_LOADS_AVAILABLE_VARBIT = 2301;
	private static final int CONTRIBUTION_VARBIT_ID = 2290;
	private static final int BREW_DAN_BOTTLE_VARBIT = 2286; // Red Team
	private static final int BREW_SAN_BOTTLE_VARBIT = 2287; // Blue Team

	private static final int SWEETGRUB_MOUND_ID = 15946;
	private static final int[] BURNING_OBJECTS_ID = { 15849, 15848, 15839, 15838, 15937, 15857, 15856, 15875, 15874, 15865, 15864, 15883, 15882 };
	private static final int[] BURNT_LUMBER_OBJECTS_ID = { 15853, 15852, 15854, 15851, 15850, 15879, 15878, 15880, 15877, 15876 };
	private static final int[] BURNT_PIPES_OBJECTS_ID = { 15843, 15842, 15844, 15841, 15840, 15838, 15869, 15868, 15870, 15867, 15866, 15938 };
	private static final int[] BURNT_BRIDGES_OBJECTS_ID = { 15861, 15860, 15862, 15859, 15858, 15887, 15886, 15888, 15885, 15884 };
	private static final int[] BOILER_OBJECTS_ID = { 15909, 15910, 15911, 15912, 15913, 15914, 15903, 15904, 15905 };

	@Inject
	private Client client;

	@Inject
	private Notifier notifier;

	@Inject
	@Getter
	private TroubleBrewingConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TroubleBrewingOverlay troubleBrewingOverlay;

	@Inject
	private ModelOutlineOverlay modelOutlineOverlay;

	@Getter
	private TroubleBrewingStats game;

	@Getter
	private final List<HighlightModel> highlightModels = new ArrayList<>();

	private boolean gameActive;

	@Override
	protected void startUp() throws Exception
	{
		game = new TroubleBrewingStats(this, client);
		overlayManager.add(troubleBrewingOverlay);
		overlayManager.add(modelOutlineOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(troubleBrewingOverlay);
		overlayManager.remove(modelOutlineOverlay);
		highlightModels.clear();
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		game.update();

		boolean isInTroubleBrewing = game.isInTroubleBrewing();

		if (isInTroubleBrewing && !gameActive) {
			gameActive = true;
			sendNotification(config.notificationStart(), "Trouble Brewing game has started.");
		}

		if (!isInTroubleBrewing && gameActive) {
			gameActive = false;
			sendNotification(config.notificationEnd(), "Trouble Brewing game has ended.");
			highlightModels.clear();
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event) {
		int varbit = event.getVarbitId();
		if (varbit == BREW_LOADS_AVAILABLE_VARBIT) {

			int currentLoadsAvailable = client.getVarbitValue(BREW_LOADS_AVAILABLE_VARBIT);
			game.updateAvailableLoads(currentLoadsAvailable);

		} else if (varbit == CONTRIBUTION_VARBIT_ID) {

			int playerContrib = client.getVarbitValue(CONTRIBUTION_VARBIT_ID);
			game.updatePlayerContrib(playerContrib);
		} else if (varbit == BREW_SAN_BOTTLE_VARBIT) {

			int blueState = client.getVarbitValue(BREW_SAN_BOTTLE_VARBIT);
			game.updateBlueRumState(blueState);
		} else if (varbit == BREW_DAN_BOTTLE_VARBIT) {

			int redState = client.getVarbitValue(BREW_DAN_BOTTLE_VARBIT);
			game.updateRedRumState(redState);
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event) {
		GameObject gameObject = event.getGameObject();
		if (gameObject.getId() == SWEETGRUB_MOUND_ID) {
			highlightModels.add(new SweetgrubMound(this, client, gameObject));
		} else if (Arrays.stream(BURNING_OBJECTS_ID).anyMatch(id -> gameObject.getId() == id)) {
			highlightModels.add(new BurningModel(this, client, gameObject));
		} else if (Arrays.stream(BURNT_LUMBER_OBJECTS_ID).anyMatch(id -> gameObject.getId() == id)) {
			highlightModels.add(new LumberRepairModel(this, client, gameObject));
		} else if (Arrays.stream(BURNT_PIPES_OBJECTS_ID).anyMatch(id -> gameObject.getId() == id)) {
			highlightModels.add(new PipeRepairModel(this, client, gameObject));
		} else if (Arrays.stream(BURNT_BRIDGES_OBJECTS_ID).anyMatch(id -> gameObject.getId() == id)) {
			highlightModels.add(new BridgeRepairModel(this, client, gameObject));
		} else if (Arrays.stream(BOILER_OBJECTS_ID).anyMatch(id -> gameObject.getId() == id)) {
			highlightModels.add(new BoilerModel(this, client, gameObject));
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event) {
		highlightModels.removeIf(obj -> obj.getGameObject() == event.getGameObject());
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event) {
		if (event.getGameState() == GameState.LOADING || event.getGameState() == GameState.HOPPING) {
			highlightModels.clear();
		}
	}

	@Provides
	TroubleBrewingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TroubleBrewingConfig.class);
	}

	private void sendNotification(Notification notification, String message) {
		notifier.notify(notification, message);
	}
}
