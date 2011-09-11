package com.zrd.zr.letuwb;

import com.zrd.zr.weiboes.Sina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WeiboShowActivity extends Activity {
	
	private TextView mTextContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_show);
		
		mTextContent = (TextView)findViewById(R.id.tvContent);
		
		Intent intent = getIntent();
		String uid = intent.getStringExtra("uid");
		Sina sina = new Sina();
    	String content = sina.getContent(uid);
		if (content == null) content = "No data passed.";
		mTextContent.setText(content);
	}

}
