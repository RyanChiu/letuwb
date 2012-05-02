package com.zrd.zr.customctrls;

import com.zrd.zr.letuwb.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ZRImageView extends ImageView {
	private Boolean loading = false;
	private Boolean mHasBeenReset = false;
	private Bitmap mBitmap = null;

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

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
		mBitmap = bm;
	}
	
	public Bitmap getImageBitmap() {
		return mBitmap;
	}

	public Boolean isLoading() {
		return loading;
	}

	public void setLoading(Boolean loading) {
		this.loading = loading;
	}

	public Boolean hasBeenReset() {
		return mHasBeenReset;
	}

	public void reset() {
		this.setImageResource(R.drawable.transparent);
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
		this.mHasBeenReset = true;
	}

}
