package com.attackhistory;

import java.awt.image.BufferedImage;

public class HitRecord
{
    public final BufferedImage weaponImage;
    public final BufferedImage spellImage;
    public final String style;
    public final int damage;
    public final boolean special;

    public HitRecord(BufferedImage weaponImage, BufferedImage spellImage, String style, int damage, boolean special)
    {
        this.weaponImage = weaponImage;
        this.spellImage = spellImage;
        this.style = style;
        this.damage = damage;
        this.special = special;
    }
}
