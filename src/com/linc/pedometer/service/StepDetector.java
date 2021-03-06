package com.linc.pedometer.service;

import java.util.ArrayList;
import java.util.Date;

import com.linc.pedometer.global.Global;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.util.Log;

public class StepDetector implements SensorEventListener {
	

	private final static String TAG = "StepDetector";
	//public static int CURRENT_STEP = 0;
	public static float SENSITIVITY = 10; // SENSITIVITY灵敏度
	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;
	
	Date lasttime;
	Date thistime;
	
	private Thread thread;
	/**
	 * 最后加速度方向
	 */
	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;
	
	private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

	/**
	 * 传入上下文的构造函数
	 * 
	 * @param context
	 */
	public StepDetector(Context context) {
		super();
		//mThread();
		start = System.currentTimeMillis();
		lasttime = new Date();
		int h = 480;
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
	}
	
	public void setSensitivity(float sensitivity) {
		SENSITIVITY = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
    }
    
    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }

	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		synchronized (this) {
			
			thistime = new Date();
			
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				float vSum = 0;
				for (int i = 0; i < 3; i++) {
					final float v = mYOffset + event.values[i] * mScale[1];
					vSum += v;
				}
				int k = 0;
				float v = vSum / 3;

				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {
					// Direction changed
					int extType = (direction > 0 ? 0 : 1); // minumum or
															// maximum?
					mLastExtremes[extType][k] = mLastValues[k];
					float diff = Math.abs(mLastExtremes[extType][k]
							- mLastExtremes[1 - extType][k]);

					if (diff > SENSITIVITY) {
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
						boolean isNotContra = (mLastMatch != 1 - extType);
						//end = System.currentTimeMillis();

						if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
								&& isNotContra) {
							
							
							if(Global.getType() == "RUN") {
								Global.RunTimeValue += ((thistime.getTime()-lasttime.getTime())/1000.0);
							} else if(Global.getType() == "BICYCLE") {
								Global.BicycleTimeValue += ((thistime.getTime()-lasttime.getTime())/1000.0);
							} else  {
								Log.w("Login", "end" + thistime.getTime());
								Log.w("Login", "start" + lasttime.getTime());
								Global.WalkTimeValue += ((thistime.getTime()-lasttime.getTime())/1000.0);
								Log.w("Login", Global.WalkTimeValue + "walktime");
							}
								
							/*
							if(Global.activityType == "RUNNING") {
								Global.RunTimeValue += (end - start)/1000;
							} else if(Global.activityType == "ON_BICYCLE") {
								Global.BicycleTimeValue += (end - start)/1000;
							} else if(Global.activityType == "ON_FOOT" || Global.activityType=="WALKING") {
								Global.WalkTimeValue += (end - start)/1000;
							}*/
								
							//CURRENT_STEP++;
							mLastMatch = extType;
							lasttime = thistime;
							
							for (StepListener stepListener : mStepListeners) {
								//Log.w(TAG, "onstep");
								stepListener.onStep();
							}
						} else {
							mLastMatch = -1;
						}
						
					}
					mLastDiff[k] = diff;
				}
				mLastDirections[k] = direction;
				mLastValues[k] = v;
			}
			if(thistime.getTime()-lasttime.getTime() > 2000)
				lasttime = thistime;

		}
		//Global.WalkStepValue = CURRENT_STEP;
		//Log.w("stepService", CURRENT_STEP+"");
		//Global.chartHomeActivity.setWalk(CURRENT_STEP);
	}
	
	//当传感器的经度发生变化时就会调用这个方法，在这里没有用
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	/*
	private void mThread() {
		if (thread == null) {

			thread = new Thread(new Runnable() {
				public void run() {
					
					int lastStep;
					float last runDistance;
					float last 
					
					while (true) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						if(Global.getType() == "RUN" && ) {
							Global.RunTimeValue += 0.01;
						} else if(Global.getType() == "BICYCLE") {
							Global.BicycleTimeValue += 0.01;
						} else  {
							
							Global.WalkTimeValue += 0.01;
							Log.w("Login", Global.WalkTimeValue + "walktime");
						}
						
					}
				}
			});
			thread.start();
		}
	}
	*/
	

}
