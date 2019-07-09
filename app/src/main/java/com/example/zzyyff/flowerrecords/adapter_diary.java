package com.example.zzyyff.flowerrecords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class adapter_diary extends RecyclerView.Adapter<adapter_diary.ViewHolder> {
    public tools_MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;
    List<class_Diary> list ;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timev ;
        TextView textv;
        TextView yearv;
        TextView monthv;
        CardView clickitem;
        ImageView image_diary;

        private ViewHolder(View view) {
            super(view);
            timev = view.findViewById(R.id.card_time);
            textv = view.findViewById(R.id.text);
            yearv = view.findViewById(R.id.diary_year);
            monthv = view.findViewById(R.id.diary_month);
            image_diary = view.findViewById(R.id.image_diary);

            clickitem = (CardView)view.findViewById(R.id.clickitem);
        }
    }
    public adapter_diary(List <class_Diary> objects, Context mcontext) {
        context = mcontext;
        list = objects;
        dbHelper = new tools_MyDatabaseHelper(mcontext, "diary.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final class_Diary card = list.get(position);
        //Bitmap bitmap = BitmapFactory.decodeFile(card.getImage_path());
        holder.timev.setText(card.getDay());
        holder.yearv.setText(card.getYear());
        holder.monthv.setText(card.getMonth()+"月");
        initTypeface(holder.textv);
        holder.textv.setText(card.getProperty());



        displayImage(card.getImage_path(),holder.image_diary);

        holder.clickitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,Activity_diaryedit.class);
                intent.putExtra("addoredit","edit");
                intent.putExtra("text",card.getText());
                intent.putExtra("date",card.getYear()+"年"+card.getMonth()+"月"+card.getDay()+"日");
                intent.putExtra("year",card.getYear());
                intent.putExtra("month",card.getMonth());
                intent.putExtra("day",card.getDay());
                intent.putExtra("property",card.getProperty());
                intent.putExtra("id",card.getId());
                intent.putExtra("image_path",card.getImage_path());
                intent.putExtra("wheather",card.getWheather());
                intent.putExtra("city",card.getCity());

                context.startActivity(intent);

            }
        });
        holder.clickitem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setIcon(R.drawable.parttime)
                        .setTitle("警告")
                        .setMessage("确认删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.delete("diary","id=?",new String[]{String.valueOf(card.getId())});
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消",null).create().show();

                return false;
            }
        });




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


    private void displayImage(String imagePath,ImageView diary_image) {
        if (imagePath != null) {
            diary_image.setImageBitmap(getBitmap(imagePath,360,640));
        }else {
            //Toast.makeText(Activity_diaryedit.this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private void initTypeface(TextView textView) {
        Typeface typefaceModeran = Typeface.createFromAsset(context.getAssets(), "fonts/label.ttf");
        textView.setTypeface(typefaceModeran);
    }

}
