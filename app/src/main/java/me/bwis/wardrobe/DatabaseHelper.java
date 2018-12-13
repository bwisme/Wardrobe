package me.bwis.wardrobe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static Integer Version = 1;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, int version){
        this(context,name,null,version);
    }

    public DatabaseHelper(Context context, String name){
        this(context,name,Version);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql = "create table clothes ( id long, name String, photoPath String, " +
                "seasons ArrayList, color int, colortype String, storeLocation String, brand String, price double," +
                "primary key (id))";
        db.execSQL(sql);
    }            //season是arraylist但没有类型，不知道行不行
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("更新数据库版本为:"+newVersion);
    }

    //没什么用的升级方法

}