package com.example.lingo.custommap.presenter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.lingo.custommap.model.HttpsModel;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.LocalStorage;
import com.example.lingo.custommap.util.MyCallBack;
import com.example.lingo.custommap.view.activity.WeiboEdit;
import com.example.lingo.custommap.view.fragment.AfterLoginUserFragment;


/**
 * Created by lingo on 2017/4/14.
 */

public class AfterLoginUserFragmentPresenter {
    private AfterLoginUserFragment view;
    private HttpsModel model;
    public AfterLoginUserFragmentPresenter(AfterLoginUserFragment view){
        this.view=view;
        model=new HttpsModel();
    }
    public void loginOut(final Handler mHandler){
        String token= LocalStorage.getToken(view.getActivity());
        model.weiboRevokeOauth2(view.getActivity(), "POST", AppConfig.weiboRevokeOauth2URL, token, new MyCallBack() {
            @Override
            public void onSuccess(Object oj) {
                view.loginOut(oj);
            }

            @Override
            public void onFail(int code, String text) {
                Message msg=mHandler.obtainMessage();
                msg.what=1;
                msg.obj=text;
                mHandler.sendMessage(msg);
            }
        });
    }
    public void writeWeibo(){
        Intent intent=new Intent(view.getActivity(), WeiboEdit.class);
        view.startActivity(intent);
    }
    public void setUserInfo(final Handler mHandler){
        String token=LocalStorage.getToken(view.getActivity());
        Long uid=LocalStorage.getUid(view.getActivity());
        if(token.equals("")||uid==0L){
            return;
        }
        String url=AppConfig.weiboGetUserInfoURL+"?access_token="+token+"&uid="+uid;
        model.weiboGetUserInfo(url, new MyCallBack() {
            @Override
            public void onSuccess(Object oj) {
                Message msg=mHandler.obtainMessage();
                msg.what=2;
                msg.obj=oj;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFail(int code, String text) {

            }
        });
    }
}
