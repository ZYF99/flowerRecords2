package com.example.zzyyff.flowerrecords;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class Activity_tagEdit extends AppCompatActivity {

    ImageView image_property;
    ImageView return_bar;
    List<String>tagList = new ArrayList <>();
    RecyclerView list_tag;
    CardView btn_showdialog;
    Button btn_addtag;
    EditText ed_addtagIn;
    String property = "";
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_tag_edit);


        dbHelper = new tools_MyDatabaseHelper(Activity_tagEdit.this, "tag.db", null, 1);
        db_mine = dbHelper.getWritableDatabase();


        list_tag = findViewById(R.id.list_tag);
        image_property = findViewById(R.id.propertyimage);
        list_tag = findViewById(R.id.list_tag);
        return_bar = findViewById(R.id.return_bar_image);
        btn_showdialog = findViewById(R.id.edit);
        View view  = View.inflate(Activity_tagEdit.this,R.layout.pop_tagadd,null);
        btn_addtag = view.findViewById(R.id.btn_addtag);
        ed_addtagIn = view.findViewById(R.id.ed_tagInput);
        final AlertDialog a = new AlertDialog.Builder(Activity_tagEdit.this).create();
        a.setView(view);


        Intent intent = getIntent();
        property = intent.getStringExtra("property");
        Log.d("XXXX", property);
        initList();
        adapter_tablelist.imageSwitch(property,image_property);


        final adapter_tagEdit adapter_tag = new adapter_tagEdit(tagList);
        list_tag.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));

        list_tag.setAdapter(adapter_tag);

        return_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_showdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.show();
            }
        });
        btn_addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tagList.size()>0)
                {
                for(int i = 0;i<tagList.size();i++){
                    if(ed_addtagIn.getText().toString().equals(tagList.get(i))){
                        a.dismiss();
                        return;
                    }

                    else {
                        ContentValues values = new ContentValues();
                        values.put("tag",ed_addtagIn.getText().toString());
                        values.put("property",property);
                        db_mine.insert("tag",null,values);
                        tagList.add(0,ed_addtagIn.getText().toString());
                        adapter_tag.updata(tagList);
                        a.dismiss();
                        return;
                    }
                }
                }else {
                    ContentValues values = new ContentValues();
                    values.put("tag",ed_addtagIn.getText().toString());
                    values.put("property",property);
                    db_mine.insert("tag",null,values);
                    tagList.add(0,ed_addtagIn.getText().toString());
                    adapter_tag.updata(tagList);
                    a.dismiss();
                }
            }
        });
    }
    void initList(){
        String now_tag ;
        tagList.clear();
        Cursor cursor = db_mine.query("tag",null,"property=?",
                new String[]{property},null,null,"id asc");
        if(cursor.moveToFirst())
        {
            do {
                now_tag = cursor.getString(cursor.getColumnIndex("tag"));
                tagList.add(now_tag);
                Log.d("SSSSS",tagList.toString());

            }while (cursor.moveToNext());
        }

    }
}
