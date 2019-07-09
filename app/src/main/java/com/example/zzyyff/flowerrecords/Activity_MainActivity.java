package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.mikephil.charting.charts.PieChart;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Activity_MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,BottomNavigationBar.OnTabSelectedListener{

    android.support.v7.widget.Toolbar toolbar;
    BottomNavigationBar bottomNavigationBar;
    adapter_MyFragmentPager adapter;
    ArrayList<Fragment> fragments;
    ViewPager myviewpager;
    AlertDialog write;
    View popWriteview;
    LinearLayout write_cost;
    LinearLayout write_diary;
    ImageView button_close;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    tools_MyDatabaseHelper dbHelper2;
    SQLiteDatabase db2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_main);



        dbHelper = new tools_MyDatabaseHelper(Activity_MainActivity.this, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper2 = new tools_MyDatabaseHelper(Activity_MainActivity.this, "diary.db", null, 1);
        db2 = dbHelper2.getWritableDatabase();
        initToolbar();
        initViewpager();
        initNavigationBar();
        initWriteDialog();
        initClickListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        quitFullScreen();
    }

    void initClickListener(){
    write_cost.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Activity_MainActivity.this,KeepAccountActivity.class);
            intent.putExtra("addoredit","add");
            startActivityForResult(intent,1);
            write.dismiss();


        }
    });
    write_diary.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Activity_MainActivity.this,Activity_diaryedit.class);
            intent.putExtra("addoredit","add");
            startActivityForResult(intent,1);
            write.dismiss();

        }
    });
    button_close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        if(myviewpager.getCurrentItem()>=2) {
            bottomNavigationBar.selectTab(myviewpager.getCurrentItem() + 1);
        } else
        {
            bottomNavigationBar.selectTab(myviewpager.getCurrentItem());
        }
            write.dismiss();
        }
    });
}
    void initWriteDialog(){
    write = new AlertDialog.Builder(Activity_MainActivity.this).create();
    popWriteview = LayoutInflater.from(Activity_MainActivity.this).inflate(R.layout.pop_layout,null);
    write.setView(popWriteview);
    write_cost = popWriteview.findViewById(R.id.write_cost);
    write_diary = popWriteview.findViewById(R.id.write_diary);
    button_close = popWriteview.findViewById(R.id.button_close);
    }
    void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    void initViewpager(){
        myviewpager = (ViewPager)findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new fragment_list());
        fragments.add(new fragment_table());
        fragments.add(new fragment_diary());
        fragments.add(new fragment_mine());
        adapter = new adapter_MyFragmentPager(getSupportFragmentManager(),fragments);
        myviewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        myviewpager.setOnPageChangeListener(this);
    }


    private void initNavigationBar(){
        final TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.botton_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.list,"明细").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.table,"图表").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.add,"花记").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.diary,"发现").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.mine,"我的").setActiveColorResource(typedValue.resourceId))
                .setFirstSelectedPosition(0).initialise();
        setBottomNavigationItem(11,16,0,30);
    }
    private void setBottomNavigationItem(int space, int imgLen,int centerSpace,int centerImglen) {
        float contentLen = 36;
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try { //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个 Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        // 获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                            if(j!=2) {
                                //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                                labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
                                //获取到Tab内的图像控件
                                ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                                //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) DisplayUtil.dip2px(this, imgLen), (int) DisplayUtil.dip2px(this, imgLen));
                                params.gravity = Gravity.CENTER;
                                iconView.setLayoutParams(params);
                            }else
                            {
                                //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                                labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - centerImglen - centerSpace)));
                                //获取到Tab内的图像控件
                                ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                                //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) DisplayUtil.dip2px(this, centerImglen), (int) DisplayUtil.dip2px(this, centerImglen));
                                params.gravity = Gravity.CENTER;
                                iconView.setLayoutParams(params);

                            }

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class DisplayUtil {
        public static int dip2px(Context context, float dipValue){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dipValue * scale + 0.5f);
        }
        public static int px2dip(Context context, float pxValue){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(pxValue / scale + 0.5f);
        }
    }

    //ViewPager的方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        switch (position)
        {
            case 0:
                bottomNavigationBar.selectTab(0);
                break;
            case 1:
                bottomNavigationBar.selectTab(1);
                break;
            case 2:
                bottomNavigationBar.selectTab(3);
                break;
            case 3:
                bottomNavigationBar.selectTab(4);
                break;
        }

    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //底部导航栏的方法重写
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                myviewpager.setCurrentItem(0);
                break;
            case 1:
                PieChart pieChart = findViewById(R.id.mPieChart);
                if(pieChart!=null) {
                    pieChart.animateY(1000);
                }
                myviewpager.setCurrentItem(1);
                break;
            case 2:
                write.show();
               // propetyAnim(write);
                break;
            case 3:
                myviewpager.setCurrentItem(2);
                break;
            case 4:
                myviewpager.setCurrentItem(3);
                break;
        }

    }
    @Override
    public void onTabUnselected(int position) {
    }
    @Override
    public void onTabReselected(int position) {
        switch (position) {
            case 0:
                myviewpager.setCurrentItem(0);
                break;
            case 1:
                PieChart pieChart = findViewById(R.id.mPieChart);
                if(pieChart!=null) {
                    pieChart.animateY(1000);
                }
                myviewpager.setCurrentItem(1);
                break;
            case 2:
                write.show();
                // propetyAnim(write);
                break;
            case 3:
                myviewpager.setCurrentItem(2);
                break;
            case 4:
                myviewpager.setCurrentItem(3);
                break;
        }
    }




    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if(KeyCode == KeyEvent.KEYCODE_BACK) {
            new android.support.v7.app.AlertDialog.Builder(Activity_MainActivity.this)
                    .setIcon(R.drawable.logo)
                    .setTitle("警告")
                    .setMessage("确定退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).setNegativeButton("取消", null)
                    .create().show();
        }
        return super.onKeyDown(KeyCode,event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    int position = data.getIntExtra("position_return",0);
                    if(position>=2) {
                        initViewpager();
                        bottomNavigationBar.selectTab(position + 1);
                    } else
                    {
                        initViewpager();
                        bottomNavigationBar.selectTab(position);
                    }
                }

        }
    }

    //展示弹出界面动画（记消费，记日记）
    /*
    private void propetyAnim(AlertDialog write) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator = ObjectAnimator.ofFloat(write, "alpha", 0, 0.2f, 0.5f, 0.6f, 0.7f, 0.9f);
        animator.setDuration(1000);
        animator.setRepeatCount(0);
        animator.start();
        write.getWindow().setBackgroundDrawableResource(R.drawable.back_half);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = write.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()); //设置宽度
        lp.height = (int)(display.getHeight());
        write.getWindow().setAttributes(lp);
    }
    */
    private void quitFullScreen(){
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}
