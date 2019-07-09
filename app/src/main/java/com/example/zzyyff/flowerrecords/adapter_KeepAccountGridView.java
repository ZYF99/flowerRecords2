package com.example.zzyyff.flowerrecords;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class adapter_KeepAccountGridView extends BaseAdapter {
    private List<class_KeepAccountAttribute> mDatas;
    private LayoutInflater mLayoutInflater;


    private int mIndex;

    private Context context;

    private int mPageSize;

    private int columnWidth;

    private TextView tvShow;

    private ImageView ivShow;

    public adapter_KeepAccountGridView(Context context, List<class_KeepAccountAttribute> mDatas, int columnWidth, int mIndex, TextView textView, ImageView imageView) {
        this.context = context;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
        this.mIndex = mIndex;
        mPageSize =context.getResources().getInteger(R.integer.HomePageHeaderColumn) * 2;
        this.columnWidth= columnWidth;
        this.tvShow = textView;
        this.ivShow = imageView;
    }


    public adapter_KeepAccountGridView(Context context, List<class_KeepAccountAttribute> mDatas, int mIndex, TextView textView, ImageView imageView) {
        this.context = context;
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
        this.mIndex = mIndex;
        mPageSize = context.getResources().getInteger(R.integer.HomePageHeaderColumn) * 2;
        this.tvShow = textView;
        this.ivShow = imageView;
    }


    @Override
    public int getCount() {
        return mDatas.size() > (mIndex + 1) * mPageSize ? mPageSize : (mDatas.size() - mIndex * mPageSize);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("TAG", "position:" + position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_keep_account_gridview_header, parent, false);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.textView);
            vh.iv = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + mIndex * mPageSize，
         */
        final int pos = position + mIndex * mPageSize;
        vh.tv.setText(mDatas.get(pos).name);
        vh.iv.setImageResource(mDatas.get(pos).iconRes);




        //设置点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShow.setText(mDatas.get(pos).name);
                ivShow.setImageResource(mDatas.get(pos).iconRes);

               // Toast.makeText(context,"第"+mIndex+"页,Item"+position+"名称:"+(mDatas.get(pos).name),Toast.LENGTH_SHORT).show();
            }
        });



        return convertView;
    }

    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }


}
