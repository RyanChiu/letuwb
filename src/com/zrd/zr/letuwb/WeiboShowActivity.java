package com.zrd.zr.letuwb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4android.Status;
import weibo4android.User;

import com.zrd.zr.weiboes.Sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WeiboShowActivity extends Activity {
	
	private TextView mTextScreenName;
	private ImageView mImageVerified;
	private TextView mTextCreatedAt;
	private TextView mTextLocation;
	private TextView mTextDescription;
	private TextView mTextCounts;
	private ListView mListStatus;
	
	private Sina mSina;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_show);
		
		mTextScreenName = (TextView)findViewById(R.id.tvScreenName);
		mImageVerified = (ImageView)findViewById(R.id.ivVerified);
		mTextCreatedAt = (TextView)findViewById(R.id.tvCreatedAt);
		mTextLocation = (TextView)findViewById(R.id.tvLocation);
		mTextDescription = (TextView)findViewById(R.id.tvDescription);
		mTextCounts = (TextView)findViewById(R.id.tvCounts);
		mListStatus = (ListView)findViewById(R.id.lvStatus);
		
		mSina = new Sina();
				
		/*
		 * show the whole user/info
		 */
		Intent intent = getIntent();
		String uid = intent.getStringExtra("uid");
    	String content = mSina.getContent(uid);
		if (content == null) content = "No data passed.";
		
		User userInfo = mSina.getLastUserInfo();
		if (userInfo != null) {
			/*
			 * show the status info
			 */
			WeiboStatusListAdapter adapter = new WeiboStatusListAdapter(
				this,
				getStatusData()
			);
			mListStatus.setAdapter(adapter);
			
			/*
			 * show the profile-image
			 */
			AsyncImageLoader ail = new AsyncImageLoader(
				WeiboShowActivity.this,
				R.id.ivTinyProfileImage,
				R.drawable.cgpretty_small
			);
			ail.execute(userInfo.getProfileImageURL());
			
			/*
			 * show the screen name
			 */
			mTextScreenName.setText(userInfo.getScreenName());
			
			/*
			 * show "v" if verified
			 */
			if (userInfo.isVerified()) {
				mImageVerified.setVisibility(ImageView.VISIBLE);
			} else {
				mImageVerified.setVisibility(ImageView.GONE);
			}
			
			/*
			 * show when did the user be created
			 */
			Date dtCreatedAt = userInfo.getCreatedAt();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			mTextCreatedAt.setText(sdf.format(dtCreatedAt));
			
			/*
			 * show the location and the description
			 */
			mTextLocation.setText(
				userInfo.getLocation()
			);
			mTextDescription.setText(
				userInfo.getDescription()
			);
			
			/*
			 * show all kinds of the counts
			 */
			mTextCounts.setText(
				"Weibos:" + userInfo.getStatusesCount()
				+ "  Favorites:" + userInfo.getFavouritesCount()
				+ "\n"
				+ "Followers:" + userInfo.getFollowersCount()
				+ "  Friends:" + userInfo.getFriendsCount()
			);
		}
		
	}
	
	private List<Map<String, Object>> getStatusData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		User usrInfo = mSina.getLastUserInfo();
		if (usrInfo != null) {
			Status status = usrInfo.getStatus();
			if (status != null) {
				map.put("createdat", status.getCreatedAt().toString());
				map.put("text", status.getText());
				map.put("image", status.getBmiddle_pic());
				map.put("reply", "Reply:" + status.getInReplyToScreenName());
				list.add(map);
			}
		}
		return list;
	}

}
