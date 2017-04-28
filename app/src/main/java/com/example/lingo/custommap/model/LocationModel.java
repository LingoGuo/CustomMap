package com.example.lingo.custommap.model;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.lingo.custommap.util.MyCallBack;

import java.util.List;


/**
 * Created by lingo on 2017/3/24.
 */

public class LocationModel {
    private BDLocation mBDLocation;
    public LocationModel(){
    }
    public  BDLocationListener getBDLocationListener(final MyCallBack callBack){
        BDLocationListener bdListener= new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                mBDLocation=location;
                //获取定位结果
                StringBuffer sb = new StringBuffer(256);

                sb.append("time : ");
                sb.append(location.getTime());    //获取定位时间

                sb.append("\nerror code : ");
                sb.append(location.getLocType());    //获取类型类型

                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());    //获取纬度信息

                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());    //获取经度信息

                sb.append("\nradius : ");
                sb.append(location.getRadius());    //获取定位精准度

                if (location.getLocType() == BDLocation.TypeGpsLocation){

                    // GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());    // 单位：公里每小时

                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());    //获取卫星数

                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                    sb.append("\ndirection : ");
                    sb.append(location.getDirection());    //获取方向信息，单位度

                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());    //获取地址信息

                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

                    // 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());    //获取地址信息

                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());    //获取运营商信息

                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                    // 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    callBack.onFail(167,"服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位");
                    return;

                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    callBack.onFail(63,"网络不通导致定位失败，请检查网络是否通畅");
                    return;

                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                    callBack.onFail(62,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果" +
                            "，可以试着重启手机");
                    return;
                }

                sb.append("\nlocationdescribe : ");
                sb.append(location.getLocationDescribe());    //位置语义化信息
                List<Poi> list = location.getPoiList();    // POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                Log.d("location",sb.toString());
                callBack.onSuccess(mBDLocation);

            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        };
        return bdListener;
    }


}
