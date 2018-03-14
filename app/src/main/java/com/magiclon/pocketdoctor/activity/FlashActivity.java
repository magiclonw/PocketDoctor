package com.magiclon.pocketdoctor.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;

import java.io.IOException;

public class FlashActivity extends AppCompatActivity {
    private boolean isfinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ImmersionBar.with(this).statusBarDarkFont(true)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isfinish) {
                    return;
                }
                Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        isfinish = true;
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}
