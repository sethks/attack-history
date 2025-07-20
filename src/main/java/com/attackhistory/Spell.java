package com.attackhistory;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.gameval.SpriteID;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum Spell
{
    //Credit to zbruennig for help on this file - Github

    WIND_STRIKE("Wind Strike", SpriteID.Magicon._0, 1, 1),
    WATER_STRIKE("Water Strike", SpriteID.Magicon._2, 5, 2),
    EARTH_STRIKE("Earth Strike", SpriteID.Magicon._4, 9, 3),
    FIRE_STRIKE("Fire Strike", SpriteID.Magicon._6, 13, 4),

    WIND_BOLT("Wind Bolt", SpriteID.Magicon._8, 17, 5),
    WATER_BOLT("Water Bolt", SpriteID.Magicon._11, 23, 6),
    EARTH_BOLT("Earth Bolt", SpriteID.Magicon._14, 29, 7),
    FIRE_BOLT("Fire Bolt", SpriteID.Magicon._17, 35, 8),

    WIND_BLAST("Wind Blast", SpriteID.Magicon._20, 41, 9),
    WATER_BLAST("Water Blast", SpriteID.Magicon._23, 47, 10),
    EARTH_BLAST("Earth Blast", SpriteID.Magicon._25, 53, 11),
    FIRE_BLAST("Fire Blast", SpriteID.Magicon._29, 59, 12),

    WIND_WAVE("Wind Wave", SpriteID.Magicon._31, 62, 13),
    WATER_WAVE("Water Wave", SpriteID.Magicon._33, 65, 14),
    EARTH_WAVE("Earth Wave", SpriteID.Magicon._36, 70, 15),
    FIRE_WAVE("Fire Wave", SpriteID.Magicon._37, 75, 16),

    CRUMBLE_UNDEAD("Crumble Undead", SpriteID.Magicon._19, 39, 17),
    MAGIC_DART("Magic Dart", SpriteID.Magicon2._5, 50, 18),

    CLAWS_OF_GUTHIX("Claws of Guthix", SpriteID.Magicon2._45, 60, 19),
    FLAMES_OF_ZAMORAK("Flames of Zamorak", SpriteID.Magicon2._44, 60, 20),

    // 21 - 30 Unused

    SMOKE_RUSH("Smoke Rush", SpriteID.Magicon2._10, 50, 31),
    SHADOW_RUSH("Shadow Rush", SpriteID.Magicon2._18, 52, 32),
    BLOOD_RUSH("Blood Rush", SpriteID.Magicon2._14, 56, 33),
    ICE_RUSH("Ice Rush", SpriteID.Magicon2._6, 58, 34),

    SMOKE_BURST("Smoke Burst", SpriteID.Magicon2._11, 62, 35),
    SHADOW_BURST("Shadow Burst", SpriteID.Magicon2._19, 64, 36),
    BLOOD_BURST("Blood Burst", SpriteID.Magicon2._15, 68, 37),
    ICE_BURST("Ice Burst", SpriteID.Magicon2._7, 70, 38),

    SMOKE_BLITZ("Smoke Blitz", SpriteID.Magicon2._12, 74, 39),
    SHADOW_BLITZ("Shadow Blitz", SpriteID.Magicon2._20, 76, 40),
    BLOOD_BLITZ("Blood Blitz", SpriteID.Magicon2._16, 80, 41),
    ICE_BLITZ("Ice Blitz", SpriteID.Magicon2._8, 82, 42),

    SMOKE_BARRAGE("Smoke Barrage", SpriteID.Magicon2._13, 86, 43),
    SHADOW_BARRAGE("Shadow Barrage", SpriteID.Magicon2._21, 88, 44),
    BLOOD_BARRAGE("Blood Barrage", SpriteID.Magicon2._17, 92, 45),
    ICE_BARRAGE("Ice Barrage", SpriteID.Magicon2._9, 94, 46),

    IBAN_BLAST("Iban's Blast", SpriteID.Magicon._38, 50, 47),

    WIND_SURGE("Wind Surge", SpriteID.Magicon2._43, 81, 48),
    WATER_SURGE("Water Surge", SpriteID.Magicon2._44, 85, 49),
    EARTH_SURGE("Earth Surge", SpriteID.Magicon2._45, 90, 50),
    FIRE_SURGE("Fire Surge", SpriteID.Magicon2._46, 95, 51),

    SARADOMIN_STRIKE("Saradomin Strike", SpriteID.Magicon2._46, 60, 52),

    INFERIOR_DEMONBANE("Inferior Demonbane", SpriteID.MagicNecroOn._27, 44, 53),
    SUPERIOR_DEMONBANE("Superior Demonbane", SpriteID.MagicNecroOn._28, 62, 54),
    DARK_DEMONBANE("Dark Demonbane", SpriteID.MagicNecroOn._29, 82, 55),

    GHOSTLY_GRASP("Ghostly Grasp", SpriteID.MagicNecroOn._20, 35, 56),
    SKELETAL_GRASP("Skeletal Grasp", SpriteID.MagicNecroOn._21, 56, 57),
    UNDEAD_GRASP("Undead Grasp", SpriteID.MagicNecroOn._22, 79, 58);

    private final String name;

    private final int spriteID;

    private final int levelRequirement;

    private final int varbitValue;

    private static final Map<Integer, Spell> AUTOCAST_SPELLS_MAP;

    static
    {
        ImmutableMap.Builder<Integer, Spell> builder = new ImmutableMap.Builder<>();

        for (Spell spell : values())
        {
            builder.put(spell.getVarbitValue(), spell);
        }
        AUTOCAST_SPELLS_MAP = builder.build();
    }

    public static Spell getSpell(int varbitValue)
    {
        return AUTOCAST_SPELLS_MAP.getOrDefault(varbitValue, null);
    }
}
