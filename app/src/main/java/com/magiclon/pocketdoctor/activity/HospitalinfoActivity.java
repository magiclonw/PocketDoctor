package com.magiclon.pocketdoctor.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.tools.GlideImageLoader;
import com.youfucheck.commoncodelib.SharePreferenceUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.Arrays;

public class HospitalinfoActivity extends AppCompatActivity {

    private Banner banner;
    private TextView tv_addr;
    private TextView tv_info;
    private NestedScrollView sv_doctorinfo;
    private ImageView back;
    private Toolbar toolbar;
    private Hospital hospital;
    private String imgs[] = {"http://m1.biz.itc.cn/pic/new/n/80/78/Img7307880_n.jpg", "http://img.taopic.com/uploads/allimg/140222/240403-14022212200685.jpg"};
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalinfo);
        hospital = (Hospital) getIntent().getExtras().get("info");
        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar), false)
                .transparentBar()
                .init();
        initView();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        banner = (Banner) findViewById(R.id.banner);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_info = (TextView) findViewById(R.id.tv_info);
        sv_doctorinfo = (NestedScrollView) findViewById(R.id.sv_doctorinfo);
        back = (ImageView) findViewById(R.id.back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(Arrays.asList(imgs));
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_name.setText(SharePreferenceUtil.INSTANCE.getString(this, "cur_city") + hospital.getHospital());
        tv_addr.setText(SharePreferenceUtil.INSTANCE.getString(this, "cur_city") + hospital.getAddr());
        tv_info.setText(hospital.getInfo());
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
