package me.bwis.wardrobe.utils;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant
{

    public static final int REQUEST_TAKE_PHOTO = 101;
    public static final int REQUEST_GET_PHOTO_FROM_GALLERY = 102;
    public static final String PREF_USER_TOKEN = "currentUserToken";
    public static final String PREF_FILE = "wardrobe.preferences";
    public static final String PREF_TYPE_SET = "typeSet";
    public static ArrayList<String> COLOR_LIST;
    public static final HashMap COLOR_MAP = initColorMap();

    public static final String PREF_CATEGORY = "currentCategory";
    public static final int CATEGORY_TYPE = 0;
    public static final int CATEGORY_SEASON = 1;
    public static final int CATEGORY_COLOR = 2;
    public static final int CATEGORY_ALL_TYPE = 3;

    public static final int CATEGORY_FOOTER = 0;
    public static final int CATEGORY_ITEM = 1;

    private static HashMap initColorMap()
    {
        HashMap<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Red", 0xB22222);
        colorMap.put("Black", 0x000000);
        colorMap.put("Blue", 0x0050FF);
        colorMap.put("Green", 0x00891D);
        colorMap.put("Cyan", 0x00E8D8);
        colorMap.put("Brown", 0x724A00);
        colorMap.put("Gray", 0xA0A0A0);
        colorMap.put("Orange", 0xF27100);
        colorMap.put("Pink", 0xFC55E6);
        colorMap.put("Purple", 0xA700BA);
        colorMap.put("White", 0xFFFFFF);
        colorMap.put("Yellow", 0xFAFF00);

        COLOR_LIST = new ArrayList<>();
        COLOR_LIST.addAll(colorMap.keySet());
        return colorMap;
    }

    public static final ArrayList<String> SEASON_LIST = initSeasonList();
    private static ArrayList initSeasonList()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("Spring");
        list.add("Summer");
        list.add("Autumn");
        list.add("Winter");
        return list;
    }

    private static double colorDistance(int c1, int c2)
    {
        int red1 =(c1 >> 16) & 0xff;
        int red2 = (c2 >> 16) & 0xff;
        int rmean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = ((c1 >>  8) & 0xff) - ((c2 >>  8) & 0xff);
        int b = (c1 & 0xff) - (c2&0xff);
        return (((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8);
    }

    public static String getNearestColorName(int color)
    {
        double distance = Double.MAX_VALUE;
        String ret = "White";
        for (String colorName : COLOR_LIST)
        {
            double d = colorDistance(color, (Integer)COLOR_MAP.get(colorName));
            if (d < distance)
            {
                distance = d;
                ret = colorName;
            }
        }
        return ret;
    }
}
