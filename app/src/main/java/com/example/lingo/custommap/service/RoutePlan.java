package com.example.lingo.custommap.service;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;


/**
 * Created by lingo on 2017/4/5.
 */

public class RoutePlan {
    private RoutePlanSearch mSearch;
    private OnGetRoutePlanResultListener mListener=null;
    public RoutePlan(){
       if(mSearch==null){
           mSearch=RoutePlanSearch.newInstance();
       }
    }
    public boolean registerListener(OnGetRoutePlanResultListener listener){
        boolean isSuccess=false;
        if(listener!=null&&(mListener==null)){
            mListener=listener;
            mSearch.setOnGetRoutePlanResultListener(mListener);
            isSuccess=true;
        }
        return isSuccess;
    }
    public void transitSearch(PlanNode stMassNode,PlanNode enMassNode,String city){
        mSearch.transitSearch(new TransitRoutePlanOption().from(stMassNode).to(enMassNode).city(city).policy(TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST));
    }
    public void release(){
        if(mSearch!=null)
           mSearch.destroy();
    }

}
