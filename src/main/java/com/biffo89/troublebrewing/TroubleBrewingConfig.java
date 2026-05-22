package com.biffo89.troublebrewing;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Notification;

@ConfigGroup("troublebrewing")
public interface TroubleBrewingConfig extends Config
{
    @ConfigItem(
            position = 1,
            keyName = "brewstats",
            name = "Show Brew Stats",
            description = "Shows how much materials needed, brewing time, and eligibility to load rum."
    )
    default boolean brewstats()
    {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "notificationStart",
            name = "Notify on Start",
            description = "Notification on Game Start"
    )
    default Notification notificationStart() { return Notification.OFF; }

    @ConfigItem(
            position = 3,
            keyName = "notificationEnd",
            name = "Notify on End",
            description = "Notification on Game End"
    )
    default Notification notificationEnd() { return Notification.OFF; }
}
