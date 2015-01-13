package com.claire.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;

import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.claire.pedometer.R.menu;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;







import com.google.android.gms.internal.js;
import com.linc.pedometer.global.Global;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class CalorieActivity extends Activity {
	 private LineChart nChart;
	 private TextView tView;
	 
	 ArrayList<Integer> allDayArrayList = new ArrayList<Integer>();
	 
	 
	 public void init(View rootView,Typeface tf){
		 Random random = new Random();
		 nChart = (LineChart) rootView.findViewById(R.id.chart1);
		 JSONArray ja = Global.calorytoday;
		 for(int i = 1;i <= 24; i++){
	        	try {
					allDayArrayList.add((int) ja.getDouble(i-1));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        Log.v("AllDayArrayList", allDayArrayList.toString());
	        
	        setLineData(allDayArrayList);
	        paintCubicChart(allDayArrayList);
	        
	       tView = (TextView) rootView.findViewById(R.id.textView1);
	       tView.setText(getCurrentDate());
	       tView.setTypeface(tf);
	 }
	 
	 private String getCurrentDate(){
		 final Calendar c = Calendar.getInstance(); 
		 
	     int  mYear = c.get(Calendar.YEAR); //获取当前年份 

	     int  mMonth = c.get(Calendar.MONTH)+1;//获取当前月份 

	     int  mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码 

	     int  mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数 

	     int mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
	     return mYear+"-" + mMonth + "-" + mDay;
	 }
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calorie);
		 nChart = (LineChart) findViewById(R.id.chart1);
        //Random random = new Random();
		
		JSONArray ja = Global.calorytoday;
		 
        for(int i = 1;i <= 24; i++){
        	try {
				allDayArrayList.add((int) ja.getDouble(i-1));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        Log.v("AllDayArrayList", allDayArrayList.toString());
        
        setLineData(allDayArrayList);
        paintCubicChart(allDayArrayList);
		
	}
	
	 
    /**
     * Paint cubic chart
     */
    public void paintCubicChart(ArrayList<Integer> allday){
    	
    	// if enabled, the chart will always start at zero on the y-axis
        nChart.setStartAtZero(true);

        // disable the drawing of values into the chart
        nChart.setDrawYValues(false);

        nChart.setDrawBorder(false);
        
        nChart.setDrawLegend(false);

        // no description text
        nChart.setDescription("");
        nChart.setUnit(" CAL");

        // enable value highlighting
        nChart.setHighlightEnabled(true);

        // enable touch gestures
        nChart.setTouchEnabled(true);

        // enable scaling and dragging
        nChart.setDragEnabled(true);
        nChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        nChart.setPinchZoom(false);

        nChart.setDrawGridBackground(false);
        nChart.setDrawVerticalGrid(false);
        
        XLabels x = nChart.getXLabels();

        YLabels y = nChart.getYLabels();

        y.setLabelCount(5);

//        // add data
//        setData(45, 100);
        
        nChart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        nChart.invalidate();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pie, menu);
        return true;
    }
    
    /**
     * Set data for cubic line chart
     * @param today
     */
    public void setLineData(ArrayList<Integer> allDay) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 1; i <= 24; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> vals1 = new ArrayList<Entry>();

        for (int i = 1; i <= 24; i++) {
            vals1.add(new Entry(allDay.get(i-1), i));
        }
        Log.v("vals1",vals1.toString());
    	
    	
//    	int count = 45;
//    	int range = 100; 
//    	ArrayList<String> xVals = new ArrayList<String>();
//         for (int i = 0; i < count; i++) {
//             xVals.add((1990 +i) + "");
//         }
//
//         ArrayList<Entry> vals1 = new ArrayList<Entry>();
//
//         for (int i = 0; i < count; i++) {
//             float mult = (range + 1);
//             float val = (float) (Math.random() * mult) + 20;// + (float)
//                                                            // ((mult *
//                                                            // 0.1) / 10);
//             vals1.add(new Entry(val, i));
//         }
//        
    	
         // create a dataset and give it a type
         LineDataSet set1 = new LineDataSet(vals1, "DataSet 1");
         set1.setDrawCubic(true);
         set1.setCubicIntensity(0.2f);
         set1.setDrawFilled(true);
         set1.setDrawCircles(false); 
         set1.setLineWidth(2f);
         set1.setCircleSize(5f);
         set1.setHighLightColor(Color.rgb(244, 117, 117));
         set1.setColor(Color.rgb(104, 241, 175));

         ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
         dataSets.add(set1);

         // create a data object with the datasets
         LineData data = new LineData(xVals, dataSets);

         // set data
         nChart.setData(data);
    }


	

}
