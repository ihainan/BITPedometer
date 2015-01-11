package com.claire.pk;

import java.util.ArrayList;
import java.util.List;

import com.claire.friend.FriendsListActivity;
import com.claire.pedometer.LeitaiActivity;
import com.claire.pedometer.MainActivity;
import com.claire.pedometer.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserPKAdapter extends BaseAdapter{
	
	private Context context = null;
	private LayoutInflater inflater = null;
	private List<friendInfo> friends = new ArrayList<friendInfo>();

	public UserPKAdapter(Context context){
		this.context=context; 
	}
	
	public UserPKAdapter(Context context,LayoutInflater inflater){
		this.context = context;
		this.inflater = inflater;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		userFriendWarpper wrapper = null;
		friendInfo info = friends.get(position);
		if(view==null) { 
		   wrapper = new userFriendWarpper();
		   if(null == inflater)
			   inflater = LayoutInflater.from(context);  
		   view = inflater.inflate(R.layout.user_friends_item, null); 
		   view.setTag(wrapper);
		   view.setPadding(15, 15, 15, 15);  
		}else{
			wrapper = (userFriendWarpper)view.getTag();
		}
		wrapper.avatar = (ImageView)view.findViewById(R.id.user_avatar);;  
		wrapper.username = (TextView)view.findViewById(R.id.user_username);;
		wrapper.distance = (TextView)view.findViewById(R.id.user_distance);;
		wrapper.pk = (Button)view.findViewById(R.id.user_pk);;
		wrapper.avatar.setBackgroundResource(R.drawable.ic_launcher);
		wrapper.username.setTextColor(Color.BLACK);
		wrapper.distance.setTextColor(Color.BLACK);
		wrapper.username.setText(info.getUsername());
		wrapper.distance.setText(info.getDistance());
		wrapper.pk.setTag(position+"");
		wrapper.pk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				TextView text = (TextView)v.findViewById(R.id.friends_username);
				int index =  Integer.parseInt(v.getTag().toString());
				Intent intent = new Intent(context,LeitaiActivity.class);

				context.startActivity(intent);
//				new AlertDialog.Builder(context).setTitle("标题").setMessage(friends.get(index).getUsername()).setPositiveButton("确定", null).show();
			}
			
		});
		return view;
	}
	
	public void addFriend(friendInfo friend){
		friends.add(friend);
	}

}
