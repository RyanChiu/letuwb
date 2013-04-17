package com.weibo.sdk.android.custom;

import java.io.Serializable;
import java.util.Date;

import weibo4android.Comment;
import weibo4android.WeiboException;
import weibo4android.WeiboResponse;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

public class Comment2 extends WeiboResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5592494565059953794L;
	private Date createdAt;                    //评论时间
	private long id;                           //评论id
	private String text;                       //评论内容
	private String source;                     //内容来源
	private boolean isTruncated;
	private long inReplyToStatusId;
	private long inReplyToUserId;
	private boolean isFavorited;               //保留字段，未弃用
	private String inReplyToScreenName;
	private double latitude = -1;              //纬度
	private double longitude = -1;             //经度
	private Comment replycomment = null;  //回复的评论内容
	private User2 user = null;
	private Status2 status = null;
	
	/* modify by hezhou add some field*/
	public Comment2(JSONObject json)throws WeiboException, JSONException{
		id = json.getLong("id");
		text = json.getString("text");
		source = json.getString("source");
		createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
		if(!json.isNull("user"))
			user = new User2(json.getJSONObject("user"));
		if(!json.isNull("status"))
			status = new Status2(json.getJSONObject("status"));	
	}
	
	/**
	 * Return the created_at
	 *
	 * @return created_at
	 * @since Weibo4J 1.2.1
	 */

	public Date getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * Returns the id of the status
	 *
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Returns the text of the status
	 *
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Returns the source
	 *
	 * @return the source
	 * @since Weibo4J 1.2.1
	 */
	public String getSource() {
		return this.source;
	}


	/**
	 * Test if the status is truncated
	 *
	 * @return true if truncated
	 * @since Weibo4J 1.2.1
	 */
	public boolean isTruncated() {
		return isTruncated;
	}

	/**
	 * Returns the in_reply_tostatus_id
	 *
	 * @return the in_reply_tostatus_id
	 * @since Weibo4J 1.2.1
	 */
	public long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	/**
	 * Returns the in_reply_user_id
	 *
	 * @return the in_reply_tostatus_id
	 * @since Weibo4J 1.2.1
	 */
	public long getInReplyToUserId() {
		return inReplyToUserId;
	}

	/**
	 * Returns the in_reply_to_screen_name
	 *
	 * @return the in_in_reply_to_screen_name
	 * @since Weibo4J 1.2.1
	 */
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	/**
	 * returns The location's latitude that this tweet refers to.
	 *
	 * @since Weibo4J 1.2.1
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * returns The location's longitude that this tweet refers to.
	 *
	 * @since Weibo4J 1.2.1
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Test if the status is favorited
	 *
	 * @return true if favorited
	 * @since Weibo4J 1.2.1
	 */
	public boolean isFavorited() {
		return isFavorited;
	}
	
	/**
	 * Return the user
	 *
	 * @return the user
	 */
	public User2 getUser() {
		return user;
	}
	
	public Status2 getStatus() {
		return status;
	}

	public Comment getReplyComment() {
		return replycomment;
	}
}
