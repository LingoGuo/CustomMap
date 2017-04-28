package com.example.lingo.custommap.view.customview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lingo.custommap.R;

/**
 * Created by 郭晓玲 on 2017/3/28.
 */

public class MyToast extends Toast {
    public MyToast(Context context,CharSequence text,int duration){
        super(context);
        View v= LayoutInflater.from(context).inflate(R.layout.mytoast, null);
        ((TextView)v.findViewById(R.id.location)).setText(text);
        setView(v);
        setDuration(duration);
    }
}
