package com.example.lingo.custommap.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.baidu.mapapi.model.LatLng;
import com.example.lingo.custommap.model.LocationModel;
import com.example.lingo.custommap.model.SuggestionModel;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.service.LocateService;
import com.example.lingo.custommap.util.MyCallBack;
import com.example.lingo.custommap.service.Suggestion;
import com.example.lingo.custommap.view.activity.SourceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingo on 2017/3/31.
 */

public class SourceActivityPresenter {
    private SourceActivity view;
    private Suggestion mSuggestion;
    private SuggestionModel suggestionModel;
    private LocateService mLocateService;
    private LocationModel locationModelmodel;
    private Handler mHandler;
    public SourceActivityPresenter(SourceActivity view, LocateService mLocationService, Handler mHandler, final Suggestion mSuggestion){//
        this.view=view;
        locationModelmodel=new LocationModel();
        this.mLocateService=mLocationService;
        this.mHandler=mHandler;
        this.mSuggestion=mSuggestion;
        suggestionModel=new SuggestionModel();

    }
    public void sure(String location, LatLng src,String city){
        Intent intent=new Intent();
        intent.putExtra("source",location);
        intent.putExtra("src",src);
        intent.putExtra("city",city);
        view.setResult(Code.SOURCEACTIVITY_RESULT_CODE,intent);
        view.finish();
    }
    public void startService(){
        mLocateService.registerListener(locationModelmodel.getBDLocationListener(new MyCallBack() {
            @Override
            public void onSuccess(Object oj) {
                Message  msg=mHandler.obtainMessage();
                msg.what=01;
                msg.obj=oj;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFail(int code, String text) {
                Message  msg=mHandler.obtainMessage();
                msg.what=02;
                msg.obj="错误代码:"+String.valueOf(code)+"错误信息:"+text;
                mHandler.sendMessage(msg);
            }
        }));
        mLocateService.start();
    }

    public void accessPermissionAndStart(){
        LocationManager mLocationManager =(LocationManager)(view.getSystemService(Context.LOCATION_SERVICE));
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            view.showMessage("为了更精准的定位，您可开启GPS");
        }
        List<String> permissionNeeds=new ArrayList<>();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//安卓>=6.0的系统需要进行权限处理
            if(ContextCompat.checkSelfPermission(view, Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED){
                permissionNeeds.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(ContextCompat.checkSelfPermission(view,Manifest.permission.READ_PHONE_STATE)!=
                    PackageManager.PERMISSION_GRANTED){
                permissionNeeds.add(Manifest.permission.READ_PHONE_STATE);
            }
            if(ContextCompat.checkSelfPermission(view,Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                permissionNeeds.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if(permissionNeeds.size()==0) {
                startService();
            }else{
                    ActivityCompat.requestPermissions(view, permissionNeeds.toArray(new String[permissionNeeds.size()]), Code.BAIDU_LOCATE);
            }
        }else{
            startService();
        }
    }
    public void getSuggestions(String text){
        mSuggestion.requestSuggestion("广州",text);
    }
    public void initSuggetstions(){
        mSuggestion.registerListener(suggestionModel.getSuggestionResultListener(new MyCallBack() {
            @Override
            public void onSuccess(Object oj) {
                Message  msg=mHandler.obtainMessage();
                msg.what=03;
                msg.obj=oj;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFail(int code, String text) {

            }
        }));
    }



}
