package com.magiclon.pocketdoctor.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.magiclon.pocketdoctor.R;

/**
 * 作者：MagicLon
 * 时间：2018/3/9 009
 * 邮箱：1348149485@qq.com
 * 描述：法规adapter
 */

public class MyPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutinflater;
    private String path = "https://raw.githubusercontent.com/magiclonw/PocketDoctor/master/pic/lows";
    private RequestOptions myOptions;

    public MyPagerAdapter(Context context) {
        super();
        this.context = context;
        layoutinflater = LayoutInflater.from(context);
        myOptions = new RequestOptions().fitCenter().placeholder(R.mipmap.image_loading).error(R.mipmap.image_loading);
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = (ImageView) layoutinflater.inflate(R.layout.item_lows, null);
        container.addView(imageView);
        Glide.with(context).load(path+(position+1)+".jpg").apply(myOptions).into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
