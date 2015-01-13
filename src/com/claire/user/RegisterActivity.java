package com.claire.user;

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.claire.pedometer.MainActivity;
import com.claire.pedometer.R;
import com.claire.pedometer.R.id;
import com.claire.pedometer.R.layout;
import com.claire.pedometer.R.menu;
import com.linc.pedometer.global.Global;
import com.linc.pedometer.service.MD5Util;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore;

public class RegisterActivity extends Activity {
	private Button okButton;
	
	public static final int TAKE_PHOTO = 0;
	public static final int TAKE_PHOTO1 = 1;
	public static final int CROP_PHOTO = 2;
	public static final int CROP_PHOTO1 = 3;
	
	/*用来标识请求照相功能的activity*/
	private static final int CAMERA_WITH_DATA = 3023;

	/*用来标识请求gallery的activity*/
	private static final int PHOTO_PICKED_WITH_DATA = 3021;

	/*拍照的照片存储位置*/
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");

	private File mCurrentPhotoFile;//照相机拍照得到的图片
	
	Uri imageUri;
	Uri originalUri;
	
	//View rootView;
	
	ImageView pictureImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		/*
		okButton = (Button) findViewById(R.id.button1);
		okButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				RegisterActivity.this.startActivity(intent);
				
			}
		});
		*/
		pictureImage = (ImageView) findViewById(R.id.avater);
        
		pictureImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPickPhotoAction();
			}
		});
		
        //pictureImage.setOnClickListener(this);
        
        Button Btn1 = (Button)findViewById(R.id.subBtn);//获取按钮资源    
        Btn1.setOnClickListener(new Button.OnClickListener(){//创建监听    
            public void onClick(View v) {    
                //String strTmp = "点击Button01";    
                //Ev1.setText(strTmp);    
            	
            	try {
            		

            		new Thread(){
            		@Override
            		public void run(){
            		//你要执行的方法
            		//执行完毕后给handler发送一个空消息
            			Log.w("Login", "rgist");
            			
            			
            			String username = ((EditText)findViewById(R.id.usernameTxt)).getText().toString();
                    	String password = ((EditText)findViewById(R.id.passwordTxt)).getText().toString();
                    	RadioButton radioButton = (RadioButton)findViewById(((RadioGroup)findViewById(R.id.gender)).getCheckedRadioButtonId());
                    	String gender = radioButton.getText().toString();
                    	String nickname = ((EditText)findViewById(R.id.nicknameTxt)).getText().toString();
                    	
                    	Map<String, String> params = new HashMap<String, String>();
                    	params.put("username", username);
                    	params.put("password", MD5Util.MD5(password));
                    	params.put("gender", gender);
                    	params.put("nickname", nickname);
                    	Log.w("Login", "rgist1");
            			try {
							String result= sendGetRequest(Global.hostUrl+"regist", params);
							Log.w("Login", "rgist2");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			//Handler.sendEmptyMessage(0);
            		}
            		}.start();

   
            	    
            		
            		//String result= sendGetRequest(hostUrl+"regist", params);
            	} catch (Exception e) {
            		// TODO Auto-generated catch block
            		e.printStackTrace();
            	}
            	
            }    
  
        });   
		

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			return rootView;
		}
	}

    private void doPickPhotoAction() {
    	
		Context context = this;
	
		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String cancel="返回";
		String[] choices;
		choices = new String[2];
		choices[0] = "拍";  //拍照
		choices[1] = "相";  //从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
	
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("选");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:{
							String status=Environment.getExternalStorageState();
							if(status.equals(Environment.MEDIA_MOUNTED)){//判断是否有SD卡
								doTakePhoto();// 用户点击了从照相机获取
							}
							else{
								//showToast("没有SD卡");
							}
							break;
							
						}
						case 1:
							doPickPhotoFromGallery();// 从相册中去获取
							break;
						}
					}
				});
		builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
			
		});
		builder.create().show();
	}

/**
* 拍照获取图片
* 
*/
protected void doTakePhoto() {
	try {
		// Launch camera to take photo for selected contact
		PHOTO_DIR.mkdirs();// 创建照片的存储目录
		mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
		final Intent intent = getTakePickIntent(mCurrentPhotoFile);
		startActivityForResult(intent, CAMERA_WITH_DATA);
	} catch (ActivityNotFoundException e) {
		//Toast.makeText(this, R.string.photoPickerNotFoundText,
			//	Toast.LENGTH_LONG).show();
	}
}

public static Intent getTakePickIntent(File f) {
	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	return intent;
}

/**
* 用当前时间给取得的图片命名
* 
*/
private String getPhotoFileName() {
	Date date = new Date(System.currentTimeMillis());
	SimpleDateFormat dateFormat = new SimpleDateFormat(
			"'IMG'_yyyy-MM-dd HH:mm:ss");
	return dateFormat.format(date) + ".jpg";
}

// 请求Gallery程序
protected void doPickPhotoFromGallery() {
	try {
		// Launch picker to choose photo for selected contact
		final Intent intent = getPhotoPickIntent();
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	} catch (ActivityNotFoundException e) {
		//Toast.makeText(this, R.string.photoPickerNotFoundText1,
		//		Toast.LENGTH_LONG).show();
	}
}

// 封装请求Gallery的intent
public static Intent getPhotoPickIntent() {
	Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
	intent.setType("image/*");
	intent.putExtra("crop", "true");
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	intent.putExtra("outputX", 100);
	intent.putExtra("outputY", 100);
	intent.putExtra("return-data", true);
	return intent;
}

// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode != this.RESULT_OK)
		return;
	switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
			final Bitmap photo = data.getParcelableExtra("data");
			// 下面就是显示照片了
			System.out.println(photo);
			//缓存用户选择的图片
			//img = getBitmapByte(photo);
			pictureImage.setImageBitmap(photo);
			System.out.println("set new photo");
			break;
		}
		case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			doCropPhoto(mCurrentPhotoFile);
			break;
		}
	}
}

protected void doCropPhoto(File f) {
	try {
		// 启动gallery去剪辑这个照片
		final Intent intent = getCropImageIntent(Uri.fromFile(f));
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	} catch (Exception e) {
		//Toast.makeText(this, R.string.photoPickerNotFoundText,
		//		Toast.LENGTH_LONG).show();
	}
}

/**
* Constructs an intent for image cropping. 调用图片剪辑程序
*/
public static Intent getCropImageIntent(Uri photoUri) {
	Intent intent = new Intent("com.android.camera.action.CROP");
	intent.setDataAndType(photoUri, "image/*");
	intent.putExtra("crop", "true");
	intent.putExtra("aspectX", 1);
	intent.putExtra("aspectY", 1);
	intent.putExtra("outputX", 100);
	intent.putExtra("outputY", 100);
	intent.putExtra("return-data", true);
	return intent;
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

}
