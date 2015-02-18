package com.interparalle.Util;

import android.app.Application;

public class ContextUtil extends Application {
	
	private static ContextUtil instance;
	
	public static ContextUtil getContext() {
        return instance;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance  = this;
	}

}
