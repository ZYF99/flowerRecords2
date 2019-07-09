package com.example.zzyyff.flowerrecords;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class adapter_wantlist extends RecyclerView.Adapter< adapter_wantlist.ViewHolder> {
    private List<class_wantthing> List;


    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;



    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameShow;
        TextView valueShow;
        Button savemoney;
        CardView cd;
        ProgressBar progressBar;
        AlertDialog alertDialogsave;
        AlertDialog.Builder builder;
        View saveView;
        EditText ed_savemoney;
        EditText ed_outmoney;
        Button btn_ture;

        public ViewHolder(View view){
            super(view);
            saveView = View.inflate(view.getContext(),R.layout.pop_savemoney,null);
            builder = new AlertDialog.Builder(view.getContext());
            nameShow = (TextView)view.findViewById(R.id.nameShow);
            valueShow = (TextView)view.findViewById(R.id.moneyShow);
            savemoney = (Button) view.findViewById(R.id.savemoney);
            cd = (CardView)view.findViewById(R.id.wantcd);
            progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        }
    }
    public adapter_wantlist(List<class_wantthing> List){

        this.List = List;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wantbuy,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        dbHelper = new tools_MyDatabaseHelper(view.getContext(), "wantlist.db", null, 1);
        db = dbHelper.getWritableDatabase();
        holder.saveView = View.inflate(parent.getContext(),R.layout.pop_savemoney,null);
        holder.builder .setView(holder.saveView);
        holder.ed_outmoney = holder.saveView.findViewById(R.id.ed_wantoutsave);
        holder.ed_savemoney = holder.saveView.findViewById(R.id.ed_wantsave);
        holder.btn_ture = holder.saveView.findViewById(R.id.btn_savewant);
        holder.alertDialogsave = holder.builder.show();
        holder.alertDialogsave.dismiss();
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.nameShow.setText(List.get(position).getName());
        holder.valueShow.setText(List.get(position).toShow());
        holder.progressBar.setProgress((int) (Double.valueOf(List.get(position).getSaveValue())/Double.valueOf(List.get(position).getValue())*100));
        holder.cd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.logo)
                        .setTitle("警告")
                        .setMessage("确定取消这次预购？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                db.delete("wantlist","id=?",new String[]{List.get(position).getId()+""});
                                List.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", null)
                        .create().show();
                return false;
            }
        });

        holder.savemoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ed_savemoney.setHint("已存："+List.get(position).getSaveValue());
                holder.alertDialogsave.show();
            }
        });

        holder.btn_ture.setOnClickListener(new View.OnClickListener() {
            ContentValues values = new ContentValues();
            @Override
            public void onClick(View v) {
                if(holder.ed_savemoney.getText().toString().length()<=0){
                    holder.ed_savemoney.setText("0");
                }
                if(holder.ed_outmoney.getText().toString().length()<=0){
                    holder.ed_outmoney.setText("0");
                }
                    values.put("savemoney",Float.valueOf(holder.ed_savemoney.getText().toString())+Float.valueOf( List.get(position).getSaveValue())-Float.valueOf( holder.ed_outmoney.getText().toString()));
                    db.update("wantlist",values,"id=?",new String[]{List.get(position).getId()+""});
                    initData();
                    notifyDataSetChanged();
                holder.ed_savemoney.setText("");
                holder.ed_outmoney.setText("");
                holder.alertDialogsave.dismiss();
            }
        });


    }

    void initData(){

        List.clear();
        Cursor cursor = db.query("wantlist",null,null,
                null,null,null,"id asc");
        if(cursor.moveToFirst())
        {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String value = cursor.getString(cursor.getColumnIndex("thingvalue"));
                String save = cursor.getString(cursor.getColumnIndex("savemoney"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                List.add(new class_wantthing(id,name,value,save));
            }while (cursor.moveToNext());
        }
    }


    @Override
    public int getItemCount() {
        return List.size();
    }

    public void updata(List<class_wantthing> List){
        this.List = List;
        notifyDataSetChanged();
    }


}
