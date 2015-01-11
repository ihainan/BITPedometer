package com.claire.workout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.claire.pedometer.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class WorkoutActivity extends Activity {


	ImageView pre;
	ImageView next;
	TextView info_date;
	TextView info_step;
	TextView info_distance;
	TextView info_calories;
	int count = 10;

	public String getDateString(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,E");
		String result = format.format(date);
		return result;
	}
	
	public Date getDateFromString(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,E");
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public Date getDate(Date date,int change){
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(date); 
		calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+change);
		return calendar.getTime();
	}
	
	public void init(View rootView,Typeface tf) {

		pre = (ImageView) rootView.findViewById(R.id.info_pre);
		next = (ImageView) rootView.findViewById(R.id.info_next);
		info_date = (TextView) rootView.findViewById(R.id.info_date);
		info_step = (TextView) rootView.findViewById(R.id.info_step);
		info_distance = (TextView) rootView.findViewById(R.id.info_distance);
		info_calories = (TextView) rootView.findViewById(R.id.info_calories);
		
		info_date.setText(this.getDateString(new Date()));
		info_date.setTypeface(tf);
		info_date.refreshDrawableState();
		pre.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				// TODO Auto-generated method stub
				String date = info_date.getText().toString(); 
				Date nowdate =  getDateFromString(date);
				Date predate = getDate(nowdate,-1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(predate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(predate));
				info_date.refreshDrawableState();
			}
			
		});
		
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String date = info_date.getText().toString();
				Date nowdate =  getDateFromString(date);
				Date nextdate = getDate(nowdate,1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(nextdate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(nextdate));
				info_date.refreshDrawableState();
			}
			
		});
		

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout);

		pre = (ImageView) this.findViewById(R.id.info_pre);
		next = (ImageView) this.findViewById(R.id.info_next);
		info_date = (TextView) this.findViewById(R.id.info_date);
		info_step = (TextView) this.findViewById(R.id.info_step);
		info_distance = (TextView) this.findViewById(R.id.info_distance);
		info_calories = (TextView) this.findViewById(R.id.info_calories);
		
		info_date.setText(this.getDateString(new Date()));
		info_date.refreshDrawableState();
		pre.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				// TODO Auto-generated method stub
				String date = info_date.getText().toString(); 
				Date nowdate =  getDateFromString(date);
				Date predate = getDate(nowdate,-1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(predate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(predate));
				info_date.refreshDrawableState();
			}
			
		});
		
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String date = info_date.getText().toString();
				Date nowdate =  getDateFromString(date);
				Date nextdate = getDate(nowdate,1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(nextdate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(nextdate));
				info_date.refreshDrawableState();
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
