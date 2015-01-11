package com.claire.statistics;

import java.util.ArrayList;
import java.util.Random;

import com.claire.paragraph.DemoBase;
import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.claire.pedometer.R.menu;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels.YLabelPosition;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.os.Build;

public class MileActivity extends DemoBase  {
	
	private TabHost myTabHost;
	
	private LineChart[] typeCharts = new LineChart[3];
	private LineData[] datas = new LineData[3];
	
	private BarChart monthChart;
	
	private Typeface mTf;
	private int[] mColors = new int[] {
	            Color.rgb(137, 230, 81), 
	            Color.rgb(240, 240, 30), 
	            Color.rgb(89, 199, 250),
	};
	
	public void init(View rootView,Typeface tf) {
		myTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);  
		myTabHost.setup();
		myTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		
		
		myTabHost.addTab(myTabHost.newTabSpec("Day").setIndicator("Day").setContent(R.id.tab1));
		myTabHost.addTab(myTabHost.newTabSpec("Month").setIndicator("Month").setContent(R.id.tab2));
		
		typeCharts[0] = (LineChart) rootView.findViewById(R.id.chart1);
		typeCharts[1] = (LineChart) rootView.findViewById(R.id.chart2);
		typeCharts[2] = (LineChart) rootView.findViewById(R.id.chart3);
		
		
		
		 //mTf = Typeface.createFromAsset(getAssets(), "monog.ttf");

		 ArrayList<Entry> footList = new ArrayList<Entry>();
		 ArrayList<Entry> runList = new ArrayList<Entry>();
		 ArrayList<Entry> bikeList = new ArrayList<Entry>();
		 
		 Random random = new Random();
		
		 
		 for(int i = 0;i <= 24;i++){
			 footList.add(new Entry(random.nextInt(101),i));
			 runList.add(new Entry(random.nextInt(101),i));
			 bikeList.add(new Entry(random.nextInt(101),i));
		 }
		 
		 
		 datas[0] = getData(footList,"Walk");
		 datas[1] = getData(runList,"Run");
		 datas[2] = getData(bikeList,"Bicycle");

	        for (int i = 0; i < typeCharts.length; i++)
	            // add some transparency to the color with "& 0x90FFFFFF"
	            setupChart(typeCharts[i], datas[i], mColors[i % mColors.length]);
	        
	        
	    /**
	     * Month
	     */
	        monthChart = (BarChart) rootView.findViewById(R.id.monthBar);
	        
	        // enable the drawing of values
	        //monthChart.setDrawYValues(true);

	        monthChart.setDescription("");

	        // if more than 60 entries are displayed in the chart, no values will be
	        // drawn
	        monthChart.setMaxVisibleValueCount(60);
	        
	        MyValueFormatter customFormatter = new MyValueFormatter();
	        
	        // set a custom formatter for the values inside the chart
	        monthChart.setValueFormatter(customFormatter);
	        
	        // if false values are only drawn for the stack sum, else each value is drawn
	        //monthChart.setDrawValuesForWholeStack(true);
	        
	        // disable 3D
	        monthChart.set3DEnabled(false);
	        // scaling can now only be done on x- and y-axis separately
	        monthChart.setPinchZoom(false);

	        monthChart.setDrawBarShadow(false);
	        
	        ArrayList<Integer> footMonthList = new ArrayList<Integer>();
			 ArrayList<Integer> runMonthList = new ArrayList<Integer>();
			 ArrayList<Integer> bikeMonthList = new ArrayList<Integer>();
			 
			 Random randomMonth = new Random();
			 
			 //不同月的天数木有考虑，先31算
			 for(int i = 0;i <= 31;i++){
				 footMonthList.add(random.nextInt(101));
				 runMonthList.add(random.nextInt(101));
				 bikeMonthList.add(random.nextInt(101));
			 }
			
