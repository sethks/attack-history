package com.attackhistory;

import lombok.Getter;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.Client;
import net.runelite.api.EnumID;
import net.runelite.api.ParamID;
import net.runelite.api.StructComposition;




@Getter
enum AttackStyle
{
    ACCURATE("Accurate"),
    AGGRESSIVE("Aggressive"),
    DEFENSIVE("Defensive"),
    CONTROLLED("Controlled"),
    RANGING("Ranging"),
    LONGRANGE("Longrange"),
    CASTING("Casting"),
    DEFENSIVE_CASTING("Defensive Casting"),
    OTHER("Other");

    private final String name;

    AttackStyle(String name)
    {
        this.name = name;
    }


    public static AttackStyle getCurrentStyle(Client client)
    {
        int weaponType = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);
        int styleIndex = client.getVarpValue(VarPlayerID.COM_MODE);
        int castingMode = client.getVarbitValue(VarbitID.AUTOCAST_DEFMODE);

        AttackStyle[] styles = getStylesFromWeaponType(client, weaponType);

        // Handle defensive casting mode (used only by staves, right?)
        if (styleIndex == 4)
        {
            styleIndex += castingMode;
        }

        if (styleIndex < 0 || styleIndex >= styles.length)
        {
            return AttackStyle.OTHER;
        }

        AttackStyle style = styles[styleIndex];
        return style != null ? style : AttackStyle.OTHER;
    }

    private static AttackStyle[] getStylesFromWeaponType(Client client, int weaponType)
    {
        int weaponStyleEnumId = client.getEnum(EnumID.WEAPON_STYLES).getIntValue(weaponType);
        if (weaponStyleEnumId == -1)
        {
            return new AttackStyle[0];
        }

        int[] structIds = client.getEnum(weaponStyleEnumId).getIntVals();
        AttackStyle[] result = new AttackStyle[structIds.length];

        for (int i = 0; i < structIds.length; i++)
        {
            StructComposition struct = client.getStructComposition(structIds[i]);
            String styleName = struct.getStringValue(ParamID.ATTACK_STYLE_NAME);

            try
            {
                AttackStyle style = AttackStyle.valueOf(styleName.toUpperCase());
                result[i] = style;
            }
            catch (IllegalArgumentException e)
            {
                result[i] = AttackStyle.OTHER;
            }
        }

        return result;
    }

}