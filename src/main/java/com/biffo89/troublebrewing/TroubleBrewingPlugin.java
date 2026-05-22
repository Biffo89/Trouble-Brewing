package com.biffo89.troublebrewing;

import com.biffo89.troublebrewing.models.HighlightModel;
import com.biffo89.troublebrewing.models.SweetgrubMound;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Notification;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
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
			highlightModels.add(new SweetgrubMound(client, gameObject));
		}
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event) {
		GameObject gameObject = event.getGameObject();
		if (gameObject.getId() == SWEETGRUB_MOUND_ID) {
			highlightModels.removeIf(obj -> obj.getGameObject() == gameObject);
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
