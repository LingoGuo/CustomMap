package com.example.lingo.custommap.view.activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.lingo.custommap.R;
import com.example.lingo.custommap.presenter.MainActivityPresenter;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.util.FragmentFactory;
import com.example.lingo.custommap.view.fragment.LocateFragment;
public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private MainActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter=new MainActivityPresenter(MainActivity.this);
        mTabLayout=(TabLayout)findViewById(R.id.tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.locate_unselected),true);
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.tag_unselected));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.route_unselected));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.user_unselected));
        Fragment fragment= FragmentFactory.getInstance().createFragment(0,false);
        getFragmentManager().beginTransaction().replace(R.id.container,fragment,AppConfig.LocateFragmentTag).commit();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                presenter.OnTabSelected(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                presenter.OnTabUnSelected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Code.BAIDU_LOCATE: {
                boolean flag=true;
                for(int i=0;i<grantResults.length;i++){
                    if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                        flag=false;
                    }
                }
                if(flag) {
                    LocateFragment fragment = (LocateFragment) getFragmentManager().findFragmentByTag(AppConfig.LocateFragmentTag);
                    fragment.startService();
                }else{
                    Toast.makeText(MainActivity.this,"您拒绝了部分的权限申请，导致无法定位",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


}


