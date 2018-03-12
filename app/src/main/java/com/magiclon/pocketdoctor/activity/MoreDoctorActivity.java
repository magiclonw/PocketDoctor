package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.DoctorMoreAdapter;
import com.magiclon.pocketdoctor.adapter.WeekAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoreDoctorActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_week_sure;
    private TextView tv_week_cancle;
    private ImageView iv_left;
    private ImageView iv_right;
    private RecyclerView rv_moredoctor;
    private RecyclerView rv_week;
    private List<Doctor> dlist = new ArrayList<>();
    private List<Doctor> curdlist = new ArrayList<>();
    private DoctorMoreAdapter dadapter;
    private DBManager dbManager;
    private LinearLayout ll_moredoctor_time;
    private View v_dim;
    private WeekAdapter adapter;
    private Disposable mdisposable;
    private String str_search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredoctor);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        str_search = (String) getIntent().getExtras().get("info");
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        initView();
        initEvents();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_week_sure = findViewById(R.id.tv_week_sure);
        tv_week_cancle = findViewById(R.id.tv_week_cancle);
        iv_left = findViewById(R.id.iv_left);
        iv_right = findViewById(R.id.iv_right);
        ll_moredoctor_time = findViewById(R.id.ll_moredoctor_time);
        v_dim = findViewById(R.id.v_dim);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.time);
        rv_moredoctor = findViewById(R.id.rv_moredoctor);
        rv_week = findViewById(R.id.rv_week);
        curdlist.addAll(dlist);
        dadapter = new DoctorMoreAdapter(curdlist, this);
        rv_moredoctor.setLayoutManager(new LinearLayoutManager(this));
        rv_moredoctor.setAdapter(dadapter);
        dadapter.setOnItemClickListener(new DoctorMoreAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MoreDoctorActivity.this, DoctorInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", curdlist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tv_title.setVisibility(View.VISIBLE);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("相关医生");
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new WeekAdapter(this);
        rv_week.setLayoutManager(new LinearLayoutManager(this));
        rv_week.setAdapter(adapter);
    }

    private void initEvents() {
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ll_moredoctor_time.isShown()) {
                    ll_moredoctor_time.setVisibility(View.VISIBLE);
                }
            }
        });
        v_dim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_moredoctor_time.setVisibility(View.GONE);
            }
        });
        tv_week_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_moredoctor_time.setVisibility(View.GONE);
            }
        });
        tv_week_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_moredoctor_time.setVisibility(View.GONE);
                searchDoctor();
            }
        });
        searchDoctor();
    }

    private void searchDoctor() {
        mdisposable = Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                Map<Integer, Boolean> selected = adapter.getSelected();
                List<Doctor> list = new ArrayList<>();
                if (selected.size() != 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("%");
                    for (Map.Entry<Integer, Boolean> entry : selected.entrySet()) {
                        stringBuilder.append(Num2Chinese(entry.getKey())).append("%");
                    }
                    list.addAll(dbManager.getSearchDoctor(str_search, stringBuilder.toString()));
                } else {
                    list.addAll(dbManager.getSearchDoctor(str_search, ""));
                }
                e.onNext(list);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        curdlist.clear();
                        curdlist.addAll((List<Doctor>) o);
                        dadapter.notifyDataSetChanged();
                        mdisposable.dispose();
                    }
                });
    }

    private String Num2Chinese(int num) {
        String chinese = "";
        if (num == 0) {
            chinese = "一";
        } else if (num == 1) {
            chinese = "二";
        } else if (num == 2) {
            chinese = "三";
        } else if (num == 3) {
            chinese = "四";
        } else if (num == 4) {
            chinese = "五";
        } else if (num == 5) {
            chinese = "六";
        } else if (num == 6) {
            chinese = "日";
        }
        return chinese;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
