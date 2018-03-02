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
import com.magiclon.pocketdoctor.model.Department;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.tools.DensityUtil;
import com.magiclon.pocketdoctor.tools.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.Arrays;

public class DeptinfoActivity extends AppCompatActivity {

    private Banner banner;
    private TextView tv_addr;
    private TextView tv_title;
    private TextView tv_info;
    private TextView tv_hospital_dept;
    private TextView tv_hospital_machine;
    private TextView tv_hospital_guide;
    private NestedScrollView sv_doctorinfo;
    private ImageView back;
    private Toolbar toolbar;
    private Department department;
    private String imgs[] = {"http://m1.biz.itc.cn/pic/new/n/80/78/Img7307880_n.jpg", "http://img.taopic.com/uploads/allimg/140222/240403-14022212200685.jpg"};
    private TextView tv_name;
    private int barheight = 0;
    private boolean isscrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptinfo);
        department = (Department) getIntent().getExtras().get("info");
        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar), false)
                .transparentBar()
                .init();
        barheight = DensityUtil.Companion.dp2px(this, 200f);
        initView();
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_title = (TextView) findViewById(R.id.tv_title);
        banner = (Banner) findViewById(R.id.banner);
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
        sv_doctorinfo.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean scrolled;
                if (scrollY > barheight) {
                    scrolled = true;
                } else {
                    scrolled = false;
                }
                changeToolBg(scrolled);
            }
        });
        tv_name.setText(department.getHname()+"-"+department.getDeptname());
        tv_title.setText(department.getHname()+"-"+department.getDeptname());
        tv_info.setText(department.getDeptinfo());
    }

    private void changeToolBg(boolean scrolled) {
        if (isscrolled != scrolled) {
            isscrolled = scrolled;
            if (isscrolled) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_title.setVisibility(View.VISIBLE);
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.transe));
                tv_title.setVisibility(View.GONE);
            }
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
}
