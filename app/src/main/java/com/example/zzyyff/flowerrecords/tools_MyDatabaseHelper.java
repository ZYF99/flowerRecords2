package com.example.zzyyff.flowerrecords;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.Serializable;

public class tools_MyDatabaseHelper extends SQLiteOpenHelper implements Serializable {
    public static final String CREATE_MANAGER = "create table record("
            + "id integer primary key autoincrement, "
            + "account integer, "
            + "inorout String, "
            + "name String, "
            + "property String, "
            + "cost float, "
            + "income float, "
            + "paymethod String, "
            + "date String, "
            + "time String, "
            + "date_year String, "
            + "date_month String, "
            + "date_day String, "
            + "remark String)";
    public static final String CREATE_DIARY = "create table diary("
            + "id integer primary key autoincrement, "
            + "account integer, "
            + "date_year String, "
            + "date_month String, "
            + "property String, "
            + "date_day String, "
            + "image_path String, "
            + "city String, "
            + "wheather String, "
            + "text String)";
    public static final String CREATE_MINE = "create table mine("
            + "id integer primary key autoincrement, "
            + "name String, "
            + "date_signed String )";

    public static final String CREATE_TAG = "create table tag("
            + "id integer primary key autoincrement, "
            + "tag String, "
            + "property String)";


    public static final String CREATE_WANTLIST = "create table wantlist("
            + "id integer primary key autoincrement, "
            + "name String, "
            + "thingvalue String, "
            + "savemoney String)";

    public static final String CREATE_WHEATHR = "create table wheather("
            + "id integer primary key autoincrement, "
            + "date String, "
            + "city String, "
            + "wheather String)";


    private Context mContext;

    public tools_MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_MANAGER);
        db.execSQL(CREATE_DIARY);
        db.execSQL(CREATE_MINE);
        db.execSQL(CREATE_TAG);
        db.execSQL(CREATE_WANTLIST);
        db.execSQL(CREATE_WHEATHR );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("drop table if exists record");
        db.execSQL("drop table if exists diary");
        db.execSQL("drop table if exists mine");
        db.execSQL("drop table if exists tag");
        db.execSQL("drop table if exists wantlist");
        db.execSQL("drop table if exists wheather");
        onCreate(db);


    }
}
