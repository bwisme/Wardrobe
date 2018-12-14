package me.bwis.wardrobe;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class ClothesItemContract
{

    private ClothesItemContract() {}

    public static class ClothesItemEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "clothes";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_CLOTHES_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_PHOTO_PATH = "photoPath";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_COLOR_TYPE = "colorType";
        public static final String COLUMN_NAME_STORE_LOCATION = "storeLocation";
        public static final String COLUMN_NAME_BRAND = "brand";
        public static final String COLUMN_NAME_PRICE = "price";
    }

    public static class ClothesSeasonEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "clothesSeason";
        public static final String _ID = "id";
        public static final String COLUMN_NAME_SEASON = "season";
    }


    public static String getCreateTableSQL(java.lang.Class classObject)
    {
        if (classObject == ClothesItemEntry.class)
        {
            String SQL = "CREATE TABLE "+ ClothesItemEntry.TABLE_NAME+" ("+
                    ClothesItemEntry._ID +" INTEGER PRIMARY KEY," +
                    ClothesItemEntry.COLUMN_NAME_CLOTHES_NAME+" TEXT," +
                    ClothesItemEntry.COLUMN_NAME_TYPE+" TEXT," +
                    ClothesItemEntry.COLUMN_NAME_PHOTO_PATH+" TEXT," +
                    ClothesItemEntry.COLUMN_NAME_COLOR+" INTEGER," +
                    ClothesItemEntry.COLUMN_NAME_COLOR_TYPE+" TEXT," +
                    ClothesItemEntry.COLUMN_NAME_STORE_LOCATION+" TEXT," +
                    ClothesItemEntry.COLUMN_NAME_BRAND+" TEXT," +
                    ClothesItemEntry.COLUMN_NAME_PRICE+" REAL)";
            return SQL;

        }
        else if (classObject == ClothesSeasonEntry.class)
        {
            String SQL = "CREATE TABLE "+ ClothesSeasonEntry.TABLE_NAME+" ("+
                    ClothesSeasonEntry._ID +" INTEGER," +
                    ClothesSeasonEntry.COLUMN_NAME_SEASON+" TEXT)";
            return SQL;
        }
        else return "";
    }


    public interface ClothesItemInterface
    {

        List getClothesByType(String type);
        List getClothesByColor(String colorType);
        List getClothesBySeason(String season);
        Cursor getClothesById(long id);

        void deleteClothes(long id);
        Cursor getClothesById_Season(long id);

    }



}
