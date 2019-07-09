package com.example.zzyyff.flowerrecords;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class fragment_diary extends Fragment {
    RecyclerView rec;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    View view;
    List<class_Diary>diaries = new ArrayList <>();
    adapter_diary adapter_diary;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper = new tools_MyDatabaseHelper(getContext(), "diary.db", null, 1);
        db = dbHelper.getWritableDatabase();
        view = inflater.inflate(R.layout.layout_diary, container, false);
        rec = view.findViewById(R.id.rec_diary);
        rec.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
    protected void initList()
    {
        diaries.clear();
        Cursor cursor = db.query("diary", null, null, null, null, null, "date_year and date_month and date_day desc");
        if (cursor.moveToFirst()) {
            do
            {
               diaries.add(new class_Diary(cursor.getInt(cursor.getColumnIndex("id")),
                       cursor.getString(cursor.getColumnIndex("property")),
                       "默认",
                        numDayToChineseCharactersDay(cursor.getString(cursor.getColumnIndex("date_year")),"YEAR"),
                        numDayToChineseCharactersDay(cursor.getString(cursor.getColumnIndex("date_month")),"MONTH"),
                        cursor.getString(cursor.getColumnIndex("date_day")),
                        cursor.getString(cursor.getColumnIndex("text")),
                        cursor.getString(cursor.getColumnIndex("image_path")),
                        cursor.getString(cursor.getColumnIndex("wheather")),
                        cursor.getString(cursor.getColumnIndex("city"))
               ));
            }while (cursor.moveToNext());
        }
    }
    public static String numDayToChineseCharactersDay(String date,String TYPE){
        String[] ChineseCharactersDay = new String[]{
                "零",
                "一","二","三","四","五","六","七","八","九",
                "十","十一","十二","十三","十四","十五","十六","十七","十八","十九",
                "二十","二十一","二十二","二十三","二十四","二十五","二十六","二十七","二十八","二十九",
                "三十","三十一"};
        switch(TYPE){
            case "YEAR":
                String returnYear="";
                for(int i=0;i<4;i++){
                    int k = date.charAt(i)-'0';
                    returnYear = returnYear+ChineseCharactersDay[k];
                }
                return returnYear;
            case "MONTH":
                int month = Integer.parseInt(date);
                return ChineseCharactersDay[month];
            case "DAY":
                int day = Integer.parseInt(date);
                return ChineseCharactersDay[day];
            default:
                return "TYPE输入YEAR，MONTH或DAY";
        }
    }
    public void onResume() {
        super.onResume();
        initList();
        adapter_diary = new adapter_diary(diaries, getContext());
        rec.setAdapter(adapter_diary);
    }


}
