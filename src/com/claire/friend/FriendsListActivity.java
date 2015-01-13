package com.claire.friend;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.claire.pedometer.BaiduMapActivity;
import com.claire.pedometer.MainActivity;
import com.claire.pedometer.R;
import com.claire.pedometer.BaiduMapActivity.Person;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsListActivity extends ListActivity implements OnScrollListener  {

	
	private ListView listView;  
	private int visibleLastIndex = 0;   //���Ŀ���������  
	private int visibleItemCount;       // ��ǰ���ڿɼ�������  
	private FriendAdapter adapter;  
	private View loadMoreView;  
	private Button loadMoreButton;  
	private Handler handler = new Handler(); 
	public static List<Person> PeopleAround = new ArrayList<Person>();
	HashMap map = new HashMap();

	
	public void init(View rootView,LayoutInflater inflater){
		loadMoreView = inflater.inflate(R.layout.friends_load_more, null);  
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);  

		listView = FriendsListActivity.this.getListView();  
		listView.addFooterView(loadMoreView);   
		initAdapter();  
		setListAdapter(adapter);   
		listView.setOnScrollListener(this);     
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_list);
		PeopleAround.addAll(BaiduMapActivity.PeopleAround);
		
		loadMoreView = getLayoutInflater().inflate(R.layout.friends_load_more, null);  
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);  

		listView = getListView();  
		listView.addFooterView(loadMoreView);   
		initAdapter();  
		setListAdapter(adapter);   
		listView.setOnScrollListener(this);     
	}
	
	private void initAdapter() {
		adapter = new FriendAdapter(this);
        for(int i=0;i<PeopleAround.size();i++){
        	FriendWrapperInfo wrapper = new FriendWrapperInfo();
			wrapper.setUsername(PeopleAround.get(i).name);
			wrapper.setDistance((int)PeopleAround.get(i).dis+"");
        	adapter.addFriend(wrapper);
        	
        }
    }  

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(item.getItemId() == R.id.back_to_map){
				Intent intent = new Intent(FriendsListActivity.this,MainActivity.class);
				intent.putExtra("page", "FIND FRIENDS");
				FriendsListActivity.this.startActivity(intent);
//				new AlertDialog.Builder(this).setTitle("back").setMessage("back to map!").setPositiveButton("ok", null).show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_list_menu, menu);
		return true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.visibleItemCount = visibleItemCount;  
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;  

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = adapter.getCount() - 1;    //��ݼ����һ�������  
		int lastIndex = itemsLastIndex + 1;             //���ϵײ���loadMoreView��  
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {  
		   Log.i("LOADMORE", "loading...");  
		}  

	}
	/** 
	* �����ť�¼� 
	* @param view 
	*/  
	public void loadMore(View view) {  
		loadMoreButton.setText("loading...");   //���ð�ť����loading  
		handler.postDelayed(new Runnable() {  
			@Override  
			public void run() {  
				loadData();  
				adapter.notifyDataSetChanged(); //��ݼ��仯��,֪ͨadapter  
				listView.setSelection(visibleLastIndex - visibleItemCount + 1); //����ѡ����  
				loadMoreButton.setText("load more");    //�ָ���ť����  
			}  
		}, 2000);  
	}  

	/** 
	* ģ�������� 
	 */  
	private void loadData() {  
		int count = adapter.getCount(); 
		
		for (int i = count; i < count + 10; i++) { 
			FriendWrapperInfo wrapper = new FriendWrapperInfo();
			wrapper.setUsername("hello:"+i);
			wrapper.setDistance(i+"");
			adapter.addFriend(wrapper);  
		}  
	}  


}
