package me.bwis.wardrobe;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataBaseFound implements ClothesItemContract.ClothesItemInterface {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase Database;

    public DataBaseFound(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, "Base_Helper");
        SQLiteDatabase Database = dbHelper.getWritableDatabase();
    }

    @Override
    public List getClothesByType(String type) {
        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
        ClothesItem add = new ClothesItem();
        Cursor result = Database.query("user", new String[]{"id", "name", "photoPath", "type", "color", "colorType", "storeLocation", "brand", "price"},
                "type=type", new String[]{"1"}, null, null, null);
        result.moveToFirst();

        while (result.moveToNext()) {
            add.id = result.getLong(result.getColumnIndex("id"));
            add.name = result.getString(result.getColumnIndex("name"));
            add.photoPath = result.getString(result.getColumnIndex("photoPath"));
            add.type = result.getString(result.getColumnIndex("type"));
            add.storeLocation = result.getString(result.getColumnIndex("storeLocation"));
            add.colorType = result.getString(result.getColumnIndex("colorType"));
            add.brand = result.getString(result.getColumnIndex("brand"));
            add.price = result.getDouble(result.getColumnIndex("price"));
            Cursor Season = getClothesById_Season(add.id);
            Season.moveToFirst();
            while (Season.moveToNext()) {
                add.seasons.add(Season.getString(Season.getColumnIndex("season")));
            }
            output.add(add);
        }

        Database.close();

        return output;
    }

    @Override
    public List getClothesByColor(String colorType) {
        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
        ArrayList<ClothesItem> colorList = new ArrayList<ClothesItem>();
        ClothesItem add = new ClothesItem();
        Cursor result = Database.query("user", new String[]{"id", "name", "photoPath", "type", "color", "colorType", "storeLocation", "brand", "price"},
                "colorType=colorType", new String[]{"1"}, null, null, null);
        Cursor Season;
        result.moveToFirst();

        while (result.moveToNext()) {
            add.id = result.getLong(result.getColumnIndex("id"));
            add.name = result.getString(result.getColumnIndex("name"));
            add.photoPath = result.getString(result.getColumnIndex("photoPath"));
            add.type = result.getString(result.getColumnIndex("type"));
            add.storeLocation = result.getString(result.getColumnIndex("storeLocation"));
            add.colorType = result.getString(result.getColumnIndex("colorType"));
            add.brand = result.getString(result.getColumnIndex("brand"));
            add.price = result.getDouble(result.getColumnIndex("price"));
            Season = getClothesById_Season(add.id);
            Season.moveToFirst();
            while (Season.moveToNext()) {
                add.seasons.add(Season.getString(Season.getColumnIndex("season")));
            }
            output.add(add);
        }

        Database.close();

        return output;
    }

    @Override
    public List getClothesBySeason(String season) {
        ArrayList<ClothesItem> output = new ArrayList<ClothesItem>();
        ClothesItem add = new ClothesItem();
        Cursor se_result = Database.query("id_Season", new String[]{"id", "season"}, "season=season",
                new String[]{"1"}, null, null, null);
        Cursor oth;
        se_result.moveToFirst();
        while (se_result.moveToNext()) {
            add.id = se_result.getLong(se_result.getColumnIndex("id"));
            oth = getClothesById(add.id);
            add.name = oth.getString(oth.getColumnIndex("name"));
            add.photoPath = oth.getString(oth.getColumnIndex("photoPath"));
            add.type = oth.getString(oth.getColumnIndex("type"));
            add.storeLocation = oth.getString(oth.getColumnIndex("storeLocation"));
            add.colorType = oth.getString(oth.getColumnIndex("colorType"));
            add.brand = oth.getString(oth.getColumnIndex("brand"));
            add.price = oth.getDouble(oth.getColumnIndex("price"));
            oth = getClothesById_Season(add.id);
            oth.moveToFirst();
            while (oth.moveToNext()) {
                add.seasons.add(oth.getString(oth.getColumnIndex("season")));
            }
            output.add(add);
        }

        Database.close();

        return output;
    }

    @Override
    public Cursor getClothesById(long id) {
        Cursor result = Database.query("user", new String[]{"id", "name", "photoPath", "type", "color", "colorType", "storeLocation", "brand", "price"},
                "id=id", new String[]{"1"}, null, null, null);
        return result;
    }

    @Override
    public Cursor getClothesById_Season(long id) {
        Cursor result = Database.query("id_Season", new String[]{"id", "season"}, "id=id",
                new String[]{"1"}, null, null, null);
        return result;
    }


    @Override
    public void deleteClothes(long id) {
        Database.delete("user", "id = id", new String[]{"1"});
        Database.delete("id_Season", "id = id", new String[]{"1"});
        Database.close();
    }

}
