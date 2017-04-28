package com.example.lingo.custommap.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;

import com.example.lingo.custommap.bean.WeiboUserInfo;
import com.example.lingo.custommap.util.Constants;
import com.example.lingo.custommap.util.LocalStorage;
import com.example.lingo.custommap.util.MyCallBack;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lingo on 2017/4/17.
 */

public class HttpsModel {
    private static final String boundary="LingoBoundary";
    private WeiboUserInfo bean;
    public HttpsModel(){
        bean=new WeiboUserInfo();
    }

    public  Bitmap getBitmap(String path) {
        HttpURLConnection mHttpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(path);
            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setConnectTimeout(5000);
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.setDoOutput(false);
            mHttpURLConnection.connect();
            if (mHttpURLConnection.getResponseCode() == 200) {
                inputStream = mHttpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (mHttpURLConnection != null) {
                mHttpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
      return null;
    }




    public void getWeiboCode(WebView webView,String url, MyCallBack callBack){
        if(url.startsWith(Constants.REDIRECT_URL)) {//获取code
            webView.stopLoading();
            Uri uri = Uri.parse(url);
            String code = uri.getQueryParameter("code");
            if(code==null){
                callBack.onFail(100,"授权失败");
            }else{
                callBack.onSuccess(code);
            }
        }

    }
    public  byte[] getTokenPostParams(String code){
        if(code!=null){
            byte[] params=("client_id="+Constants.APP_KEY+"&client_secret="+Constants.APP_SECRET+
                    "&grant_type=authorization_code&code="+code+"&redirect_uri="+Constants.REDIRECT_URL).getBytes();
            return params;
        }
        return null;
    }

    public void weiboGetAccessToken(final Context context,final String code,final String urlStr,final MyCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpsURLConnection mHttpsURLConnection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL(urlStr);
                    mHttpsURLConnection=(HttpsURLConnection) url.openConnection();
                    mHttpsURLConnection.setRequestMethod("POST");
                    mHttpsURLConnection.setDoInput(true);
                    mHttpsURLConnection.setDoOutput(true);
                    mHttpsURLConnection.setUseCaches(false);
                    mHttpsURLConnection.setConnectTimeout(5000);
                    OutputStream params=mHttpsURLConnection.getOutputStream();
                    params.write(getTokenPostParams(code));
                    params.close();
                    mHttpsURLConnection.connect();
                    reader=new BufferedReader(new InputStreamReader(mHttpsURLConnection.getInputStream()));
                    StringBuffer content=new StringBuffer();
                    String line;
                    while((line=reader.readLine())!=null){
                        content.append(line);
                    }
                    Log.d("令牌信息",content.toString());
                    try {
                        JSONObject jsonObject=new JSONObject(content.toString());
                        String accessToken=jsonObject.getString("access_token");
                        String expireIn=jsonObject.getString("expires_in");
                        Long endline=Long.valueOf(expireIn)*1000+System.currentTimeMillis();
                        Long uid=Long.valueOf(jsonObject.getString("uid"));
                        if(!accessToken.equals("")) {
                            //写入本地
                            boolean flag=LocalStorage.writeAccessToken(context,accessToken,endline,uid);
                            if(flag) {
                                Log.d("存储access_token","成功");
                                callBack.onSuccess("success");
                            }else{
                                Log.d("存储本地access_token","失败");
                                callBack.onFail(1,"存储access_token失败");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(mHttpsURLConnection!=null){
                        mHttpsURLConnection.disconnect();
                    }
                    if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {e.printStackTrace();

                        }
                    }
                }
            }
        }.start();
    }
    public void weiboGetUserInfo(final String urlStr, final MyCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpsURLConnection mHttpsURLConnection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL(urlStr);
                    mHttpsURLConnection=(HttpsURLConnection) url.openConnection();
                    mHttpsURLConnection.setRequestMethod("GET");
                    mHttpsURLConnection.setDoInput(true);
                    mHttpsURLConnection.setDoOutput(false);
                    mHttpsURLConnection.setUseCaches(true);
                    mHttpsURLConnection.setConnectTimeout(5000);
                    mHttpsURLConnection.connect();
                    reader=new BufferedReader(new InputStreamReader(mHttpsURLConnection.getInputStream()));
                    StringBuffer content=new StringBuffer();
                    String line;
                    while((line=reader.readLine())!=null){
                        content.append(line);
                    }
                    Log.d("微博用户信息",content.toString());
                    try {
                        JSONObject jsonObject=new JSONObject(content.toString());
                        String screen_name=jsonObject.getString("screen_name");
                        String avatar_hd=jsonObject.getString("avatar_hd");
                        bean.setAvatar_large(avatar_hd);
                        bean.setScreen_name(screen_name);
                        Bitmap bitmap=getBitmap(avatar_hd);
                        bean.setBitmap(bitmap);
                        callBack.onSuccess(bean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(mHttpsURLConnection!=null){
                        mHttpsURLConnection.disconnect();
                    }
                    if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {e.printStackTrace();

                        }
                    }
                }
            }
        }.start();

    }
    public void weiboRevokeOauth2(final Context context, final String way, final String urlStr, final String token, final MyCallBack callBack){
        if(token.equals("")){
            return;
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpsURLConnection mHttpsURLConnection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL(urlStr);
                    mHttpsURLConnection=(HttpsURLConnection) url.openConnection();
                    if(way.equals("POST")) {
                        mHttpsURLConnection.setRequestMethod("POST");
                        mHttpsURLConnection.setDoInput(true);
                        mHttpsURLConnection.setDoOutput(true);
                        mHttpsURLConnection.setUseCaches(false);
                        mHttpsURLConnection.setConnectTimeout(5000);
                        OutputStream params=mHttpsURLConnection.getOutputStream();
                        params.write(("access_token="+token).getBytes());
                        params.close();
                    }
                    else if(way.equals("GET")) {
                        mHttpsURLConnection.setRequestMethod("GET");
                        mHttpsURLConnection.setDoInput(true);
                        mHttpsURLConnection.setDoOutput(false);
                        mHttpsURLConnection.setUseCaches(true);
                        mHttpsURLConnection.setConnectTimeout(5000);
                    }
                    mHttpsURLConnection.connect();
                    reader=new BufferedReader(new InputStreamReader(mHttpsURLConnection.getInputStream()));
                    StringBuffer content=new StringBuffer();
                    String line;
                    while((line=reader.readLine())!=null){
                        content.append(line);
                    }
                    Log.d("撤销令牌结果",content.toString());
                    try {
                        JSONObject jsonObject=new JSONObject(content.toString());
                        String result=jsonObject.getString("result");
                        if(result.equals("true")) {
                            //清除本地记录
                            boolean flag=LocalStorage.clearAccessToken(context);
                            if(flag) {
                                Log.d("清除本地access_token","成功");
                                callBack.onSuccess("success");
                            }else{
                                Log.d("清除本地access_token","失败");
                                callBack.onFail(1,"清除本地access_token失败");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(mHttpsURLConnection!=null){
                        mHttpsURLConnection.disconnect();
                    }
                    if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {e.printStackTrace();

                        }
                    }
                }
            }
        }.start();
    }

    public void weiboUpload(final String urlStr, final String token, final String status, final File pic, final MyCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpsURLConnection mHttpsURLConnection=null;
                BufferedReader reader=null;
                try {
                    URL url=new URL(urlStr);
                    String __boundary="--"+boundary;
                    StringBuffer paramStr=new StringBuffer();
                    //添加token参数
                    paramStr.append(__boundary+"\r\n");//第一行
                    paramStr.append("Content-Disposition:form-data;name=\"access_token\""+"\r\n");//第二行
                    paramStr.append("\r\n");//第三行
                    paramStr.append(token+"\r\n");//第四行
                    //添加status参数
                    paramStr.append(__boundary+"\r\n");//第一行
                    paramStr.append("Content-Disposition:form-data;name=\"status\""+"\r\n");//第二行
                    paramStr.append("\r\n");//第三行
                    paramStr.append(status+"\r\n");//第四行
                    //添加pic参数
                    paramStr.append(__boundary+"\r\n");//第一行
                    paramStr.append("Content-Disposition:form-data;name=\"pic\""+";filename=\""+pic.getName()+"\""+"\r\n");//第二行
                    paramStr.append("Content-Type:image/*"+"\r\n");//第三行
                    paramStr.append("\r\n");//第四行
                    FileInputStream stream = new FileInputStream(pic);
                    byte[] file = new byte[(int) pic.length()];
                    stream.read(file);
                    //最后
                    String end="\r\n"+__boundary+"--"+"\r\n";
                    mHttpsURLConnection=(HttpsURLConnection) url.openConnection();
                    mHttpsURLConnection.setRequestMethod("POST");
                    mHttpsURLConnection.setDoInput(true);
                    mHttpsURLConnection.setDoOutput(true);
                    mHttpsURLConnection.setUseCaches(false);
                    mHttpsURLConnection.setConnectTimeout(5000);
                    mHttpsURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);//设置为multipart/form-data
                    mHttpsURLConnection.setRequestProperty("Content-Length",String.valueOf(paramStr.toString().getBytes().length+file.length+end.getBytes().length));
                    OutputStream params=mHttpsURLConnection.getOutputStream();
                    params.write(paramStr.toString().getBytes());
                    params.write(file);
                    params.write(end.getBytes());
                    params.flush();
                    params.close();
                    mHttpsURLConnection.connect();
                    reader=new BufferedReader(new InputStreamReader(mHttpsURLConnection.getInputStream()));
                    StringBuffer content=new StringBuffer();
                    String line;
                    while((line=reader.readLine())!=null){
                        content.append(line);
                    }
                    Log.d("发送微博得到的JSON信息",content.toString());
                    if(!content.toString().equals("")){
                        Log.d("发送微博成功","");
                        callBack.onSuccess("success");
                    }else{
                        callBack.onFail(1,"发送微博失败");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(mHttpsURLConnection!=null){
                        mHttpsURLConnection.disconnect();
                    }
                    if(reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {e.printStackTrace();

                        }
                    }
                }
            }
        }.start();
    }
}
