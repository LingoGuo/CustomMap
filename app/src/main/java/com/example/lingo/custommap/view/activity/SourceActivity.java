package com.example.lingo.custommap.view.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.example.lingo.custommap.R;
import com.example.lingo.custommap.presenter.SourceActivityPresenter;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.service.LocateService;
import com.example.lingo.custommap.service.Suggestion;
import com.example.lingo.custommap.util.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lingo on 2017/3/30.
 */

public class SourceActivity extends AppCompatActivity {
    private  List<Map<String,Object>> list;
    private Button navigationIcon;
    private Button sure;
    private ListView listView;
    private SimpleAdapter adapter;
    private EditText location;
    private SourceActivityPresenter presenter;
    private LinearLayout myLocation;
    private Suggestion mSuggestion;
    private LocateService mLocateService;
    private List<LatLng>src=new ArrayList<>();
    private LatLng selectedSrc;
    private String selectedCity;
    private List<String>citys=new ArrayList<>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 01:{
                    //更新界面
                    getMyLocation(msg.obj);
                    break;
                }
                case 02:{
                    //错误提示
                    showMessage((String) msg.obj);
                    break;
                }
                case 03:{//建议搜索结果
                    getSuggestionList(msg.obj);
                    break;
                }

            }
            super.handleMessage(msg);
        }
    };
    public void getSuggestionList(Object oj){
        List<SuggestionResult.SuggestionInfo> result=(List<SuggestionResult.SuggestionInfo>)oj;
        list.clear();
        src.clear();
        for(int i=0;i<result.size();i++){
            Map<String,Object>map=new HashMap<>();
            map.put("location",result.get(i).key);
            list.add(map);
            citys.add(result.get(i).city);
            src.add(result.get(i).pt);
        }
        adapter.notifyDataSetChanged();

    }
    public void getMyLocation(Object oj){
        BDLocation MyBDLocation=(BDLocation)oj;
        location.setText(MyBDLocation.getAddrStr());
        LatLng latLng=new LatLng(MyBDLocation.getLatitude(),MyBDLocation.getLongitude());
        selectedSrc=latLng;
        selectedCity=MyBDLocation.getCity();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        mLocateService=new LocateService(getApplicationContext());
        mSuggestion=new Suggestion();
        presenter=new SourceActivityPresenter(SourceActivity.this,mLocateService,mHandler,mSuggestion);
        initView();
    }

    private void initView() {
        listView=(ListView)findViewById(R.id.listview);
        navigationIcon=(Button)findViewById(R.id.navigationIcon);
        sure=(Button)findViewById(R.id.sure);
        location=(EditText)findViewById(R.id.location);
        myLocation=(LinearLayout)findViewById(R.id.mylocation);
        list=new ArrayList<Map<String, Object>>();//未填充数据
        adapter=new SimpleAdapter(SourceActivity.this,list,R.layout.listitem,new String[]{"location"},new int[]{R.id.location});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                location.setText((String)list.get(position).get("location"));
                selectedSrc=src.get(position);
                selectedCity=citys.get(position);
            }
        });
        navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter.initSuggetstions();
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.getSuggestions(s.toString().trim());
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.getText().toString().equals("")){
                    showMessage("请输入起点");
                }else{
                    presenter.sure(location.getText().toString().trim(),selectedSrc,selectedCity);
                }
            }
        });
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.accessPermissionAndStart();
            }
        });

    }
    public void showMessage(String text){
        Toast.makeText(SourceActivity.this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Code.BAIDU_LOCATE: {
                boolean flag=true;
                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                        flag=false;
                    }
                }
                if(flag) {
                    presenter.startService();
                }else{
                    showMessage("您拒绝了部分的权限申请，导致无法定位");
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocateService.stop();
        mSuggestion.release();
    }
}
