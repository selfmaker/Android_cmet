package com.example.registerapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


	private String url = "http://202.38.70.138/cmetTest/doUserAction.php?act=login";
	private String username;
	private String password;
	
	private EditText etUsername;
	private EditText etPassword;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);
        
        
        tvRegisterLink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, RegisterActivity.class));
			}
		});


        bLogin.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View arg0) {
                
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //��ȡ������е�����
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
  
                //�滻��ֵ�ԣ�����ļ�����ͽӿ���post���ݵļ�һ��
//                params.add(new BasicNameValuePair("name", a));
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                
                JSONParser jsonParser = new JSONParser();
                
                try{   
//                    JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
                	String jsonstring = jsonParser.makeHttpRequest(url,"POST", params);
                    JSONObject json = new JSONObject(jsonstring);
                    
                    
                    Log.i("main",json.toString());
                    if(json.getString("responseCode").equals("success")){
                  	  
                  	Toast.makeText(MainActivity.this,"登陆成功！",Toast.LENGTH_SHORT).show();
        				startActivity(new Intent(MainActivity.this, StartActivity.class));
        				Log.i("main","startActivity");
        			
                    }else{
                    	Toast.makeText(MainActivity.this,"登陆失败！请重新输入",Toast.LENGTH_SHORT).show();
                    }
                  
                }catch(Exception e){   
                    e.printStackTrace(); 
                }   
                
        
                
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
