package com.claire.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.claire.pedometer.*;

public class MenuAdapter extends BaseAdapter{
	private Context context;  
	   
	 public MenuAdapter(Context context) {  
	  this.context=context;  
	 }  
	 public void ChangeImg(int index,Integer dreawble)
	 {
		 images[index] = dreawble;
	 }
	 private Integer[] images = {  
			 	R.drawable.home,
			 	R.drawable.login,
			 	R.drawable.walking_mate,
			 	R.drawable.mile,
			 	R.drawable.calorie,
			 	R.drawable.friends,
			 	R.drawable.pk
			      };
	 
			   
	 private String[] texts = {  
			   "HOME", 
			   "LOGIN",
			   "WORKOUT", 
			   "SPORTS INFO", 
			   "CALORIE BURN", 
			   "FIND FRIENDS", 
			   "PK"
			   }; 
	 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;  
	}

	@Override  
	 public Object getItem(int position) {  
	  return position;  
	 }  
	 
	 //get the current selector's id number  
	 @Override  
	 public long getItemId(int position) {  
	  return position;  
	 }  

	 @Override  
	 public View getView(int position, View view, ViewGroup viewgroup) {  
		  ImgTextWrapper wrapper;  
		  if(view==null) {  
		   wrapper = new ImgTextWrapper();  
		   LayoutInflater inflater = LayoutInflater.from(context);  
		   view = inflater.inflate(R.layout.hx_m_item, null);  
		   view.setTag(wrapper);  
		   view.setPadding(15, 15, 15, 15);  //æ¯�æ ¼çš„é—´è·�  
		  } else {  
		   wrapper = (ImgTextWrapper)view.getTag();  
		  }  
	    
		  wrapper.imageView = (ImageView)view.findViewById(R.id.MainActivityImage);  
		  wrapper.imageView.setBackgroundResource(images[position]);  
		  wrapper.textView = (TextView)view.findViewById(R.id.MainActivityText);  
		  wrapper.textView.setTextColor(Color.WHITE);
		  wrapper.textView.setText(texts[position]);  
		    
		  return view;  
	 }  
}
 
