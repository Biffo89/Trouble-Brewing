package com.biffo89.troublebrewing;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TroubleBrewingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TroubleBrewingPlugin.class);
		RuneLite.main(args);
	}
}