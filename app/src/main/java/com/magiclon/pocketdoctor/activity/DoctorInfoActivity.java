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
import com.magiclon.pocketdoctor.model.Doctor;
import com.youfucheck.commoncodelib.SharePreferenceUtil;

public class DoctorInfoActivity extends AppCompatActivity {

    private TextView name;
    private TextView deptandlevel;
    private TextView hospital;
    private TextView tv_shanchang;
    private TextView tv_info;
    private NestedScrollView sv_doctorinfo;
    private Toolbar toolbar;
    private Doctor doctor;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        doctor = (Doctor) getIntent().getExtras().get("info");
        ImmersionBar.with(this)
                .titleBar(findViewById(R.id.toolbar), false)
                .transparentBar()
                .init();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        deptandlevel = (TextView) findViewById(R.id.deptandlevel);
        hospital = (TextView) findViewById(R.id.hospital);
        tv_shanchang = (TextView) findViewById(R.id.tv_shanchang);
        tv_info = (TextView) findViewById(R.id.tv_info);
        sv_doctorinfo = (NestedScrollView) findViewById(R.id.sv_doctorinfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name.setText(doctor.getName());
        deptandlevel.setText(doctor.getDepartment() + "/" + doctor.getLevel());
        hospital.setText(SharePreferenceUtil.INSTANCE.getString(this, "cur_city") + doctor.getHospital());
        tv_shanchang.setText(doctor.getShanchang());
        tv_info.setText(doctor.getInfo());
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
