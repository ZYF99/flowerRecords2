package com.example.zzyyff.flowerrecords;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class class_Day {
    static tools_MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;
    String date;
    double outcome_day = 0;
    double income_day = 0;


    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public double getOutcome_day() {
        return outcome_day;
    }
    public double getIncome_day() {
        return income_day;
    }


    public class_Day(String date, Context context){
        this.date = date;
        dbHelper = new tools_MyDatabaseHelper(context, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sum(cost) from record where date=?",new String[]{date});
        if(cursor.moveToFirst()) {
            this.outcome_day = cursor.getDouble(0);
        }
        Cursor cursor1 = db.rawQuery("select sum(income) from record where date=?",new String[]{date});
        if(cursor1.moveToFirst()) {
            this.income_day = cursor1.getDouble(0);
        }

    }
}
