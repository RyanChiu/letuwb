package com.zrd.zr.pnj;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import com.zrd.zr.protos.WeibousersProtos.UCMappings;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ThreadPNJDealer implements Runnable {

	public static final int GET_UCMAPPINGS = 0x7330001;
	
	public static final String KEY_DATA = "data"; 
	
	private int mAction;
	private String mLink;
	private Handler mHandler;
	
	/*
	 * if parameter handler is null, or action is illegal,
	 * then the instance of the class will do nothing, nothing at all!!
	 * so, please make sure that the 2 parameters mentioned above
	 * are available.
	 */
	public ThreadPNJDealer(int action, String link, Handler handler) {
		this.mAction = action;
		this.mLink = link;
		this.mHandler = handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (mAction != GET_UCMAPPINGS) {
			return;
		}
		if (mHandler == null) return;
		Message msg = new Message();
		msg.what = mAction;
		Bundle bundle = new Bundle();
		switch (mAction) {
		case GET_UCMAPPINGS:
			SecureURL su = new SecureURL();
	    	URLConnection conn = su.getConnection(mLink);
	    	if (conn == null) {
	    		bundle.putSerializable(KEY_DATA, null);
	    	} else {
	    		try {
					conn.connect();
					InputStream is = conn.getInputStream();
					UCMappings mappings = UCMappings.parseFrom(is);
					bundle.putSerializable(KEY_DATA, mappings);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					bundle.putSerializable(KEY_DATA, null);
				}
	    	}
	    	msg.setData(bundle);
			mHandler.sendMessage(msg);
			break;
		}
	}

}
