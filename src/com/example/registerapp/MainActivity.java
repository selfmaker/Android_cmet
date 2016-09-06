package com.example.registerapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.registerapp.utils.ConstantUtil;
import com.example.registerapp.utils.HttpCallbackListener;
import com.example.registerapp.utils.HttpUtils;

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

	private final int SHOW_RESPONSE = 1;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				try {
					String jsonString = (String) msg.obj;
					JSONObject json = new JSONObject(jsonString);
					Log.i("main", json.toString());
					if (json.getString("responseCode").equals("success")) {
						ConstantUtil.username = username;
						Toast.makeText(MainActivity.this, "登陆成功！",
								Toast.LENGTH_SHORT).show();
						startActivity(new Intent(MainActivity.this,
								StartActivity.class));
						Log.i("main", "startActivity");

					} else {
						Toast.makeText(MainActivity.this, "登陆失败！请重新输入",
								Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
		final Button bLogin = (Button) findViewById(R.id.bSignIn);

		tvRegisterLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						RegisterActivity.class));
			}
		});

		bLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				username = etUsername.getText().toString();
				password = etPassword.getText().toString();

				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));

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
