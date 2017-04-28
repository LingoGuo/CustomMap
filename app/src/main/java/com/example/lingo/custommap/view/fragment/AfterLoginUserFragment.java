package com.example.lingo.custommap.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lingo.custommap.R;
import com.example.lingo.custommap.bean.WeiboUserInfo;
import com.example.lingo.custommap.presenter.AfterLoginUserFragmentPresenter;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.FragmentFactory;

/**
 * Created by lingo on 2017/4/14.
 */

public class AfterLoginUserFragment extends Fragment {
    private AfterLoginUserFragmentPresenter presenter;
    private TextView login_out,nickName;
    private ImageView portrait;
    private RelativeLayout share;
    private View mView;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:{
                    showToast(msg.obj.toString());
                    break;
                }
                case 2:{
                    setUserInfo(msg.obj);
                    break;
                }
            }
        }
    };
    public void setUserInfo(Object oj){
        WeiboUserInfo bean=(WeiboUserInfo)oj;
        portrait.setImageBitmap(bean.getBitmap());
        nickName.setText(bean.getScreen_name());

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView =inflater.inflate(R.layout.fragment_afterloginuser,container,false);
        presenter=new AfterLoginUserFragmentPresenter(AfterLoginUserFragment.this);
        initView();
        return mView;
    }

    public void initView(){
        nickName=(TextView) mView.findViewById(R.id.nickName);
        portrait=(ImageView) mView.findViewById(R.id.portrait);
        presenter.setUserInfo(mHandler);
        share=(RelativeLayout)mView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.writeWeibo();
            }
        });
        login_out=(TextView) mView.findViewById(R.id.login_out);
        login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginOut(mHandler);
            }
        });
    }
    public void loginOut(Object oj){
        Log.d("退出登录",oj.toString());
        Fragment fragment= FragmentFactory.getInstance().createFragment(3,false);
        getActivity().getFragmentManager().beginTransaction().replace(R.id.container,fragment, AppConfig.UserFragmentTag).commit();

    }
    public void showToast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
}
