package com.biffo89.troublebrewing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("troublebrewing")
public interface TroubleBrewingConfig extends Config
{
    @ConfigItem(
            keyName = "brewstats",
            name = "Show Brew Stats",
            description = "Shows how much materials needed, brewing time, and eligibility to load rum."
    )
    default boolean brewstats()
    {
        return false;
    }
}
