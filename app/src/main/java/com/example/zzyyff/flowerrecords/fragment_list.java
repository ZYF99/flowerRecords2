package com.example.zzyyff.flowerrecords;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class fragment_list extends Fragment {
    View view;
    TextView label;
    TextView labelout;
    TextView labelin;
    TextView label_year;
    TextView label_month;
    RecyclerView rec_day;
    LinearLayout choose_date;
    List <class_Day> dayList = new ArrayList <>();
    String year;
    String month;
    private tools_CustomDatePicker datePicker;
    private String time;
    private String date;
    int dateInput_year;
    int dateInput_month;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        year = String.valueOf(calendar.get(Calendar.YEAR));
        month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        initView(inflater, container);
        initTypeface();
        initPicker();

        return view;
    }

    void initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.layout_list, container, false);
        label = view.findViewById(R.id.label);
        label_year = view.findViewById(R.id.label_year);
        label_month = view.findViewById(R.id.label_month);
        labelout = view.findViewById(R.id.label_out);
        labelin = view.findViewById(R.id.label_in);
        rec_day = view.findViewById(R.id.mainlist);
        choose_date = view.findViewById(R.id.choose_date);
        rec_day.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new tools_MyDatabaseHelper(getContext(), "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        label_year.setText(year);
        label_month.setText(month);
        rec_day.setVerticalScrollBarEnabled(false);

        choose_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(date,2);
            }
        });

/*    //下拉加载上个月
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
        public void onRefresh() {

                    dateInput_year = Integer.parseInt(label_year.getText().toString());
                    dateInput_month = Integer.parseInt(label_month.getText().toString());
                    dateInput_month -= 1;
                    if (dateInput_month == 0) {
                        dateInput_year -= 1;
                        dateInput_month = 12;
                        if(dateInput_year<2017)
                        {
                            dateInput_year = 2017;
                            dateInput_month = 1;
                        }
                    }
                    label_year.setText(addZeroForNum(dateInput_year+"",2));
                    label_month.setText(addZeroForNum(dateInput_month+"",2));
                    initDaylist();
                    swipeRefreshLayout.setRefreshing(false);
                    }
                    });
                    */
    }
    @Override
    public void onResume() {
        super.onResume();
        initDaylist();

    }
    private void initTypeface() {
        Typeface typefaceModeran = Typeface.createFromAsset(getContext().getAssets(), "fonts/label.ttf");
        label.setTypeface(typefaceModeran);
    }
    void initDaylist() {
        dayList.clear();
        //查询全部天数据
        Cursor cursor = db.rawQuery("select date from record where date_year=? " +
                "and date_month=? group by date order by date desc", new String[]{label_year.getText().toString(), label_month.getText().toString()});
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                dayList.add(new class_Day(date, getContext()));
                adapter_day adapter_day1 = new adapter_day(dayList, date, getContext());

                rec_day.setAdapter(adapter_day1);
            } while (cursor.moveToNext());
            //将每天数据传入对应日期类

            int i = 0;
            double outcome = 0;
            double income = 0;
            do {
                outcome += dayList.get(i).getOutcome_day();
                income += dayList.get(i).getIncome_day();
                DecimalFormat df = new DecimalFormat("#.##");

                labelout.setText(String.valueOf(df.format(outcome)));
                labelin.setText(String.valueOf(df.format(income)));
                i++;
            } while (i < dayList.size());
        } else {
            adapter_day adapter_day1 = new adapter_day(dayList, date, getContext());

            rec_day.setAdapter(adapter_day1);
            labelout.setText("0");
            labelin.setText("0");
        }

    }
    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];


        year = date.substring(0,4);
        month = date.substring(5,7);
        //设置当前显示的日期
        label_month.setText(month);
        label_year.setText(year);

        datePicker = new tools_CustomDatePicker(getContext(), "请选择日期", new tools_CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                year = time.split(" ")[0].substring(0,4);
                month = time.split(" ")[0].substring(5,7);
                    label_year.setText(year);
                    label_month.setText(month);
                    dateInput_year = Integer.parseInt(year);
                    dateInput_month = Integer.parseInt(month);
                    initDaylist();
            }
        }, "2017-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);
    }


}
