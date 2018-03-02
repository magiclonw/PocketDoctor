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
import com.magiclon.pocketdoctor.adapter.DoctorAdapter;
import com.magiclon.pocketdoctor.adapter.DoctorMoreAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class MoreDoctorActivity extends AppCompatActivity {

    private TextView tv_title;
    private ImageView iv_left;
    private RecyclerView rv_moredoctor;
    private List<Doctor> dlist = new ArrayList<>();
    private DoctorMoreAdapter dadapter;
//    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredoctor);
        dlist= (List<Doctor>) getIntent().getExtras().get("info");
//        dbManager = new DBManager(this);
//        dbManager.copyDBFile();
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        rv_moredoctor = (RecyclerView) findViewById(R.id.rv_moredoctor);
//        dlist.addAll(dbManager.getAllDoctor(""));
        dadapter = new DoctorMoreAdapter(dlist, this);
        rv_moredoctor.setLayoutManager(new LinearLayoutManager(this));
        rv_moredoctor.setAdapter(dadapter);
        dadapter.setOnItemClickListener(new DoctorMoreAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MoreDoctorActivity.this, DoctorInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", dlist.get(position));
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
    }
}
