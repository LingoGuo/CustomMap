package com.example.lingo.custommap.view.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.lingo.custommap.R;
import com.example.lingo.custommap.presenter.UserAuthWebViewPresenter;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Code;


/**
 * Created by lingo on 2017/4/18.
 */

public class UserAuthWebView extends AppCompatActivity {
    private WebView mWebView;
    private UserAuthWebViewPresenter presenter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:{
                    showToast(msg.obj.toString());
                    break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        presenter=new UserAuthWebViewPresenter(UserAuthWebView.this);
        mWebView=(WebView)findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        Log.d("webview发起授权请求",AppConfig.weiboGetCodeURL);
        mWebView.setWebViewClient(presenter.getWebViewClient(mHandler));
        mWebView.loadUrl(AppConfig.weiboGetCodeURL);
    }
    public void showAfterLoginPage(Object oj){
        Log.d("登录",oj.toString());
        setResult(Code.WEIBOAUTHACTIVITY_RESULT_CODE);
        finish();
    }

    public void cancelAuth(){
        setResult(Code.WEIBOAUTHACTIVITY_CANCELAUTH_RESULT_CODE);
        finish();
    }
    public void showToast(String text){
        Toast.makeText(UserAuthWebView.this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}
