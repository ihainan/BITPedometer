package com.claire.pedometer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.baidu.mapapi.SDKInitializer;
import com.claire.adapter.MenuAdapter;
import com.claire.friend.FriendsListActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.claire.pedometer.*;
import com.claire.pk.NoLoginActivity;
import com.claire.pk.SinglePKLoginActivity;
import com.claire.statistics.CalorieActivity;
import com.claire.statistics.MileActivity;
import com.claire.user.LoginActivity;
import com.claire.user.RegisterActivity;
import com.claire.workout.WorkoutActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;


//bcc
@SuppressLint("ValidFragment")
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private MenuAdapter mMenuAdapter;
    public String page = null;
    
    public int layoutid = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext()); 
        setContentView(R.layout.activity_main);

        
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
//				LoginActivity loginActivity = new LoginActivity();
//				loginActivity.init(rootView);
				TextView tView = (TextView) rootView.findViewById(R.id.textView1);
		        tView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						layoutid = R.layout.activity_register;
//						selectItem(layoutid,1);
						Intent intent = new Intent(MainActivity.this,RegisterActivity.class);

						MainActivity.this.startActivity(intent);				
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
}
