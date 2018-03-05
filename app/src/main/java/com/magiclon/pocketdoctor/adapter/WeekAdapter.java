package com.magiclon.pocketdoctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.activity.MoreDoctorActivity;
import com.magiclon.pocketdoctor.model.Doctor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by MagicLon on 2017/7/18.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {
    private String weeks[]={"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private Context mContext;
    private Map<Integer,Boolean> selected=new TreeMap<>();

    public WeekAdapter( Context context) {
        this.mContext = context;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ll_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected.get(position)==null){
                    selected.put(position,true);
                }else {
                    selected.remove(position);
                }
                notifyItemChanged(position);
            }
        });
        holder.tv_weekday.setText(weeks[position]);
        if(selected.get(position)==null){
            holder.iv_selected.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.unselected));
        }else {
            holder.iv_selected.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.selected));
        }
    }


    @Override
    public int getItemCount() {
        return 7;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_weekday;
        private ImageView iv_selected;
        private LinearLayout ll_week;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_weekday = itemView.findViewById(R.id.tv_week);
            iv_selected = itemView.findViewById(R.id.iv_selected);
            ll_week = itemView.findViewById(R.id.ll_week);
        }
    }

    public Map<Integer, Boolean> getSelected() {
        return selected;
    }
}
