package com.biffo89.troublebrewing;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Trouble Brewing"
)
public class TroubleBrewingPlugin extends Plugin
{

	private static final int BREW_LOADS_AVAILABLE_VARBIT = 2301;
	private static final int CONTRIBUTION_VARBIT_ID = 2290;
	private static final int BREW_DAN_BOTTLE_VARBIT = 2286; // Red Team
	private static final int BREW_DAN_PLAYER_LOAD = 2289;
	private static final int BREW_SAN_BOTTLE_VARBIT = 2287; // Blue Team
	private static final int BREW_SAN_PLAYER_LOAD = 2288;

	@Inject
	private Client client;

	@Inject
	@Getter
	private TroubleBrewingConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TroubleBrewingOverlay troubleBrewingOverlay;

	@Getter
	@Setter
	private int resourcePoints, bottles;

	@Getter
	private TroubleBrewingStats game;

	@Override
	protected void startUp() throws Exception
	{
		game = new TroubleBrewingStats(this, client);
		overlayManager.add(troubleBrewingOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(troubleBrewingOverlay);
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		game.update();
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

	@Provides
	TroubleBrewingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TroubleBrewingConfig.class);
	}
}
