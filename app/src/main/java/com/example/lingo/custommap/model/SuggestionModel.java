package com.example.lingo.custommap.model;


import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.example.lingo.custommap.util.MyCallBack;

/**
 * Created by lingo on 2017/4/5.
 */

public class SuggestionModel {
    public OnGetSuggestionResultListener getSuggestionResultListener(final MyCallBack callBack){
        OnGetSuggestionResultListener listener=new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    return;
                }else{
                    callBack.onSuccess(res.getAllSuggestions());
                }
            }
        };
        return listener;
    }
}
