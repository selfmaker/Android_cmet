package com.example.registerapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.registerapp.utils.HttpCallbackListener;
import com.example.registerapp.utils.HttpUtils;

public class RegisterActivity extends Activity {

	private String url = "http://202.38.70.138/cmetTest/doUserAction.php?act=addUser";
	
	private String username;
	private String password;
	private String email;

	private EditText et1;
	private EditText et2;
	private EditText et3;
	private Button btn;
	private final int SHOW_RESPONSE = 1;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				try {
					String jsonString = (String) msg.obj;
					JSONObject json = new JSONObject(jsonString);
					if (json.getString("responseCode").equals("success")) {

						Toast.makeText(RegisterActivity.this, "注册成功！",
								Toast.LENGTH_SHORT).show();
						startActivity(new Intent(RegisterActivity.this,
								MainActivity.class));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		et1 = (EditText) findViewById(R.id.etUsername);
		et2 = (EditText) findViewById(R.id.etPassword);
		et3 = (EditText) findViewById(R.id.etMail);
		btn = (Button) findViewById(R.id.bRegister);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				username = et1.getText().toString();
				password = et2.getText().toString();
				email = et3.getText().toString();

				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("email", email));
				HttpUtils.sendRequestWithHttpClient(url, params,
						new HttpCallbackListener() {

							// 此onFinish()方法在新开的线程里运行，故不能在此进行UI操作，应该用handle异步消息处理来进行UI更新
							@Override
							public void onFinsh(String response) {
								// TODO Auto-generated method stub
								Message message = new Message();
								message.what = SHOW_RESPONSE;
								message.obj = response;
								handler.sendMessage(message);

							}

							@Override
							public void onError(Exception e) {
								// TODO Auto-generated method stub
								e.printStackTrace();
							}
						});

			}
		});

	}

}
