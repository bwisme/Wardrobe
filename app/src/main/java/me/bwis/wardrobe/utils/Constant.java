package me.bwis.wardrobe.utils;

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
        // and other colors...
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
}
