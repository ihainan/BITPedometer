package com.claire.pedometer;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class LeitaiActivity extends Activity {
	
  public static int id;
	
	TextView t1;
	TextView t2;
	TextView t3;
	TextView t4;
	TextView t5;
	TextView t6;
	TextView t7;
	TextView t8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_leitai);
		Intent intent = getIntent();
		 Bundle bundle = intent.getExtras();
		id = bundle.getInt("id");
		System.out.println("id$$$$$$$$$" + id);
		
		t1 = (TextView) findViewById(R.id.textView1);
		t2 = (TextView) findViewById(R.id.textView4);
		t3 = (TextView) findViewById(R.id.textView7);
		t4 = (TextView) findViewById(R.id.textView11);
		t5 = (TextView) findViewById(R.id.textView3);
		t6 = (TextView) findViewById(R.id.textView6);
		t7 = (TextView) findViewById(R.id.textView9);
		t8 = (TextView) findViewById(R.id.textView12);
	
		t1.setText(((int) BaiduMapActivity.me.step) +"");
		t2.setText( ((int)BaiduMapActivity.me.time)+"");
		t3.setText(((int) BaiduMapActivity.me.dis)+"");
		
		t4.setText(((int) BaiduMapActivity.me.calory)+"");
		t5.setText(((int) BaiduMapActivity.PeopleAround.get(id).step)+"");
		t6.setText(((int) BaiduMapActivity.PeopleAround.get(id).time)+"");
		t7.setText( ((int)BaiduMapActivity.PeopleAround.get(id).dis)+"");
		t8.setText(((int) BaiduMapActivity.PeopleAround.get(id).calory)+"");
/*
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leitai, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public  class PlaceholderFragment extends Fragment {
		
	

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_leitai,
					container, false);
			
			
			
			return rootView;
		}
	}

}
