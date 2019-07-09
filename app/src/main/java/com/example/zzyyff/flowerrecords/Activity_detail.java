package com.example.zzyyff.flowerrecords;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_detail extends AppCompatActivity {
    ImageView return_bar;
    ImageView delete_bar;
    CardView edit_bar;
    ImageView imageProperty;
    TextView propertyText;
    TextView moneyText;
    TextView remarkText;
    TextView dateText;
    TextView paymethodText;
    String property = "";
    String id = "";
    String inorout = "";
    double money = 0;
    String date = "";
    String remark = "";
    String paymethod = "";
    static tools_MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_detail);
        initView();
        setViewvalue();
        initClicklistener();
    }
    void initClicklistener(){
    return_bar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    delete_bar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new android.support.v7.app.AlertDialog.Builder(Activity_detail.this)
                    .setTitle("警告")
                    .setMessage("确定删除吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            db.delete("record","id=?",new String[]{id});
                            finish();
                        }
                    }).setNegativeButton("取消", null)
                    .create().show();

        }
    });
    edit_bar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Activity_detail.this,KeepAccountActivity.class);
            intent .putExtra("addoredit","edit");
            intent.putExtra("money",money);
            intent.putExtra("property",property);
            intent.putExtra("id",id);
            intent.putExtra("date",date);
            intent.putExtra("inorout",inorout);
            intent.putExtra("remark",remark);
            intent.putExtra("paymethod",paymethod);

            startActivityForResult(intent,2);

        }
    });

}
    void setViewvalue(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        property = bundle.getString("property");
        id = bundle.getString("id");
        inorout = bundle.getString("inorout");
        date = bundle.getString("date");
        money = bundle.getDouble("money");
        remark = bundle.getString("remark");
        paymethod = bundle.getString("paymethod");
        adapter_tablelist.imageSwitch(property,imageProperty);
        propertyText.setText(property);
        moneyText.setText(money+"");
        remarkText.setText(remark);
        dateText.setText(date);
        paymethodText.setText(paymethod);
    }
    void initView(){
        dbHelper = new tools_MyDatabaseHelper(Activity_detail.this, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        imageProperty = (ImageView)findViewById(R.id.propertyimage);
        propertyText = (TextView)findViewById(R.id.propertytext);
        moneyText = (TextView)findViewById(R.id.moneytext);
        remarkText = (TextView)findViewById(R.id.remarktext);
        dateText = (TextView)findViewById(R.id.datetext);
        paymethodText =(TextView)findViewById(R.id.paymethodtext);
        return_bar = (ImageView)findViewById(R.id.return_bar_image);
        delete_bar = (ImageView)findViewById(R.id.delete_bar);
        edit_bar = (CardView) findViewById(R.id.edit);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 2:
if (resultCode == RESULT_OK) {
    property = data.getStringExtra("property");
    id = data.getStringExtra("id");
    inorout = data.getStringExtra("inorout");
    date = data.getStringExtra("date");
    money = data.getDoubleExtra("money", 0);
    remark = data.getStringExtra("remark");
    paymethod = data.getStringExtra("paymethod");
    adapter_tablelist.imageSwitch(property, imageProperty);
    propertyText.setText(property);
    moneyText.setText(money + "");
    remarkText.setText(remark);
    dateText.setText(date);
    paymethodText.setText(paymethod);
    break;
}
        }

    }
}
