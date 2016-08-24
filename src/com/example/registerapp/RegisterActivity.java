package com.example.registerapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	
	
	//声明接口地址
//  private String url = "http://192.168.1.100/project01/register.php";
  private String url = "http://202.38.70.138/cmetTest/doUserAction.php?act=addUser";
  
  private String username;
  private String password;
  private String email;
  
  private EditText et1;
  private EditText et2;
  private EditText et3;
  private Button btn;
  
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_register);
      
      et1 = (EditText) findViewById(R.id.etUsername);
      et2 = (EditText) findViewById(R.id.etPassword);
      et3 = (EditText) findViewById(R.id.etMail);
      btn = (Button) findViewById(R.id.bRegister);
      
      btn.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View arg0) {
              
              List<NameValuePair> params = new ArrayList<NameValuePair>();
              //获取输入框中的内容
              username = et1.getText().toString();
              password = et2.getText().toString();
              email = et3.getText().toString();
              
              //替换键值对，这里的键必须和接口中post传递的键一致
//              params.add(new BasicNameValuePair("name", a));
              params.add(new BasicNameValuePair("username", username));
              params.add(new BasicNameValuePair("password", password));
              params.add(new BasicNameValuePair("email", email));
              
              JSONParser jsonParser = new JSONParser();
              
              try{   
                  JSONObject json = jsonParser.makeHttpRequest(url,"POST", params);
                  Log.i("main",json.toString());
                  if(json.getString("responseCode").equals("success")){
                	  
                	Toast.makeText(RegisterActivity.this,"注册成功！请登录",Toast.LENGTH_SHORT).show();
      				startActivity(new Intent(RegisterActivity.this, MainActivity.class));
      				Log.i("main","startActivity");
      			
                  }
//                  Log.i("main",json);
                  Log.v("uploadsucceed", "uploadsucceed");   
                
              }catch(Exception e){   
                  e.printStackTrace(); 
              }   
              
      
              
          }
      });
      
      //下面的代码是必须加上的，具体的意义还需要大家去探索吧，这里不是主要讲的
      
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
