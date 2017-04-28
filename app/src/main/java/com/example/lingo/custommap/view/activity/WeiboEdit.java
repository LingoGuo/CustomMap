package com.example.lingo.custommap.view.activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lingo.custommap.R;
import com.example.lingo.custommap.presenter.WeiboEditPresenter;
import com.example.lingo.custommap.util.AppConfig;
import com.example.lingo.custommap.util.Code;
import com.example.lingo.custommap.util.UnitConvert;
import com.example.lingo.custommap.view.fragment.LocateFragment;

import java.io.IOException;



/**
 * Created by lingo on 2017/4/21.
 */

public class WeiboEdit extends AppCompatActivity {
    private ImageView photo;
    private Toolbar mToolbar;
    private Button send,cancel;
    private ImageView imageView;
    private RelativeLayout layout;
    private EditText editText;
    private WeiboEditPresenter presenter;
    private Uri uri;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:{
                    showToast("发送微博成功");
                    finish();
                    break;
                }
                case 2:{
                    showToast("发送微博失败:"+msg.obj.toString());
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weiboedit);
        presenter=new WeiboEditPresenter(WeiboEdit.this,mHandler);
        initView();
    }
    public void initView(){
        editText=(EditText)findViewById(R.id.weiboContent);
        layout=(RelativeLayout)findViewById(R.id.layout);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        LayoutInflater inflater=getLayoutInflater();
        inflater.inflate(R.layout.toolbarlayout,mToolbar);
        photo=(ImageView)findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","选择图片");
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Code.GETPHOTO_REQUEST_CODE);
            }
        });
        cancel=(Button)findViewById(R.id.navigationIcon);
        send=(Button)findViewById(R.id.send);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(WeiboEdit.class.getSimpleName(),"选择发送微博");
                String text=editText.getText().toString();
                if(text.equals("")){
                    editText.setHintTextColor(Color.RED);
                    editText.setHint("微博不能为空，请编辑140字内的内容");
                }else if(text.length()>140) {
                    showToast("字数太多，请编辑140字内的内容");
                }else if(imageView==null){
                    showToast("请选择一张图片");
                }else{
                    presenter.accessPermission(uri,text);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Code.READ_EXTERNAL_REQEUST_CODE: {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    presenter.sendWeibo(uri,editText.getText().toString());
                }else{
                    showToast("读取图片权限被限制，无法发送微博");
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            Log.d("data","null");//取消了选择
        }else{
            if(requestCode==Code.GETPHOTO_REQUEST_CODE){
                uri=data.getData();
                Log.d("uri",uri.toString());
                try{
                    if(imageView!=null){
                        layout.removeView(imageView);
                    }
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    imageView=new ImageView(WeiboEdit.this);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(UnitConvert.dip2px(50,WeiboEdit.this),UnitConvert.dip2px(50,WeiboEdit.this));
                    params.addRule(RelativeLayout.BELOW,R.id.weiboContent);
                    params.addRule(RelativeLayout.ALIGN_LEFT,R.id.weiboContent);
                    imageView.setLayoutParams(params);
                    layout.addView(imageView);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void showToast(String text){
        Toast.makeText(WeiboEdit.this,text,Toast.LENGTH_SHORT).show();
    }
}
