package com.zrd.zr.letuwb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4android.Status;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;

import com.zrd.zr.weiboes.Sina;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
	
	private static Sina mSina = null;
	private static User mLastUser = null;
	
	public enum Action {
		SHOW_USER,
		SHOW_
	}

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
						
		/*
		 * show the whole user/info
		 */
		Intent intent = getIntent();
		String uid = intent.getStringExtra("uid");
    	
		AsyncWeiboLoader loader = new AsyncWeiboLoader();
		loader.execute(Action.SHOW_USER, uid);
	}
	
	private List<Map<String, Object>> getStatusData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (mLastUser != null) {
			Status status = mLastUser.getStatus();
			if (status != null) {
				Date dt = status.getCreatedAt();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				map.put("createdat", sdf.format(dt));
				map.put("text", status.getText());
				map.put("image", status.getBmiddle_pic());
				map.put("reply", "Reply:" + status.getInReplyToScreenName());
				list.add(map);
			}
		}
		return list;
	}
	
	private void dealWithFailed() {
		mTextCreatedAt.setText("N/A");
		mTextDescription.setText("Please try again...");
	}

	/*
	 * AsyncTask<Params, Progress, Result>
	 */
	private class AsyncWeiboLoader extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			if (result == null) {
				dealWithFailed();
				return;
			}
			Object[] res = (Object[])result;
			Action action = (Action)res[0];
			switch (action) {
			case SHOW_USER:
				mLastUser = (User)res[1];
				if (mLastUser != null) {
					/*
					 * show the status info
					 */
					WeiboStatusListAdapter adapter = new WeiboStatusListAdapter(
						WeiboShowActivity.this,
						getStatusData()
					);
					mListStatus.setAdapter(adapter);
					
					/*
					 * show the profile-image
					 */
					AsyncImageLoader ail = new AsyncImageLoader(
						WeiboShowActivity.this,
						R.id.ivTinyProfileImage,
						R.drawable.person
					);
					ail.execute(mLastUser.getProfileImageURL());
					
					/*
					 * show the screen name
					 */
					mTextScreenName.setText(mLastUser.getScreenName());
					
					/*
					 * show "v" if verified
					 */
					if (mLastUser.isVerified()) {
						mImageVerified.setVisibility(ImageView.VISIBLE);
					} else {
						mImageVerified.setVisibility(ImageView.GONE);
					}
					
					/*
					 * show when did the user be created
					 */
					Date dtCreatedAt = mLastUser.getCreatedAt();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					mTextCreatedAt.setText(sdf.format(dtCreatedAt));
					
					/*
					 * show the location and the description
					 */
					mTextLocation.setText(
						mLastUser.getLocation()
					);
					mTextDescription.setText(
						mLastUser.getDescription()
					);
					
					/*
					 * show all kinds of the counts
					 */
					mTextCounts.setText(
						"Weibos:" + mLastUser.getStatusesCount()
						+ "  Favorites:" + mLastUser.getFavouritesCount()
						+ "\n"
						+ "Followers:" + mLastUser.getFollowersCount()
						+ "  Friends:" + mLastUser.getFriendsCount()
					);
				} else {
					dealWithFailed();
				}
				break;
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			if (mSina == null) {
				mSina = new Sina();
			}
			if (params.length < 1) return null;
			Action action = (Action) params[0];
			Object[] res = new Object[2];
			res[0] = action;
			switch (action) {
			case SHOW_USER:
				if (params.length != 2) return null;
				String uid = (String)params[1];
				Weibo weibo = mSina.getWeibo();
				if (weibo != null) {
					try {
						res[1] = weibo.showUser(uid);
						return res;
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				}
				break;
			}
			
			return null;
		}
		
	}
}
