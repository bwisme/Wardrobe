package me.bwis.wardrobe.utils;

import java.util.HashMap;

public class Constant
{

    public static final int REQUEST_TAKE_PHOTO = 101;
    public static final int REQUEST_GET_PHOTO_FROM_GALLERY = 102;
    public static final String PREF_USER_TOKEN = "currentUserToken";
    public static final String PREF_FILE = "wardrobe.preferences";
    public static final String PREF_TYPE_SET = "typeSet";

    public static final HashMap COLOR_MAP = initColorMap();
    private static HashMap initColorMap()
    {
        HashMap<String, Integer> colorMap = new HashMap<>();
        colorMap.put("Red", 0xB22222);
        // and other colors...
        return colorMap;
    }
}
