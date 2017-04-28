package com.example.lingo.custommap.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.example.lingo.custommap.model.LocationModel;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.service.LocateService;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.util.MyCallBack;
import com.example.lingo.custommap.view.fragment.LocateFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lingo on 2017/3/23.
 */

public class LocateFragmentPresenter {
    private LocateFragment view;
    private LocationModel model;
    private Handler mHandler;
    private LocateService mLocateService;
    public LocateFragmentPresenter(LocateFragment view, Handler handler,LocateService mLocateService){
        this.view=view;
        this.mHandler=handler;
        model=new LocationModel();
        this.mLocateService=mLocateService;
    }

    public void startService(){
        mLocateService.registerListener(model.getBDLocationListener(new MyCallBack() {
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
        LocationManager mLocationManager =(LocationManager)((view.getActivity()).getSystemService(Context.LOCATION_SERVICE));
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            view.showMessage("为了更精准的定位，您可开启GPS");
        }
        List<String> permissionNeeds=new ArrayList<>();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//安卓>=6.0的系统需要进行权限处理
            if(ContextCompat.checkSelfPermission(view.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED){
                permissionNeeds.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(ContextCompat.checkSelfPermission(view.getActivity(),Manifest.permission.READ_PHONE_STATE)!=
                    PackageManager.PERMISSION_GRANTED){
                permissionNeeds.add(Manifest.permission.READ_PHONE_STATE);
            }
            if(ContextCompat.checkSelfPermission(view.getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){
                permissionNeeds.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if(permissionNeeds.size()==0) {
                startService();
            }else{
                ActivityCompat.requestPermissions(view.getActivity(), permissionNeeds.toArray(new String[permissionNeeds.size()]),Code.BAIDU_LOCATE);
            }
        }else{
            startService();
        }
    }


}
