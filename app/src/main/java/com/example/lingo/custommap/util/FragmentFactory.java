package com.example.lingo.custommap.util;

import android.app.Fragment;

import com.example.lingo.custommap.view.fragment.AfterLoginUserFragment;
import com.example.lingo.custommap.view.fragment.LocateFragment;
import com.example.lingo.custommap.view.fragment.RouteFragment;
import com.example.lingo.custommap.view.fragment.TagFragment;
import com.example.lingo.custommap.view.fragment.UserFragment;

/**
 * Created by lingo on 2017/3/23.
 */

public class FragmentFactory {
    private static FragmentFactory fragmentFactory_Instance=null;
    private Fragment fragments[]=new Fragment[]{
            null,null,null,null,null
    };
    private FragmentFactory(){

    }
    public static FragmentFactory getInstance(){
        if(fragmentFactory_Instance==null){
            synchronized(FragmentFactory.class){
                if(fragmentFactory_Instance==null){
                    fragmentFactory_Instance=new FragmentFactory();
                }
            }

        }
        return fragmentFactory_Instance;
    }
    public  Fragment createFragment(int type,boolean isLogin){
        switch(type){
            case 0:if(fragments[type]==null){
                      fragments[type]=new LocateFragment();
                    }
                   return fragments[type];
            case 2:if(fragments[type]==null){
                      fragments[type]=new RouteFragment();
                   }
                   return fragments[type];
            case 1:if(fragments[type]==null) {
                      fragments[type]=new TagFragment();
                   }
                   return fragments[type];
            case 3:
                if(isLogin==false) {
                    if(fragments[3]==null) {
                        fragments[3] = new UserFragment();
                    }
                    return fragments[3];
                }else{
                    if(fragments[4]==null){
                        fragments[4]=new AfterLoginUserFragment();
                    }
                    return fragments[4];
                }
            default:return  null;
        }

    }

}
