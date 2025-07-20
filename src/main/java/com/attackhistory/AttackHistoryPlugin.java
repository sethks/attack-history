package com.attackhistory;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.ItemContainer;
import net.runelite.client.game.ItemManager;

import java.awt.image.BufferedImage;


@Slf4j
@PluginDescriptor(
		name = "Attack History",
		description =  "Tracks individual player attacks.",
		tags = {"combat", "damage", "history", "tracker", "overlay"}
)
public class AttackHistoryPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AttackHistoryOverlay overlay;

	@Inject
	private SpriteManager spriteManager;

	private int lastSpecialEnergy = -1;
	private boolean specialAttackUsed = false;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
		log.info("Attack History Started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		log.info("Attack History Stopped!");
	}

	private Spell getAutocastSpell()
	{
		int spellId = client.getVarbitValue(VarbitID.AUTOCAST_SPELL);

        return Spell.getSpell(spellId);
	}


	public CombatStyleInfo getCombatStyle()
	{
		CombatStyleInfo info = new CombatStyleInfo();

		ItemContainer equipment = client.getItemContainer(InventoryID.WORN); // This worked
		if (equipment == null) return info;

		Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (weapon == null) return info;

		int weaponId = weapon.getId();
		String weaponName = itemManager.getItemComposition(weaponId).getName().toLowerCase();
		info.weaponName = weaponName;

		// Detect non-spell using weapon
		if (weaponName.contains("trident") || weaponName.contains("shadow"))
		{
			info.attackStyle = "Magic";
			return info;
		}

		// Get combat style (e.g., Aggressive, Accurate)
		AttackStyle style = AttackStyle.getCurrentStyle(client);
		String baseStyle = style != null ? style.getName() : "Unknown";

		// Get move name (e.g., Slash, Smash, Stab)
		int styleIndex = client.getVarbitValue(VarPlayerID.ATTACK); //This worked
		String moveName = null;

		Widget[] widgets = new Widget[] {
				client.getWidget(InterfaceID.CombatInterface._0),
				client.getWidget(InterfaceID.CombatInterface._1),
				client.getWidget(InterfaceID.CombatInterface._2),
				client.getWidget(InterfaceID.CombatInterface._3)
		};

		if (style == AttackStyle.CASTING || style == AttackStyle.DEFENSIVE_CASTING)
		{
			if (!weaponName.contains("trident") && !weaponName.contains("shadow"))
			{
				Spell spell = getAutocastSpell();
				if (spell != null)
				{
					info.attackStyle = style.getName() + " (" + spell.getName() + ")";
					return info;
				}
			}
		}

		if (styleIndex >= 0 && styleIndex < widgets.length)
		{
			Widget styleWidget = widgets[styleIndex];
			if (styleWidget != null && styleWidget.getText() != null)
			{
				moveName = styleWidget.getText();
			}
		}
		info.attackStyle = (moveName != null && !moveName.isEmpty())
				? baseStyle + " (" + moveName + ")"
				: baseStyle;

		return info;
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied event)
	{
		Actor target = event.getActor();
		Hitsplat hitsplat = event.getHitsplat();

		if (!hitsplat.isMine())
			return;

		if (target == client.getLocalPlayer())
			return;

		int damage = hitsplat.getAmount();
		CombatStyleInfo info = getCombatStyle();

		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		if (equipment != null)
		{
			Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
			if (weapon != null)
			{
				int weaponId = weapon.getId();

				// Preload to warm up cache
				itemManager.getImage(weaponId);

				Spell spell = getAutocastSpell();
				BufferedImage spellImage = null;
				BufferedImage weaponImage = itemManager.getImage(weaponId);

				// Only fetch spell image if one is active
				if (spell != null)
				{
					spellImage = spriteManager.getSprite(spell.getSpriteID(), 0);
				}
				boolean hasSpellImage = (spellImage != null);
				overlay.addHit(weaponId, info.attackStyle, damage, specialAttackUsed, hasSpellImage ? spellImage : null);
				specialAttackUsed = false;
			}
		}
	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		if (!(event.getActor() instanceof Player)) return;
		if (event.getActor() != client.getLocalPlayer()) return;

		int animId = client.getLocalPlayer().getAnimation();
	}
	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() != VarPlayerID.SA_ENERGY)
		{
			return;
		}

		int newValue = event.getValue();

        specialAttackUsed = lastSpecialEnergy != -1 && newValue < lastSpecialEnergy;

		lastSpecialEnergy = newValue;
	}


	@Provides
	AttackHistoryConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AttackHistoryConfig.class);
	}
}
