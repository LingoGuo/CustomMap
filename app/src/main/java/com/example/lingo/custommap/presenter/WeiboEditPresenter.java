package com.example.lingo.custommap.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.lingo.custommap.model.HttpsModel;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.util.LocalStorage;
import com.example.lingo.custommap.util.MyCallBack;
import com.example.lingo.custommap.util.UnitConvert;
import com.example.lingo.custommap.view.activity.WeiboEdit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingo on 2017/4/24.
 */

public class WeiboEditPresenter {
    private WeiboEdit view;
    private HttpsModel model;
    private Handler mHandler;
    public WeiboEditPresenter(WeiboEdit view,Handler mHandler){
        this.view=view;
        this.mHandler=mHandler;
        model=new HttpsModel();
    }
    public void accessPermission(Uri uri, String text){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//安卓>=6.0的系统需要进行权限处理
            if(ContextCompat.checkSelfPermission(view, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(view, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Code.READ_EXTERNAL_REQEUST_CODE);
            }else{
                sendWeibo(uri,text);
            }
        }else{
            sendWeibo(uri,text);
        }
    }
    public void sendWeibo(Uri uri, String text){
        String token=LocalStorage.getToken(view);
        String status;
        File pic=null;
        String url= AppConfig.weiboUploadURL;
        if(token.equals("")){
            Log.d(WeiboEditPresenter.class.getSimpleName(),"token为空");
            view.showToast("发送失败，请重新登录");
        }else{
            try {
                status=URLEncoder.encode(text,"UTF-8");
                pic=new File(UnitConvert.getRealFilePath(view,uri));
                model.weiboUpload(url, token, status, pic, new MyCallBack() {
                    @Override
                    public void onSuccess(Object oj) {
                        Message msg=mHandler.obtainMessage();
                        msg.what=1;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onFail(int code, String text) {
                        Message msg=mHandler.obtainMessage();
                        msg.what=2;
                        msg.obj=text;
                        mHandler.sendMessage(msg);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

}
