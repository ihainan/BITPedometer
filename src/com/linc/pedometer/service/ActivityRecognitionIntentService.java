package com.linc.pedometer.service;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.linc.pedometer.global.Global;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ActivityRecognitionIntentService extends IntentService{

	private String TAG = this.getClass().getSimpleName();

	public ActivityRecognitionIntentService() {
		super("My Activity Recognition Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
		Global.activityType = this.getType(result.getMostProbableActivity().getType());
		Global.confidence = result.getMostProbableActivity().getConfidence();
		//Log.i("Login",  Global.activityType + ", " +  Global.confidence);
	}
	
	/* ��ȡ�˶����ͣ������ַ��� */
	private String getType(int type){
		if(type == DetectedActivity.UNKNOWN)
			return "UNKNOWN";
		else if(type == DetectedActivity.ON_BICYCLE)
			return "ON_BICYCLE";
		else if(type == DetectedActivity.ON_FOOT)
			return "ON_FOOT";
		else if(type == DetectedActivity.RUNNING)
			return "RUNNING";
		else if(type == DetectedActivity.STILL)
			return "STILL";
		else if(type == DetectedActivity.IN_VEHICLE)
			return "IN_VEHICLE";
		else if(type == DetectedActivity.WALKING)
			return "WALKING";
		else if(type == DetectedActivity.TILTING)
			return "TILTING";
		return null;
	}

}
