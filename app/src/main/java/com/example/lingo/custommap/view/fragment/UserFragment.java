package com.example.lingo.custommap.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lingo.custommap.R;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.util.FragmentFactory;
import com.example.lingo.custommap.view.activity.UserAuthWebView;


/**
 * Created by lingo on 2017/3/29.
 */

public  class UserFragment extends Fragment implements View.OnClickListener {
    private Button login;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View mView =inflater.inflate(R.layout.fragment_user,container,false);
        login=(Button)mView.findViewById(R.id.login);
        login.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), UserAuthWebView.class);
        startActivityForResult(intent, Code.ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Code.WEIBOAUTHACTIVITY_RESULT_CODE&&(requestCode==Code.ACTIVITY_REQUEST_CODE)){
            Fragment fragment= FragmentFactory.getInstance().createFragment(3,true);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment, AppConfig.AfterLoginFragmentTag).commit();
        }else if(resultCode==Code.WEIBOAUTHACTIVITY_CANCELAUTH_RESULT_CODE&&(requestCode==Code.ACTIVITY_REQUEST_CODE)){
            //取消授权不做处理

        }
    }



}
