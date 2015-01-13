package com.linc.pedometer.global;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.claire.pedometer.HomeActivity;

public class Global {
	
	public static final String hostUrl = "http://10.4.20.139:8080/PedometerServer/";
	
	public static boolean isLogin = false;
	public static int userid;
	
	public static int WalkStepValue;
	public static int WalkPaceValue;	
	public static float WalkDistanceValue;
	public static float WalkSpeedValue;
	public static float WalkTimeValue;
	public static float WalkCaloryValue;
	
	public static float RunCaloryValue;
	public static float RunDistanceValue;
	public static float RunTimeValue;
	
	public static float BicycleCaloryValue;
	public static float BicycleDistanceValue;
	public static float BicycleTimeValue;
	
	public static boolean mIsRunning;
	
	public static HomeActivity chartHomeActivity;
	
	public static List<LatLng> PeopleAround = new ArrayList<LatLng> ();

}
