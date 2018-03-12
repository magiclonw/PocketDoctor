package com.magiclon.pocketdoctor.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.MyPagerAdapter;

public class LowsActivity extends AppCompatActivity {
    private ViewPager vp_lows;
    private MyPagerAdapter adapter;
    private TextView tv_title;
    private ImageView iv_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lows);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        initView();
        initEvents();
        initData();
    }

    private void initView() {
        vp_lows = findViewById(R.id.vp_lows);
        tv_title = findViewById(R.id.tv_title);
        iv_left = findViewById(R.id.iv_left);
    }

    private void initEvents() {
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        tv_title.setVisibility(View.VISIBLE);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("相关政策");
        adapter = new MyPagerAdapter(this);
        vp_lows.setAdapter(adapter);
        vp_lows.setOffscreenPageLimit(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
