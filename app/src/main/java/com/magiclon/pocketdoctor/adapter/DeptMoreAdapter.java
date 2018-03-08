package com.magiclon.pocketdoctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.model.Department;

import java.util.List;

/**
 * Created by MagicLon on 2018/3/2.
 */
public class DeptMoreAdapter extends RecyclerView.Adapter<DeptMoreAdapter.ViewHolder> {
    private List<Department> mList;
    private Context mContext;

    public DeptMoreAdapter(List<Department> list, Context context) {
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
        holder.tv_name.setText(mList.get(position).getDeptname());
        holder.tv_addr.setText(mList.get(position).getHname());
    }

    @Override
    public int getItemCount() {
        return  mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_addr;
        private LinearLayout ll_hospital_info;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_addr = itemView.findViewById(R.id.tv_addr);
            ll_hospital_info = itemView.findViewById(R.id.ll_hospital_info);
        }
    }
}
