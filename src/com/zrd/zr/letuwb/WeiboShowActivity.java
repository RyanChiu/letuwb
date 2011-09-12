package com.zrd.zr.letuwb;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.zrd.zr.weiboes.Sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiboShowActivity extends Activity {
	
	private TextView mTextContent;
	private TextView mTextScreenName;
	private ImageView mImageVerified;
	private TextView mTextCreatedAt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_show);
		
		mTextContent = (TextView)findViewById(R.id.tvContent);
		mTextScreenName = (TextView)findViewById(R.id.tvScreenName);
		mImageVerified = (ImageView)findViewById(R.id.ivVerified);
		mTextCreatedAt = (TextView)findViewById(R.id.tvCreatedAt);
		
		/*
		 * show the whole user/info
		 */
		Intent intent = getIntent();
		String uid = intent.getStringExtra("uid");
		Sina sina = new Sina();
    	String content = sina.getContent(uid);
		if (content == null) content = "No data passed.";
		mTextContent.setText(content);
		
		/*
		 * show the profile-image
		 */
		AsyncImageLoader ail = new AsyncImageLoader(
			WeiboShowActivity.this,
			R.id.ivTinyProfileImage,
			R.drawable.cgpretty_small
		);
		ail.execute(sina.getLastUserInfo().getProfileImageURL());
		
		/*
		 * show the screen name
		 */
		mTextScreenName.setText(sina.getLastUserInfo().getScreenName());
		
		/*
		 * show "v" if verified
		 */
		if (sina.getLastUserInfo().isVerified()) {
			mImageVerified.setVisibility(ImageView.VISIBLE);
		} else {
			mImageVerified.setVisibility(ImageView.GONE);
		}
		
		/*
		 * show when did the user be created
		 */
		Date dtCreatedAt = sina.getLastUserInfo().getCreatedAt();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		mTextCreatedAt.setText(sdf.format(dtCreatedAt));
	}

}
