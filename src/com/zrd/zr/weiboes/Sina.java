package com.zrd.zr.weiboes;

import java.util.List;

import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.Weibo;
import weibo4android.OAuthConstant;
import weibo4android.WeiboException;

public class Sina {
	
	private Weibo mWeibo = null;
	
	/*
	 * when some account of SINA_weibo actually logged in,
	 * we told every activities that they should open some
	 * rights for it.
	 */
	private boolean mLoggedIn = false;
	
	public Sina() {
		this(true);
		/*
		 * we use the following tokens for default using,
		 * but don't allow further more actions, like submit
		 * a SINA_weibo or something like that.
		 */
		mWeibo.setOAuthAccessToken(
			Weibo.ANFFERNEE_TOKEN, 
			Weibo.ANFFERNEE_TOKEN_SECRET
		);
	}
	
	public Sina(boolean noAccessToken) {
		mWeibo = OAuthConstant.getInstance().getWeibo();
		
		/*
		 * the necessary key/secret pair that needed when
		 * try to using the API of SINA_weibo
		 */
		mWeibo.setOAuthConsumer(
			Weibo.CONSUMER_KEY,
			Weibo.CONSUMER_SECRET
		);
	}
	
	public Weibo getWeibo() {
		return mWeibo;
	}

	public void setLoggedIn(boolean loggedin) {
		mLoggedIn = loggedin;
	}
	
	public boolean isLoggedIn() {
		return mLoggedIn;
	}
	
	public String getContent() {
		
		List<Status> friendsTimeline;
		try {
			friendsTimeline = mWeibo.getTrendStatus("seaeast", new Paging(1,20));
			StringBuilder stringBuilder = new StringBuilder("");
			for (Status status : friendsTimeline) {
				stringBuilder.append(
					status.getUser().getScreenName() + "说:\n"
					+ status.getText() 
					+ "\n--------------------------------------------------\n"
				);
			}
			return stringBuilder.toString();
		} catch (WeiboException e) {
			e.printStackTrace();
			return e.toString();
		}
	}
		
}
