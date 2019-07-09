package com.example.zzyyff.flowerrecords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;

public class adapter_tablelist extends RecyclerView.Adapter<adapter_tablelist.ViewHolder>{
    Context mContext;
    List<class_tablelist>tablelist;
    public adapter_tablelist(List<class_tablelist>tablelist, Context context){

        mContext = context;
        this.tablelist = tablelist;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tablelist_item,parent,false);
        final adapter_tablelist.ViewHolder holder = new adapter_tablelist.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.##");
        class_tablelist item = tablelist.get(position);
        if(item.getPercent()<0.01)
        {
            holder.percentText.setText("太小了！");
        }else {

            holder.percentText.setText(df.format(item.getPercent()*100)+"%");
        }


        holder.propertyText.setText(String.valueOf(item.getProperty()));
        holder.moneyText.setText(df.format(item.getMoney()));
        holder.countText.setText(String.valueOf(item.getCount())+"笔");
        imageSwitch(item.getProperty(),holder.image_property);



    }
    @Override
    public int getItemCount() {
        return tablelist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView percentText;
        TextView propertyText;
        ImageView image_property;
        TextView moneyText;
        TextView countText;

        public ViewHolder(View itemView) {
        super(itemView);
        percentText = itemView.findViewById(R.id.itempercent);
        propertyText = itemView.findViewById(R.id.property);
        image_property = itemView.findViewById(R.id.property_image);
        moneyText = itemView.findViewById(R.id.money);
        countText = itemView.findViewById(R.id.count);

        }
    }
    static void imageSwitch(String property,ImageView imageProperty){
        switch (property)
        {
            case"餐饮":
                imageProperty.setImageResource(R.drawable.food);
                break;
            case"公交":
                imageProperty.setImageResource(R.drawable.traffic);
                break;
            case"出租车":
                imageProperty.setImageResource(R.drawable.taxi);
                break;
            case"火车":
                imageProperty.setImageResource(R.drawable.train);
                break;
            case"飞机":
                imageProperty.setImageResource(R.drawable.aircraft);
                break;
            case"轮船":
                imageProperty.setImageResource(R.drawable.ship);
                break;
            case"购物":
                imageProperty.setImageResource(R.drawable.shopping);
                break;
            case"停放":
                imageProperty.setImageResource(R.drawable.park);
                break;
            case"学习":
                imageProperty.setImageResource(R.drawable.study);
                break;
            case"数码":
                imageProperty.setImageResource(R.drawable.camera);
                break;
            case"娱乐":
                imageProperty.setImageResource(R.drawable.entertainment);
                break;
            case"户外":
                imageProperty.setImageResource(R.drawable.outdoors);
                break;
            case"度假":
                imageProperty.setImageResource(R.drawable.vacation);
                break;
            case"健身":
                imageProperty.setImageResource(R.drawable.bodybuilding);
                break;
            case"旅游":
                imageProperty.setImageResource(R.drawable.travel);
                break;
            case"出差":
                imageProperty.setImageResource(R.drawable.businesstravel);
                break;
            case"酒店":
                imageProperty.setImageResource(R.drawable.hotel);
                break;
            case"剧院":
                imageProperty.setImageResource(R.drawable.show);
                break;
            case "零食":
                imageProperty.setImageResource(R.drawable.snacks);
                break;
            case "工资":
                imageProperty.setImageResource(R.drawable.salary);
                break;
            case "投资":
                imageProperty.setImageResource(R.drawable.investment);
                break;
            case "彩票":
                imageProperty.setImageResource(R.drawable.lottery);
                break;
            case "红包":
                imageProperty.setImageResource(R.drawable.redenvelopes);
                break;
            case "福利":
                imageProperty.setImageResource(R.drawable.welfare);
                break;
            case "兼职":
                imageProperty.setImageResource(R.drawable.parttime);
                break;
            case "利息":
                imageProperty.setImageResource(R.drawable.interest);
                break;
            case "贷款":
                imageProperty.setImageResource(R.drawable.loan);
                break;
            case "风投":
                imageProperty.setImageResource(R.drawable.windcast);
                break;
            case "变卖":
                imageProperty.setImageResource(R.drawable.selloff);
                break;
            case "其他":
                imageProperty.setImageResource(R.drawable.other);
                break;



            default:
                break;
        }

    }
}
