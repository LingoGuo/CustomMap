package com.example.lingo.custommap.util;

/**
 * Created by lingo on 2017/3/24.
 */

public class AppConfig {
    public static final String weiboGetCodeURL="https://open.weibo.cn/oauth2/authorize?client_id="+Constants.APP_KEY+"&redirect_uri="+Constants.REDIRECT_URL+"&display=mobile";
    public static final String weiboRevokeOauth2URL="https://api.weibo.com/oauth2/revokeoauth2";
    public static final String weiboGetAccessTokenURL="https://api.weibo.com/oauth2/access_token";
    public static final String weiboGetUserInfoURL="https://api.weibo.com/2/users/show.json";
    public static final String weiboUploadURL="https://upload.api.weibo.com/2/statuses/upload.json";
    public static final String LocateFragmentTag="id_locate";
    public static final String TagFragmentTag="id_tag";
    public static final String RouteFragmentTag="id_route";
    public static final String UserFragmentTag="id_use";
    public static final String AfterLoginFragmentTag="id_afterLogin";
}
