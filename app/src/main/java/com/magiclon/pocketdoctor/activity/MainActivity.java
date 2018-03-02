package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.tools.DensityUtil;
import com.magiclon.pocketdoctor.tools.GlideImageLoader;

import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Banner banner;
    private TextView city;
    private LinearLayout ll_main_search;
    private NestedScrollView sv_main;
    private Toolbar toolbar;
    private String imgs[] = {"http://m1.biz.itc.cn/pic/new/n/80/78/Img7307880_n.jpg", "http://img.taopic.com/uploads/allimg/140222/240403-14022212200685.jpg", "http://www.qhnews.com/pic/0/00/48/53/485313_972948.jpg", "http://img.tvzn.com/roleimg/370222938.jpg"};
    private LinearLayout ll_main_qiaomen;
    private LinearLayout ll_main_zixun;
    private LinearLayout ll_main_guanzhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar), false)
                .transparentBar()
                .init();
        initView();
        initData();
    }

    private void initView() {
        ll_main_zixun = (LinearLayout) findViewById(R.id.ll_main_zixun);
        ll_main_guanzhu = (LinearLayout) findViewById(R.id.ll_main_guanzhu);
        ll_main_zixun.setOnClickListener(this);
        ll_main_guanzhu.setOnClickListener(this);
        ll_main_qiaomen = (LinearLayout) findViewById(R.id.ll_main_qiaomen);
        ll_main_qiaomen.setOnClickListener(this);
        banner = (Banner) findViewById(R.id.banner);
        city = (TextView) findViewById(R.id.city);
        ll_main_search = (LinearLayout) findViewById(R.id.ll_main_search);
        sv_main = (NestedScrollView) findViewById(R.id.sv_main);
        city.setOnClickListener(this);
        ll_main_search.setOnClickListener(this);
        final int height_banner = DensityUtil.Companion.dp2px(this, 230);
        sv_main.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getScrollY() >= height_banner) {
                    toolbar.setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimary));
                } else if (v.getScrollY() < height_banner) {
                    toolbar.setBackgroundDrawable(getResources().getDrawable(R.color.transe));
                }
            }
        });

    }

    private void initData() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
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
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(MainActivity.this, ZiXunActivity.class);
                startActivity(intent);
            }
        });
        if (!SharePreferenceUtil.INSTANCE.getString(this, "cur_city").equals("")) {
            city.setText(SharePreferenceUtil.INSTANCE.getString(this, "cur_city"));
        } else {
            SharePreferenceUtil.INSTANCE.setValue(this, "cur_city", "北京");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city:
                Intent intent = new Intent(this, SelectCityActivity.class);
                startActivityForResult(intent, 1001);
                break;
            case R.id.ll_main_search:
                Intent sintent = new Intent(this, SearchActivity.class);
                startActivity(sintent);
                break;
            case R.id.ll_main_qiaomen:
                Intent intent2 = new Intent(MainActivity.this, ZiXunActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_main_zixun:
                Intent intent3 = new Intent(MainActivity.this, ZiXunActivity.class);
                startActivity(intent3);
                break;
            case R.id.ll_main_guanzhu:
                Intent intent4 = new Intent(MainActivity.this, ZiXunActivity.class);
                startActivity(intent4);
                break;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (!SharePreferenceUtil.INSTANCE.getString(this, "cur_city").equals("")) {
                city.setText(SharePreferenceUtil.INSTANCE.getString(this, "cur_city"));
            }
        }
    }

}
