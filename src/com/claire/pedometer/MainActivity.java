	package com.claire.pedometer;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xclcharts.renderer.XEnum.Location;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.claire.adapter.MenuAdapter;
import com.claire.friend.FriendsListActivity;
import com.claire.pk.SinglePKLoginActivity;
import com.claire.statistics.CalorieActivity;
import com.claire.statistics.MileActivity;
import com.claire.user.RegisterActivity;
import com.claire.workout.WorkoutActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.linc.pedometer.global.Global;
import com.linc.pedometer.service.ActivityRecognitionIntentService;
import com.linc.pedometer.service.MD5Util;
import com.linc.pedometer.service.PedometerSettings;
import com.linc.pedometer.service.StepService;



@SuppressLint("ValidFragment")
public class MainActivity extends FragmentActivity 
	implements ConnectionCallbacks, OnConnectionFailedListener{
	
	private static final String TAG = "PedometerMain";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private MenuAdapter mMenuAdapter;
    public String page = null;
    
    public int layoutid = 0;
    
    /* Google Play Service */
    private GoogleApiClient mGoogleApiClient;	// Google API Client 类，用于与 Google Play 服务的连接工作
    
    // 下述成员用于处理无法连接 Google Play 服务时候的相关工作
    private static final int REQUEST_RESOLVE_ERROR = 1001;    
    private static final String DIALOG_ERROR = "dialog_error";
	private static final String STATE_RESOLVING_ERROR = "resolving_error";	
    private boolean mResolvingError = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext()); 
        setContentView(R.layout.activity_main);
        
        /* START : Google Play Service */
        // 构建 Google 服务连接器
        
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        		.addApi(LocationServices.API)
        		.addApi(ActivityRecognition.API)
               	.addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
                
        /* END : Google Play Service */
        
        Global.mIsRunning = false;

        
        Intent intent = getIntent();
        if(null != intent){
        	page = intent.getStringExtra("page");
        }
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mMenuAdapter = new MenuAdapter(this);
        
        mDrawerList.setAdapter(mMenuAdapter);
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0,0);
        }
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	// 连接到 Google Play 服务
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }
    
    @Override
    protected void onStop(){
    	// 断开与 Google Play 服务的连接
        mGoogleApiClient.disconnect();
    	super.onStop();
    }
    
    @Override
    protected void onResume() {
        Log.i(TAG, "[ACTIVITY] onResume");
        super.onResume();
        
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        
        // Read from preferences if the service was running on the last onPause
        //Global.mIsRunning = mPedometerSettings.isServiceRunning();
        
        
        // Start the service if this is considered to be an application start (last onPause was long ago)
        if (!Global.mIsRunning) {
            startStepService();
            bindStepService();
        }
        else if (Global.mIsRunning) {
            bindStepService();
        }
        
        mPedometerSettings.clearServiceRunning();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.exit:
        	//resetValues(false);
            unbindStepService();
            stopStepService();
            //mQuitting = true;
            finish();
            return true;
       /* case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            }
            return true;*/
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* ç›‘å�¬è�œå�•listview */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position,0);
        }
    }

    /**
     * 
     * @param position 
     * @param flag 0-position 1-layout_id
     */
    private void selectItem(int position,int flag) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        args.putInt("FLAG", flag);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        if(0 == flag){
	        mDrawerList.setItemChecked(position, true);
	        setTitle(mPlanetTitles[position]);
	        mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    
    private StepService mService;
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = ((StepService.StepBinder)service).getService();

            mService.registerCallback(mCallback);
            mService.reloadSettings();
            
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };
    

    private void startStepService() {
        if (! Global.mIsRunning) {
            Log.i(TAG, "[SERVICE] Start");
            Global.mIsRunning = true;
            startService(new Intent(this,
                    StepService.class));
        }
    }
    
    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        bindService(new Intent(this, 
                StepService.class), mConnection, Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    private void unbindStepService() {
        Log.i(TAG, "[SERVICE] Unbind");
        unbindService(mConnection);
    }
    
    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(this,
                  StepService.class));
        }
        Global.mIsRunning = false;
    }
    
    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            //mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }
        public void paceChanged(int value) {
           // mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
        }
        public void distanceChanged(float value) {
            //mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG, (int)(value*1000), 0));
        }
        public void speedChanged(float value) {
            //mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG, (int)(value*1000), 0));
        }
        public void caloriesChanged(float value) {
           // mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG, (int)(value), 0));
        }
    };


    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public  class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        @SuppressLint("ValidFragment")
		public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            int flag = getArguments().getInt("FLAG");
            String planet = "";
            String title = "";
            if(flag == 1){
            	layoutid = i;
            }else{
            	if(null != page){
            		String[] menus = getResources().getStringArray(R.array.planets_array);
            		String[] planets = getResources().getStringArray(R.array.menu_content);
            		int j=0;
            		for(j=0;j<menus.length;j++){
            			if(page.equals(menus[j])){
            				layoutid = j;break;
            			}
            		}
            		if(j == menus.length){
            			j = 0;
            		}
            		planet = planets[j];
            		layoutid = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "layout", getActivity().getPackageName());
            		title = page;
            		page = null;
            	}else{
		            planet = getResources().getStringArray(R.array.menu_content)[i];
		            title = getResources().getStringArray(R.array.planets_array)[i];
		            
		            layoutid = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
	                            "layout", getActivity().getPackageName());
            	}
            }
            Log.v("title",title);
            Log.v("planet", planet);
            View rootView = inflater.inflate(layoutid, container, false);
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "monog.ttf");
            
            switch (title) {
			case "HOME":
				HomeActivity homeActivity = new HomeActivity();
		        homeActivity.init(rootView,tf);
				break;
			case "SPORTS INFO":
				MileActivity mileActivity = new MileActivity();
	            mileActivity.init(rootView, tf);
				break;
			case "LOGIN":
				//final LoginActivity loginActivity = new LoginActivity();
				//loginActivity.init(rootView);
				TextView tView = (TextView) rootView.findViewById(R.id.textView1);
		        tView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
						MainActivity.this.startActivity(intent);				
					}
				});
		        
		        
		        Button sub = (Button) rootView.findViewById(R.id.loginBtn);
		        sub.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						String username = ((EditText)findViewById(R.id.usernameEdit)).getText().toString();
				    	String password = ((EditText)findViewById(R.id.passwordEdit)).getText().toString();
				    	
				    	final Map<String, String> params = new HashMap<String, String>();
				    	params.put("username", username);
				    	params.put("password", MD5Util.MD5(password));
				    	
				    	try {
				    		
				    		new Thread(){
				    		@Override
				    		public void run(){
				    		//你要执行的方法
				    		//执行完毕后给handler发送一个空消息
				    			try {
				    				//selectItem(0, 0);
				    				
				    				Log.w("Login", "login");
									String result= sendGetRequest(Global.hostUrl+"login", params);
								
									JSONObject json = new JSONObject(result);
									String res = json.getString("res");
									
									
									if(res.equals("true")) {
										
										Global.isLogin = true;
										Global.userid = json.getInt("id");
										
										Intent intent = new Intent(MainActivity.this,MainActivity.class);
					    				intent.putExtra("page", "HOME");
					    				MainActivity.this.startActivity(intent);
					    				
					    				//onDestroy();
										
									}
									else {
										Looper.prepare();
										Toast.makeText(getApplicationContext(), "登录失败 : " + json.getString("mes"), Toast.LENGTH_SHORT).show();
										Looper.loop();
									}
									
								
									Log.w("Login", res);
									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    			//Handler.sendEmptyMessage(0);
				    		}
				    		}.start();
//
//				    		//String result= sendGetRequest(hostUrl+"regist", params);
				    	} catch (Exception e) {
				    		// TODO Auto-generated catch block
				    		e.printStackTrace();
				    	}
					}
				});
				break;
			case "WORKOUT":
				WorkoutActivity workoutActivity = new WorkoutActivity();
				workoutActivity.init(rootView,tf);
				break;
				
			case "CALORIE BURN":
				CalorieActivity calorieActivity = new CalorieActivity();
				calorieActivity.init(rootView,tf);
				break;
				
			case "FIND FRIENDS":
				BaiduMapActivity mapActivity = new BaiduMapActivity();
				mapActivity.init(rootView, tf);
				
				Button button = (Button) rootView.findViewById(R.id.button1);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,FriendsListActivity.class);

						MainActivity.this.startActivity(intent);
					}
				});
