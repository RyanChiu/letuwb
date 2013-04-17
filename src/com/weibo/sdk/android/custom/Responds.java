package com.weibo.sdk.android.custom;

import java.io.IOException;
import java.io.Serializable;

import com.weibo.sdk.android.WeiboException;

public class Responds implements Serializable {

	/**
	 * Auto generated serial version uid
	 */
	private static final long serialVersionUID = -6227316711224039209L;
	public static final int STATUSES_UPDATE = 0x20130300;
	public static final int STATUSES_FRIENDSTIMELINE = 0X20130301;
	public static final int USERS_SHOW = 0x20130302;
	public static final int STATUSES_USERTIMELINE = 0x20130303;
	public static final int FRIENDSHIPS_CREATE = 0x20130304;
	public static final int FAVORITES_CREATE = 0x20130305;
	public static final int COMMENTS_CREATE = 0x20130306;
	public static final int STATUSES_REPOST = 0x20130307;
	public static final int COMMENTS_SHOW = 0x20130308;
	public static final String KEY_DATA = "weibo_request_respond_data";
	public static final int TYPE_COMPLETE = 0;
	public static final int TYPE_ERROR = 1;
	public static final int TYPE_IO_ERROR= 2;
	private int mMsgWhat;
	private String mRespOnComplete = null;
	private WeiboException mRespOnError = null;
	private IOException mRespOnIOError = null;
	
	public Responds (int what) {
		this.mMsgWhat = what;
	}
	
	public int getMsgWhat() {
		return mMsgWhat;
	}

	public String getRespOnComplete() {
		return mRespOnComplete;
	}

	public void setRespOnComplete(String mRespOnComplete) {
		this.mRespOnComplete = mRespOnComplete;
		mRespOnError = null;
		mRespOnIOError = null;
	}

	public WeiboException getRespOnError() {
		return mRespOnError;
	}

	public void setRespOnError(WeiboException mRespOnError) {
		this.mRespOnError = mRespOnError;
		mRespOnComplete = null;
		mRespOnIOError = null;
	}

	public IOException getRespOnIOError() {
		return mRespOnIOError;
	}

	public void setRespOnIOError(IOException mRespOnIOError) {
		this.mRespOnIOError = mRespOnIOError;
		mRespOnComplete = null;
		mRespOnError = null;
	}
	
	public int getRespType() {
		if (mRespOnComplete != null && mRespOnError == null && mRespOnIOError == null) return TYPE_COMPLETE;
		else if (mRespOnComplete == null && mRespOnError != null && mRespOnIOError == null) return TYPE_ERROR;
		else if (mRespOnComplete == null && mRespOnError == null && mRespOnIOError != null) return TYPE_IO_ERROR;
		else return -1;
	}
}
