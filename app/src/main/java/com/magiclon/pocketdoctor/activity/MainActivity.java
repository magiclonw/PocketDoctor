package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.individuationtoast.ToastUtil;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.HospitalMoreAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.tools.DensityUtil;
import com.magiclon.pocketdoctor.tools.GlideImageLoader;
import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

//import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Banner banner;
    private TextView city;
    private LinearLayout ll_main_search;
    private NestedScrollView sv_main;
    private Toolbar toolbar;
    private String imgs[] = {"https://raw.githubusercontent.com/magiclonw/PocketDoctor/master/pic/app2.jpg", "http://img.taopic.com/uploads/allimg/140222/240403-14022212200685.jpg", "http://www.qhnews.com/pic/0/00/48/53/485313_972948.jpg", "http://img.tvzn.com/roleimg/370222938.jpg"};
    private LinearLayout ll_main_qiaomen;
    private LinearLayout ll_main_zixun;
    private LinearLayout ll_main_guanzhu;
    private RecyclerView rv_main_hospital;
    private List<Hospital> hoslist = new ArrayList<>();
    private HospitalMoreAdapter hadapter;
    private DBManager dbManager;
    private boolean isscrolled = false;
    private Disposable mdisposable;
    private long mPressedTime= 0;//双击退出
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar), false)
                .transparentBar().navigationBarColor(R.color.line).fullScreen(false)
                .init();
//        SQLiteDatabase.loadLibs(this);
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        initView();
        initData();
    }

    private void initView() {
        ll_main_zixun = findViewById(R.id.ll_main_zixun);
        ll_main_guanzhu = findViewById(R.id.ll_main_guanzhu);
        rv_main_hospital = findViewById(R.id.rv_main_hospital);
        ll_main_zixun.setOnClickListener(this);
        ll_main_guanzhu.setOnClickListener(this);
        ll_main_qiaomen = findViewById(R.id.ll_main_qiaomen);
        ll_main_qiaomen.setOnClickListener(this);
        banner = findViewById(R.id.banner);
        city = findViewById(R.id.city);
        ll_main_search = findViewById(R.id.ll_main_search);
        sv_main = findViewById(R.id.sv_main);
        city.setOnClickListener(this);
        ll_main_search.setOnClickListener(this);
        final int height_banner = DensityUtil.Companion.dp2px(this, 230);
        sv_main.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean scrolled;
                if (scrollY > height_banner) {
                    scrolled = true;
                } else {
                    scrolled = false;
                }
                changeToolBg(scrolled);
            }
        });
        hadapter = new HospitalMoreAdapter(hoslist, this);
        rv_main_hospital.setLayoutManager(new LinearLayoutManager(this));
        rv_main_hospital.setNestedScrollingEnabled(false);
        rv_main_hospital.setAdapter(hadapter);
        hadapter.setOnItemClickListener(new HospitalMoreAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, HospitalinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", hoslist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mdisposable = Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                List<Hospital> list = dbManager.getAllHospital("");
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        hoslist.clear();
                        hoslist.addAll((List<Hospital>) o);
                        hadapter.notifyDataSetChanged();
                        mdisposable.dispose();
                    }
                });
    }

    private void changeToolBg(boolean scrolled) {
        if (isscrolled != scrolled) {
            isscrolled = scrolled;
            if (isscrolled) {
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                toolbar.setBackgroundColor(getResources().getColor(R.color.transe));
            }
        }

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

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
            ToastUtil.showinfo(this,"再按一次退出程序");
            mPressedTime = mNowTime;
        } else {
            super.onBackPressed();
        }
    }
}
