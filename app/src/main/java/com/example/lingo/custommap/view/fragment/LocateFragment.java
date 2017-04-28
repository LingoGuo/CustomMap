package com.example.lingo.custommap.view.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.lingo.custommap.R;
import com.example.lingo.custommap.presenter.LocateFragmentPresenter;
import com.example.lingo.custommap.service.LocateService;
import com.example.lingo.custommap.view.customview.MyToast;

/**
 * Created by lingo on 2017/3/23.
 */

public class LocateFragment extends Fragment {
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private LocateService mLocateService;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 01:{
                    //更新界面
                    updateView(msg.obj);
                    break;
                }
                case 02:{
                    //错误提示
                    showMessage((String) msg.obj);
                    break;
                }

            }
            super.handleMessage(msg);
        }
    };
    private LocateFragmentPresenter presenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view=inflater.inflate(R.layout.fragment_locate,container,false);
        mLocateService=new LocateService(getActivity().getApplicationContext());//传全Application有效的context,并且必须在主线程
        presenter=new LocateFragmentPresenter(LocateFragment.this,mHandler,mLocateService);
        Button search=(Button) view.findViewById(R.id.search);
        mMapView=(MapView)view.findViewById(R.id.mapView);
        mBaiduMap=mMapView.getMap();
        setBaiduMapConfig();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.accessPermissionAndStart();//获取权限，启动服务
            }
        });

        return view;
    }
    public void setBaiduMapConfig(){
        mBaiduMap.setMyLocationEnabled(true);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,
                true,null);
        mBaiduMap.setMyLocationConfigeration(config);
    }

    public void updateView(Object oj){
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(19));
        BDLocation location=(BDLocation)oj;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                .direction(location.getDirection()).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        new MyToast(getActivity(),"您当前位置:"+location.getAddrStr(),Toast.LENGTH_SHORT).show();
        ImageView imageView=new ImageView(getActivity());
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.locate_selected);
        imageView.setImageBitmap(bitmap);
        LatLng mLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        InfoWindow infoWindow=new InfoWindow(imageView,mLatLng,0);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    public void showMessage(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    public void startService(){
        presenter.startService();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocateService.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public  void onPause() {
        super.onPause();
        mMapView.onPause();
    }


}
