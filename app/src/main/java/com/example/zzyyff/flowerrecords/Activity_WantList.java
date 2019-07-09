package com.example.zzyyff.flowerrecords;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Activity_WantList extends AppCompatActivity {
    List<class_wantthing> list;
    RecyclerView recyclerView;
    adapter_wantlist adapter_wantlist;
    ImageView addwant;
    ImageView backhome;
    EditText ed_inputName;
    EditText ed_inputValue;
    Button btn_Addwant;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity__want_list);
        View addView = View.inflate(this,R.layout.pop_wantadd,null);
        final AlertDialog alertDialogAdd = new AlertDialog.Builder(this).create();
        alertDialogAdd.setView(addView);

        dbHelper = new tools_MyDatabaseHelper(this, "wantlist.db", null, 1);
        db = dbHelper.getWritableDatabase();
        list = new ArrayList<>();
        initData();


        View saveView = View.inflate(this,R.layout.pop_savemoney,null);
        final AlertDialog alertDialogsave = new AlertDialog.Builder(this).create();
        alertDialogsave.setView(saveView);

        ed_inputName = addView.findViewById(R.id.ed_wantInputname);
        ed_inputValue =addView.findViewById(R.id.ed_wantInputvalue);
        btn_Addwant = addView.findViewById(R.id.btn_addwant);

        recyclerView = (RecyclerView)findViewById(R.id.rec_wantBuy);
        addwant = (ImageView)findViewById(R.id.addwant);
        backhome = (ImageView)findViewById(R.id.returnback1);
        adapter_wantlist = new adapter_wantlist(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_wantlist);


        addwant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAdd.show();
            }
        });

        btn_Addwant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed_inputName.getText().toString().length()>0&&ed_inputValue.getText().toString().length()>0){

                    ContentValues values = new ContentValues();
                    values.put("name",(ed_inputName.getText().toString()));
                    values.put("thingvalue",ed_inputValue.getText().toString());
                    values.put("savemoney","0");
                    db.insert("wantlist",null,values);

                    ed_inputName.setText("");
                    ed_inputValue.setText("");

                    initData();
                    adapter_wantlist = new adapter_wantlist(list);
                    recyclerView.setAdapter(adapter_wantlist);
                    alertDialogAdd.dismiss();
                }

            }
        });

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void initData(){

        list.clear();
        Cursor cursor = db.query("wantlist",null,null,
             null,null,null,"id asc");
        if(cursor.moveToFirst())
        {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String value = cursor.getString(cursor.getColumnIndex("thingvalue"));
                String save = cursor.getString(cursor.getColumnIndex("savemoney"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                list.add(new class_wantthing(id,name,value,save));
            }while (cursor.moveToNext());
        }
    }

}
