package com.example.lingo.custommap.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.example.lingo.custommap.model.RoutePlanModel;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.util.MyCallBack;
import com.example.lingo.custommap.service.RoutePlan;
import com.example.lingo.custommap.view.activity.DestinationActivity;
import com.example.lingo.custommap.view.activity.SourceActivity;
import com.example.lingo.custommap.view.fragment.RouteFragment;

/**
 * Created by lingo on 2017/3/29.
 */

public class RouteFragmentPresenter {
    private RouteFragment view;
    private RoutePlanModel model;
    private RoutePlan mRoutePlan;
    private Handler mHandler;
    public RouteFragmentPresenter(RouteFragment view,RoutePlan mRoutePlan,Handler mHandler){
        this.view=view;
        this.mRoutePlan=mRoutePlan;
        model=new RoutePlanModel();
        this.mHandler=mHandler;
    }
    public void search(int way, LatLng src,LatLng des,String srcCity,String desCity){
        if(!srcCity.equals(desCity)) {
            view.showMessage("目前只能支持同城");
            return;
        }
        if(src.latitude==des.latitude&&(src.longitude==des.longitude)){
            view.showMessage("起点与终点一致，您无需移动");
        }else{
            mRoutePlan.registerListener(model.getRoutePlanResultListener(new MyCallBack() {
                @Override
                public void onSuccess(Object oj) {
                    Message msg=mHandler.obtainMessage();
                    msg.what=01;
                    msg.obj=oj;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onFail(int code, String text) {
                   view.showMessage(text);
                }
            }));
            PlanNode st=PlanNode.withLocation(src);
            PlanNode ed=PlanNode.withLocation(des);
            if(way==1){
                mRoutePlan.transitSearch(st,ed,srcCity);
            }
        }

    }
    public void getSourceLocation(){
        Intent intent=new Intent(view.getActivity(), SourceActivity.class);
        view.startActivityForResult(intent, Code.ACTIVITY_REQUEST_CODE);
    }
    public void getDestionationLoc(){
        Intent intent=new Intent(view.getActivity(), DestinationActivity.class);
        view.startActivityForResult(intent, Code.ACTIVITY_REQUEST_CODE);
    }


}
