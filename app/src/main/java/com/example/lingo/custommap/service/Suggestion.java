package com.example.lingo.custommap.service;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

/**
 * Created by lingo on 2017/4/5.
 */

public class Suggestion {
    private SuggestionSearch mSuggestionSearch;
    private OnGetSuggestionResultListener getSuggestionResultListener=null;
    public Suggestion(){
        mSuggestionSearch= SuggestionSearch.newInstance();
    }
    public boolean registerListener(OnGetSuggestionResultListener listener){
        boolean isSuccess=false;
        if(listener!=null){
            getSuggestionResultListener=listener;
            mSuggestionSearch.setOnGetSuggestionResultListener(getSuggestionResultListener);
            isSuccess=true;
        }
        return isSuccess;
    }
    public void requestSuggestion(String city,String keyword){
        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption().keyword(keyword).city(city));
    }
    public void release(){
        if(mSuggestionSearch!=null){
            mSuggestionSearch.destroy();
        }
    }
}
