package me.bwis.wardrobe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static me.bwis.wardrobe.ClothesItemContract.ClothesItemEntry;
import static me.bwis.wardrobe.ClothesItemContract.ClothesSeasonEntry;

public class ClothesItemDatabase implements ClothesItemContract.ClothesItemInterface {

    private ClothesItemDBHelper dbHelper;
    private SQLiteDatabase db;

    public ClothesItemDatabase(Context context) {
        this.dbHelper = new ClothesItemDBHelper(context);
        this.db = dbHelper.getReadableDatabase();
    }


//
//    @Override
//    public List getClothesByType(String type) {
//        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
//        Cursor result = db.query(ClothesItemEntry.TABLE_NAME, null,
//                ClothesItemEntry.COLUMN_NAME_TYPE+"=?", new String[]{type}, null, null, null);
//        result.moveToFirst();
//
//        while (result.moveToNext()) {
//            ClothesItem add = new ClothesItem();
//            add.id = result.getLong(result.getColumnIndex(ClothesItemEntry._ID));
//            add.name = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_CLOTHES_NAME));
//            add.photoPath = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_PHOTO_PATH));
//            add.type = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_TYPE));
//            add.storeLocation = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_STORE_LOCATION));
//            //add.colorType = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_COLOR_TYPE));
//            add.brand = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_BRAND));
//            add.price = result.getDouble(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_PRICE));
//            Cursor Season = getClothesById_Season(add.id);
//            Season.moveToFirst();
//            while (Season.moveToNext()) {
//                add.seasons.add(Season.getString(Season.getColumnIndex(ClothesSeasonEntry.COLUMN_NAME_SEASON)));
//            }
//            output.add(add);
//        }
//
//        db.close();
//
//        return output;
//    }
//
//    @Override
//    public List getClothesByColor(String colorType) {
//        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
//        ArrayList<ClothesItem> colorList = new ArrayList<ClothesItem>();
//        ClothesItem add = new ClothesItem();
//        Cursor result = db.query("user", new String[]{"id", "name", "photoPath", "type", "color", "colorType", "storeLocation", "brand", "price"},
//                "colorType=colorType", new String[]{"1"}, null, null, null);
//        Cursor Season;
//        result.moveToFirst();
//
//        while (result.moveToNext()) {
//            add.id = result.getLong(result.getColumnIndex("id"));
//            add.name = result.getString(result.getColumnIndex("name"));
//            add.photoPath = result.getString(result.getColumnIndex("photoPath"));
//            add.type = result.getString(result.getColumnIndex("type"));
//            add.storeLocation = result.getString(result.getColumnIndex("storeLocation"));
//            add.colorType = result.getString(result.getColumnIndex("colorType"));
//            add.brand = result.getString(result.getColumnIndex("brand"));
//            add.price = result.getDouble(result.getColumnIndex("price"));
//            Season = getClothesById_Season(add.id);
//            Season.moveToFirst();
//            while (Season.moveToNext()) {
//                add.seasons.add(Season.getString(Season.getColumnIndex("season")));
//            }
//            output.add(add);
//        }
//
//        db.close();
//
//        return output;
//    }
//
//    @Override
//    public List getClothesBySeason(String season) {
//        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
//        ClothesItem add = new ClothesItem();
//        Cursor se_result = db.query("id_Season", new String[]{"id", "season"}, "season=season",
//                new String[]{"1"}, null, null, null);
//        Cursor oth;
//        se_result.moveToFirst();
//        while (se_result.moveToNext()) {
//            add.id = se_result.getLong(se_result.getColumnIndex("id"));
//            oth = getClothesById(add.id);
//            add.name = oth.getString(oth.getColumnIndex("name"));
//            add.photoPath = oth.getString(oth.getColumnIndex("photoPath"));
//            add.type = oth.getString(oth.getColumnIndex("type"));
//            add.storeLocation = oth.getString(oth.getColumnIndex("storeLocation"));
//            add.colorType = oth.getString(oth.getColumnIndex("colorType"));
//            add.brand = oth.getString(oth.getColumnIndex("brand"));
//            add.price = oth.getDouble(oth.getColumnIndex("price"));
//            oth = getClothesById_Season(add.id);
//            oth.moveToFirst();
//            while (oth.moveToNext()) {
//                add.seasons.add(oth.getString(oth.getColumnIndex("season")));
//            }
//            output.add(add);
//        }
//
//        db.close();
//
//        return output;
//    }

