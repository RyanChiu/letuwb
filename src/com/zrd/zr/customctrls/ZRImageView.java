package com.zrd.zr.customctrls;

import com.zrd.zr.letuwb.AsyncImageLoader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ZRImageView extends ImageView {
	private AsyncImageLoader mAsyncLoader = null;
	private Boolean mHasBeenReset = false;

	public ZRImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ZRImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ZRImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AsyncImageLoader getAsyncLoader() {
		return mAsyncLoader;
	}

	public void setAsyncLoader(AsyncImageLoader mAsyncLoader) {
		this.mAsyncLoader = mAsyncLoader;
	}

	public Boolean hasBeenReset() {
		return mHasBeenReset;
	}

	public void reset(int resId) {
		this.setImageResource(resId);
		this.mHasBeenReset = true;
	}

}
