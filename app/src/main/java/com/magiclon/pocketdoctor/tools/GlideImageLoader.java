package com.magiclon.pocketdoctor.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Admin on 2017/8/22 022.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions().centerInside();
        Glide.with(context).applyDefaultRequestOptions(myOptions).load(path).into(imageView);
    }
}
