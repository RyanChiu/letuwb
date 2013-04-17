package com.weibo.sdk.android.custom;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import weibo4android.WeiboException;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

public class Status2 implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3399091219601404180L;

	private static Map<String,SimpleDateFormat> formatMap = new HashMap<String,SimpleDateFormat>();
	
	private User2 user = null;
	
	private Date createdAt;             //status创建时间
	private long id;                    //status id
	private String text;                //微博内容
	private String source;              //微博来源
	private boolean isTruncated;        //保留字段
	private long inReplyToStatusId;
	private long inReplyToUserId;
	private boolean isFavorited;        //保留字段，未弃用
	private String inReplyToScreenName;
	private double latitude = -1;       //纬度
	private double longitude = -1;      //经度
	private String thumbnail_pic;       //微博内容中的图片的缩略地址
	private String bmiddle_pic;         //中型图片
	private String original_pic;        //原始图片
	private Status2 retweeted_status;    //转发的微博内容
	private String mid;                 //mid
	
	public Status2(JSONObject json) throws WeiboException {
		constructJson(json);
	}
	
	private void constructJson(JSONObject json) throws WeiboException {
		try {
			id = json.getLong("id");
			text = json.getString("text");
			source = json.getString("source");
			createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
			inReplyToStatusId = getLong("in_reply_to_status_id", json);
			inReplyToUserId = getLong("in_reply_to_user_id", json);
			isFavorited = getBoolean("favorited", json);
			thumbnail_pic = json.getString("thumbnail_pic");
			bmiddle_pic = json.getString("bmiddle_pic");
			original_pic = json.getString("original_pic");
			if(!json.isNull("user"))
				user = new User2(json.getJSONObject("user"));
			inReplyToScreenName=json.getString("inReplyToScreenName");
			if(!json.isNull("retweeted_status")){
				retweeted_status= new Status2(json.getJSONObject("retweeted_status"));
			}
			
			mid=json.getString("mid");
			String geo= json.getString("geo");
			if(geo!=null &&!"".equals(geo) &&!"null".equals(geo)){
				getGeoInfo(geo);
			}
		} catch (JSONException je) {
			throw new WeiboException(je.getMessage() + ":" + json.toString(), je);
		}
	}
	
	private void getGeoInfo(String geo) {
		StringBuffer value= new StringBuffer();
		for(char c:geo.toCharArray()){
			if(c>45&&c<58){
				value.append(c);
			}
			if(c==44){
				if(value.length()>0){
					latitude=Double.parseDouble(value.toString());
					value.delete(0, value.length());
				}
			}
		}
		longitude=Double.parseDouble(value.toString());
	}
	
	private static Date parseDate(String str, String format) throws WeiboException{
        if(str==null||"".equals(str)){
        	return null;
        }
    	SimpleDateFormat sdf = formatMap.get(format);
        if (null == sdf) {
            sdf = new SimpleDateFormat(format, Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            formatMap.put(format, sdf);
        }
        try {
            synchronized(sdf){
                // SimpleDateFormat is not thread safe
                return sdf.parse(str);
            }
        } catch (ParseException pe) {
            throw new WeiboException("Unexpected format(" + str + ") returned from sina.com.cn");
        }
    }
	
	private static long getLong(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if(null == str || "".equals(str)||"null".equals(str)){
            return -1;
        }
        return Long.parseLong(str);
    }
	
	private static boolean getBoolean(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if(null == str || "".equals(str)||"null".equals(str)){
            return false;
        }
        return Boolean.valueOf(str);
    }
	
	/**
	 * Return the created_at
	 *
	 * @return created_at
	 * @since Weibo4J 1.1.0
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

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}


	/**
	 * Return the user
	 *
	 * @return the user
	 */
	public User2 getUser() {
		return user;
	}

	/**
	 *
	 * @since Weibo4J 1.2.1
	 */
	public boolean isRetweet(){
		return null != retweeted_status;
	}

	public Status2 getRetweeted_status() {
		return retweeted_status;
	}

	public String getMid() {
		return mid;
	}
}