//				FriendsListActivity frActivity = new FriendsListActivity();
//				frActivity.init(rootView,inflater);
				
				break;
			case "PK":
				
				SinglePKLoginActivity singlePKLoginActivity = new SinglePKLoginActivity();
				singlePKLoginActivity.init(rootView, inflater,MainActivity.this);
				
				/*
				 * No login, jump to login
				 */
//				NoLoginActivity singlePKActivity = new NoLoginActivity();
//				Button button2 = (Button)rootView.findViewById(R.id.button1);
//				button2.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//						MainActivity.this.startActivity(intent);
//					}
//				});
				
			default:
				break;
			}
            
            
            getActivity().setTitle(title);
            return rootView;
        	
//        	 View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
//             int i = getArguments().getInt(ARG_PLANET_NUMBER);
//             String planet = getResources().getStringArray(R.array.planets_array)[i];
//
//             int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                             "drawable", getActivity().getPackageName());
//             ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//             getActivity().setTitle(planet);
//             return rootView;
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

    /* 连接谷歌服务失败 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
        	// 已经在尝试修复错误
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
        	// 连接错误可以自动解决
            try {
                mResolvingError = true;
                // 尝试自动解决，会启动一个 Activity，一般情况是选择用户
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (SendIntentException e) {
            	// 无法启动自动解决，直接尝试重连
                mGoogleApiClient.connect();
            }
        } else {
        	// 无法修复错误，直接冻结窗口，弹窗
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
	}

	/* 创建一个提示框用于错误提示 */
    private void showErrorDialog(int errorCode) {
    	// 创建一个错误提示框
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        
        // 显示错误
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }
    
    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* 创建 fragment 用于显示错误信息 */
    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity)getActivity()).onDialogDismissed();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }
    
	@Override
	public void onConnected(Bundle arg0) {
		/*
        Toast toast = Toast.makeText(getApplicationContext(), "连接 Google Play 成功", Toast.LENGTH_LONG);
        toast.show();
        android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            toast = Toast.makeText(getApplicationContext(),
            		String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude())
, Toast.LENGTH_LONG);
            toast.show();
        }
        
        Intent i = new Intent(this, com.linc.pedometer.service.ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecognitionPendingIntent = 
        		PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.
        	requestActivityUpdates(mGoogleApiClient, 0, mActivityRecognitionPendingIntent);
        	*/
		
        Toast toast = Toast.makeText(getApplicationContext(), "连接 Google Play 成功 : ", Toast.LENGTH_LONG);
        toast.show();
        android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
        	// tv_location.setText(String.valueOf(mLastLocation.getLatitude()) + ", " + String.valueOf(mLastLocation.getLongitude()));
        }
        
        /* 初始化 Google 运动感知 */
        // 初始状态设置未知
        
        Intent i = new Intent(this, ActivityRecognitionIntentService.class);
        PendingIntent mActivityRecognitionPendingIntent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.
        	requestActivityUpdates(mGoogleApiClient, 0, mActivityRecognitionPendingIntent);
	}


	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
