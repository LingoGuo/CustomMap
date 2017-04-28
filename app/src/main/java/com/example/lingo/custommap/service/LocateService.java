package com.example.lingo.custommap.service;


import android.content.Context;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


/**
 * Created by lingo on 2017/4/5.
 */

public class LocateService {
    private LocationClient client = null;
    private LocationClientOption mOption;
    private BDLocationListener mListener=null;
    public LocateService(Context locationContext){
            if(client == null){
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }

    }

    public boolean registerListener(BDLocationListener listener){
        boolean isSuccess = false;
        if(listener != null&&(mListener==null)){
            mListener=listener;
            client.registerLocationListener(mListener);
            isSuccess = true;
        }
        return  isSuccess;
    }

    public void unregisterListener(BDLocationListener listener){
        if(listener != null){
            client.unRegisterLocationListener(listener);
        }
    }


    public boolean setLocationOption(LocationClientOption option){
        boolean isSuccess = false;
        if(option != null){
            if(!client.isStarted())
            {
                client.setLocOption(option);
                isSuccess=true;
            }
        }
        return isSuccess;
    }


    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            mOption.setCoorType("bd09ll");
            mOption.setScanSpan(0);
            mOption.setIsNeedAddress(true);
            mOption.setOpenGps(true);
            mOption.setLocationNotify(true);
            mOption.setIsNeedLocationDescribe(true);
            mOption.setIsNeedLocationPoiList(true);
            mOption.setIgnoreKillProcess(false);
            mOption.SetIgnoreCacheException(false);
            mOption.setEnableSimulateGps(false);//个人理解为虚拟定位

        }
        return mOption;
    }

    public void start(){
            if(client != null){
                if(!client.isStarted()){
                    client.start();
                }else{
                    client.requestLocation();
                }

            }
    }
    public void stop(){
            if(client != null && client.isStarted()){
                client.stop();
            }
    }


}
