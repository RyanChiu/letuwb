package com.zrd.zr.letuwb;

import java.util.Date;

public class WeibouserInfo {
	public Integer id;
	public Integer uid;
	public String screen_name;
	public String name;
	public Integer province;
	public Integer city;
	public String location;
	public String description;
	public String url;
	public String profile_image_url;
	public String domain;
	public String gender;
	public Integer followers_count;
	public Integer friends_count;
	public Integer statuses_count;
	public Integer favourites_count;
	public String created_at;
	public Integer following;
	public Integer allow_all_act_msg;
	public Integer geo_enabled;
	public Integer verified;
	public Integer status_id;
	public Integer clicks;
	public Integer likes;
	public Integer dislikes;
	
	/*
	 * -1 means dislike voted,
	 * 1 means like voted,
	 * 0 means never voted. 
	 */
	public Integer mLastVote = 0;
	public Date mLastVoteTime = null;
	
	public WeibouserInfo() {
		this.id = 0;
		this.uid = 0;
		this.screen_name = "";
		this.name = "";
		this.province = -1;
		this.city = -1;
		this.location = "";
		this.description = "";
		this.url = "";
		this.profile_image_url = "";
		this.domain = "";
		this.gender = "";
		this.followers_count = 0;
		this.friends_count = 0;
		this.statuses_count = 0;
		this.favourites_count = 0;
		this.created_at = "";
		this.following = -1;
		this.allow_all_act_msg = -1;
		this.geo_enabled = -1;
		this.verified = -1;
		this.status_id = 0;
		this.clicks = 0;
		this.likes = 0;
		this.dislikes = 0;
	}
	
	public WeibouserInfo(WeibouserInfo pi) {
		this.id = pi.id;
		this.uid = pi.uid;
		this.screen_name = pi.screen_name;
		this.name = pi.name;
		this.province = pi.province;
		this.city = pi.city;
		this.location = pi.location;
		this.description = pi.description;
		this.url = pi.url;
		this.profile_image_url = pi.profile_image_url;
		this.domain = pi.domain;
		this.gender = pi.gender;
		this.followers_count = pi.followers_count;
		this.friends_count = pi.friends_count;
		this.statuses_count = pi.statuses_count;
		this.favourites_count = pi.favourites_count;
		this.created_at = pi.created_at;
		this.following = pi.following;
		this.allow_all_act_msg = pi.allow_all_act_msg;
		this.geo_enabled = pi.geo_enabled;
		this.verified = pi.verified;
		this.status_id = pi.status_id;
		this.clicks = 0;
		this.likes = 0;
		this.dislikes = 0;
	}
	
	public WeibouserInfo(Integer id, Integer uid, String screen_name, String name,
			Integer province, Integer city, String location, String description,
			String url, String profile_image_url, String domain, String gender,
			Integer followers_count, Integer friends_count, Integer statuses_count,
			Integer favourites_count, String created_at, Integer following,
			Integer allow_all_act_msg, Integer geo_enabled, Integer verified,
			Integer status_id, Integer clicks, Integer likes, Integer dislikes) {
		super();
		this.id = id;
		this.uid = uid;
		this.screen_name = screen_name;
		this.name = name;
		this.province = province;
		this.city = city;
		this.location = location;
		this.description = description;
		this.url = url;
		this.profile_image_url = profile_image_url;
		this.domain = domain;
		this.gender = gender;
		this.followers_count = followers_count;
		this.friends_count = friends_count;
		this.statuses_count = statuses_count;
		this.favourites_count = favourites_count;
		this.created_at = created_at;
		this.following = following;
		this.allow_all_act_msg = allow_all_act_msg;
		this.geo_enabled = geo_enabled;
		this.verified = verified;
		this.status_id = status_id;
		this.clicks = clicks;
		this.likes = likes;
		this.dislikes = dislikes;
	}
}
