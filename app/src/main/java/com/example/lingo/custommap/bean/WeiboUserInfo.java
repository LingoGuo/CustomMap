package com.example.lingo.custommap.bean;

import android.graphics.Bitmap;

/**
 * Created by lingo on 2017/4/21.
 */

public class WeiboUserInfo {
    /**
     * screen_name :
     * avatar_large :
     */

    private String screen_name;
    private String avatar_large;
    private Bitmap portrait;
    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }
    public Bitmap getBitmap(){
        return portrait;
    }
    public void setBitmap(Bitmap portrait){
        this.portrait=portrait;
    }
}
