package com.linc.pedometer.global;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.baidu.mapapi.model.LatLng;
import com.claire.pedometer.HomeActivity;

public class Global {
	
	public static final String hostUrl = "http://10.4.20.139:8080/PedometerServer/";
	
	public static boolean isLogin = false;
	public static int userid;
	
	public static int WalkStepValue;
	public static int WalkPaceValue;	
	public static float WalkDistanceValue = 0;
	public static float WalkSpeedValue;
	public static float WalkTimeValue = 0;
	public static float WalkCaloryValue;
	
	public static float RunCaloryValue;
	public static float RunDistanceValue = 0;
	public static float RunTimeValue;
	
	public static float BicycleCaloryValue;
	public static float BicycleDistanceValue = 0;
	public static float BicycleTimeValue;
	
	public static boolean mIsRunning;
	public static float speed;
	

	
	public static JSONArray historydata;
	public static JSONArray calorytoday;
	public static JSONArray walkdistoday;
	public static JSONArray rundistoday;
	public static JSONArray bicycledistoday;
	
	/* Activity Status */
	public static String activityType = "UNKNOWN";
	public static int confidence = 100;
	
	public static String getType () {
		if(Global.speed < 2)
			return "WALK";
		else if(Global.speed < 10)
			return "RUN";
		else
			return "BICYCLE";
	}

}
