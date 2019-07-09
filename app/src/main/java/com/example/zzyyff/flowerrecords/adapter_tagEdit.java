package com.example.zzyyff.flowerrecords;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class adapter_tagEdit extends RecyclerView.Adapter< adapter_tagEdit.ViewHolder> {
    private List<String> remarkList;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvRemarkShow;
        CardView cd;

        public ViewHolder(View view){
            super(view);
            tvRemarkShow = (TextView)view.findViewById(R.id.tv_tab);
            cd = (CardView)view.findViewById(R.id.cd);
        }
    }
    public adapter_tagEdit(List<String> List){

        remarkList = List;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        dbHelper = new tools_MyDatabaseHelper(view.getContext(), "tag.db", null, 1);
        db = dbHelper.getWritableDatabase();

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvRemarkShow.setText(remarkList.get(position));
        holder.cd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(v.getContext())
                        .setIcon(R.drawable.logo)
                        .setTitle("警告")
                        .setMessage("确定删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                db.delete("tag","tag=?",new String[]{remarkList.get(position)});
                                remarkList.remove(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("取消", null)
                        .create().show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return remarkList.size();
    }

    public void updata(List<String> List){
        this.remarkList = List;
        notifyDataSetChanged();
    }


}
