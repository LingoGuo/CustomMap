package com.example.lingo.custommap.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lingo on 2017/4/18.
 */

public class LocalStorage {
    private static final String storageName="weiboAccess_token";
    public static boolean writeAccessToken(Context context,String token,Long endline,Long uid){
           if(context!=null){
               SharedPreferences.Editor editor=context.getSharedPreferences
                       (storageName,Context.MODE_PRIVATE).edit();
               if(!token.equals("")) {
                   editor.putString("access_token", token);
                   editor.putLong("endline", endline);
                   editor.putLong("uid",uid);
                   editor.commit();
                   return true;
               }

           }
           return false;
    }
    public static boolean clearAccessToken(Context context){
        if(context!=null) {
            SharedPreferences.Editor editor = context.getSharedPreferences
                    (storageName, Context.MODE_PRIVATE).edit();
            editor.putLong("endline",0L);
            editor.putString("access_token","");
            editor.putString("uid","");
            editor.commit();
            return true;
        }
        return false;

    }
    public static String getToken(Context context){
            if(context!=null){
                SharedPreferences preferences=context.getSharedPreferences
                    (storageName,Context.MODE_PRIVATE);
                return preferences.getString("access_token","");
            }
            return "";
    }

    public static Long getEndline(Context context){
        if(context!=null){
            SharedPreferences preferences=context.getSharedPreferences
                    (storageName,Context.MODE_PRIVATE);
            return preferences.getLong("endline",0L);

        }
        return 0L;
    }

    public static long getUid(Context context){
        if(context!=null){
            SharedPreferences preferences=context.getSharedPreferences
                    (storageName,Context.MODE_PRIVATE);
            return preferences.getLong("uid",0L);
        }
        return 0L;
    }
}
