package com.example.zzyyff.flowerrecords;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class fragment_table extends Fragment {

    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    PieChart outPiechart;
    TextView nullrecord1;
    TextView nullrecord2;
    TextView choose_year;
    TextView choose_month;
    LinearLayout clicktocost;
    LinearLayout clicktoincome;
    LinearLayout chooseDate;
    TextView costshow;
    TextView incomeshow;
    TextView outcome_all;
    TextView income_all;
    ScrollView scrollView;
    RecyclerView rec;
    List<class_tablelist>tablelist = new ArrayList<>();
    List<Integer>countList = new ArrayList<>();
    List<Double>percntList = new ArrayList <>();
    List <String> property_out = new ArrayList <>();
    List <Float> costList = new ArrayList <>();
    tools_CustomDatePicker customDatePicker;
    String time;
    String date;
    String date_year;
    String date_month;
    TypedValue typedValue = new TypedValue();


    int COLORFUL[] = new int[]{Color.parseColor("#f6ec66"),
            Color.parseColor("#f97272"),
            Color.parseColor("#00818a"),
            Color.parseColor("#97de95"),
            Color.parseColor("#11cbd7"),
            Color.parseColor("#5fcc9c"),
            Color.parseColor("#ffdd93"),
            Color.parseColor("#ffb6b9")};


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_tble, container, false);
        outPiechart = view.findViewById(R.id.mPieChart);
        nullrecord1 = view.findViewById(R.id.nullrecord1);
        nullrecord2 = view.findViewById(R.id.nullrecord2);
        choose_year = view.findViewById(R.id.year);
        choose_month = view.findViewById(R.id.month);
        rec = view.findViewById(R.id.table_list);
        clicktocost = view.findViewById(R.id.clickcost);
        clicktoincome = view.findViewById(R.id.clickincome);
        outcome_all = view.findViewById(R.id.outcome_all);
        income_all = view.findViewById(R.id.income_all);
        costshow = view.findViewById(R.id.costshow);
        incomeshow = view.findViewById(R.id.incomeshow);
        chooseDate = view.findViewById(R.id.clickchangedate);
        scrollView = view.findViewById(R.id.scrollview);
        final boolean[] selectIsout = {false};
        final boolean[] selectIsin = {false};
        dbHelper = new tools_MyDatabaseHelper(getContext(), "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        scrollView.setVerticalScrollBarEnabled(false);
        initPicker();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);




        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDatePicker.show(date,2);
            }
        });


        selectIsout[0] = true;
        clicktoincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectIsout[0] ==true&& selectIsin[0] ==false)
                {
                    selectin();//查收入表并显示饼状图

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        income_all.setTextColor(getContext().getColor(typedValue.resourceId));
                        incomeshow.setTextColor(getContext().getColor(typedValue.resourceId));
                    }
                    outcome_all.setTextColor(Color.parseColor("#BEBEBE"));
                    costshow.setTextColor(Color.parseColor("#BEBEBE"));
                    selectIsout[0] = false;
                    selectIsin[0] = true;
                }
            }
        });
        clicktocost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectIsout[0] ==false&& selectIsin[0] ==true)
                {
                    selectout();//查支出表并显示饼状图
                    income_all.setTextColor(Color.parseColor("#BEBEBE"));
                    incomeshow.setTextColor(Color.parseColor("#BEBEBE"));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        outcome_all.setTextColor(getContext().getColor(typedValue.resourceId));
                        costshow.setTextColor(getContext().getColor(typedValue.resourceId));
                    }

                    selectIsout[0] = true;
                    selectIsin[0] = false;
                }
            }
        });
        return view;
    }
    private void initoutPieChart(String inorout) {

        DecimalFormat df = new DecimalFormat("#.##");
        double alloutcome = 0;
        List<PieEntry> yVals = new ArrayList <>(); //值坐标
        yVals.clear();
            for (int i = 0; i < property_out.size(); i++) {
                alloutcome += costList.get(i);
                if(inorout.equals("支出")) {
                    outcome_all.setText(df.format(alloutcome));
                }else {
                    income_all.setText(df.format(alloutcome));
                }
                yVals.add(new PieEntry((costList.get(i)), property_out.get(i)));
            }

            percntList.clear();
            for (int i = 0; i < property_out.size(); i++) {
                percntList.add(costList.get(i) / alloutcome);
            }




            outPiechart.setExtraOffsets(10, 10, 10, 10);
            //outPiechart.setUsePercentValues(true);// 是否使用百分比
            PieDataSet pieDataSet = new PieDataSet(yVals, "");//创建饼图的一个数据集
            pieDataSet.setValueTextSize(11f);
            pieDataSet.setColors(COLORFUL); //设置成丰富多彩的颜色


            PieData piedata = new PieData(pieDataSet);//生成PieData
            outPiechart.setData(piedata);//给PieChart填充数据
            outPiechart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
            outPiechart.getLegend().setForm(Legend.LegendForm.CIRCLE);//设置注解的位置和形状
            outPiechart.getLegend().setTextSize(12);
            outPiechart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {//设置值选择时的Listener

                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    outPiechart.animateY(1000);
                }

                @Override
                public void onNothingSelected() {
                }
            });


            outPiechart.setCenterText(inorout);//中间写的文字

            outPiechart.setCenterTextColor(Color.BLACK);//设置中间文字的颜色
            outPiechart.setCenterTextRadiusPercent(0.5f);//设置文字显示的角度，180横着，默认是竖着
            outPiechart.setCenterTextSize(12f);//设置中心文字的字体大小
            outPiechart.setCenterTextTypeface(null);//设置字体
            outPiechart.setDrawCenterText(true);//中心字使能开关，false时中间无法显示文字

            outPiechart.setTransparentCircleAlpha(100);//透明圈的透明度，分3圈，一个是外面的值，然后是这个，然后就是下面的那个Hole
            outPiechart.setTransparentCircleColor(Color.WHITE); //设置颜色
            outPiechart.setTransparentCircleRadius(40f);//设置半径

            outPiechart.setDrawHoleEnabled(true);//基本同上
            outPiechart.setHoleColor(Color.WHITE);
            outPiechart.setHoleRadius(30f);

            Description description = new Description();
            description.setText("");
            outPiechart.setDescription(description);//设置描述文字
            outPiechart.animateY(1000);

    }
    private void selectout(){
        property_out.clear();
        costList.clear();
        countList.clear();
        float sum_cost = 0;
        int count = 0;
        //求各类别数量
        Cursor cursor = db.rawQuery("select count(*) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"out", choose_year.getText().toString(),
                        choose_month.getText().toString()});
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
                countList.add(count);
            } while (cursor.moveToNext());
        }
                else {
            outcome_all.setText("");
        }

        //求各类别总和
        Cursor cursor1 = db.rawQuery("select sum(cost) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"out", choose_year.getText().toString(),
                                     choose_month.getText().toString()});
        if (cursor1.moveToFirst()) {
            do {
                sum_cost = cursor1.getFloat(0);
                costList.add(sum_cost);
            } while (cursor1.moveToNext());
        }
        else {
            outcome_all.setText("");
        }
        //找类别
        Cursor cursor2 = db.query("record", null,
                "date_year=? and date_month=? and inorout=?",
                new String[]{choose_year.getText().toString(),
                        choose_month.getText().toString(),"out"},
                "property", null, null);
        if (cursor2.moveToFirst()) {
            do {
                property_out.add(cursor2.getString(cursor2.getColumnIndex("property")));
                outPiechart.setVisibility(View.VISIBLE);
                nullrecord1.setVisibility(View.INVISIBLE);
                nullrecord2.setVisibility(View.INVISIBLE);
            } while (cursor2.moveToNext());
            rec.setVisibility(View.VISIBLE);
            initoutPieChart("支出");
            initRecList();
        }
        else {
                outPiechart.setVisibility(View.INVISIBLE);
                nullrecord1.setVisibility(View.VISIBLE);
                nullrecord2.setVisibility(View.VISIBLE);
                rec.setVisibility(View.INVISIBLE);
        }
    }
    private void selectin(){
        property_out.clear();
        costList.clear();
        countList.clear();
        float sum_cost = 0;
        int count = 0;
        //求各类别数量
        Cursor cursor = db.rawQuery("select count(*) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"in", choose_year.getText().toString(),
                        choose_month.getText().toString()});
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
                countList.add(count);
            } while (cursor.moveToNext());
        }
        //求各类别总和
        Cursor cursor1 = db.rawQuery("select sum(income) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"in", choose_year.getText().toString(),
                        choose_month.getText().toString()});
        if (cursor1.moveToFirst()) {
            do {
                sum_cost = cursor1.getFloat(0);
                costList.add(sum_cost);
            } while (cursor1.moveToNext());
        }
        //找类别
        Cursor cursor2 = db.query("record", null,
                "date_year=? and date_month=? and inorout=?",
                new String[]{choose_year.getText().toString(),
                        choose_month.getText().toString(),"in"},
                "property", null, null);
        if (cursor2.moveToFirst()) {
            do {
                property_out.add(cursor2.getString(cursor2.getColumnIndex("property")));
                outPiechart.setVisibility(View.VISIBLE);
                nullrecord1.setVisibility(View.INVISIBLE);
                nullrecord2.setVisibility(View.INVISIBLE);
                rec.setVisibility(View.VISIBLE);


            } while (cursor2.moveToNext());
            initoutPieChart("收入");
            initRecList();
        }
        else {
            outPiechart.setVisibility(View.INVISIBLE);
            nullrecord1.setVisibility(View.VISIBLE);
            nullrecord2.setVisibility(View.VISIBLE);
            rec.setVisibility(View.INVISIBLE);
        }
    }
    void initRecList(){
        //查询的数据化作对象放入tableList中
        tablelist.clear();
            for (int i = 0; i < property_out.size(); i++) {
                tablelist.add(new class_tablelist(percntList.get(i), property_out.get(i), countList.get(i), costList.get(i)));
            }
            rec.setLayoutManager(new LinearLayoutManager(getContext()));
            rec.setAdapter(new adapter_tablelist(tablelist, getContext()));
     }
    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];

        date_year=date.substring(0,4);
        date_month=date.substring(5,7);
        choose_year.setText(date_year);
        choose_month.setText(date_month);


        customDatePicker = new tools_CustomDatePicker(getContext(), "请选择日期", new tools_CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {

                    date_year=time.split(" ")[0].substring(0,4);
                    date_month=time.split(" ")[0].substring(5,7);
                    choose_year.setText(date_year);
                    choose_month.setText(date_month);
                    onResume();
            }
        }, "2017-01-01 00:00", time);
        customDatePicker.showSpecificTime(false); //显示时和分
        customDatePicker.setIsLoop(false);
        customDatePicker.setDayIsLoop(true);
        customDatePicker.setMonIsLoop(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        selectin();
        selectout();//查支出表并显示饼状图
    }
}
