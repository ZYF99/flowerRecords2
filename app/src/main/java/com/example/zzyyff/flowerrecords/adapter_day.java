package com.example.zzyyff.flowerrecords;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class adapter_day extends RecyclerView.Adapter<adapter_day.ViewHolder>{

    static tools_MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;
    adapter_single adapter_single;
    static Context mContext;
    List<class_Day>dayList;
    List<class_Single>singles;


    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemList;
        TextView day_outcome;
        TextView day_income;
        TextView date;


        public ViewHolder(View itemView) {
            super(itemView);


            itemList = itemView.findViewById(R.id.list_everyday);
            day_outcome = itemView.findViewById(R.id.day_outcome);
            day_income = itemView.findViewById(R.id.day_income);
            date = itemView.findViewById(R.id.title_date);
        }
    }
    public adapter_day(List<class_Day> objects, String date, Context mcontext){
        mContext = mcontext;
        dbHelper = new tools_MyDatabaseHelper(mContext, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dayList = objects;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<class_Single>singleList;
        DecimalFormat df = new DecimalFormat("#.##");

        singleList = initSinglelist(dayList.get(position).getDate());
        singles = singleList;

        class_Day day = dayList.get(position);
        holder.day_outcome.setText(df.format(day.getOutcome_day()));
        holder.day_income.setText(df.format(day.getIncome_day()));
        holder.date.setText(String.valueOf(day.getDate()));
        holder.itemList.setLayoutManager(new LinearLayoutManager(mContext));
        adapter_single = new adapter_single(singleList,mContext);
        holder.itemList.setAdapter(adapter_single);

    }
    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public List<class_Single> initSinglelist(String date){

        List<class_Single>singleList = new ArrayList<>();
        String property = "";
        String inorout = "";
        String pay_method = "";
        String remark = "";
        int id = 0;
        double income = 0;
        double outcome = 0;
        Cursor cursor = db.query("record",null, "date=?", new String[]{date}, null,null,"id desc");
        if (cursor.moveToFirst()) {
            do {
                inorout = cursor.getString(cursor.getColumnIndex("inorout"));
                property = cursor.getString(cursor.getColumnIndex("property"));
                pay_method = cursor.getString(cursor.getColumnIndex("paymethod"));
                remark = cursor.getString(cursor.getColumnIndex("remark"));
                id = cursor.getInt(cursor.getColumnIndex("id"));
                switch (inorout)
                {
                    case "in":
                        income = cursor.getDouble(cursor.getColumnIndex("income"));
                        outcome = 0;
                        break;
                    case "out":
                        outcome = cursor.getDouble(cursor.getColumnIndex("cost"));
                        income = 0;
                        break;
                }
                singleList.add(new class_Single(property, income,outcome,inorout,pay_method,remark,date,id));
            } while (cursor.moveToNext());
        }
        return singleList;
    }

}
