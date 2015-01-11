package com.claire.pk;

import com.claire.friend.FriendAdapter;
import com.claire.friend.FriendWrapperInfo;
import com.claire.friend.FriendsListActivity;
import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.claire.pedometer.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.os.Build;

public class SinglePKLoginActivity extends Activity implements OnScrollListener {

	
	private ListView listView;  
	private int visibleLastIndex = 0;   //���Ŀ���������  
	private int visibleItemCount;       // ��ǰ���ڿɼ�������  
	private UserPKAdapter adapter;  
	private View loadMoreView;  
	private Button loadMoreButton;  
	private Handler handler = new Handler(); 

	
	public void init(View rootView,LayoutInflater inflater,Context context){
		loadMoreView = inflater.inflate(R.layout.friends_load_more, null);  
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
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
		});

//		listView = SinglePKLoginActivity.this.getListView();  
		listView = (ListView) rootView.findViewById(R.id.friends_list);
		listView.addFooterView(loadMoreView);
		initAdapter(context,inflater);  
		listView.setAdapter(adapter);   
		listView.setOnScrollListener(this);     
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_pklogin);
		loadMoreView = getLayoutInflater().inflate(R.layout.friends_load_more, null);  
		loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);  

		listView = (ListView) this.findViewById(R.id.friends_list);
//		listView = getListView();  
		listView.addFooterView(loadMoreView);   
		initAdapter();  
		listView.setAdapter(adapter);   
		listView.setOnScrollListener(this);     
	}
	
	private void initAdapter() {
		adapter = new UserPKAdapter(this);
        for(int i=0;i<10;i++){
        	friendInfo wrapper = new friendInfo();
			wrapper.setUsername("hello:"+i);
			wrapper.setDistance(i+"");
        	adapter.addFriend(wrapper);
        }
    }
	
	private void initAdapter(Context context,LayoutInflater inflater) {
		adapter = new UserPKAdapter(context,inflater);
        for(int i=0;i<10;i++){
        	friendInfo wrapper = new friendInfo();
			wrapper.setUsername("hello:"+i);
			wrapper.setDistance(i+"");
        	adapter.addFriend(wrapper);
        }
    }
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		
			case R.id.back_to_map:
				new AlertDialog.Builder(this).setTitle("back to").setMessage("back to map!").setPositiveButton("ȷ��", null).show();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.friends_list_menu, menu);
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
			friendInfo wrapper = new friendInfo();
			wrapper.setUsername("hello:"+i);
			wrapper.setDistance(i+"");
			adapter.addFriend(wrapper);  
		}  
	}  


	
}
