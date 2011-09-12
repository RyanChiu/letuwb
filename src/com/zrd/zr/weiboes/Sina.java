package com.zrd.zr.weiboes;

import java.util.List;

import weibo4android.Paging;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.OAuthConstant;
import weibo4android.WeiboException;

public class Sina {
	
	private Weibo mWeibo = null;
	private User mLastUserInfo = null;
	
	public Sina() {
		mWeibo = OAuthConstant.getInstance().getWeibo();
		
		mWeibo.setOAuthConsumer(
			Weibo.CONSUMER_KEY,
			Weibo.CONSUMER_SECRET
		);
		mWeibo.setOAuthAccessToken(
			Weibo.ANFFERNEE_TOKEN, 
			Weibo.ANFFERNEE_TOKEN_SECRET
		);
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
	
	public String getContent(String uid) {
		try {
			mLastUserInfo = mWeibo.showUser(uid);
			return mLastUserInfo.toString();
		} catch (WeiboException e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	public User getLastUserInfo() {
		return mLastUserInfo;
	}
}
