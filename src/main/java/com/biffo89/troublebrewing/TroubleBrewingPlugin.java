package com.biffo89.troublebrewing;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
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

	@Inject
	private Client client;

	@Inject
	private TroubleBrewingConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TroubleBrewingOverlay troubleBrewingOverlay;

	@Getter
	@Setter
	private int resourcePoints, bottles;

	@Override
	protected void startUp()
	{
		log.info("Trouble Brewing started!");
		overlayManager.add(troubleBrewingOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Trouble Brewing stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged) {}

	@Provides
	TroubleBrewingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TroubleBrewingConfig.class);
	}
}
