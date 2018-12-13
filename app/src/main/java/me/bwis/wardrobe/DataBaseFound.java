package me.bwis.wardrobe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class DataBaseFound implements ClothesItemContract.ClothesItemInterface {


    @Override
    public List getClothesByType(String type) {
        DatabaseHelper dbHelper = new DatabaseHelper(getAplication(),"Base_Helper");
        SQLiteDatabase Database = dbHelper.getWritableDatabase();
    }

    @Override
    public List getClothesByColor(String colorType) {
        return null;
    }

    @Override
    public List getClothesBySeason(String season) {
        return null;
    }

    @Override
    public List getClothesById(long id) {
        return null;
    }

    @Override
    public void addClothes(ClothesItem item) {

    }

    @Override
    public void deleteClothes(long id) {

    }

    @Override
    public void modifyClothes(long id, ClothesItem newItem) {

    }
}
