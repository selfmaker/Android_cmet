package com.example.registerapp;

import android.app.Application;

public class MyApplication extends Application {

	 private String username ;     
	    public String getUsername(){ 
	        return username; 
	    }    
	    public void setUsername(String s){ 
	        this.username = s; 
	    } 

	    @Override 
	    public void onCreate() { 
	        // TODO Auto-generated method stub 
	        super.onCreate(); 
	        setUsername("admin"); //初始化全局变量        
	    }    
	
}
