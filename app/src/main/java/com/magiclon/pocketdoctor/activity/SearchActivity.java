package com.magiclon.pocketdoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.DoctorAdapter;
import com.magiclon.pocketdoctor.adapter.HistoryAdapter;
import com.magiclon.pocketdoctor.adapter.HospitalAdapter;
import com.magiclon.pocketdoctor.adapter.NotifyAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.model.Notify;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_search_cancle;
    private EditText edt_search;
    private RecyclerView rv_search;
    private RecyclerView rv_history;
    private RecyclerView rv_doctor;
    private RecyclerView rv_hospital;
    private HistoryAdapter hadapter;
    private NotifyAdapter nadapter;
    private DoctorAdapter dadapter;
    private HospitalAdapter hosadapter;
    private List<String> hlist = new ArrayList<>();
    private List<Notify> nlist = new ArrayList<>();
    private List<Doctor> dlist = new ArrayList<>();
    private List<Hospital> hoslist = new ArrayList<>();
    private DBManager dbManager;
    private LinearLayout ll_searchmore;
    private NestedScrollView nest_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        initView();
        addEvents();
        initData();
    }


    private void initView() {
        tv_search_cancle = (TextView) findViewById(R.id.tv_search_cancle);
        edt_search = (EditText) findViewById(R.id.edt_search);
        rv_search = (RecyclerView) findViewById(R.id.rv_search);
        rv_history = (RecyclerView) findViewById(R.id.rv_history);
        rv_doctor = (RecyclerView) findViewById(R.id.rv_doctor);
        rv_hospital = (RecyclerView) findViewById(R.id.rv_hospital);
        ll_searchmore = (LinearLayout) findViewById(R.id.ll_searchmore);
        nest_search = (NestedScrollView) findViewById(R.id.nest_search);
    }

    private void addEvents() {
        tv_search_cancle.setOnClickListener(this);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED || i == EditorInfo.IME_ACTION_GO) {
                    ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    dbManager.insertHistory(edt_search.getText().toString().trim());
                    submit();
                    return true;
                }
                return false;
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("----", charSequence.toString() + "---" + i + "----" + i1 + "---" + i2);


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edt_search.getText().toString().trim().equals("")) {
                    List<Notify> list = dbManager.getAllNotify(edt_search.getText().toString().trim());
                    if (list.size() > 0) {
                        nlist.clear();
                    }
                    nlist.addAll(list);
                    nadapter.notifyDataSetChanged();
                    rv_search.setVisibility(View.VISIBLE);
                    rv_history.setVisibility(View.GONE);
                    ll_searchmore.setVisibility(View.GONE);
                    nest_search.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        /********************历史列表**********************/
        hlist.addAll(dbManager.getAllHistory());
        hadapter = new HistoryAdapter(hlist, this);
        rv_history.setLayoutManager(new LinearLayoutManager(this));
        rv_history.setAdapter(hadapter);
        hadapter.setOnItemClickListener(new HistoryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                submit();
            }
        });
        hadapter.setOnDeleteLister(new HistoryAdapter.OnDeleteLister() {
            @Override
            public void onDeleteOne(String name) {
                dbManager.deleteOneHistory(name);
                hlist.clear();
                hlist.addAll(dbManager.getAllHistory());
                hadapter.notifyDataSetChanged();
                if (hlist.size() == 0) {
                    ll_searchmore.setVisibility(View.VISIBLE);
                } else {
                    rv_history.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onDeleteAll() {
                dbManager.deleteAllHistory();
                hlist.clear();
                hadapter.notifyDataSetChanged();
                ll_searchmore.setVisibility(View.VISIBLE);
            }
        });
        if (hlist.size() == 0) {
            ll_searchmore.setVisibility(View.VISIBLE);
        } else {
            rv_history.setVisibility(View.VISIBLE);
        }
        /************提示列表*********/
        nadapter = new NotifyAdapter(nlist, this);
        rv_search.setLayoutManager(new LinearLayoutManager(this));
        rv_search.setAdapter(nadapter);
        nadapter.setOnItemClickListener(new NotifyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                dbManager.insertHistory(nlist.get(position).getName());
                submit();
            }
        });
        /***********医生列表**********/
        dadapter = new DoctorAdapter(dlist, this);
        rv_doctor.setLayoutManager(new LinearLayoutManager(this));
        rv_doctor.setAdapter(dadapter);
        dadapter.setOnItemClickListener(new DoctorAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, DoctorInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info",dlist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        /*********************医院列表**********************/
        hosadapter = new HospitalAdapter(hoslist, this);
        rv_hospital.setLayoutManager(new LinearLayoutManager(this));
        rv_hospital.setAdapter(hosadapter);
        hosadapter.setOnItemClickListener(new HospitalAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, HospitalinfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info",hoslist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void submit() {
        // validate
        String search = edt_search.getText().toString().trim();
        dlist.clear();
        hoslist.clear();
        dlist.addAll(dbManager.getAllDoctor());
        hoslist.addAll(dbManager.getAllHospital());
        dadapter.notifyDataSetChanged();
        hosadapter.notifyDataSetChanged();
        nest_search.setVisibility(View.VISIBLE);
        rv_search.setVisibility(View.GONE);
        rv_history.setVisibility(View.GONE);
        ll_searchmore.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_cancle:
                finish();
                break;
        }
    }
}
