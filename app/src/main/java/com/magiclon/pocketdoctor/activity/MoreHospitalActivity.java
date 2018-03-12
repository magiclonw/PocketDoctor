package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.HospitalMoreAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Hospital;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoreHospitalActivity extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_left;
    private RecyclerView rv_morehospital;
    private List<Hospital> hoslist = new ArrayList<>();
    private HospitalMoreAdapter hadapter;
    private Disposable mdisposable;
    private String str_search = "";
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_hospital);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        str_search= (String) getIntent().getExtras().get("info");
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        iv_left = findViewById(R.id.iv_left);
        rv_morehospital = findViewById(R.id.rv_morehospital);
        hadapter = new HospitalMoreAdapter(hoslist, this);
        rv_morehospital.setLayoutManager(new LinearLayoutManager(this));
        rv_morehospital.setAdapter(hadapter);
        hadapter.setOnItemClickListener(new HospitalMoreAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MoreHospitalActivity.this, HospitalinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", hoslist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        tv_title.setVisibility(View.VISIBLE);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("相关医院");
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchHospital();
    }
    private void searchHospital() {
        mdisposable = Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                List<Hospital> list = new ArrayList<>();
                list.addAll(dbManager.getSearchHospital(str_search));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
