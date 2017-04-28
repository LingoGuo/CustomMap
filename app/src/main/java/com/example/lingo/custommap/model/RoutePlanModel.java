package com.example.lingo.custommap.model;
import android.util.Log;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.lingo.custommap.util.MyCallBack;

/**
 * Created by lingo on 2017/4/5.
 */

public class RoutePlanModel {
    public OnGetRoutePlanResultListener getRoutePlanResultListener(final MyCallBack callBack){
        OnGetRoutePlanResultListener listener=new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult result) {
                if(result.error== SearchResult.ERRORNO.NO_ERROR){
                    callBack.onSuccess(result);

                }else{
                    callBack.onFail(1,"搜索不到结果,可能您输入的起点和终点之间不存在公交站");
                }

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        return listener;
    }

}
