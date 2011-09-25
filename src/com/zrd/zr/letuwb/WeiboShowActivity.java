package com.zrd.zr.letuwb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4android.Status;
import weibo4android.User;
import weibo4android.WeiboException;

import com.zrd.zr.weiboes.Sina;
import com.zrd.zr.weiboes.ThreadSinaDealer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

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
	private Button mBtnFriend;
	
	private String mUid = null;
	private static Sina mSina = null;
	private User mLastUser = null;
	private List<Status> mLastUserTimeline = null;
	
	private AlphaAnimation mAnimFadein = new AlphaAnimation(0.1f, 1.0f);
	private AlphaAnimation mAnimFadeout = new AlphaAnimation(1.0f, 0.1f);
	
	public enum Action {
		SHOW_USER,
		GET_USER_TIMELINE,
		CREATE_FRIENDSHIP
	}
	
	/*
	 * Handler for showing all kinds of SINA_weibo data from background thread.
	 */
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			Sina sina = (Sina)msg.getData().getSerializable(ThreadSinaDealer.KEY_SINA);
			if (sina == null) {
				sina = mSina;
			} else {
				setSina(sina);
			}
			WeiboException wexp = (WeiboException)msg.getData().getSerializable(ThreadSinaDealer.KEY_WEIBO_ERR);
			if (wexp != null) {
				Toast.makeText(
					WeiboShowActivity.this,
					wexp.toString(),
					Toast.LENGTH_LONG
				).show();
				turnDealing(false);
				return;
			}
			switch (msg.what) {
			case ThreadSinaDealer.SHOW_USER:
				mLastUser = (User)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
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
					mTextCreatedAt.setText("N/A");
					mTextDescription.setText("Please try again...");
				}
				break;
			case ThreadSinaDealer.GET_USER_TIMELINE:
				mLastUserTimeline = (ArrayList<weibo4android.Status>)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (mLastUserTimeline != null) {
					/*
					 * show the user time_line
					 */
					WeiboStatusListAdapter adapter = new WeiboStatusListAdapter(
						WeiboShowActivity.this,
						getStatusData(Action.GET_USER_TIMELINE)
					);
					mListStatus.setAdapter(adapter);
				} else {
					//deal with failing to get time_line
				}
				break;
			case ThreadSinaDealer.CREATE_FRIENDSHIP:
				User user = (User)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (user != null) {
					if (!user.equals(mSina.getLoggedInUser())) {
						Toast.makeText(
							WeiboShowActivity.this,
							"Friends made.",
							Toast.LENGTH_LONG
						).show();
					} else {
						Toast.makeText(
							WeiboShowActivity.this,
							"Friends already.",
							Toast.LENGTH_LONG
						).show();
					}
				} else {
					//deal with failing to make friends
				}
				break;
			}
			
			turnDealing(false);
		}
	};
	
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
		mBtnFriend = (Button)findViewById(R.id.btnFriend);
		
		mLayoutStatusCtrls.setVisibility(LinearLayout.GONE);
		/*
		 * show the whole user/info
		 */
		Intent intent = getIntent();
		mUid = intent.getStringExtra("uid");
    	
		new Thread(
			new ThreadSinaDealer(
				mSina, 
				ThreadSinaDealer.SHOW_USER, 
				new String[] {mUid}, 
				mHandler
			)
		).start();
		turnDealing(true);
		
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
					mAnimFadein.setDuration(500);
					mLayoutStatusCtrls.startAnimation(mAnimFadein);
				} else {
					mAnimFadeout.setDuration(300);
					mLayoutStatusCtrls.startAnimation(mAnimFadeout);
					mLayoutStatusCtrls.setVisibility(LinearLayout.GONE);
				}
			}
			
		});
		
		mBtnWeibos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(
					new ThreadSinaDealer(
						mSina,
						ThreadSinaDealer.GET_USER_TIMELINE,
						new String[] {mUid},
						mHandler
					)
				).start();
				mLayoutStatusCtrls.setVisibility(LinearLayout.GONE);
				turnDealing(true);
			}
			
		});
		
		mBtnFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLayoutStatusCtrls.setVisibility(LinearLayout.GONE);
				if (mSina != null && mSina.isLoggedIn()) {
					new Thread(
						new ThreadSinaDealer(
							mSina,
							ThreadSinaDealer.CREATE_FRIENDSHIP,
							new String[] {mUid},
							mHandler
						)
					).start();
				} else {
					RegLoginActivity.shallWeLogin(R.string.title_loginfirst, WeiboShowActivity.this);
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
	
	/*
	 * change the status for the components when dealing with SINA_weibo data
	 */
	private void turnDealing(boolean on) {
		if (on == true) {
			mBtnWeibos.setEnabled(false);
			mBtnFriend.setEnabled(false);
			mProgressStatusLoading.setVisibility(ProgressBar.VISIBLE);
		} else {
			mBtnWeibos.setEnabled(true);
			mBtnFriend.setEnabled(true);
			mProgressStatusLoading.setVisibility(ProgressBar.GONE);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