    public List getClothesBy(String columnName, String value)
    {
        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
        Cursor result;
        if (!db.isOpen())
            db = dbHelper.getReadableDatabase();
        if (columnName == ClothesSeasonEntry.COLUMN_NAME_SEASON)
        {
            String query = "SELECT DISTINCT CI.* FROM "+ClothesItemEntry.TABLE_NAME+" CI, "+
                    ClothesSeasonEntry.TABLE_NAME+" CS WHERE CI."+ClothesItemEntry._ID+
                    "==CS."+ClothesSeasonEntry._ID+" AND CS."+ClothesSeasonEntry.COLUMN_NAME_SEASON+"=?";
            result = db.rawQuery(query, new String[]{value});
            Log.d("DB.getClothesBy", "exec:"+query);
        }
        else
        {
            result = db.query(ClothesItemEntry.TABLE_NAME, null,
                    columnName+"=?", new String[]{value}, null, null, null);
            Log.d("DB.getClothesBy", "exec:"+columnName+"="+value);
        }

        //result.moveToFirst();

        while (result.moveToNext()) {
            ClothesItem add = new ClothesItem();
            add.id = result.getLong(result.getColumnIndex(ClothesItemEntry._ID));
            add.name = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_CLOTHES_NAME));
            add.photoPath = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_PHOTO_PATH));
            add.type = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_TYPE));
            add.storeLocation = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_STORE_LOCATION));
            add.color = result.getInt(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_COLOR));
            //add.colorType = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_COLOR_TYPE));
            add.brand = result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_BRAND));
            add.price = result.getDouble(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_PRICE));
            Cursor Season = db.query(ClothesSeasonEntry.TABLE_NAME, null, ClothesSeasonEntry._ID+"=?",new String[]{Long.toString(add.id)},
                    null, null, null);
            while (Season.moveToNext()) {
                add.seasons.add(Season.getString(Season.getColumnIndex(ClothesSeasonEntry.COLUMN_NAME_SEASON)));
            }
            output.add(add);
            Season.close();
            Log.d("getClothesBy", "found one");
        }

        //db.close();
        result.close();
        return output;
    }


    @Override
    public List getClothesById(long id) {
        return getClothesBy(ClothesItemEntry._ID, Long.toString(id));
    }

//    @Override
//    public Cursor getClothesById_Season(long id) {
//        Cursor result = db.query("id_Season", new String[]{"id", "season"}, "id=id",
//                new String[]{"1"}, null, null, null);
//        return result;
//    }


    @Override
    public void deleteClothes(long id) {
        if (!db.isOpen())
            db = dbHelper.getWritableDatabase();
        db.delete(ClothesItemEntry.TABLE_NAME, ClothesItemEntry._ID+"=?", new String[]{Long.toString(id)});
        db.delete(ClothesSeasonEntry.TABLE_NAME, ClothesSeasonEntry._ID+"=?", new String[]{Long.toString(id)});
    }


    public List<String> getTypes()
    {
        ArrayList<String> output = new ArrayList<>();
        Cursor result;
        if (!db.isOpen())
            db = dbHelper.getReadableDatabase();

        String query = "SELECT DISTINCT "+ClothesItemEntry.COLUMN_NAME_TYPE+" FROM "+ClothesItemEntry.TABLE_NAME;
        result = db.rawQuery(query, null);
        while (result.moveToNext())
        {
            output.add(result.getString(result.getColumnIndex(ClothesItemEntry.COLUMN_NAME_TYPE)));
        }
        result.close();
        return output;

    }

    public void renameType(String oldName, String newName)
    {
        if (!db.isOpen())
            db = dbHelper.getReadableDatabase();
        String query = "UPDATE "+ClothesItemContract.ClothesItemEntry.TABLE_NAME+
                " SET "+ ClothesItemContract.ClothesItemEntry.COLUMN_NAME_TYPE+"=? WHERE "+
                ClothesItemContract.ClothesItemEntry.COLUMN_NAME_TYPE+"=?";
        Cursor c = db.rawQuery(query, new String[]{newName, oldName});
        c.moveToFirst();c.close();
    }

    public void deleteClothesByType(String type)
    {

        if (!db.isOpen())
            db = dbHelper.getReadableDatabase();
        String query = "DELETE FROM " + ClothesSeasonEntry.TABLE_NAME +
                " WHERE " + ClothesSeasonEntry._ID + " IN ( SELECT " + ClothesItemEntry._ID +
                " FROM " + ClothesItemEntry.TABLE_NAME + " WHERE " + ClothesItemEntry.COLUMN_NAME_TYPE + "='" + type + "')";


        db.execSQL(query);

        query = "DELETE FROM " + ClothesItemEntry.TABLE_NAME + " WHERE " + ClothesItemEntry.COLUMN_NAME_TYPE + "='" + type + "'";
        db.execSQL(query);
    }


}
