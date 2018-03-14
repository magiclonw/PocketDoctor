package com.magiclon.pocketdoctor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.DeptAdapter;
import com.magiclon.pocketdoctor.adapter.DoctorAdapter;
import com.magiclon.pocketdoctor.adapter.HistoryAdapter;
import com.magiclon.pocketdoctor.adapter.HospitalAdapter;
import com.magiclon.pocketdoctor.adapter.NotifyAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Department;
import com.magiclon.pocketdoctor.model.DeptDocHosListBean;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.model.Hospital;
import com.magiclon.pocketdoctor.model.Notify;
import com.magiclon.pocketdoctor.utils.OnMoreClickLister;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_search_cancle;
    private EditText edt_search;
    private ImageView iv_search_clear;
    private ImageView iv_empty;
    private RecyclerView rv_search;
    private RecyclerView rv_history;
    private RecyclerView rv_doctor;
    private RecyclerView rv_hospital;
    private RecyclerView rv_deptname;
    private HistoryAdapter hadapter;
    private NotifyAdapter nadapter;
    private DoctorAdapter dadapter;
    private HospitalAdapter hosadapter;
    private DeptAdapter deptadapter;
    private List<String> hlist = new ArrayList<>();//历史
    private List<Notify> nlist = new ArrayList<>();//提示
    private List<Doctor> dlist = new ArrayList<>();//医生
    private List<Hospital> hoslist = new ArrayList<>();//医院
    private List<Department> deptlist = new ArrayList<>();//科室
    private DBManager dbManager;
    private LinearLayout ll_searchmore;
    private NestedScrollView nest_search;
    private Disposable mdisposable;
    private String str_cursearch="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        initView();
        addEvents();
        initData();
    }


    private void initView() {
        tv_search_cancle = findViewById(R.id.tv_search_cancle);
        edt_search = findViewById(R.id.edt_search);
        rv_search = findViewById(R.id.rv_search);
        rv_history = findViewById(R.id.rv_history);
        rv_doctor = findViewById(R.id.rv_doctor);
        rv_hospital = findViewById(R.id.rv_hospital);
        rv_deptname = findViewById(R.id.rv_deptname);
        ll_searchmore = findViewById(R.id.ll_searchmore);
        nest_search = findViewById(R.id.nest_search);
        iv_search_clear = findViewById(R.id.iv_search_clear);
        iv_empty = findViewById(R.id.iv_empty);
    }

    private void addEvents() {
        tv_search_cancle.setOnClickListener(this);
        iv_search_clear.setOnClickListener(this);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH || i == EditorInfo.IME_ACTION_UNSPECIFIED || i == EditorInfo.IME_ACTION_GO) {
                    ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    dbManager.insertHistory(edt_search.getText().toString().trim());
                    submit("", edt_search.getText().toString().trim());
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

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edt_search.getText().toString().trim().equals("")) {
                    iv_search_clear.setVisibility(View.VISIBLE);
                    List<Notify> list = dbManager.getAllNotify(edt_search.getText().toString().trim().replaceAll("[.·|-]", ""));
                    if (list.size() > 0) {
                        nlist.clear();
                    }
                    nlist.addAll(list);
                    nadapter.notifyDataSetChanged();
                    rv_history.setVisibility(View.GONE);
                    ll_searchmore.setVisibility(View.GONE);
                    nest_search.setVisibility(View.GONE);
                    if(nlist.size()==0){
                        rv_search.setVisibility(View.GONE);
                        iv_empty.setVisibility(View.VISIBLE);
                    }else {
                        rv_search.setVisibility(View.VISIBLE);
                        iv_empty.setVisibility(View.GONE);
                    }
                } else {
                    iv_search_clear.setVisibility(View.GONE);
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
                edt_search.setText(hlist.get(position));
                rv_history.setVisibility(View.GONE);
                rv_search.setVisibility(View.VISIBLE);
            }
        });
        hadapter.setOnDeleteLister(new HistoryAdapter.OnDeleteLister() {
            @Override
            public void onDeleteOne(final String name) {
                new MaterialDialog.Builder(SearchActivity.this)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
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
                        })
                        .title("提示")
                        .content("确定要删除么")
                        .positiveText("确定").positiveColor(getResources().getColor(R.color.colorPrimary))
                        .negativeText("取消")
                        .show();

            }

            @Override
            public void onDeleteAll() {
                new MaterialDialog.Builder(SearchActivity.this)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dbManager.deleteAllHistory();
                                hlist.clear();
                                hadapter.notifyDataSetChanged();
                                ll_searchmore.setVisibility(View.VISIBLE);
                            }
                        })
                        .title("提示")
                        .content("确定要删除全部么")
                        .positiveText("确定").positiveColor(getResources().getColor(R.color.colorPrimary))
                        .negativeText("取消")
                        .show();
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
                if ("常见疾病".equals(nlist.get(position).getType())) {
                    submit(nlist.get(position).getType(), nlist.get(position).getName());
                } else {
                    submit(nlist.get(position).getType(), nlist.get(position).getNotifyid());
                }

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
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", dlist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        dadapter.setOnMoreClickLister(new OnMoreClickLister() {
            @Override
            public void onMoreClick(int position) {
                Intent intent = new Intent(SearchActivity.this, MoreDoctorActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putSerializable("info", (Serializable) mList);
                bundle.putString("info",str_cursearch);
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
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", hoslist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        hosadapter.setOnMoreClickLister(new OnMoreClickLister() {
            @Override
            public void onMoreClick(int position) {
                Intent intent = new Intent(SearchActivity.this, MoreHospitalActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("info", str_cursearch);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        /*************科室列表*******************/
        deptadapter = new DeptAdapter(deptlist, this);
        rv_deptname.setLayoutManager(new LinearLayoutManager(this));
        rv_deptname.setAdapter(deptadapter);
        deptadapter.setOnItemClickListener(new DeptAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this, DeptinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("info", deptlist.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        deptadapter.setOnMoreClickLister(new OnMoreClickLister() {
            @Override
            public void onMoreClick(int position) {
                Intent intent = new Intent(SearchActivity.this, MoreDeptActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("info", str_cursearch);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void submit(String type, String name) {
        // validate
        if ("医院".equals(type)) {
            dlist.clear();
            hoslist.clear();
            deptlist.clear();
            hoslist.addAll(dbManager.getAllHospital(name));
            hosadapter.notifyDataSetChanged();
            dadapter.notifyDataSetChanged();
            deptadapter.notifyDataSetChanged();
            if(deptlist.size()==0&&dlist.size()==0&&hoslist.size()==0){
                iv_empty.setVisibility(View.VISIBLE);
                nest_search.setVisibility(View.GONE);
            }else {
                iv_empty.setVisibility(View.GONE);
                nest_search.setVisibility(View.VISIBLE);
            }
        } else if ("医生".equals(type)) {
            dlist.clear();
            hoslist.clear();
            deptlist.clear();
            dlist.addAll(dbManager.getAllDoctor(name));
            dadapter.notifyDataSetChanged();
            hosadapter.notifyDataSetChanged();
            deptadapter.notifyDataSetChanged();
            if(deptlist.size()==0&&dlist.size()==0&&hoslist.size()==0){
                iv_empty.setVisibility(View.VISIBLE);
                nest_search.setVisibility(View.GONE);
            }else {
                iv_empty.setVisibility(View.GONE);
                nest_search.setVisibility(View.VISIBLE);
            }
        } else if ("科室".equals(type)) {
            deptlist.clear();
            dlist.clear();
            hoslist.clear();
            deptlist.addAll(dbManager.getAllDept(name));
            deptadapter.notifyDataSetChanged();
            hosadapter.notifyDataSetChanged();
            dadapter.notifyDataSetChanged();
            if(deptlist.size()==0&&dlist.size()==0&&hoslist.size()==0){
                iv_empty.setVisibility(View.VISIBLE);
                nest_search.setVisibility(View.GONE);
            }else {
                iv_empty.setVisibility(View.GONE);
                nest_search.setVisibility(View.VISIBLE);
            }
        } else if ("常见疾病".equals(type)) {
            searchAll(name);
        } else if ("".equals(type)) {
            searchAll(name);
        }
        rv_search.setVisibility(View.GONE);
        rv_history.setVisibility(View.GONE);
        ll_searchmore.setVisibility(View.GONE);
    }

    private void searchAll(final String name) {
        str_cursearch=name;
        mdisposable = Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                DeptDocHosListBean deptDocHosListBean = dbManager.getAllInfo(name.replaceAll("[·.|-]", ""));
                e.onNext(deptDocHosListBean);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        dlist.clear();
                        hoslist.clear();
                        deptlist.clear();
                        hoslist.addAll(((DeptDocHosListBean) o).getHospitalList());
                        dlist.addAll(((DeptDocHosListBean) o).getDoctorList());
                        deptlist.addAll(((DeptDocHosListBean) o).getDepartmentList());
                        hosadapter.notifyDataSetChanged();
                        dadapter.notifyDataSetChanged();
                        deptadapter.notifyDataSetChanged();
                        if(deptlist.size()==0&&dlist.size()==0&&hoslist.size()==0){
                            iv_empty.setVisibility(View.VISIBLE);
                            nest_search.setVisibility(View.GONE);
                        }else {
                            iv_empty.setVisibility(View.GONE);
                            nest_search.setVisibility(View.VISIBLE);
                        }
                        mdisposable.dispose();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_cancle:
                ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
                break;
            case R.id.iv_search_clear:
                edt_search.setText("");
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
