package com.attackhistory;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("attackhistory")
public interface AttackHistoryConfig extends Config
{
	@ConfigItem(
		keyName = "limit",
		name = "History Limit",
		description = "The amount of attacks you want in your history. MAX = 5"
	)
	default int historyLimit()
	{
		return 3;
	}

	@ConfigItem(
			keyName = "showWeaponImage",
			name = "Show Weapon Image",
			description = "Display the icon of the weapon used for each hit"
	)
	default boolean showWeaponImage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showDamage",
			name = "Show Damage Number",
			description = "Display the amount of damage dealt"
	)
	default boolean showDamage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showSpellIcon",
			name = "Show Spell Icon",
			description = "Show the spell icon used (e.g. Air Wave) if applicable"
	)
	default boolean showSpellIcon()
	{
		return false;
	}
}
