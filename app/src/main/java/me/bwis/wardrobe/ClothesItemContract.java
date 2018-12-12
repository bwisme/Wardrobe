package me.bwis.wardrobe;

import android.provider.BaseColumns;

public class ClothesItemContract
{

    private ClothesItemContract() {}

    public static class ClothesItemEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "clothes";
        public static final String COLUMN_NAME_CLOTHES_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_PHOTO_PATH = "photo_path";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_STORE_LOCATION = "store_location";
        public static final String COLUMN_NAME_BRAND = "brand";
        public static final String COLUMN_NAME_PRICE = "price";
    }

    public static class ClothesSeasonEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "clothes_season";
        public static final String COLUMN_NAME_CLOTHES_NAME = "name";
        public static final String COLUMN_NAME_SEASON = "season";
    }


}
