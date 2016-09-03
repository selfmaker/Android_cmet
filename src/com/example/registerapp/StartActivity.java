package com.example.registerapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.registerapp.utils.ConstantUtil;

import android.R.string;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {
	
	private Button startBtn;
	
	private ImageView left;
	private TextView title;
	private String url = "http://202.38.70.138/cmetTest/doUserAction.php?act=startTest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        
        left=(ImageView) findViewById(R.id.left);
        title=(TextView) findViewById(R.id.title);
        startBtn=(Button) findViewById(R.id.start);
        
        left.setVisibility(View.GONE);
        title.setText("答题测试");
        
        startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				 params.add(new BasicNameValuePair("username", ConstantUtil.username));
	              params.add(new BasicNameValuePair("status", "考试中"));
	              
	              JSONParser jsonParser = new JSONParser();
	              
	              try{   
//	                  JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
	                  String jsonstring = jsonParser.makeHttpRequest(url,"POST", params);
	                  JSONObject json = new JSONObject(jsonstring);
	                  Log.i("main",json.toString());
	                  if(json.getString("responseCode").equals("success")){
	                	  
	                  }
	                
	              }catch(Exception e){   
	                  e.printStackTrace(); 
	              }   
	              
	              
				Intent intent=new Intent(StartActivity.this,AnalogyExaminationActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()    
        .detectDiskReads()    
        .detectDiskWrites()    
        .detectNetwork()   // or .detectAll() for all detectable problems    
        .penaltyLog()    
        .build());    
        
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()    
        .detectLeakedSqlLiteObjects()    
        .detectLeakedClosableObjects()    
        .penaltyLog()    
        .penaltyDeath()    
        .build()); 
        
        
        
    }


}
