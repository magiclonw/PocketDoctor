package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.DoctorMoreAdapter;
import com.magiclon.pocketdoctor.adapter.HospitalAdapter;
import com.magiclon.pocketdoctor.adapter.HospitalMoreAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Doctor;
import com.magiclon.pocketdoctor.model.Hospital;

import java.util.ArrayList;
import java.util.List;

public class MoreHospitalActivity extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_left;
    private RecyclerView rv_morehospital;
    private List<Hospital> hoslist = new ArrayList<>();
    private HospitalMoreAdapter hadapter;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_hospital);
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        rv_morehospital = (RecyclerView) findViewById(R.id.rv_morehospital);
        hoslist.addAll(dbManager.getAllHospital());
        hadapter = new HospitalMoreAdapter(hoslist, this);
        rv_morehospital.setLayoutManager(new LinearLayoutManager(this));
        rv_morehospital.setAdapter(hadapter);
        hadapter.setOnItemClickListener(new HospitalMoreAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MoreHospitalActivity.this, HospitalinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", hoslist.get(position));
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
    }
}
