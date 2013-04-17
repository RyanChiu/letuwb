package com.weibo.sdk.android.custom;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import weibo4android.WeiboException;
import weibo4android.WeiboResponse;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;

/**
 * A data class representing Basic user information element
 */
public class User2 extends WeiboResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7081539351651898753L;
	static final String[] POSSIBLE_ROOT_NAMES = new String[]{"user", "sender", "recipient", "retweeting_user"};
	private long id;                      //用户id
	private String screenName;            //微博昵称
	private String name;                  //友好显示名称，如Bill Gates(此特性暂不支持)
	private int province;                 //省份编码（参考省份编码表）
	private int city;                     //城市编码（参考城市编码表）
	private String location;              //地址
	private String description;           //个人描述
	private String url;                   //用户博客地址
	private String profileImageUrl;       //自定义图像
	private String userDomain;            //用户个性化URL
	private String gender;                //性别,m--男，f--女,n--未知
	private int followersCount;           //粉丝数
	private int friendsCount;             //关注数
	private int statusesCount;            //微博数
	private int favouritesCount;          //收藏数
	private Date createdAt;               //创建时间
	private boolean following;            //保留字段,是否已关注(此特性暂不支持)
	private boolean verified;             //加V标示，是否微博认证用户
	private boolean geoEnabled;           //地理
	private boolean allowAllActMsg;       //保留字段（暂时不支持）

	private Status2 status = null;

	public User2(JSONObject json) throws WeiboException {
		super();
		init(json);
	}

	private void init(JSONObject json) throws WeiboException {
		if(json!=null){
			try {
				id = json.getLong("id");
				name = json.getString("name");
				screenName = json.getString("screen_name");
				location = json.getString("location");
				description = json.getString("description");
				profileImageUrl = json.getString("profile_image_url");
				url = json.getString("url");
				allowAllActMsg = json.getBoolean("allow_all_act_msg");
				followersCount = json.getInt("followers_count");
				friendsCount = json.getInt("friends_count");
				createdAt = parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
				favouritesCount = json.getInt("favourites_count");
				following = getBoolean("following", json);
				verified=getBoolean("verified", json);
				statusesCount = json.getInt("statuses_count");
				userDomain = json.getString("domain");
				gender = json.getString("gender");
				province = json.getInt("province");
				city = json.getInt("city");
				if (!json.isNull("status")) {
					setStatus(new Status2(json.getJSONObject("status")));
				}
			} catch (JSONException jsone) {
				throw new WeiboException(jsone.getMessage() + ":" + json.toString(), jsone);
			}
		}
	}

	public void setStatus(Status2 status) {
		this.status = status;
	}
	
	public Status2 getStatus() {
		return status;
	}
	
	/**
	 * Returns the id of the user
	 *
	 * @return the id of the user
	 */
	public long getId() {
		return id;
	}

	/**
	 * Returns the name of the user
	 *
	 * @return the name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the screen name of the user
	 *
	 * @return the screen name of the user
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * Returns the location of the user
	 *
	 * @return the location of the user
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Returns the description of the user
	 *
	 * @return the description of the user
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the profile image url of the user
	 *
	 * @return the profile image url of the user
	 */
	public URL getProfileImageURL() {
		try {
			return new URL(profileImageUrl);
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/**
	 * Returns the url of the user
	 *
	 * @return the url of the user
	 */
	public URL getURL() {
		try {
			return new URL(url);
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/**
	 * Test if the user status is protected
	 *
	 * @return true if the user status is protected
	 */
	public boolean isAllowAllActMsg() {
		return allowAllActMsg;
	}


	public String getUserDomain() {
		return userDomain;
	}

	/**
	 * Returns the number of followers
	 *
	 * @return the number of followers
	 * @since Weibo4J 1.2.1
	 */
	public int getFollowersCount() {
		return followersCount;
	}

	/**
	 * Returns the code of province
	 *
	 * @return the code of province
	 * @since Weibo4J 1.2.1
	 */
	public int getProvince() {
		return province;
	}

	/**
	 * Returns the code of city
	 *
	 * @return the code of city
	 * @since Weibo4J 1.2.1
	 */
	public int getCity() {
		return city;
	}
	
	public int getFriendsCount() {
		return friendsCount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public String getGender() {
		return gender;
	}
	
	/**
	 *
	 * @deprecated
	 */
	public boolean isFollowing() {
		return following;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	/**
	 * @return the user is enabling geo location
	 * @since Weibo4J 1.2.1
	 */
	public boolean isGeoEnabled() {
		return geoEnabled;
	}

	/**
	 * @return returns true if the user is a verified celebrity
	 * @since Weibo4J 1.2.1
	 */
	public boolean isVerified() {
		return verified;
	}
}
