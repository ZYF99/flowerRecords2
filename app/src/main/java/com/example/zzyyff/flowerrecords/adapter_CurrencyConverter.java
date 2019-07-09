package com.example.zzyyff.flowerrecords;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class adapter_CurrencyConverter extends RecyclerView.Adapter<adapter_CurrencyConverter.ViewHolder> {

    List<class_CurrencyCouvrterMoney> list ;
    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView tvCurrencyCouvrter_show_money ;
        TextView tvCurrencyCouvrter_show_namec;
        TextView tvCurrencyCouvrter_show_nameo;
        ImageView imageLineBack;
        private ViewHolder(View view) {
            super(view);
            tvCurrencyCouvrter_show_money = view.findViewById(R.id.tv_changed_show);
            tvCurrencyCouvrter_show_namec = view.findViewById(R.id.tvCurrencyCouvrter_show_namec);
            tvCurrencyCouvrter_show_nameo = view.findViewById(R.id.tvCurrencyCouvrter_show_nameo);
            imageLineBack = view.findViewById(R.id.imageBackLine);
        }
    }

    public adapter_CurrencyConverter(List<class_CurrencyCouvrterMoney> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency_converter, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        class_CurrencyCouvrterMoney ccm= list.get(position);
        holder.tvCurrencyCouvrter_show_nameo.setText(ccm.getNameO());
        holder.tvCurrencyCouvrter_show_namec.setText(ccm.getNameC());
        holder.tvCurrencyCouvrter_show_money.setText(ccm.getMoney());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<class_CurrencyCouvrterMoney> list){
        this.list = list;
        notifyDataSetChanged();
    }
}



