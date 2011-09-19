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
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WeiboShowActivity extends Activity {
	
	private TextView mTextScreenName;
	private ImageView mImageVerified;
	private TextView mTextCreatedAt;
	private TextView mTextLocation;
	private TextView mTextDescription;
	private TextView mTextCounts;
	private ListView mListStatus;
	private ProgressBar mProgressStatusLoading;
	private LinearLayout mLayoutStatusCtrls;
	private Button mBtnWeibos;
	
	private static Sina mSina = null;
	private User mLastUser = null;
	private List<Status> mLastUserTimeline = null; 
	
	public enum Action {
		SHOW_USER,
		GET_USER_TIMELINE
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
		mProgressStatusLoading = (ProgressBar)findViewById(R.id.pbStatusLoading);
		mLayoutStatusCtrls = (LinearLayout)findViewById(R.id.llStatusCtrls);
		mBtnWeibos = (Button)findViewById(R.id.btnWeibos);
						
		/*
		 * show the whole user/info
		 */
		Intent intent = getIntent();
		String uid = intent.getStringExtra("uid");
    	
		AsyncWeiboLoader loader = new AsyncWeiboLoader();
		loader.execute(Action.SHOW_USER, uid);
		
		/*
		 * deal actions for components
		 */
		mListStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (mLayoutStatusCtrls.getVisibility() == LinearLayout.GONE) {
					mLayoutStatusCtrls.setVisibility(LinearLayout.VISIBLE);
				} else {
					mLayoutStatusCtrls.setVisibility(LinearLayout.GONE);
				}
			}
			
		});
		
		mBtnWeibos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLastUser != null) {
					AsyncWeiboLoader loader = new AsyncWeiboLoader();
					loader.execute(Action.GET_USER_TIMELINE, "" + mLastUser.getId());
				}
			}
			
		});
	}
	
	public static Sina getSina() {
		return mSina;
	}
	
	public static void setSina(Sina sina) {
		mSina = sina;
	}
	
	private void turnLoading(boolean on) {
		if (on == true) {
			mProgressStatusLoading.setVisibility(ProgressBar.VISIBLE);
		} else {
			mProgressStatusLoading.setVisibility(ProgressBar.GONE);
		}
	}
	
	private List<Map<String, Object>> getStatusData(Action type) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		Status status;
		switch (type) {
		case SHOW_USER:
			if (mLastUser != null) {
				status = mLastUser.getStatus();
				if (status != null) {
					map = new HashMap<String, Object>();
					map.put("status", status);
					list.add(map);
				}	
			}
			break;
		case GET_USER_TIMELINE:
			if (mLastUserTimeline != null) {
				for (int i = 0; i < mLastUserTimeline.size(); i++) {
					status = mLastUserTimeline.get(i);
					map = new HashMap<String, Object>();
					map.put("status", status);
					list.add(map);
				}
			}
			break;
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

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			if (result == null) {
				dealWithFailed();
			} else {
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
							getStatusData(Action.SHOW_USER)
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
						 * show when was the user created
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
				case GET_USER_TIMELINE:
					mLastUserTimeline = (List<weibo4android.Status>)res[1];
					if (mLastUserTimeline != null) {
						/*
						 * show the user time_line
						 */
						WeiboStatusListAdapter adapter = new WeiboStatusListAdapter(
							WeiboShowActivity.this,
							getStatusData(Action.GET_USER_TIMELINE)
						);
						mListStatus.setAdapter(adapter);
					}
					break;
				}
			}
			
			turnLoading(false);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			turnLoading(true);
			super.onPreExecute();
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
			String uid;
			Weibo weibo = mSina.getWeibo();
			switch (action) {
			case SHOW_USER:
				if (params.length != 2) return null;
				uid = (String)params[1];
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
			case GET_USER_TIMELINE:
				if (params.length != 2) return null;
				uid = (String)params[1];
				if (weibo != null) {
					try {
						res[1] = weibo.getUserTimeline(uid);
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
