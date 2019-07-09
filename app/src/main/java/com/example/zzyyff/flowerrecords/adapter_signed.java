package com.example.zzyyff.flowerrecords;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class adapter_signed extends RecyclerView.Adapter<adapter_signed.ViewHolder> {

    List<String> list ;

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView dateV ;

        private ViewHolder(View view) {
            super(view);
            dateV = view.findViewById(R.id.item_date);

        }
    }

    public adapter_signed(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_signed, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.dateV.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }
}



