package com.example.lingo.custommap.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.example.lingo.custommap.R;
import com.example.lingo.custommap.presenter.RouteFragmentPresenter;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.service.RoutePlan;
import com.example.lingo.custommap.util.UnitConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lingo on 2017/3/23.
 */

public class RouteFragment extends Fragment {
    private RouteFragmentPresenter presenter;
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> list;
    private Button selectedItems[],search;
    private View view;
    private RoutePlan mRoutePlan;
    private int way=-1;
    private TextView source,destination;
    private LatLng srcLatLng,desLatLng;
    private String srcCity;
    private String desCity;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 01: {
                    //更新界面
                    updateScheme(msg.obj);
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_route,container,false);
        mRoutePlan=new RoutePlan();
        presenter=new RouteFragmentPresenter(RouteFragment.this,mRoutePlan,mHandler);
        initView();
        return view;
    }
    public void updateScheme(Object oj){
        TransitRouteResult result=(TransitRouteResult)oj;
        List<TransitRouteLine> lines=result.getRouteLines();
        for(int i=0;i<lines.size();i++){
              Map<String,Object> map=new HashMap<String, Object>();
              List<TransitRouteLine.TransitStep> steps=lines.get(i).getAllStep();
              int time=0;
              String scheme="";
              int totalstations=0;
              for(TransitRouteLine.TransitStep transitStep:steps){
                  time+=transitStep.getDuration();
                  scheme+=transitStep.getInstructions()+">";
                  if(transitStep.getStepType()!= TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING)
                      totalstations+=transitStep.getVehicleInfo().getPassStationNum();
              }
              scheme=scheme.substring(0,scheme.length()-1);
              map.put("time",String.valueOf((int)(time/60+0.5))+"分钟");
              map.put("scheme",scheme);
              map.put("totalstations","共"+String.valueOf(totalstations)+"站");
              list.add(map);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRoutePlan.release();
    }

    private void initView(){
        selectedItems=new Button[]{
                (Button)view.findViewById(R.id.drive),
                (Button)view.findViewById(R.id.bus),
                (Button)view.findViewById(R.id.onfoot)
        };
        source=(TextView) view.findViewById(R.id.source);
        destination=(TextView)view.findViewById(R.id.destination);
        search=(Button)view.findViewById(R.id.search);
        listView=(ListView)view.findViewById(R.id.listview);
        list=new ArrayList<Map<String, Object>>();
        adapter=new SimpleAdapter(getActivity(),list,R.layout.routelist,new String[]{"time","scheme","totalstations"},
                new int[]{R.id.time,R.id.scheme,R.id.totalstations});
        listView.setAdapter(adapter);
        View.OnTouchListener listener=new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for(Button b:selectedItems){
                    b.getLayoutParams().width=UnitConvert.dip2px(40,getActivity());
                    b.getLayoutParams().height=UnitConvert.dip2px(30,getActivity());
                }
                if(v==selectedItems[0]){
                    selectedItems[0].setBackgroundResource(R.drawable.drive_selected);
                    selectedItems[0].getLayoutParams().height=UnitConvert.dip2px(40,getActivity());
                    selectedItems[0].getLayoutParams().width=UnitConvert.dip2px(50,getActivity());
                    selectedItems[1].setBackgroundResource(R.drawable.bus_unselected);
                    selectedItems[2].setBackgroundResource(R.drawable.onfoot_unselected);
                    way=1;//
                }
                if(v==selectedItems[1]){
                    selectedItems[0].setBackgroundResource(R.drawable.drive_unselected);
                    selectedItems[1].setBackgroundResource(R.drawable.bus_selected);
                    selectedItems[1].getLayoutParams().height=UnitConvert.dip2px(40,getActivity());
                    selectedItems[1].getLayoutParams().width=UnitConvert.dip2px(50,getActivity());
                    selectedItems[2].setBackgroundResource(R.drawable.onfoot_unselected);
                    way=1;//
                }
                if(v==selectedItems[2]){
                    selectedItems[0].setBackgroundResource(R.drawable.drive_unselected);
                    selectedItems[1].setBackgroundResource(R.drawable.bus_unselected);
                    selectedItems[2].setBackgroundResource(R.drawable.onfoot_selected);
                    selectedItems[2].getLayoutParams().height=UnitConvert.dip2px(40,getActivity());
                    selectedItems[2].getLayoutParams().width=UnitConvert.dip2px(50,getActivity());
                    way=1;//
                }
                return false;
            }
        };
        for(Button b:selectedItems){
            b.setOnTouchListener(listener);
        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    list.clear();
                    if(way!=-1&&(desLatLng!=null)&&(srcLatLng!=null)){
                        presenter.search(way,srcLatLng,desLatLng,srcCity,desCity);
                    }

            }
        });
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getSourceLocation();
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getDestionationLoc();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode!= Code.ACTIVITY_REQUEST_CODE) return;
        switch (resultCode){
            case Code.SOURCEACTIVITY_RESULT_CODE:
                source.setText(data.getStringExtra("source"));
                srcLatLng=data.getParcelableExtra("src");
                srcCity=data.getStringExtra("city");
                break;
            case Code.DESTINATIONACTIVITY_RESULT_CODE:
                destination.setText(data.getStringExtra("destination"));
                desLatLng=data.getParcelableExtra("des");
                desCity=data.getStringExtra("city");
                break;
        }
    }
    public void showMessage(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}
