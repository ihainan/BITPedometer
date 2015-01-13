package com.claire.workout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.claire.pedometer.R.menu;
import com.linc.pedometer.global.Global;
import com.linc.sina.Constants;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboShareException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class WorkoutActivity extends Activity  implements  IWeiboHandler.Response{

	ImageView sina;
	ImageView pre;
	ImageView next;
	TextView info_date;
	TextView info_step;
	TextView info_distance;
	TextView info_calories;
	int count = 10;
	Date date;
	JSONArray data;
	
	View rootView;
	
    private IWeiboShareAPI  mWeiboShareAPI = null;
	

	public String getDateString(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,E");
		String result = format.format(date);
		return result;
	}
	

	
	public Date getDateFromString(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,E");
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public Date getDate(Date date,int change){
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(date); 
		calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+change);
		return calendar.getTime();
	}
	
	public void init(final View rootView,Typeface tf) {
		this.rootView = rootView;

		if(!Global.isLogin)
			return;
		
		date = new Date();
		pre = (ImageView) rootView.findViewById(R.id.info_pre);
		next = (ImageView) rootView.findViewById(R.id.info_next);
		sina = (ImageView) rootView.findViewById(R.id.share);
		info_date = (TextView) rootView.findViewById(R.id.info_date);
		info_step = (TextView) rootView.findViewById(R.id.info_step);
		info_distance = (TextView) rootView.findViewById(R.id.info_distance);
		info_calories = (TextView) rootView.findViewById(R.id.info_calories);
		
		info_date.setText(this.getDateString(new Date()));
		info_date.setTypeface(tf);
		info_date.refreshDrawableState();
	
		
		data = Global.historydata;
		setdata();
		
		sina.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if (R.id.share == v.getId()) {
			            try {
			            	
			                // 检查微博客户端环境是否正常，如果未安装微博，弹出对话框询问用户下载微博客户端
			                if (mWeiboShareAPI.checkEnvironment(true)) {    
			                	System.out.println("click");
			                    sendMessage(false, 
			                    		false, 
			                    		false,
			                    		false, 
			                    		false, 
			                    		false);
			                }
			            } catch (WeiboShareException e) {
			                e.printStackTrace();
			               Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			            }
			        }
			}
		
		});
		pre.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				// TODO Auto-generated method stub
				//String datestr = info_date.getText().toString(); 
				//Date nowdate =  getDateFromString(date);
				date = getDate(date, -1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(predate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(date));
				setdata();
				info_date.refreshDrawableState();
			}
			
		});
		
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//String date = info_date.getText().toString();
				//Date nowdate =  getDateFromString(date);
				date = getDate(date,1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(nextdate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(date));
				setdata();
				info_date.refreshDrawableState();
			}
			
		});
		
		
		  // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(rootView.getContext(), Constants.APP_KEY);
        
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();
        
   
	}
	
	public void setdata() {
		if(data == null) {
			Log.w("Login", "data is null");
			return;
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String datestr = df.format(date);
		
		for(int i = 0; i < data.length(); ++i) {
			JSONObject jo = (JSONObject)data.opt(i);
			try {
				Log.w("Login", jo.getString("date") + " " + datestr);
				if(jo.getString("date").equals(datestr)) {
					info_step.setText("TOTAL STEP:" + jo.getInt("walk_step"));
					info_distance.setText( "Distance:\n" + String.format("%.2f", jo.getDouble("walk_distance")) + "m" );
					info_calories.setText( "Calories:\n" + String.format("%.2f", jo.getDouble("walk_calory")) + "Cal");
					return;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		info_step.setText("TOTAL STEP:0");
		info_distance.setText( "Distance:\n0.00m" );
		info_calories.setText( "Calories:\n0.00Cal");
		return;
		
		
		
	}
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout);

		pre = (ImageView) this.findViewById(R.id.info_pre);
		next = (ImageView) this.findViewById(R.id.info_next);
		info_date = (TextView) this.findViewById(R.id.info_date);
		info_step = (TextView) this.findViewById(R.id.info_step);
		info_distance = (TextView) this.findViewById(R.id.info_distance);
		info_calories = (TextView) this.findViewById(R.id.info_calories);
		
		info_date.setText(this.getDateString(new Date()));
		info_date.refreshDrawableState();
		pre.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				// TODO Auto-generated method stub
				String date = info_date.getText().toString(); 
				Date nowdate =  getDateFromString(date);
				Date predate = getDate(nowdate,-1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(predate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(predate));
				info_date.refreshDrawableState();
			}
			
		});
		
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String date = info_date.getText().toString();
				Date nowdate =  getDateFromString(date);
				Date nextdate = getDate(nowdate,1);
//				new  AlertDialog.Builder(MainActivity.this).setTitle("标题" ).setMessage(date+":"+getDateString(nowdate)+":"+getDateString(nextdate)).setPositiveButton("确定" ,  null ).show();
				info_date.setText(getDateString(nextdate));
				info_date.refreshDrawableState();
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     * 
     * @param baseRequest 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
	@Override
	public void onResponse(BaseResponse  baseResp) {
		// TODO Auto-generated method stub
		  switch (baseResp.errCode) {
	        case WBConstants.ErrorCode.ERR_OK:
	            Toast.makeText(rootView.getContext(), "success", Toast.LENGTH_LONG).show();
	            break;
	        case WBConstants.ErrorCode.ERR_CANCEL:
	            Toast.makeText(rootView.getContext(), "canceled", Toast.LENGTH_LONG).show();
	            break;
	        case WBConstants.ErrorCode.ERR_FAIL:
	            Toast.makeText(rootView.getContext(), 
	                    "failed" + "Error Message: " + baseResp.errMsg, 
	                    Toast.LENGTH_LONG).show();
	            break;
	        }
		
	}
	/**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
     */
    private void sendMessage(boolean hasText, boolean hasImage, 
			boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
            } else {
                sendSingleMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
            }
        } else {
            Toast.makeText(rootView.getContext(), "weibosdk_demo_not_support_api_hint", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        
    	System.out.println("send mutilpe");
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

            weiboMessage.textObject = getTextObj();
        
        

            weiboMessage.imageObject = getImageObj();
        
        
        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }
        
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private void sendSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {
    	System.out.println("send single");
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj();
        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(request);
    }

    /**
     * 获取分享的文本模板。
     * 
     * @return 分享的文本模板
     */
    private String getSharedText() {
    	return "今天共跑1000米，消耗卡路里80 ";
    }

    /**
     * 创建文本消息对象。
     * 
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * 
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
    	
        ImageObject imageObject = new ImageObject();
       // BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources().openRawResource(R.drawable.ic_launcher));  
        imageObject.setImageObject(screenShoot()  );
        return imageObject;

    }
    
    public  Bitmap screenShoot() { 
        // 获取窗口的顶层视图对象 
        View v = rootView;
        v.setDrawingCacheEnabled(true); 
        v.buildDrawingCache(); 

        // 第一步:获取保存屏幕图像的Bitmap对象 

        Bitmap srcBitmap = v.getDrawingCache(); 

        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, 
                srcBitmap.getWidth(), srcBitmap.getHeight()); 

        v.destroyDrawingCache(); 
        return bitmap;

        
    }

    
    

    /**
     * 创建多媒体（网页）消息对象。
     * 
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
    	/*
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = mShareWebPageView.getTitle();
        mediaObject.description = mShareWebPageView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        mediaObject.setThumbImage(mShareWebPageView.getThumbBitmap());
        mediaObject.actionUrl = mShareWebPageView.getShareUrl();
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;*/
    	return null;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     * 
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj() {
    	/*
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = mShareMusicView.getTitle();
        musicObject.description = mShareMusicView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        musicObject.setThumbImage(mShareMusicView.getThumbBitmap());
        musicObject.actionUrl = mShareMusicView.getShareUrl();
        musicObject.dataUrl = "www.weibo.com";
        musicObject.dataHdUrl = "www.weibo.com";
        musicObject.duration = 10;
        musicObject.defaultText = "Music 默认文案";
        return musicObject;*/
       	return null;
    }

    /**
     * 创建多媒体（视频）消息对象。
     * 
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
    	/*
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = mShareVideoView.getTitle();
        videoObject.description = mShareVideoView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        videoObject.setThumbImage(mShareVideoView.getThumbBitmap());
        videoObject.actionUrl = mShareVideoView.getShareUrl();
        videoObject.dataUrl = "www.weibo.com";
        videoObject.dataHdUrl = "www.weibo.com";
        videoObject.duration = 10;
        videoObject.defaultText = "Vedio 默认文案";
        return videoObject;*/
       	return null;
    }

    /**
     * 创建多媒体（音频）消息对象。
     * 
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj() {
    	/*
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = mShareVoiceView.getTitle();
        voiceObject.description = mShareVoiceView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        voiceObject.setThumbImage(mShareVoiceView.getThumbBitmap());
        voiceObject.actionUrl = mShareVoiceView.getShareUrl();
        voiceObject.dataUrl = "www.weibo.com";
        voiceObject.dataHdUrl = "www.weibo.com";
        voiceObject.duration = 10;
        voiceObject.defaultText = "Voice 默认文案";
        return voiceObject;*/
       	return null;
    }
    /**
     * @see {@link Activity#onNewIntent}
     */	
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

	

}
