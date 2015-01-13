package com.claire.pedometer;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.claire.pedometer.UpLoadLocationService.UploadBinder;
import com.linc.pedometer.global.Global;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class BaiduMapActivity extends Activity {
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	
	boolean isFirstLoc = true;// 是否首次定位

	private LatLng lastPoint = null;

	private final static double DEF_PI = 3.14159265359; // PI
	private final static double DEF_2PI = 6.28318530712; // 2*PI
	private final static double DEF_PI180 = 0.01745329252; // PI/180.0
	private final static double DEF_R = 6370693.5; // radius of earth
	
	UpLoadLocationService uploadService;  

	
    public void init(View rootView,Typeface tf) {
    	

		

    	
    	
//   	 SDKInitializer.initialize(getApplicationContext());    
    	//sSDKInitializer.initialize(getApplicationContext());
		//setContentView(R.layout.activity_map);
		// 地图初始化
		mMapView = (MapView) rootView.findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(rootView.getContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(2000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		
		LatLng p1 = new LatLng(39.97923, 116.357428);
		LatLng p2 = new LatLng(39.94923, 116.397428);
		LatLng p3 = new LatLng(39.97923, 116.437428);
		
		com.linc.pedometer.global.Global.PeopleAround.add(p1);
		com.linc.pedometer.global.Global.PeopleAround.add(p2);
		com.linc.pedometer.global.Global.PeopleAround.add(p3);
		

	
		   Runnable myRunnable= new Runnable() {    
		        public void run() {  
		        	while(true){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Thread run");
						if(lastPoint != null && Global.isLogin)
		        	     {
							peopleAround();
							upload(lastPoint);
		        	     }
		        	//handler.postDelayed(this, 1000); 
		        	}

		        }  
		    };  
			  Thread s = new Thread(myRunnable);
			  s.start();
		    
   }
public void peopleAround(){
		
		System.out.println("send");
		Map<String, String> params = new HashMap<String, String>();
		  params.put("id", Global.userid+"");
		 try {
						String result= sendGetRequest(Global.hostUrl+"peoplearound", params);
						System.out.println(result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}

public void upload(LatLng location){
		
		System.out.println("send");
		Map<String, String> params = new HashMap<String, String>();
		  params.put("id", Global.userid+"");
	      params.put("lat", location.latitude+"");
		  params.put("lon", location.longitude+"");
		 try {
						String result= sendGetRequest(Global.hostUrl+"uploadlocation", params);
						System.out.println(result);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
	}

	
	public String sendGetRequest(String path, Map<String, String> params) throws Exception{
		StringBuilder sb = new StringBuilder(path);
		sb.append('?');
		// ?method=save&title=435435435&timelength=89&
		for(Map.Entry<String, String> entry : params.entrySet()){
			sb.append(entry.getKey()).append('=')
				.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
		}
		sb.deleteCharAt(sb.length()-1);
		
		String url = sb.toString();
	
		HttpGet request = new HttpGet (url);
		// 发送请求 
		HttpResponse httpResponse = new DefaultHttpClient().execute(request); 
		// 得到应答的字符串，这也是一个 JSON 格式保存的数据 
		String retSrc = EntityUtils.toString(httpResponse.getEntity()); 
		// 生成 JSON 对象 
		
		return retSrc;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("OnCreat");
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		



	}
	
	
    
	

	/**
	 * 定位SDK监听函数
	 */

	public double getLongDistance(double lat1, double lon1, double lat2,
			double lon2) {
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1)
				* Math.cos(ns2) * Math.cos(ew1 - ew2);
		// 调整到[-1..1]范围内，避免溢出
		if (distance > 1.0) {
			distance = 1.0;
		} else if (distance < -1.0) {
			distance = -1.0;
		}
		// 求大圆劣弧长度
		distance = DEF_R * Math.acos(distance);
		return distance / 1000;
	}

	/**
	 * 
	 * 检测是否在正确的移动
	 * 
	 *
	 * 
	 * @param distance
	 * 
	 * @return
	 */

	private boolean checkProperMove(double distance) {

		if (distance <= 8) {

			return true;

		} else {

			return false;

		}

	}

	private double totalDistance = 0;
	
	
	private void showAroundPeople(){

		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.marker);
		
		OverlayOptions ooA;
		for(int i = 0; i < com.linc.pedometer.global.Global.PeopleAround.size(); i++){
			LatLng person =  com.linc.pedometer.global.Global.PeopleAround.get(i);
			ooA = new MarkerOptions().position(person).icon(bdA)
					.zIndex(9).draggable(true);
			mBaiduMap.addOverlay(ooA);
		}

		//LatLng llA = new LatLng(39.963175, 116.400244);

		
		
		
	}
	

  


	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			
			showAroundPeople();
			
			
			
			
			
			
			//System.out.println( "lon:" + location.getLongitude()+ "lat: " + location.getLatitude());

			LatLng currentLocation = new LatLng(location.getLatitude(),
					location.getLongitude());
			
		
			
			
			if (lastPoint == null)
				lastPoint = currentLocation;
			
			 float distance = (float) (405* getLongDistance(lastPoint.latitude,lastPoint.longitude, currentLocation.latitude,currentLocation.longitude));
			
			// if(Global.)
			 /*
			 if(com.linc.pedometer.global.Global.activityType == "RUNNING")
				 com.linc.pedometer.global.Global.RunDistanceValue += distance;
			 else if (com.linc.pedometer.global.Global.activityType == "ON_BICYCLE")
				 com.linc.pedometer.global.Global.BicycleDistanceValue += distance;
			 else  if(Global.activityType == "ON_FOOT" || Global.activityType=="WALKING")
				 com.linc.pedometer.global.Global.WalkDistanceValue += distance;
			 */
			 
			 if(Global.getType() == "RUN")
				 Global.RunDistanceValue += distance;
			 else if (Global.getType() == "BICYCLE")
				 Global.BicycleDistanceValue += distance;
			 else  if(Global.getType() == "WALK" )
				 Global.WalkDistanceValue += distance;
			 

			 if(Global.getType() == "RUN")
				 com.linc.pedometer.global.Global.RunDistanceValue += distance;
			 else if (Global.getType() == "BICYCLE")
				 com.linc.pedometer.global.Global.BicycleDistanceValue += distance;
			 else 
				 com.linc.pedometer.global.Global.WalkDistanceValue += distance;
			 
			 //Log.w("Login", Global.WalkDistanceValue + "");

			DotOptions tmp = new  DotOptions();
			tmp.color(0xAAFF0000);
			tmp.center(currentLocation);
		
			mBaiduMap.addOverlay(tmp);
			
			
		//	System.out.print("distance:" + distance);
			
				totalDistance += distance;

				lastPoint = currentLocation;
				DecimalFormat    df   = new DecimalFormat("######0.00");   
			//Toast.makeText(getApplicationContext(), "distance:" + df.format(totalDistance), Toast.LENGTH_LONG).show();

			/*LatLng p1 = new LatLng(39.97923, 116.357428);

			List<LatLng> points2 = new ArrayList<LatLng>();
			points2.add(p1);
			points2.add(currentLocation);

			OverlayOptions ooPolyline2 = new PolylineOptions().width(10)
					.color(0xAAFF0000).points(points2);
			mBaiduMap.addOverlay(ooPolyline2);
*/
			// System.out.println(location.getLongitude() + ";" +
			// location.getAltitude());
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}