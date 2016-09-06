package com.example.registerapp.utils;

public interface HttpCallbackListener {

	void onFinsh(String response);
	void onError(Exception e);
}
