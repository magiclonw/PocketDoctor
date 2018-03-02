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
import com.magiclon.pocketdoctor.activity.MoreDeptActivity;
import com.magiclon.pocketdoctor.activity.MoreHospitalActivity;
import com.magiclon.pocketdoctor.model.Department;
import com.magiclon.pocketdoctor.model.Hospital;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MagicLon on 2017/7/18.
 */
public class DeptnameAdapter extends RecyclerView.Adapter<DeptnameAdapter.ViewHolder> {
    private List<Department> mList;
    private Context mContext;

    public DeptnameAdapter(List<Department> list, Context context) {
        this.mContext = context;
        this.mList = list;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deptment, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ll_hospital_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view,position);
            }
        });
        if (position == 0) {
            holder.ll_hospital_top.setVisibility(View.VISIBLE);
        } else {
            holder.ll_hospital_top.setVisibility(View.GONE);
        }
        if (position == 2) {
            holder.tv_hospital_more.setVisibility(View.VISIBLE);
        } else {
            holder.tv_hospital_more.setVisibility(View.GONE);
        }
        holder.tv_name.setText(mList.get(position).getDeptname());
        holder.tv_addr.setText(mList.get(position).getHname());
        holder.tv_hospital_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MoreDeptActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info", (Serializable) mList);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  mList.size()>3?3:mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_addr;
        private TextView tv_hospital_more;
        private LinearLayout ll_hospital_top;
        private LinearLayout ll_hospital_info;
        private ImageView iv_hospital_header;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_addr = (TextView) itemView.findViewById(R.id.tv_addr);
            tv_hospital_more = (TextView) itemView.findViewById(R.id.tv_hospital_more);
            ll_hospital_top = (LinearLayout) itemView.findViewById(R.id.ll_hospital_top);
            ll_hospital_info = (LinearLayout) itemView.findViewById(R.id.ll_hospital_info);
            iv_hospital_header = (ImageView) itemView.findViewById(R.id.iv_hospital_header);
        }
    }
}
