package com.claire.pedometer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.baidu.mapapi.model.LatLng;
import com.linc.pedometer.global.Global;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


public class UpLoadLocationService extends Service {
	  private final UploadBinder binder = new UploadBinder();  

	@Override
	public void onCreate() {

		
		
	}
	
	   public class UploadBinder extends Binder {  
		   UpLoadLocationService getService() {  
	            return UpLoadLocationService.this;  
	        }  
	    }  
	  
	

	@Override
	public void onDestroy() {

	}
	
	public void upload(LatLng location){
		
		
		Map<String, String> params = new HashMap<String, String>();
		  params.put("id", Global.userid+"");
	      params.put("lat", location.latitude+"");
		  params.put("lon", location.longitude+"");
		 try {
						String result= sendGetRequest(Global.hostUrl+"uploadlocation", params);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}

	
	public String sendGetRequest(String path, Map<String, String> params) throws Exception{
		StringBuilder sb = new StringBuilder(path);
		sb.append('?');
		// ?method=save&title=435435435&timelength=89&
		for(Map.Entry<String, String> entry : params.entrySet()){
			sb.append(entry.getKey()).append('=')
				.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
		}
		sb.deleteCharAt(sb.length()-1);
		
		String url = sb.toString();
	
		HttpGet request = new HttpGet (url);
		// 发送请求 
		HttpResponse httpResponse = new DefaultHttpClient().execute(request); 
		// 得到应答的字符串，这也是一个 JSON 格式保存的数据 
		String retSrc = EntityUtils.toString(httpResponse.getEntity()); 
		// 生成 JSON 对象 
		
		return retSrc;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	
	
	    
	}





	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}
}
