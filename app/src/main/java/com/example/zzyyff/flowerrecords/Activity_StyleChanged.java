package com.example.zzyyff.flowerrecords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Activity_StyleChanged extends AppCompatActivity implements View.OnClickListener {


    CardView Apptheme;
    CardView Redtheme;
    CardView Yellowtheme;
    CardView Blacktheme;
    CardView Pinktheme;
    ImageView returnback;
    ImageView setture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SPUtils.get(this,"choose","F").equals("F")){
            setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        }
        else {
            setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"choose","AppTheme")));
        }

        setContentView(R.layout.activity__style_changed);
        Apptheme = (CardView)findViewById(R.id.style_basic);
        Redtheme = (CardView)findViewById(R.id.style_red);
        Yellowtheme = (CardView)findViewById(R.id.style_yellow);
        Blacktheme = (CardView)findViewById(R.id.style_black);
        Pinktheme = (CardView)findViewById(R.id.style_pink);
        returnback = findViewById(R.id.returnback);
        setture = findViewById(R.id.setture);

        Apptheme.setOnClickListener(this);
        Redtheme.setOnClickListener(this);
        Yellowtheme.setOnClickListener(this);
        Blacktheme.setOnClickListener(this);
        Pinktheme.setOnClickListener(this);
        returnback.setOnClickListener(this);
        setture.setOnClickListener(this);


    }


    public static int gettheme(String themeName){
       if(themeName.equals("RedTheme")){
           return R.style.RedTheme;
       }
        if(themeName.equals("YellowTheme")){
            return R.style.YellowTheme;
        }
        if(themeName.equals("BlackTheme")){
            return R.style.BlackTheme;
        }
        if(themeName.equals("PinkTheme")){
            return R.style.PinkTheme;
        }
        if(themeName.equals("AppTheme")){
            return R.style.AppTheme;
        }

       return R.style.AppTheme;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.style_basic:
                SPUtils.put(this,"choose","AppTheme");

              recreate();
                break;
            case R.id.style_red:
                SPUtils.put(this,"choose","RedTheme");
               recreate();
                break;
            case R.id.style_black:
                SPUtils.put(this,"choose","BlackTheme");
                recreate();
                break;
            case R.id.style_pink:
                SPUtils.put(this,"choose","PinkTheme");
               recreate();
                break;
            case R.id.style_yellow:
                SPUtils.put(this,"choose","YellowTheme");
               recreate();
                break;
            case R.id.setture:
                Intent intent = new Intent();

                intent.putExtra("resetTheme","ture");
                SPUtils.put(this,"theme",SPUtils.get(this,"choose","AppTheme"));
                SPUtils.put(this,"choose","F");

                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.returnback:
                finish();
                break;
        }
    }
}
