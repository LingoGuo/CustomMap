package com.example.lingo.custommap.presenter;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import com.example.lingo.custommap.R;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.FragmentFactory;
import com.example.lingo.custommap.util.LocalStorage;
import com.example.lingo.custommap.view.activity.MainActivity;

/**
 * Created by lingo on 2017/3/23.
 */

public class MainActivityPresenter {
    private MainActivity view;
    public MainActivityPresenter(MainActivity view){
        this.view=view;
    }
    public void OnTabSelected(TabLayout.Tab tab){
        int position=tab.getPosition();
        int icon[]=new int[]{
                R.drawable.locate_selected,R.drawable.tag_selected,R.drawable.route_selected,R.drawable.user_selected
        };
        tab.setIcon(icon[position]);
        //判断是否登录，确定第4个tab的内容
        Fragment fragment;
        if(position==3){
            fragment= FragmentFactory.getInstance().createFragment(position, checkIsLogin());
        }else{
            fragment= FragmentFactory.getInstance().createFragment(position,false);
        }
        String tag[]=new String[]{
                AppConfig.LocateFragmentTag,AppConfig.TagFragmentTag,AppConfig.RouteFragmentTag,AppConfig.UserFragmentTag
        };
        if(!checkIsLogin()) {
            view.getFragmentManager().beginTransaction().replace(R.id.container, fragment, tag[position]).commit();
        }else{
            view.getFragmentManager().beginTransaction().replace(R.id.container, fragment, AppConfig.AfterLoginFragmentTag).commit();
        }

    }
    public void OnTabUnSelected(TabLayout.Tab tab){
        int position=tab.getPosition();
        int icon[]=new int[]{
                R.drawable.locate_unselected,R.drawable.tag_unselected,R.drawable.route_unselected,R.drawable.user_unselected
        };
        tab.setIcon(icon[position]);

    }
    public boolean checkIsLogin(){
        if(!LocalStorage.getToken(view).equals(""))//判断本地是否存有access_token
        {
           //判断本地的access_token是否过否过期

            if(LocalStorage.getEndline(view)>System.currentTimeMillis()&&(LocalStorage.getUid(view)!=0L)) {
                return true;
            }

        }
        return false;
    }



}
