package com.zrd.zr.weiboes;

import java.util.ArrayList;
import java.util.List;

import weibo4android.Status;
import weibo4android.User;
import weibo4android.WeiboException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ThreadSinaDealer implements Runnable {
	
	public static final int SHOW_USER = 0x7320001;
	public static final int GET_USER_TIMELINE = 0x7320002;
	public static final int CREATE_FRIENDSHIP = 0x7320003;
	public static final String KEY_DATA = "data";
	public static final String KEY_SINA = "sina";
	
	private Sina mSina = null;
	private int mAction;
	private String[] mParams = null;
	private Handler mHandler = null;
	
	public ThreadSinaDealer(Sina sina, int action, String[] params, Handler handler) {
		this.mSina = sina;
		this.mAction = action;
		this.mParams = params;
		this.mHandler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (mSina == null) {
			mSina = new Sina();
		}
		if (mAction != SHOW_USER
			&& mAction != GET_USER_TIMELINE
			&& mAction != CREATE_FRIENDSHIP)
			return;
		if (mHandler == null) return;
		
		Message msg = new Message();
		msg.what = mAction;
		User user;
		ArrayList<Status> statuses = new ArrayList<Status>();
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY_SINA, mSina);
		switch (mAction) {
		case SHOW_USER:
			if (mParams != null && mParams.length != 1 && mParams[0] != null) return;
			try {
				user = mSina.getWeibo().showUser(mParams[0]);
				bundle.putSerializable(KEY_DATA, user);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GET_USER_TIMELINE:
			if (mParams != null && mParams.length != 1 && mParams[0] != null) return;
			try {
				List<Status> list = mSina.getWeibo().getUserTimeline(mParams[0]);
				for (int i = 0; i < list.size(); i++) {
					statuses.add(list.get(i));
				}
				bundle.putSerializable(KEY_DATA, statuses);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case CREATE_FRIENDSHIP:
			if (mParams != null && mParams.length != 1 && mParams[0] != null) return;
			if (!mSina.isLoggedIn()) {
				return;
			}
			try {
				if (mSina.getWeibo().existsFriendship("" + mSina.getLoggedInUser().getId(), mParams[0])) {
					bundle.putSerializable(KEY_DATA, mSina.getLoggedInUser());
				} else {
					bundle.putSerializable(KEY_DATA, mSina.getWeibo().createFriendship(mParams[0]));
				}
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

}
