package com.claire.user;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.linc.pedometer.global.Global;
import com.linc.pedometer.service.MD5Util;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Build;

public class LoginActivity extends Activity {
	private TextView tView;
	
	public void init(View rootView) {
		tView = (TextView) rootView.findViewById(R.id.textView1);
        tView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.w("Login", "no account");
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				
				LoginActivity.this.startActivity(intent);
				
			}
		});
        
        
        
        
	}
	
	
	 protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);   

	        setContentView(R.layout.activity_login);
	       
	}  
	    
}
