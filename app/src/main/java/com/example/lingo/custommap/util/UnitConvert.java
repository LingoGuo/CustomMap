package com.example.lingo.custommap.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


/**
 * Created by lingo on 2017/3/29.
 */

public class UnitConvert {
    public static int dip2px(int dip, Context context){
        float density=context.getResources().getDisplayMetrics().density;
        return (int)(density*dip+0.5f);
    }
    public static int px2dip(int px, Context context){
        float density=context.getResources().getDisplayMetrics().density;
        return (int)(px/density+0.5f);
    }
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data=null;
        if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] {MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}
