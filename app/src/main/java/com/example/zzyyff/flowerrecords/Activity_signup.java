package com.example.zzyyff.flowerrecords;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity_signup extends AppCompatActivity {

    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    CardView signup;
    ImageView back;
    int date_year;
    int date_month;
    int date_day;
    String monthtoEn[] = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    TextView month_enV,date_yearV,date_mdV,day_sum;
    RecyclerView rec;
    com.example.zzyyff.flowerrecords.adapter_signed adapter_signed;
    List<String>dateList = new ArrayList <>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_signup_activity);

        dbHelper = new tools_MyDatabaseHelper(this, "mine.db", null, 1);
        db = dbHelper.getWritableDatabase();

        day_sum = findViewById(R.id.day_signed);
        rec = findViewById(R.id.sign_list);
        month_enV = findViewById(R.id.month_english);
        date_yearV = findViewById(R.id.date_y);
        date_mdV = findViewById(R.id.date_md);
        signup = findViewById(R.id.signup_button);
        Calendar calendar = Calendar.getInstance();
        date_year = calendar.get(Calendar.YEAR);
        date_month = calendar.get(Calendar.MONTH)+1;
        date_day = calendar.get(Calendar.DAY_OF_MONTH);
        back = findViewById(R.id.backbtn);
        date_yearV.setText(date_year+"");
        date_mdV.setText(date_month+"月"+date_day+"日");
        month_enV.setText(monthtoEn[date_month-1]);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rec.setLayoutManager(new LinearLayoutManager(Activity_signup.this));
        initList();
        adapter_signed = new adapter_signed(dateList);
        rec.setAdapter(adapter_signed);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select * from mine where date_signed=?"
                        ,new String[]{date_year + "年" + date_month + "月" + date_day + "日"});
                if(cursor.moveToFirst())
                {
                    return;
                }else
                {
                    ContentValues values = new ContentValues();
                    values.put("date_signed", date_year + "年" + date_month + "月" + date_day + "日");
                    db.insert("mine", null, values);
                    dateList.clear();
                    adapter_signed.updateData(dateList);
                    initList();
                }

            }
        });
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void initList(){
        int signSumday = 0;
        Cursor cursor = db.query("mine",null,null,
                null,null,null,"date_signed desc");
        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(cursor.getColumnIndex("date_signed"))
                        .equals(date_year + "年" + date_month + "月" + date_day + "日"))
                {
                    signup.setBackground(getResources().getDrawable(R.drawable.button_gry));
                }

                dateList.add(cursor.getString(cursor.getColumnIndex("date_signed")));
                signSumday++;
            }while (cursor.moveToNext());
            cursor.close();
        }
           day_sum.setText(signSumday+"");
    }
}
