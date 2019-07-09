package com.example.zzyyff.flowerrecords;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_CurrencyConverter extends AppCompatActivity {
    RecyclerView rec_CurrencyConverter;
    EditText ed_CurrencyConverter;
    ImageView imageBack;
    List<class_CurrencyCouvrterMoney> list;
    List<Double> listChangedD;
    List<String> listChangedS;
    adapter_CurrencyConverter adapter;


    private static final String Uri = "https://forex.1forge.com/1.0.3/quotes?pairs=";
    private static final String Key = "&api_key=x4XNVqWvl3YggrFRwpy6tEAtzzGjiOJP";
    private static final String CNY = "CNH";
    private static final String NoHelper = "No network/data";
    StringBuilder requestCode = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_currency_converter);

        findViewById();
        initData();
        listChangedD = new ArrayList<>();
        listChangedS = new ArrayList<>();
        final LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rec_CurrencyConverter.setLayoutManager(linearLayout);
        adapter = new adapter_CurrencyConverter(list);
        rec_CurrencyConverter.setAdapter(adapter);
        sendRequsetWithOkHttp();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ed_CurrencyConverter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(listChangedD.size()>0) {
                    if (ed_CurrencyConverter.getText().toString().length() > 0) {
                        double inputdata = Double.valueOf(ed_CurrencyConverter.getText().toString());
                        for (int i = 0; i < list.size(); i++) {

                            if (listChangedS.get(i).equals(list.get(i).getNameO())) {
                                list.get(i).setMoney(inputdata * listChangedD.get(i) + "");
                            }
                            adapter.updateData(list);
                        }


                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setMoney("No data");
                        }
                        adapter.updateData(list);
                    }
                }
                else
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setMoney("No network");
                    }
                adapter.updateData(list);

            }
        });


    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new class_CurrencyCouvrterMoney("美元","USD","No network/data"));
        list.add(new class_CurrencyCouvrterMoney("日元","JPY", NoHelper ));
        list.add(new class_CurrencyCouvrterMoney("欧元","EUR", NoHelper ));
        list.add(new class_CurrencyCouvrterMoney("英镑","GBP", NoHelper ));
        list.add(new class_CurrencyCouvrterMoney("港币","HKD", NoHelper ));
        list.add(new class_CurrencyCouvrterMoney("澳元","AUD", NoHelper ));
        list.add(new class_CurrencyCouvrterMoney("加元","CAD", NoHelper ));


        for (int i=0;i<list.size();i++){
            if(i==0){
                requestCode.append(CNY+list.get(i).getNameO());
            }
            else
                requestCode.append(","+CNY+list.get(i).getNameO());
        }
    }

    private void findViewById(){
        rec_CurrencyConverter = (RecyclerView)findViewById(R.id.rec_CurrencyConverter);
        ed_CurrencyConverter = (EditText)findViewById(R.id.ed_CurrencyConverter);
        imageBack = (ImageView)findViewById(R.id.btnBack1);
    }


    private void sendRequsetWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(Uri+requestCode.toString()+Key)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();

                    parseJSONObject(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private void parseJSONObject(String jsonData){
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol").substring(3,6);
                double price = jsonObject.getDouble("price");
                listChangedD.add(price);
                listChangedS.add(symbol);
                Log.e("show ", symbol+"   "+price);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

