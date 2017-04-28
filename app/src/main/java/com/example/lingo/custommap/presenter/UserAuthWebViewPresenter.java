package com.example.lingo.custommap.presenter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lingo.custommap.model.HttpsModel;

import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Constants;
import com.example.lingo.custommap.util.MyCallBack;
import com.example.lingo.custommap.view.activity.UserAuthWebView;

/**
 * Created by lingo on 2017/4/18.
 */

public class UserAuthWebViewPresenter {
    private UserAuthWebView view;
    private HttpsModel model;
    public UserAuthWebViewPresenter(UserAuthWebView view){
        this.view=view;
        model=new HttpsModel();
    }
    public WebViewClient getWebViewClient(final Handler mHandler){
        WebViewClient webViewClient=new WebViewClient(){

            @Override
            public void onPageStarted(final WebView webView, String url, Bitmap favicon) {
                super.onPageStarted(webView, url, favicon);
                    Log.d("url",url);
                    model.getWeiboCode(webView, url, new MyCallBack() {
                        @Override
                        public void onSuccess(Object oj) {
                            String code=oj.toString();
                            model.weiboGetAccessToken(view,code, AppConfig.weiboGetAccessTokenURL,new MyCallBack() {
                                @Override
                                public void onSuccess(Object oj) {
                                    view.showAfterLoginPage(oj);
                                }
                                @Override
                                public void onFail(int code, String text) {
                                    Message msg=mHandler.obtainMessage();
                                    msg.what=1;
                                    msg.obj=text;
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }
                        @Override
                        public void onFail(int code, String text) {
                                if(code==100){
                                    view.cancelAuth();
                                }
                        }
                    });
            }

        };
        return webViewClient;
    }


}
