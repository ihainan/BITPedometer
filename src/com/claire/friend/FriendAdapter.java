package com.claire.friend;

import java.util.ArrayList;
import java.util.List;

import com.claire.adapter.ImgTextWrapper;
import com.claire.pedometer.LeitaiActivity;
import com.claire.pedometer.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter{

	private Context context = null;
	private LayoutInflater inflater = null;
	private List<FriendWrapperInfo> friends = new ArrayList<FriendWrapperInfo>();
	public FriendAdapter(Context context) {  
		  this.context=context; 
		  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		FriendWrapper wrapper = null;
		FriendWrapperInfo info = friends.get(position);
		if(view==null) { 
		   wrapper = new FriendWrapper();
		   LayoutInflater inflater = LayoutInflater.from(context);  
		   view = inflater.inflate(R.layout.friends_item, null); 
		   view.setTag(wrapper);
		   view.setPadding(15, 15, 15, 15);  
		}else{
			wrapper = (FriendWrapper)view.getTag();
		}
		wrapper.avatar = (ImageView)view.findViewById(R.id.friends_avatar);;  
		wrapper.username = (TextView)view.findViewById(R.id.friends_username);;
		wrapper.distance = (TextView)view.findViewById(R.id.friends_distance);;
		wrapper.pk = (Button)view.findViewById(R.id.friends_pk);;
		wrapper.addfriend = (Button)view.findViewById(R.id.friends_add);;
		wrapper.avatar.setBackgroundResource(R.drawable.ic_launcher);
		wrapper.username.setTextColor(Color.BLACK);
		wrapper.distance.setTextColor(Color.BLACK);
		wrapper.username.setText(info.getUsername());
		wrapper.distance.setText(info.getDistance());
		wrapper.pk.setTag(position+"");
		wrapper.addfriend.setTag(position+"");
		wrapper.pk.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				TextView text = (TextView)v.findViewById(R.id.friends_username);
				int index =  Integer.parseInt(v.getTag().toString());
				Intent intent = new Intent(context,LeitaiActivity.class);
				intent.putExtra("id", index);

				context.startActivity(intent);
//				new AlertDialog.Builder(context).setTitle("标题").setMessage(friends.get(index).getUsername()).setPositiveButton("确定", null).show();
			}
			
		});
		wrapper.addfriend.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				TextView text = (TextView)v.findViewById(R.id.friends_username);
				int index =  Integer.parseInt(v.getTag().toString());
				new AlertDialog.Builder(context).setTitle("标题").setMessage(friends.get(index).getUsername()+"add success!").setPositiveButton("确定", null).show();
			}
			
		});
		return view;
	}
	
	public void addFriend(FriendWrapperInfo friend){
		friends.add(friend);
	}

}
