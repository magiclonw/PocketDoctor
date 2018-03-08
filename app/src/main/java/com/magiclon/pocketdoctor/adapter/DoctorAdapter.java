package com.magiclon.pocketdoctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.activity.MoreDoctorActivity;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;
import com.magiclon.pocketdoctor.utils.OnMoreClickLister;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MagicLon on 2017/7/18.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private List<Doctor> mList;
    private Context mContext;

    public DoctorAdapter(List<Doctor> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private OnMoreClickLister onMoreClickLister = null;

    public void setOnMoreClickLister(OnMoreClickLister onMoreClickLister) {
        this.onMoreClickLister = onMoreClickLister;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //define interface
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.item_doctor_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);
            }
        });
        if (position == 0) {
            holder.ll_doctor_top.setVisibility(View.VISIBLE);
        } else {
            holder.ll_doctor_top.setVisibility(View.GONE);
        }
        if (position == 2 && mList.size() > 3) {
            holder.tv_doctor_more.setVisibility(View.VISIBLE);
        } else {
            holder.tv_doctor_more.setVisibility(View.GONE);
        }
        holder.tv_dept.setText(mList.get(position).getDepartment());
        holder.tv_hos.setText(mList.get(position).getHospital());
        holder.tv_top.setText(mList.get(position).getName() + "  " + mList.get(position).getLevel());
        holder.tv_doctor_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoreClickLister.onMoreClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() > 3 ? 3 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_top;
        private TextView tv_hos;
        private TextView tv_dept;
        private TextView tv_doctor_more;
        private LinearLayout ll_doctor_top;
        private LinearLayout item_doctor_info;
        private ImageView iv_doctor_header;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_top = (TextView) itemView.findViewById(R.id.tv_top);
            tv_hos = (TextView) itemView.findViewById(R.id.tv_hos);
            tv_dept = (TextView) itemView.findViewById(R.id.tv_dept);
            tv_doctor_more = (TextView) itemView.findViewById(R.id.tv_doctor_more);
            ll_doctor_top = (LinearLayout) itemView.findViewById(R.id.ll_doctor_top);
            item_doctor_info = (LinearLayout) itemView.findViewById(R.id.item_doctor_info);
            iv_doctor_header = (ImageView) itemView.findViewById(R.id.iv_doctor_header);
        }
    }
}
