package com.example.zzyyff.flowerrecords;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;



public class Activity_clock extends AppCompatActivity {


    Calendar mCalendar;
    Switch mSwitch;
    TextView time_hour;
    TextView time_minute;
    ImageView back_back;
    LinearLayout clicktochange;
    String hour = "00";
    String minute = "00";
    AlarmManager am = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_clock);

        mSwitch = findViewById(R.id.switch_one);
        time_hour = findViewById(R.id.time_hour);
        time_minute = findViewById(R.id.time_minute);
        back_back = (ImageView)findViewById(R.id.btnBack1);
        clicktochange = findViewById(R.id.cliketochange);
        hour = SPUtils.get(Activity_clock.this,"hour","00").toString();
        minute = SPUtils.get(Activity_clock.this,"minute","00").toString();
        time_hour.setText(hour);
        time_minute.setText(minute);

        String isorno = (String) SPUtils.get(Activity_clock.this,"isorno","no");
        switch (isorno)
        {
            case "is":
                mSwitch.setChecked(true);
                clicktochange.setEnabled(true);
                break;
            case "no":
                mSwitch.setChecked(false);
                clicktochange.setEnabled(false);
                break;
        }
        initClickListener();
    }
    private void showTimeDialog() {
                TimePickerDialog timeDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {
                            //从这个方法中取得获得的时间
                            @Override
                            public void onTimeSet(TimePicker view,int hourOfDay,int minute) {
                                if(hourOfDay<10){
                                    Activity_clock.this.hour="0"+hourOfDay;
                                }else {
                                    hour = hourOfDay+"";
                                }
                                if(minute<10){
                                    Activity_clock.this.minute="0"+minute;
                                }else {
                                    Activity_clock.this.minute = minute+"";
                                }
                                time_hour.setText(hour);
                                time_minute.setText(Activity_clock.this.minute);
                                SPUtils.put(Activity_clock.this,"hour",time_hour.getText());
                                SPUtils.put(Activity_clock.this,"minute",time_minute.getText());
                                startRemind();
                            }

                        }, Integer.parseInt(hour), Integer.parseInt(minute), true);
                timeDialog.show();



            }
    private void startRemind(){
        if(am!=null) {
            stopRemind();
        }
        mCalendar = Calendar.getInstance();
        int now_hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int now_minute = mCalendar.get(Calendar.MINUTE);
        mCalendar.set(Calendar.HOUR_OF_DAY, now_hour);
        mCalendar.set(Calendar.MINUTE, now_minute);
        long systemTime = mCalendar.getTimeInMillis();
        mCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String) SPUtils.get(Activity_clock.this,"hour","00")));
        mCalendar.set(Calendar.MINUTE, Integer.parseInt((String) SPUtils.get(Activity_clock.this,"minute","00")));
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        long selectTime = mCalendar.getTimeInMillis();
        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(Activity_clock.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(Activity_clock.this, 0, intent, 0);

            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }
    private void stopRemind(){
        Intent intent = new Intent(Activity_clock.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(Activity_clock.this, 0,
                intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.cancel(pi);
    }
    void initClickListener(){
        back_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clicktochange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SPUtils.put(Activity_clock.this,"isorno","is");
                    clicktochange.setEnabled(true);
                    startRemind();
                }else {
                    clicktochange.setEnabled(false);
                    SPUtils.put(Activity_clock.this,"isorno","no");
                    stopRemind();
                }
            }
        });

    }

}
