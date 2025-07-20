package com.attackhistory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;

@Slf4j
@Singleton
public class AttackHistoryOverlay extends Overlay
{
    private final ItemManager itemManager;
    private final AttackHistoryConfig config;

    private final PanelComponent panel = new PanelComponent();

    @Getter
    private final Deque<HitRecord> hits = new LinkedList<>();

    @Inject
    public AttackHistoryOverlay(Client client, ItemManager itemManager, AttackHistoryConfig config)
    {
        this.itemManager = itemManager;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(2.0f);
    }

    public void addHit(int weaponId, String style, int damage, boolean special, BufferedImage spellImage)
    {
        BufferedImage weaponImage = itemManager.getImage(weaponId);
        // Only proceed if we have at least something to show
        if (weaponImage == null && spellImage == null && !config.showDamage())
            return;

        hits.addFirst(new HitRecord(weaponImage, spellImage, style, damage, special));

        final int hardLimit = 5;
        int configLimit = Math.min(config.historyLimit(), hardLimit);

        while (hits.size() > configLimit)
        {
            hits.removeLast();
        }
    }


    @Override
    public Dimension render(Graphics2D graphics)
    {
        panel.getChildren().clear();

        final int hardLimit = 5;
        int max = Math.min(config.historyLimit(), hardLimit);
        int index = 0;

        for (HitRecord hit : hits)
        {
            if (index >= max)
                break;

            float ageFactor = 1f - (index / (float) max); // Newest = 1.0, Oldest = ~0.0
            float scale = 0.8f + 0.2f * ageFactor;
            float alpha = 0.3f + 0.7f * ageFactor;

            if (config.showWeaponImage() && hit.weaponImage != null)
            {
                BufferedImage scaled = scaleImage(hit.weaponImage, scale);
                BufferedImage faded = applyAlpha(scaled, alpha);
                panel.getChildren().add(new ImageComponent(faded));
            }

            if (config.showSpellIcon() && hit.spellImage != null)
            {
                BufferedImage scaled = scaleImage(hit.spellImage, scale);
                BufferedImage faded = applyAlpha(scaled, alpha);
                panel.getChildren().add(new ImageComponent(faded));
            }

            //spec td
            String styleText = hit.special ? "★ Casting" : (hit.style.contains("Casting") ? "Casting" : hit.style);

            panel.getChildren().add(LineComponent.builder()
                    .left(styleText)
                    .right(config.showDamage() ? String.valueOf(hit.damage) : "")
                    .build());

            index++;
        }
        return panel.render(graphics);
    }


    private BufferedImage applyAlpha(BufferedImage image, float alpha)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return newImage;
    }

    private BufferedImage scaleImage(BufferedImage img, float scale)
    {
        int w = (int) (img.getWidth() * scale);
        int h = (int) (img.getHeight() * scale);

        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return scaled;
    }
}
