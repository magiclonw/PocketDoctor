package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.DeptMorenameAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Department;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.tools.DensityUtil;
import com.magiclon.pocketdoctor.tools.GlideImageLoader;
import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

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

public class HospitalinfoActivity extends AppCompatActivity {

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
    private Hospital hospital;
    private TextView tv_name;
    private int barheight = 0;
    private boolean isscrolled = false;
    private RecyclerView rv_hospital_peptlist;
    private Disposable mdisposable;
    private List<Department> deptlist = new ArrayList<>();
    private DeptMorenameAdapter hadapter;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalinfo);
        hospital = (Hospital) getIntent().getExtras().get("info");
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar), false)
                .transparentBar().navigationBarColor(R.color.line).fullScreen(false)
                .init();
        barheight = DensityUtil.Companion.dp2px(this, 230f);
        initView();
    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_title = findViewById(R.id.tv_title);
        banner = findViewById(R.id.banner);
        tv_addr = findViewById(R.id.tv_addr);
        tv_info = findViewById(R.id.tv_info);
        rv_hospital_peptlist = findViewById(R.id.rv_hospital_peptlist);
        tv_hospital_dept = findViewById(R.id.tv_hospital_dept);
        tv_hospital_machine = findViewById(R.id.tv_hospital_machine);
        tv_hospital_guide = findViewById(R.id.tv_hospital_guide);
        sv_doctorinfo = findViewById(R.id.sv_doctorinfo);
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        String imgs[] = {"https://raw.githubusercontent.com/magiclonw/PocketDoctor/master/pic/"+hospital.getHid()+".jpg", "https://raw.githubusercontent.com/magiclonw/PocketDoctor/master/pic/"+hospital.getHid()+".jpg"};
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
                scrolled = scrollY > barheight;
                changeToolBg(scrolled);
            }
        });
        tv_name.setText(hospital.getHname());
        tv_title.setText(hospital.getHname());
        tv_addr.setText(hospital.getInfo());
        tv_info.setText(hospital.getDetail());
        tv_hospital_dept.setText(hospital.getDepartment());
        tv_hospital_machine.setText(hospital.getMachine());
        tv_hospital_guide.setText(hospital.getGuide());
        hadapter = new DeptMorenameAdapter(deptlist, this);
        rv_hospital_peptlist.setLayoutManager(new LinearLayoutManager(this));
        rv_hospital_peptlist.setAdapter(hadapter);
        hadapter.setOnItemClickListener(new DeptMorenameAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HospitalinfoActivity.this, DeptinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", deptlist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mdisposable = Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                List<Department> list = dbManager.getAllHospitalForDept(hospital.getHid());
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        deptlist.clear();
                        deptlist.addAll((List<Department>)o);
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
