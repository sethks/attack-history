package com.attackhistory;

import java.util.HashMap;
import java.util.Map;

public class SpellSpriteMap
{
    private static final Map<String, Integer> SPELL_TO_SPRITE = new HashMap<>();

    static
    {
        // Strikes
        SPELL_TO_SPRITE.put("Wind Strike", 15);
        SPELL_TO_SPRITE.put("Water Strike", 67);
        SPELL_TO_SPRITE.put("Earth Strike", 69);
        SPELL_TO_SPRITE.put("Fire Strike", 21);

        // Bolts
        SPELL_TO_SPRITE.put("Wind Bolt", 73);
        SPELL_TO_SPRITE.put("Water Bolt", 76);
        SPELL_TO_SPRITE.put("Earth Bolt", 79);
        SPELL_TO_SPRITE.put("Fire Bolt", 82);

        // Blasts
        SPELL_TO_SPRITE.put("Wind Blast", 85);
        SPELL_TO_SPRITE.put("Water Blast", 88);
        SPELL_TO_SPRITE.put("Earth Blast", 90);
        SPELL_TO_SPRITE.put("Fire Blast", 94);

        // Surges
        SPELL_TO_SPRITE.put("Wind Surge", 412);
        SPELL_TO_SPRITE.put("Water Surge", 413);
        SPELL_TO_SPRITE.put("Earth Surge", 414);
        SPELL_TO_SPRITE.put("Fire Surge", 415);

        // Binding spells
        SPELL_TO_SPRITE.put("Bind", 369);
        SPELL_TO_SPRITE.put("Snare", 370);
        SPELL_TO_SPRITE.put("Entangle", 371);

        // Curse spells
        SPELL_TO_SPRITE.put("Confuse", 66);
        SPELL_TO_SPRITE.put("Weaken", 70);
        SPELL_TO_SPRITE.put("Curse", 74);
        SPELL_TO_SPRITE.put("Enfeeble", 107);

        // Extras
        SPELL_TO_SPRITE.put("Iban Blast", 103);
        SPELL_TO_SPRITE.put("Crumble Undead", 84);
        SPELL_TO_SPRITE.put("Magic Dart", 374);
        SPELL_TO_SPRITE.put("Teleport Block", 402);
        SPELL_TO_SPRITE.put("Saradomin Strike", 111);
        SPELL_TO_SPRITE.put("Claws of Guthix", 110);
        SPELL_TO_SPRITE.put("Flames of Zamorak", 109);
    }

    public static Integer getSpriteId(String spellName)
    {
        if (spellName == null) return null;
        return SPELL_TO_SPRITE.get(spellName);
    }
}
