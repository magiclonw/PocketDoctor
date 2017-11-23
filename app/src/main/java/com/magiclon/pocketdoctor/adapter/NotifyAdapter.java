package com.magiclon.pocketdoctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.model.Notify;
import com.youfucheck.commoncodelib.SharePreferenceUtil;

import java.util.List;

/**
 * Created by MagicLon on 2017/7/18.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    private List<Notify> mList;
    private Context mContext;

    public NotifyAdapter(List<Notify> list, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = vh.getLayoutPosition();
                mOnItemClickListener.onItemClick(vh.itemView, pos);
                Log.e("22222222","2222222222222");
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(SharePreferenceUtil.INSTANCE.getString(mContext,"cur_city")+mList.get(position).getName());
        holder.type.setText(mList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView type;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_notify_info);
            type = (TextView) itemView.findViewById(R.id.tv_notify_type);
        }
    }
}
