package com.claire.user;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;

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
import android.widget.TextView;
import android.os.Build;

public class LoginActivity extends Activity {
	private TextView tView;
	
	public void init(View rootView) {
		tView = (TextView) rootView.findViewById(R.id.textView1);
        tView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
//				if(intent==null){
//					Log.v("intent", "tt");	
//				}
				LoginActivity.this.startActivity(intent);
//				ComponentName componentname = new ComponentName(LoginActivity.this, "com.claire.pedometer.RegisterActivity");
//				Intent intent = new Intent();
//				intent.setComponent(componentname);
			//	startActivity(intent);

				
			}
		});
	}
	
	 protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);   

	        setContentView(R.layout.activity_login);  
	     
	        
	        
	      
	    }  
	    


}
