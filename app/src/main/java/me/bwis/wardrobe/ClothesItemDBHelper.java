package me.bwis.wardrobe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ClothesItemDBHelper extends SQLiteOpenHelper {

    private static Integer Version = 2;
    public static final String DB_NAME = "WardrobeDatabase";

    public ClothesItemDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                               int version) {
        super(context, name, factory, version);
    }

//    public ClothesItemDBHelper(Context context, String name, int version){
//        this(context,name,null,version);
//    }
//
//    public ClothesItemDBHelper(Context context, String name){
//        this(context,name,Version);
//    }

    public ClothesItemDBHelper(Context context)
    {
        this(context, DB_NAME, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createClothesTable = ClothesItemContract.getCreateTableSQL(ClothesItemContract.ClothesItemEntry.class);
        db.execSQL(createClothesTable);
        String createSeasonTable = ClothesItemContract.getCreateTableSQL(ClothesItemContract.ClothesSeasonEntry.class);
        db.execSQL(createSeasonTable);
        Log.d("ClothesItemDBHelper", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.delete(ClothesItemContract.ClothesItemEntry.TABLE_NAME,null,null);
        db.delete(ClothesItemContract.ClothesSeasonEntry.TABLE_NAME,null,null);
        onCreate(db);
        Log.d("ClothesItemDBHelper", "update new table");
    }


}