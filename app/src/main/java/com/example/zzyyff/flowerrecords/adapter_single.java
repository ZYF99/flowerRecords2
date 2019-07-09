package com.example.zzyyff.flowerrecords;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class adapter_single extends RecyclerView.Adapter<adapter_single.ViewHolder>{

    List<LinearLayout>hide = new ArrayList <>();
    Button button;
    Context mContext;
    List<class_Single> singleList;
    boolean isHide = true;
    int i = 0;


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView property;
        TextView paymethod;
        TextView money;
        Button button_lookmore;
        ImageView image_property;
        LinearLayout clickItem;
        LinearLayout single_all;
        public ViewHolder(View itemView) {
            super(itemView);
            property = itemView.findViewById(R.id.text_property);
            paymethod = itemView.findViewById(R.id.text_paymethod);
            money = itemView.findViewById(R.id.money);
            image_property = itemView.findViewById(R.id.propertyimage);
            clickItem = itemView.findViewById(R.id.click_item);
            button_lookmore = itemView.findViewById(R.id.button_lookmore);
            single_all = itemView.findViewById(R.id.itemsingle_all);

        }
    }
    public adapter_single(List<class_Single>singleList, Context mcontext){
        mContext = mcontext;
        this.singleList = singleList;

    }

    @Override
    public adapter_single.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single,parent,false);


        final adapter_single.ViewHolder holder = new adapter_single.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.##");
        class_Single single = singleList.get(position);
        switch (single.getInorout())
        {
            case "in":
                holder.money.setText(df.format(single.getIncome()));
                break;
            case "out":
                holder.money.setText("-"+df.format(single.getOutcome()));
                break;
        }
        adapter_tablelist.imageSwitch(single.getProperty(),holder.image_property);
        holder.property.setText(single.getProperty());
        holder.paymethod.setText(String.valueOf(single.getPay_method()));
if(singleList.size()>3)
{
    if(position>1&&position!=(singleList.size()-1))
    {
        setVisibility(false,holder.single_all);
        hide.add(holder.single_all);

    }
    if(position==(singleList.size()-1)){

        holder.button_lookmore.setVisibility(View.VISIBLE);
        button = holder.button_lookmore;
    }

}
        initClickListener(holder,single);
    }

    @Override
    public int getItemCount() {
        return singleList.size();
    }

    void initClickListener(final ViewHolder holder, final class_Single single){
        holder.clickItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Activity_detail.class);
                Bundle bundle = new Bundle();
                switch (single.getInorout())
            {
                case "in":
                    bundle.putDouble("money",single.getIncome());
                    break;
                case "out":
                    bundle.putDouble("money",single.getOutcome());
                    break;
                    default:
                        break;
            }
                bundle.putString("paymethod",single.getPay_method());
                bundle.putString("property",single.getProperty());
                bundle.putString("inorout",single.getInorout());
                bundle.putString("date",single.getDate());
                bundle.putString("remark",single.getRemark());
                bundle.putString("id",String.valueOf(single.getId()));
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });
        holder.button_lookmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHide) {
                    for (int j = 0; j < hide.size(); j++) {
                        setVisibility(true, hide.get(j));
                    }
                    button.setText("收起");
                    isHide = false;
                }else {
                    for (int j = 0; j < hide.size(); j++) {
                        setVisibility(false, hide.get(j));
                    }
                    button.setText("查看更多");
                    isHide = true;
                }
            }
        });

    }

    public void setVisibility(boolean isVisible,LinearLayout itemView){
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        if (isVisible){
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }else{
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }

}
