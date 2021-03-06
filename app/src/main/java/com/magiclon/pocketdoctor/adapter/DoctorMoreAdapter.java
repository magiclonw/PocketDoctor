package com.magiclon.pocketdoctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;

import java.util.List;

/**
 * Created by MagicLon on 2017/7/18.
 */
public class DoctorMoreAdapter extends RecyclerView.Adapter<DoctorMoreAdapter.ViewHolder> {
    private List<Doctor> mList;
    private Context mContext;

    public DoctorMoreAdapter(List<Doctor> list, Context context) {
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
        holder.tv_dept.setText(mList.get(position).getDepartment());
        holder.tv_hos.setText(mList.get(position).getHospital());
        holder.tv_top.setText(mList.get(position).getName() + "  " + mList.get(position).getLevel());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_top;
        private TextView tv_hos;
        private TextView tv_dept;
        private LinearLayout item_doctor_info;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_top = itemView.findViewById(R.id.tv_top);
            tv_hos = itemView.findViewById(R.id.tv_hos);
            tv_dept = itemView.findViewById(R.id.tv_dept);
            item_doctor_info = itemView.findViewById(R.id.item_doctor_info);
        }
    }
}
