package com.magiclon.pocketdoctor.activity;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.magiclon.pocketdoctor.R;
import com.magiclon.pocketdoctor.utils.SonicRuntimeImpl;
import com.magiclon.pocketdoctor.utils.SonicSessionClientImpl;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;

/**
 * 资讯页面
 */
public class ZiXunActivity extends AppCompatActivity implements OnClickListener {
    private TextView tv_title;
    private ImageView iv_left;
    private WebView web_zixun;
    private SonicSession sonicSession;
        private final static String url = "http://jiankang.163.com/special/jbbk/";
//    private final static String url = "http://mc.vip.qq.com/demo/indexv3";
    SonicSessionClientImpl sonicSessionClient = null;
//    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//        intent=new Intent();
//        intent.putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis());
        // init sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }


        // if it's sonic mode , startup sonic session at first time

        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();

        // create sonic session and run sonic flow
        sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            // this only happen when a same sonic session is already running,
            // u can comment following codes to feedback as a default mode.
//            throw new UnknownError("create session fail!");
//                        Toast.makeText(this, "create sonic session fail!", Toast.LENGTH_LONG).show();
        }


        // start init flow ...
        // in the real world, the init flow may cost a long time as startup
        // runtime、init configs....
        setContentView(R.layout.activity_zixun);
        ImmersionBar.with(this).statusBarColor(R.color.colorPrimary)
                .navigationBarColor(R.color.line).fullScreen(false)
                .init();
        initView();
        initData();
        addListers();

    }

    @Override
    public void onBackPressed() {
        if (web_zixun.canGoBack()) {
            web_zixun.goBack();
            return;
        }
        super.onBackPressed();
    }

    private void initView() {
        // TODO Auto-generated method stub
        web_zixun = findViewById(R.id.web_zixun);
        //        web_zixun.getSettings().setBlockNetworkImage(false);
        //        web_zixun.getSettings().setJavaScriptEnabled(true);
        //        web_zixun.setWebViewClient(new WebViewClient());
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("资讯");
        iv_left = findViewById(R.id.iv_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setOnClickListener(this);

    }

    private void initData() {
        // TODO Auto-generated method stub
        // init webview
        web_zixun.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
//                Log.e("1111", url);
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }
        });

        WebSettings webSettings = web_zixun.getSettings();

        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.setJavaScriptEnabled(true);
//        web_zixun.removeJavascriptInterface("searchBoxJavaBridge_");
//        intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
//        web_zixun.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");

        // init webview settings
        webSettings.setAllowContentAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


        // webview is ready now, just tell session client to bind
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(web_zixun);
            sonicSessionClient.clientReady();
        } else { // default mode
            web_zixun.loadUrl(url);
        }
    }

    private void addListers() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.iv_left:
                ZiXunActivity.this.finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}
