package com.example.zzyyff.flowerrecords;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_diaryedit extends AppCompatActivity {

    String addoredit;
    TextView myText;
    TextView dateText;
    ImageView return_bar;
    ImageView ok_Bar;
    ImageView diary_image;
    Button takePhoto;
    Button select;
    TextView property_1;
    TextView property_2;
    TextView city_show;
    ImageView wheather_show;
    String property = "食";
    String city = "";
    String wheather = "";
    final ContentValues values = new ContentValues();


    static tools_MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;

    static tools_MyDatabaseHelper dbHelper_w;
    static SQLiteDatabase db_w;



    private static final String Uri_wheather = "https://www.sojson.com/open/api/weather/json.shtml?city=";
    private static final String Uri_city = "http://ip.taobao.com/service/getIpInfo.php?ip=";
    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;
    File outputImage;
    AlertDialog a;
    String takenPath;

    AlertDialog propertySelect ;
    View selectView;
    TextView food ;
    TextView thing ;
    TextView space ;
    TextView people;

    private tools_CustomDatePicker datePicker;
    private String time;
    private String date;
    String date_year;
    String date_month;
    String date_day;
    Handler mHandler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_diaryedit);
        setFullScreen();
        propertySelect  = new AlertDialog.Builder(Activity_diaryedit.this).create();
        selectView = LayoutInflater.from(Activity_diaryedit.this).inflate(R.layout.pop_propertyselect,null);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        dbHelper = new tools_MyDatabaseHelper(Activity_diaryedit.this, "diary.db", null, 1);
        db = dbHelper.getWritableDatabase();

        dbHelper_w = new tools_MyDatabaseHelper(Activity_diaryedit.this, "wheather.db", null, 1);
        db_w = dbHelper.getWritableDatabase();

        wheather_show = findViewById(R.id.wheather_show);
        city_show = (TextView)findViewById(R.id.city_show);
        myText = (EditText)findViewById(R.id.mytext);
        dateText = findViewById(R.id.datetext);
        return_bar = findViewById(R.id.return_bar_image);
        ok_Bar = findViewById(R.id.ok_bar);
        diary_image = findViewById(R.id.dia_image);
        property_1 = findViewById(R.id.property_1);
        property_2 = findViewById(R.id.property_2);
        initPicker();
        myText.setFocusable(false);
        myText.setFocusableInTouchMode(false);
        propertySelect = new AlertDialog.Builder(Activity_diaryedit.this).create();
        selectView = LayoutInflater.from(Activity_diaryedit.this).inflate(R.layout.pop_propertyselect,null);
        food = selectView.findViewById(R.id.food);
        thing = selectView.findViewById(R.id.thing);
        space = selectView.findViewById(R.id.space);
        people = selectView.findViewById(R.id.people);
        propertySelect.setView(selectView);



        mHandler = new Handler(){
            public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    city_show.setText(city);
                    Cursor cursor = db_w.rawQuery("select * from wheather where date = ? and city = ? ",new String[]{date,city} );
                    if(cursor.moveToFirst()){
                        wheather = cursor.getString(cursor.getColumnIndex("wheather"));
                        //数据库加载图片
                        initWheather(wheather,wheather_show);

                    }else {
                        sendRequsetWithOkHttp_wheather(city);
                    }

                    break;
                case 2:
                    initWheather(wheather,wheather_show);
                    //网络加载图片
                    values.put("date",date);
                    values.put("city",city);
                    values.put("wheather",wheather);
                    db_w.insert("wheather",null,values);
                    break;
            }
        }
        };

        initClickListener();



    }


    void initClickListener(){

        property_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertySelect.show();
            }
        });

        property_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propertySelect.show();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property = "食";
                property_1.setText(property);
                property_2.setText(property+"↑");
                propertySelect.dismiss();
            }
        });
        thing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property = "物";
                property_1.setText(property);
                property_2.setText(property+"↑");
                propertySelect.dismiss();
            }
        });
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property = "景";
                property_1.setText(property);
                property_2.setText(property+"↑");
                propertySelect.dismiss();
            }
        });
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property = "人";
                property_1.setText(property);
                property_2.setText(property+"↑");
                propertySelect.dismiss();
            }
        });











        //照片的选择
        diary_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = new AlertDialog.Builder(Activity_diaryedit.this).create();
                View selectView = LayoutInflater.from(Activity_diaryedit.this).inflate(R.layout.pop_selectimage,null);
                takePhoto = selectView.findViewById(R.id.takephoto);
                select = selectView.findViewById(R.id.select);
                a.setView(selectView);
                a.show();
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(Activity_diaryedit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Activity_diaryedit.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }
                        //拍照获取图片
                        take_photo();
                        a.dismiss();
                    }
                });
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从相册中选取图片
                        select_photo();
                        a.dismiss();
                    }
                });
            }
        });




        myText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myText.setFocusable(true);
                myText.setFocusableInTouchMode(true);
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(date,3);
            }
        });

        final Intent intent = getIntent();
        addoredit = intent.getStringExtra("addoredit");
        switch (addoredit)
        {
            case "add":
                //得到城市
                sendRequsetWithOkHttp();

                ok_Bar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put("date_year", date_year);
                        values.put("date_month", date_month);
                        values.put("date_day", date_day);
                        values.put("text",myText.getText().toString());
                        values.put("image_path",takenPath);
                        values.put("property",property);
                        values.put("city",city);
                        values.put("wheather",wheather);

                        db.insert("diary", null, values);
                        Intent intent1 = new Intent();
                        intent1.putExtra("position_return",2);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                });
                break;
            case "edit":
                final int id = intent.getIntExtra("id",0);
                String text = intent.getStringExtra("text");

                displayImage(intent.getStringExtra("image_path"));
                myText.setText(text);

                city_show.setText(intent.getStringExtra("city"));
                initWheather(intent.getStringExtra("wheather"),wheather_show);


                dateText.setText(intent.getStringExtra("date"));
                takenPath = intent.getStringExtra("image_path");
                property = intent.getStringExtra("property");
                property_1.setText(property);
                property_2.setText(property+"↑");

                ok_Bar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        values.put("text",myText.getText().toString());
                        values.put("date_year", date_year);
                        values.put("date_month", date_month);
                        values.put("date_day", date_day);
                        values.put("property",property);
                        values.put("image_path",takenPath);

                        db.update("diary",values,"id=?",new String[]{String.valueOf(id)});
                        Intent intent1 = new Intent();
                        intent1.putExtra("position_return",2);
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                });
                break;
        }


        return_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.putExtra("position_return",2);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });
    }
    private void initPicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        time = sdf.format(new Date());
        date = time.split(" ")[0];
        //设置当前显示的日期
        date_year = date.substring(0,4);
        date_month = date.substring(5,7);
        date_day = date.substring(8,10);
        dateText.setText(com.example.zzyyff.flowerrecords.fragment_diary.numDayToChineseCharactersDay(date_year,"YEAR")+"年"
                +fragment_diary.numDayToChineseCharactersDay(date_month,"MONTH")+"月"
                +fragment_diary.numDayToChineseCharactersDay(date_day,"DAY")+"日");

        datePicker = new tools_CustomDatePicker(this, "请选择日期", new tools_CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                date_year = time.split(" ")[0].substring(0,4);
                date_month = time.split(" ")[0].substring(5,7);
                date_day = time.split(" ")[0].substring(8,10);
                dateText.setText(fragment_diary.numDayToChineseCharactersDay(date_year,"YEAR")+"年"
                        +fragment_diary.numDayToChineseCharactersDay(date_month,"MONTH")+"月"
                        +fragment_diary.numDayToChineseCharactersDay(date_day,"DAY")+"日");

            }
        }, "2017-01-01 00:00", time);
        datePicker.showSpecificTime(false); //显示时和分
        datePicker.setIsLoop(false);
        datePicker.setDayIsLoop(true);
        datePicker.setMonIsLoop(true);
    }
    public void take_photo() {

        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)) {
            //创建File对象，用于存储拍照后的图片
            takenPath = getSDPath()+"/DCIM/Camera/"+ getPhotoFileName();//设置图片文件路径，getSDPath()和getPhotoFileName()具体实现在下面
            outputImage = new File(takenPath);
            if (!outputImage.exists()) {
                try {
                    outputImage.createNewFile();//创建新文件
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //启动相机程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

            intent.putExtra(MediaStore.EXTRA_OUTPUT,//Intent有了图片的信息
                    Uri.fromFile(outputImage));


            startActivityForResult(intent, TAKE_PHOTO);
        }else
        {

            Toast.makeText(Activity_diaryedit.this, "没有储存卡",Toast.LENGTH_LONG).show();
        }
    }
    public void select_photo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO :
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = BitmapFactory.decodeFile(takenPath, null);
                    diary_image.setImageBitmap(bitmap);
                    Intent intent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(outputImage);
                    intent1.setData(uri);
                    sendBroadcast(intent1);
                }
                break;
            case SELECT_PHOTO :
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImgeOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri,null);
            }else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            //根据图片路径显示图片
            displayImage(imagePath);
        }
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            diary_image.setImageBitmap(getBitmap(imagePath,720,1280));
        }else {
            //Toast.makeText(Activity_diaryedit.this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
    private String getImagePath(Uri uri,String selection) {
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                takenPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));


            }
            cursor.close();
        }
        return takenPath;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }else {
                    Toast.makeText(Activity_diaryedit.this,"failed to get image",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();

    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'yyyyMMddHHmmss");
        return dateFormat.format(date)  +".jpg";
    }
    public static Bitmap getBitmap(String filePath,int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize =  inSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }
    public static int inSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width<=height){//竖屏拍照
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height
                        / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
        }else {//横屏
            if (height > reqWidth || width > reqHeight) {
                final int heightRatio = Math.round((float) height
                        / (float) reqWidth);
                final int widthRatio = Math.round((float) width / (float) reqHeight);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
        }
        return inSampleSize;
    }
    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    private void sendRequsetWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Log.d("SSSSSS", GetNetIp());
                Request request = new Request.Builder()
                        .url(Uri_city+GetNetIp())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONObject_city(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONObject_city(String jsonData){
        try {
            if(jsonData!=null&&!"".equals(jsonData)) {
                city = new JSONObject(jsonData).getJSONObject("data").getString("city").toString();
                Log.d("AAAAAAA",city);
                Message message = new Message();
                message.what = 1;
                mHandler.sendMessage(message);

               // sendRequsetWithOkHttp_wheather(city);
            }
        } catch (JSONException e) {
            sendRequsetWithOkHttp();
            e.printStackTrace();
        }
    }
    private void sendRequsetWithOkHttp_wheather(final String city){
        //查天气表
            values.put("date",date);
            values.put("city",city);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    Log.d("SSSSSS", GetNetIp());
                    Request request = new Request.Builder()
                            .url(Uri_wheather+city)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        values.put("wheather",parseJSONObject_wheather(responseData));






                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }
    private String parseJSONObject_wheather(String jsonData){

        try {
            Log.e("AAAAAAAAAAAa", jsonData);
            JSONArray jsonArray = new JSONObject(jsonData).getJSONObject("data").getJSONArray("forecast");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            wheather = jsonObject.getString("type").toString();
            Message message = new Message();
            message.what = 2;
            mHandler.sendMessage(message);
            Log.e("AAAAAAAAAAAa", wheather);
           // Toast.makeText(this, wheather, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
       return wheather;
    }
    public static String GetNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String line = "";
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        line = jsonObject.optString("cip");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    static void initWheather(String wheather,ImageView wheather_show){
        switch (wheather)
        {
            case "晴":
                wheather_show.setImageResource(R.drawable.sun);
                break;
            case "多云":
                wheather_show.setImageResource(R.drawable.moreclound);
                break;
            case "小雨":
                wheather_show.setImageResource(R.drawable.smallrain);
                break;
            case "中雨":
                wheather_show.setImageResource(R.drawable.middlerain);
                break;
            case "大雨":
                wheather_show.setImageResource(R.drawable.bigrain);
                break;
            case "暴雨":
                wheather_show.setImageResource(R.drawable.rainstorm);
                break;
            case "阴":
                wheather_show.setImageResource(R.drawable.yin);
            break;

        }
    }


}
