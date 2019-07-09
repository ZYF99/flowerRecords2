package com.example.zzyyff.flowerrecords;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KeepAccountActivity extends AppCompatActivity implements View.OnClickListener {


    static tools_MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;

    static tools_MyDatabaseHelper dbHelper_tag;
    static SQLiteDatabase db_tag;


    Boolean isNotifycation = false;

    private ViewPager mViewPagerGrid;
    private List<View> mViewPagerGridList;
    private List<class_KeepAccountAttribute> mDatas;
    private List<class_tagitem> remarkDatas;
    private int pageSize;
    private int pageCount;
    private boolean pageOut = true;
    Intent intent = null;


    private RecyclerView remarkRecyclerView;
    private adapter_KeepAccountRemarkShowRv keepAccountRemarkShowRvAdapter;


    private tools_CustomDatePicker datePicker,timePicker;
    private String time;
    private String date;

    private int payModeCount = 0;
    private int inputNumCount = 9;

    private boolean pointhave = false;
    String[] payMode = new String[]{"现金","支付宝","微信","银行卡","代付","信用卡"};

    private ArrayList<View> dots;

    private TextView moneyShow;
    private TextView tvShow;
    private ImageView ivShow;

    private TextView tvIn;
    private TextView tvOut;

    private Button btnNum0;
    private Button btnNum1;
    private Button btnNum2;
    private Button btnNum3;
    private Button btnNum4;
    private Button btnNum5;
    private Button btnNum6;
    private Button btnNum7;
    private Button btnNum8;
    private Button btnNum9;
    private Button btnPoint;
    private Button btnDel;
    private Button btnDate;
    private Button btnPayMode;
    private Button btnOk;
    private ImageView btnSave;
    private ImageView btnBack;
    private ImageView tagEditBtn;


    StringBuilder sB_MoneyInput = new StringBuilder();
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private int beforeIndexPage = 0;//记录上一次点的位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keep_account);
        dbHelper = new tools_MyDatabaseHelper(KeepAccountActivity.this, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();

        dbHelper_tag = new tools_MyDatabaseHelper(KeepAccountActivity.this, "tag.db", null, 1);
        db_tag = dbHelper_tag.getWritableDatabase();

        sp = getSharedPreferences("mine",MODE_PRIVATE);
        editor = sp.edit();

        initTaglist();

        findViewById();

        btnNum0.setOnClickListener(this);
        btnNum1.setOnClickListener(this);
        btnNum2.setOnClickListener(this);
        btnNum3.setOnClickListener(this);
        btnNum4.setOnClickListener(this);
        btnNum5.setOnClickListener(this);
        btnNum6.setOnClickListener(this);
        btnNum7.setOnClickListener(this);
        btnNum8.setOnClickListener(this);
        btnNum9.setOnClickListener(this);
        btnPayMode.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnPoint.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvOut.setOnClickListener(this);
        tvIn.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tagEditBtn.setOnClickListener(this);


        initPicker();
        resetData();
        initMenuDatas();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);
        keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
        remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);
        buildMenuChoose();
        tvShow.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

               initRemarkDatas();
            }
        });
    }
    private void buildMenuChoose(){

        LayoutInflater inflater = LayoutInflater.from(this);
        //塞GridView至ViewPager中：
        pageSize = getResources().getInteger(R.integer.HomePageHeaderColumn) * 2;
        //一共的页数等于 总数/每页数量，并取整。
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);

        dots = new ArrayList<View>();
        View view0 = this.findViewById(R.id.dot_0);
        View view1 = this.findViewById(R.id.dot_1);
        View view2 = this.findViewById(R.id.dot_2);
        View view3 = this.findViewById(R.id.dot_3);

        if(pageCount==4){
        }else if(pageCount == 3){
            dots.add(view0);
            dots.add(view1);
            dots.add(view2);
            view3.setVisibility(View.GONE);
        }else if(pageCount == 2){
            dots.add(view0);
            dots.add(view1);
            view2.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
        }
        //获取屏幕的宽度,单位px
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        //获取GridView中每个item的宽度 = 屏幕宽度 / GridView显示的列数
        int columnWidth = (int) Math.ceil((screenWidth) * 1.0 / (getResources().getInteger(R.integer.HomePageHeaderColumn)));
        mViewPagerGridList = new ArrayList<View>();
        for (int index = 0; index < pageCount; index++) {
            //每个页面都是inflate出一个新实例
            GridView grid = (GridView) inflater.inflate(R.layout.item_keep_account_viewpager, mViewPagerGrid, false);
            grid.setAdapter(new adapter_KeepAccountGridView(this, mDatas, index,tvShow,ivShow));
            mViewPagerGridList.add(grid);
        }




        mViewPagerGrid.setAdapter(new adapter_KeepAccountViewpager(mViewPagerGridList));

        mViewPagerGrid.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dots.get(beforeIndexPage).setBackgroundResource(R.drawable.dot_normal);
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);
                beforeIndexPage  = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initRemarkDatas(){
        remarkDatas= new ArrayList<class_tagitem>();

            String tag_now ;



            Cursor cursor1 = db_tag.query("tag",null,"property=?",
                    new String[]{tvShow.getText().toString()},null,null,"id desc");
            if (cursor1.moveToFirst())
            {
                do{
                        tag_now = cursor1.getString(cursor1.getColumnIndex("tag"));
                        class_tagitem class_tagitem = new class_tagitem(tag_now,false);
                        remarkDatas.add(class_tagitem);
                }while (cursor1.moveToNext());
                keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
                remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);
            }else {
                //Toast.makeText(KeepAccountActivity.this,"!!",Toast.LENGTH_SHORT).show();

                keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
                remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);

                //keepAccountRemarkShowRvAdapter.notifyDataSetChanged();
            }


    }

    private void initRemarkDatas(String tag){
        remarkDatas= new ArrayList<class_tagitem>();
        String[] taghave = null;
        taghave = tag.split("/");
        String tag_now ;
        if(taghave.length<=1){
            Cursor cursor2 = db_tag.query("tag",null,"property=?",
                    new String[]{tvShow.getText().toString()},null,null,"id desc");
            if (cursor2.moveToFirst())
            {
                int j=0;
                do{
                    class_tagitem class_tagitem = null;
                    tag_now = cursor2.getString(cursor2.getColumnIndex("tag"));
                        if(tag_now.equals(tag)){
                            class_tagitem = new class_tagitem(tag_now,true);
                        }
                        else {
                            class_tagitem = new class_tagitem(tag_now,false);
                    }

                    remarkDatas.add(class_tagitem);
                }while (cursor2.moveToNext());
                keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
                remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);
            }else {
                //Toast.makeText(KeepAccountActivity.this,"!!",Toast.LENGTH_SHORT).show();

                keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
                remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);

                //keepAccountRemarkShowRvAdapter.notifyDataSetChanged();
            }

        }
        else {
            Cursor cursor1 = db_tag.query("tag",null,"property=?",
                    new String[]{tvShow.getText().toString()},null,null,"id desc");
            if (cursor1.moveToFirst())
            {
                int j=0;
                do{
                    class_tagitem class_tagitem = null;
                    tag_now = cursor1.getString(cursor1.getColumnIndex("tag"));

                    for(int i = j;i<taghave.length;i++){

                        if(tag_now.equals(taghave[i])){
                            class_tagitem = new class_tagitem(tag_now,true);
                            Log.e("DDDDDDDDDDDD", taghave[i]);
                            j++;
                            break;
                        }
                        else {
                            class_tagitem = new class_tagitem(tag_now,false);
                            Log.e("XXXXXXXXXXX", taghave[i]);
                        }
                    }

                    remarkDatas.add(class_tagitem);
                }while (cursor1.moveToNext());
                keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
                remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);
            }else {
                //Toast.makeText(KeepAccountActivity.this,"!!",Toast.LENGTH_SHORT).show();

                keepAccountRemarkShowRvAdapter = new adapter_KeepAccountRemarkShowRv(remarkDatas);
                remarkRecyclerView.setAdapter(keepAccountRemarkShowRvAdapter);

                //keepAccountRemarkShowRvAdapter.notifyDataSetChanged();
            }
        }

    }

    private void initMenuDatas() {
        mDatas = new ArrayList<class_KeepAccountAttribute>();
        if(pageOut){
            mDatas.add(new class_KeepAccountAttribute("餐饮", R.drawable.food));
            mDatas.add(new class_KeepAccountAttribute("零食", R.drawable.snacks));
            mDatas.add(new class_KeepAccountAttribute("购物", R.drawable.shopping));
            mDatas.add(new class_KeepAccountAttribute("娱乐", R.drawable.entertainment));
            mDatas.add(new class_KeepAccountAttribute("学习", R.drawable.study));
            mDatas.add(new class_KeepAccountAttribute("数码", R.drawable.camera));
            mDatas.add(new class_KeepAccountAttribute("停放", R.drawable.park));
            mDatas.add(new class_KeepAccountAttribute("酒店", R.drawable.hotel));
            mDatas.add(new class_KeepAccountAttribute("出差", R.drawable.businesstravel));
            mDatas.add(new class_KeepAccountAttribute("公交", R.drawable.traffic));
            mDatas.add(new class_KeepAccountAttribute("飞机", R.drawable.aircraft));
            mDatas.add(new class_KeepAccountAttribute("旅行", R.drawable.travel));
            mDatas.add(new class_KeepAccountAttribute("度假", R.drawable.vacation));
            mDatas.add(new class_KeepAccountAttribute("健身", R.drawable.bodybuilding));
            mDatas.add(new class_KeepAccountAttribute("户外", R.drawable.outdoors));
            mDatas.add(new class_KeepAccountAttribute("出租车", R.drawable.taxi));
            mDatas.add(new class_KeepAccountAttribute("火车", R.drawable.train));
            mDatas.add(new class_KeepAccountAttribute("轮船", R.drawable.ship));
            mDatas.add(new class_KeepAccountAttribute("剧院", R.drawable.show));
        }
        else {
            mDatas.add(new class_KeepAccountAttribute("工资", R.drawable.salary));
            mDatas.add(new class_KeepAccountAttribute("投资", R.drawable.investment));
            mDatas.add(new class_KeepAccountAttribute("彩票", R.drawable.lottery));
            mDatas.add(new class_KeepAccountAttribute("红包", R.drawable.redenvelopes));
            mDatas.add(new class_KeepAccountAttribute("福利", R.drawable.welfare));
            mDatas.add(new class_KeepAccountAttribute("兼职", R.drawable.parttime));
            mDatas.add(new class_KeepAccountAttribute("利息", R.drawable.interest));
            mDatas.add(new class_KeepAccountAttribute("贷款", R.drawable.loan));
            mDatas.add(new class_KeepAccountAttribute("风投", R.drawable.windcast));
            mDatas.add(new class_KeepAccountAttribute("变卖", R.drawable.selloff));
            mDatas.add(new class_KeepAccountAttribute("其他", R.drawable.other));
        }

    }
    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
        //设置当前显示的日期
        btnDate.setText("今日");

        datePicker = new tools_CustomDatePicker(this, "请选择日期", new tools_CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if(date.equals(time.split(" ")[0])){
                    btnDate.setText("今日");
                    btnDate.setTextSize(18);
                }
                else {
                    btnDate.setText(time.split(" ")[0]);
                    btnDate.setTextSize(13);
                }

            }
        }, "2017-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);

        timePicker = new tools_CustomDatePicker(this, "请选择时间", new tools_CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
               // currentTime.setText(time);
            }
        }, "2017-01-01 00:00", "2037-12-31 23:59");//"2027-12-31 23:59"
        timePicker.showSpecificTime(true);
        timePicker.setIsLoop(true);
    }
    private void resetData(){


            intent = getIntent();
            isNotifycation = false;
            if (intent.getStringExtra("addoredit").equals("edit")) {
                tvShow.setText(intent.getStringExtra("property"));
                adapter_tablelist.imageSwitch(intent.getStringExtra("property"), ivShow);
                String intentData = intent.getStringExtra("date").substring(0, 4) + "-" + intent.getStringExtra("date").substring(5, 7) + "-" + intent.getStringExtra("date").substring(8, 10);
                if (!date.equals(intentData)) {
                    btnDate.setText(intentData);
                    btnDate.setTextSize(13);
                } else {
                    btnDate.setText("今日");
                    btnDate.setTextSize(18);
                }

                if (intent.getStringExtra("inorout").equals("out")) {
                    pageOut = true;
                    tvOut.setTextColor(this.getResources().getColor(R.color.colorSelectedText));
                    tvIn.setTextColor(this.getResources().getColor(R.color.color_gold));
                } else {
                    pageOut = false;
                    tvIn.setTextColor(this.getResources().getColor(R.color.colorSelectedText));
                    tvOut.setTextColor(this.getResources().getColor(R.color.color_gold));
                }

                for (int i = 0; i < payMode.length; i++) {
                    if (payMode[i].equals(intent.getStringExtra("paymethod"))) {
                        payModeCount++;
                        btnPayMode.setText(payMode[i]);
                    }
                }
                moneyShow.setText(String.valueOf(intent.getDoubleExtra("money", 0.00)));
                sB_MoneyInput.append(String.valueOf(intent.getDoubleExtra("money", 0.00)));
                for (int i = 0; i < sB_MoneyInput.length(); i++) {
                    if (sB_MoneyInput.charAt(i) == '.') {
                        pointhave = true;
                        if (i == sB_MoneyInput.length() - 2) {
                            inputNumCount = i + 3;
                        } else if (i == sB_MoneyInput.length() - 3) {
                            inputNumCount = i + 3;
                        }
                    }
                }
                if (!intent.getStringExtra("remark").equals("")) {
                    // Log.e("SSSSSSSSSSSSSSSSS", intent.getStringExtra("remark"));
                    initRemarkDatas(intent.getStringExtra("remark"));
                } else {
                    initRemarkDatas();
                }

            } else {
                initRemarkDatas();
            }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        isNotifycation = true;

    }

    private void findViewById(){
        tvShow = (TextView)findViewById(R.id.tvShow);
        ivShow = (ImageView)findViewById(R.id.ivShow);
        mViewPagerGrid = (ViewPager) findViewById(R.id.keepAccountViewpage);
        remarkRecyclerView = (RecyclerView)findViewById(R.id.rvRemark);

        sB_MoneyInput.append("");
        tvIn = (TextView)findViewById(R.id.tvIn);
        tvOut = (TextView)findViewById(R.id.tvOut);
        moneyShow = (TextView)findViewById(R.id.moneyShow);
        btnNum0 = (Button)findViewById(R.id.btnNum0);
        btnNum1 = (Button)findViewById(R.id.btnNum1);
        btnNum2 = (Button)findViewById(R.id.btnNum2);
        btnNum3 = (Button)findViewById(R.id.btnNum3);
        btnNum4 = (Button)findViewById(R.id.btnNum4);
        btnNum5 = (Button)findViewById(R.id.btnNum5);
        btnNum6 = (Button)findViewById(R.id.btnNum6);
        btnNum7 = (Button)findViewById(R.id.btnNum7);
        btnNum8 = (Button)findViewById(R.id.btnNum8);
        btnNum9 = (Button)findViewById(R.id.btnNum9);
        btnPoint = (Button)findViewById(R.id.btnPoint);
        btnDel = (Button)findViewById(R.id.btnDel);
        btnPayMode = (Button)findViewById(R.id.btnPayMode);
        btnOk = (Button)findViewById(R.id.btnOK);
        btnDate = (Button)findViewById(R.id.btnDate);
        btnSave = (ImageView)findViewById(R.id.btnSave);
        btnBack = (ImageView)findViewById(R.id.btnBack1);
        tagEditBtn = (ImageView)findViewById(R.id.tag_edit);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvIn:
                if(pageOut){
                    pageOut = false;
                    initMenuDatas();
                    tvIn.setTextColor(this.getResources().getColor(R.color.colorSelectedText));
                    tvOut.setTextColor(this.getResources().getColor(R.color.color_gold));
                    buildMenuChoose();

                }
                break;
            case R.id.tvOut:
                if(!pageOut){
                    pageOut = true;
                    initMenuDatas();
                    tvOut.setTextColor(this.getResources().getColor(R.color.colorSelectedText));
                    tvIn.setTextColor(this.getResources().getColor(R.color.color_gold));
                    buildMenuChoose();

                }
                break;
            case R.id.btnNum0:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("0");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum1:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("1");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum2:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("2");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum3:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("3");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum4:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("4");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum5:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("5");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum6:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("6");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum7:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("7");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum8:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("8");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnNum9:
                if(sB_MoneyInput.length()<inputNumCount)
                    sB_MoneyInput.append("9");
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnPoint:
                if(sB_MoneyInput.length()<inputNumCount+1&&!pointhave)
                {
                    if(sB_MoneyInput.length()==0){
                        sB_MoneyInput.append("0.");
                        inputNumCount = 4;
                    }
                    else
                    sB_MoneyInput.append(".");
                    pointhave = true;
                    inputNumCount = sB_MoneyInput.length()+2;
                }
                moneyShow.setText(sB_MoneyInput);
                break;
            case R.id.btnPayMode:
                btnPayMode.setText(payMode[payModeCount%6]);
                payModeCount++;
                break;
            case R.id.tag_edit:
                Intent intent3 = new Intent(this,Activity_tagEdit.class);
                intent3.putExtra("property",tvShow.getText().toString());
                startActivity(intent3);
                break;
            case R.id.btnOK:
            case  R.id.btnSave:
                if(sB_MoneyInput.length()!=0){
                    double numberInput = Double.valueOf(sB_MoneyInput.toString());
                    String reMarkInput = keepAccountRemarkShowRvAdapter.getRemark();
                    String payModeInput = btnPayMode.getText().toString();
                    String dateInput;
                    String dateInput_year;
                    String dateInput_month;
                    String dateInput_day;
                    String propertyInput = tvShow.getText().toString();
                    if(btnDate.getText().toString().equals("今日")){
                        dateInput_year = date.substring(0,4);
                        dateInput_month = date.substring(5,7);
                        dateInput_day = date.substring(8,10);
                        dateInput  = dateInput_year+"年"+dateInput_month+"月"+dateInput_day+"日";
                    }
                    else{
                       dateInput_year = btnDate.getText().toString().substring(0,4);
                       dateInput_month = btnDate.getText().toString().substring(5,7);
                       dateInput_day = btnDate.getText().toString().substring(8,10);
                       dateInput  = dateInput_year+"年"+dateInput_month+"月"+dateInput_day+"日";

                    }
                    String inOrOut;
                    if(pageOut){
                        inOrOut = "out";
                    }
                    else{
                        inOrOut = "in";
                    }
                    ContentValues values = new ContentValues();
                    values.put("date",dateInput);
                    values.put("inorout", inOrOut);
                    if(pageOut) {
                        values.put("cost", numberInput);
                        values.put("income",0);
                    }
                    else {
                        values.put("cost", 0);
                        values.put("income",numberInput);
                    }
                    values.put("remark",reMarkInput);
                    values.put("date_year",dateInput_year);
                    values.put("date_month",dateInput_month);
                    values.put("date_day",dateInput_day);
                    //values.put("time","12:20");
                    values.put("property",propertyInput);
                    values.put("paymethod",payModeInput);

                    if(intent.getStringExtra("addoredit").equals("add"))
                    {
                        db.insert("record",null,values);

                    }else {
                        db.update("record",values,"id=?",new String[]{intent.getStringExtra("id")});
                    }
                    Intent intent1 = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("property",propertyInput);
                    bundle.putString("id",intent.getStringExtra("id"));
                    bundle.putString("inorout",inOrOut);
                    bundle.putString("date",dateInput);
                    bundle.putDouble("money",numberInput);
                    bundle.putString("remark",reMarkInput);
                    bundle.putString("paymethod",payModeInput);
                    setResult(RESULT_OK,intent1);
                    intent1.putExtras(bundle);
                    finish();


                }

                else
                {
                    Toast.makeText(this,"没有输入",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnBack1:

                finish();
                break;

            case R.id.btnDate:
                datePicker.show(date,3);
                break;
            case R.id.btnDel:
                if(sB_MoneyInput.length()!=0){
                    if(sB_MoneyInput.charAt(sB_MoneyInput.length()-1)=='.'){
                        pointhave=false;
                        inputNumCount = 9;
                    }

                    sB_MoneyInput.delete(sB_MoneyInput.length()-1,sB_MoneyInput.length());
                    moneyShow.setText(sB_MoneyInput);
                }
                break;


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(intent.getStringExtra("addoredit").equals("edit")) {
            initRemarkDatas(intent.getStringExtra("remark"));
        }
        else {
            initRemarkDatas();
        }

    }



    void initTaglist(){
        if(!sp.getString("isinsert","false").equals("true")) {
            ContentValues values = new ContentValues();
            String[][]tag = new String[][]
                    {{"餐饮","晚餐","午餐","早餐"},{"零食","薯片","辣条"},{"购物","衣服","化妆品"},{"娱乐","网吧","KTV"},{"学习","书籍","文具"},
                    {"数码","手机","相机"},{"停放","兰博基尼","法拉利"},{"酒店","六善","豪生"},{"出差","海南","成都"},{"公交","101路","110路"},
                    {"飞机","飞海南","飞南海"},{"旅行","三亚","夏威夷"},{"度假","地中海"},{"健身","跑步"},{"户外","登山","远足"},
                    {"出租车","滴滴"},{"火车","高铁"},{"轮船","都江堰-成都"},{"剧院","周杰伦演唱会"},
                    {"工资","月薪"},{"投资","花记"},{"彩票","体彩"},{"红包","妈给的"},{"福利","老板给的"},
                            {"兼职","洗盘子"},{"利息","中国银行"},{"贷款","房贷"},{"风投","天使轮"},{"变卖","显示器"},
                            {"其他","随便写点啥"}};

            for(int i=0;i<tag.length;i++)
            {
                for(int j=1;j<tag[i].length;j++)
                {
                    values.put("tag", tag[i][j]);
                    values.put("property", tag[i][0]);
                    db_tag.insert("tag", null, values);
                }
            }
            editor.putString("isinsert","true");
            editor.commit();
        }



    }


}
