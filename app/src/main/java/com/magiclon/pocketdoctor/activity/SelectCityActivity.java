package com.magiclon.pocketdoctor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.adapter.CityRecyclerAdapter;
import com.magiclon.pocketdoctor.db.DBManager;
import com.magiclon.pocketdoctor.model.City;
import com.magiclon.pocketdoctor.tools.SharePreferenceUtil;
import com.magiclon.pocketdoctor.tools.SideBar;

import java.util.List;


public class SelectCityActivity extends AppCompatActivity {

    private RecyclerView mRecyCity;
    private DBManager dbManager;
    private List<City> allCities;
    private SideBar mContactSideber;
    private CityRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ImageView iv_left;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcity);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        initView();
        initData();
    }

    private void initView() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mRecyCity = (RecyclerView) findViewById(R.id.recy_city);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_left.setVisibility(View.VISIBLE);
        tv_title.setText("选择城市");
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView mContactDialog = (TextView) findViewById(R.id.contact_dialog);
        mContactSideber = (SideBar) findViewById(R.id.contact_sidebar);
        mContactSideber.setTextView(mContactDialog);
    }

    private void initData() {
        allCities = dbManager.getAllCities();
        adapter = new CityRecyclerAdapter(this, allCities);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyCity.setLayoutManager(linearLayoutManager);
        mRecyCity.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyCity.setAdapter(adapter);
        adapter.setOnCityClickListener(new CityRecyclerAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                Log.e("MainActivity", "onCityClick:" + name);
                SharePreferenceUtil.INSTANCE.setValue(SelectCityActivity.this, "cur_city", name);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onLocateClick() {
                //重新定位
                Log.e("MainActivity", "onLocateClick");
                adapter.updateLocateState(CityRecyclerAdapter.LOCATING, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String location = extractLocation("北京市", "北京市");
                        adapter.updateLocateState(CityRecyclerAdapter.SUCCESS, location);
                    }
                }, 1000);
            }
        });


        mContactSideber.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = adapter.getPositionForSection(s);
                if (position != -1) {
//                    mRecyCity.scrollToPosition(position);
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                }

            }
        });
        String location = "北京";
        if (!SharePreferenceUtil.INSTANCE.getString(this, "cur_city").equals("")) {
            location = SharePreferenceUtil.INSTANCE.getString(this, "cur_city");
        }

        adapter.updateLocateState(CityRecyclerAdapter.SUCCESS, location);
    }


    /**
     * 提取出城市或者县
     *
     * @param city
     * @param district
     * @return
     */
    public static String extractLocation(final String city, final String district) {
        return district.contains("县") ? district.substring(0, district.length() - 1) : city.substring(0, city.length() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }
}