			 setMonthData(footMonthList, runMonthList, bikeMonthList);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mile);
		myTabHost = (TabHost) findViewById(android.R.id.tabhost);  
		myTabHost.setup();
		myTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		
		
		myTabHost.addTab(myTabHost.newTabSpec("Day").setIndicator("Day").setContent(R.id.tab1));
		myTabHost.addTab(myTabHost.newTabSpec("Month").setIndicator("Month").setContent(R.id.tab2));
		
		typeCharts[0] = (LineChart) findViewById(R.id.chart1);
		typeCharts[1] = (LineChart) findViewById(R.id.chart2);
		typeCharts[2] = (LineChart) findViewById(R.id.chart3);
		
		
		
		 mTf = Typeface.createFromAsset(getAssets(), "monog.ttf");

		 ArrayList<Entry> footList = new ArrayList<Entry>();
		 ArrayList<Entry> runList = new ArrayList<Entry>();
		 ArrayList<Entry> bikeList = new ArrayList<Entry>();
		 
		 Random random = new Random();
		
		 
		 for(int i = 0;i <= 24;i++){
			 footList.add(new Entry(random.nextInt(101),i));
			 runList.add(new Entry(random.nextInt(101),i));
			 bikeList.add(new Entry(random.nextInt(101),i));
		 }
		 
		 
		 datas[0] = getData(footList,"Walk");
		 datas[1] = getData(runList,"Run");
		 datas[2] = getData(bikeList,"Bicycle");

	        for (int i = 0; i < typeCharts.length; i++)
	            // add some transparency to the color with "& 0x90FFFFFF"
	            setupChart(typeCharts[i], datas[i], mColors[i % mColors.length]);
	        
	        
	    /**
	     * Month
	     */
	        monthChart = (BarChart) findViewById(R.id.monthBar);
	        
	        // enable the drawing of values
	        monthChart.setDrawYValues(true);

	        monthChart.setDescription("");

	        // if more than 60 entries are displayed in the chart, no values will be
	        // drawn
	        monthChart.setMaxVisibleValueCount(60);
	        
	        MyValueFormatter customFormatter = new MyValueFormatter();
	        
	        // set a custom formatter for the values inside the chart
	        monthChart.setValueFormatter(customFormatter);
	        
	        // if false values are only drawn for the stack sum, else each value is drawn
	        monthChart.setDrawValuesForWholeStack(true);
	        
	        // disable 3D
	        monthChart.set3DEnabled(false);
	        // scaling can now only be done on x- and y-axis separately
	        monthChart.setPinchZoom(false);

	        monthChart.setDrawBarShadow(false);
	        
	        ArrayList<Integer> footMonthList = new ArrayList<Integer>();
			 ArrayList<Integer> runMonthList = new ArrayList<Integer>();
			 ArrayList<Integer> bikeMonthList = new ArrayList<Integer>();
			 
			 Random randomMonth = new Random();
			 
			 //不同月的天数木有考虑，先31算
			 for(int i = 0;i <= 31;i++){
				 footMonthList.add(random.nextInt(101));
				 runMonthList.add(random.nextInt(101));
				 bikeMonthList.add(random.nextInt(101));
			 }
			
			 setMonthData(footMonthList, runMonthList, bikeMonthList);
//	        // change the position of the y-labels
//	        YLabels yLabels = monthChart.getYLabels();
//	        yLabels.setPosition(YLabelPosition.BOTH_SIDED);
//	        yLabels.setLabelCount(5);
//	        yLabels.setFormatter(customFormatter);
//
//	        XLabels xLabels = monthChart.getXLabels();
//	        xLabels.setPosition(XLabelPosition.TOP);
//	        xLabels.setCenterXLabelText(true);

	        // mChart.setDrawXLabels(false);
	        // mChart.setDrawYLabels(false);

		
	}
	
	private void setMonthData(ArrayList<Integer> walk,ArrayList<Integer> run, ArrayList<Integer> bike ){
		ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i <= 31; i++) {
            xVals.add(i + "");
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i <= 31; i++) {
            
        	yVals1.add(new BarEntry(new float[] {
        			walk.get(i),run.get(i),bike.get(i)
           }, i));

        }
        BarDataSet monthDataSet = new BarDataSet(yVals1, "Statistics Vienna 2014");
        monthDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        monthDataSet.setStackLabels(new String[] {
                "Walk", "Run", "Bike"
        });

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(monthDataSet);

        BarData data = new BarData(xVals, dataSets);

        monthChart.setData(data);
        monthChart.invalidate();

	}
	
	private void setupChart(LineChart chart, LineData data, int color) {

        // if enabled, the chart will always start at zero on the y-axis
        chart.setStartAtZero(true);

        // disable the drawing of values into the chart
        chart.setDrawYValues(false);

        chart.setDrawBorder(false);

        // no description text
        chart.setDescription("");
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable / disable grid lines
        chart.setDrawVerticalGrid(false);
        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
        chart.setGridColor(Color.WHITE & 0x70FFFFFF);
        chart.setGridWidth(1.25f);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        chart.setValueTypeface(mTf);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.CIRCLE);
        l.setFormSize(6f);
        l.setTextColor(Color.WHITE);
        l.setTypeface(mTf);

        YLabels y = chart.getYLabels();
        y.setTextColor(Color.WHITE);
        y.setTypeface(mTf);
        y.setLabelCount(4);

        XLabels x = chart.getXLabels();
        x.setTextColor(Color.WHITE);
        x.setTypeface(mTf);

        // animate calls invalidate()...
        chart.animateX(2500);
    }
	
	 private LineData getData(ArrayList<Entry> WalkList,String name) {

	        ArrayList<String> time = new ArrayList<String>();
	        for (int i = 0; i <= 24; i++) {
	            time.add(i+"");
	        }


	        // create a dataset and give it a type
	        LineDataSet footDataSet = new LineDataSet(WalkList,name);
	        // set1.setFillAlpha(110);
	        // set1.setFillColor(Color.RED);

	        footDataSet.setLineWidth(1.75f);
	        footDataSet.setCircleSize(3f);
	        footDataSet.setColor(Color.WHITE);
	        footDataSet.setCircleColor(Color.WHITE);
	        footDataSet.setHighLightColor(Color.WHITE);
	        
	      
	        

	        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	        dataSets.add(footDataSet); // add the datasets
	       
	        // create a data object with the datasets
	        LineData data = new LineData(time, dataSets);

	        return data;
	    }



}
