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
import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;

public class DoctorInfoActivity extends AppCompatActivity {

    private TextView name;
    private TextView level;
    private TextView hospitalanddept;
    private TextView tv_info;
    private TextView tv_time;
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
                .transparentBar().navigationBarColor(R.color.line).fullScreen(false)
                .init();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    private void initView() {
        name = findViewById(R.id.name);
        level = findViewById(R.id.tv_level);
        tv_time = findViewById(R.id.tv_time);
        hospitalanddept = findViewById(R.id.hospitalanddept);
        tv_info = findViewById(R.id.tv_info);
        sv_doctorinfo = findViewById(R.id.sv_doctorinfo);
        toolbar = findViewById(R.id.toolbar);
        name.setText(doctor.getName());
        level.setText( doctor.getLevel());
        hospitalanddept.setText(doctor.getHospital()+"-"+doctor.getDepartment());
        tv_info.setText(doctor.getInfo());
        tv_time.setText(doctor.getTime());
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
