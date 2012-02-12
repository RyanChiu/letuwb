package com.zrd.zr.letuwb;

import java.net.MalformedURLException;
import java.net.URL;

import com.sonyericsson.zoom.DynamicZoomControl;
import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.LongPressZoomListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;

public class ImageActivity extends Activity implements OnTouchListener {
	private ProgressBar mProgressLoading;
	private ImageZoomView mImageZoom;
	/** Zoom control */
    private DynamicZoomControl mZoomControl;
    /** On touch listener for zoom view */
    private LongPressZoomListener mZoomListener;
    private GestureDetector mGestureDetector = null;
    
    private boolean mIsDooming = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_brow);
		
		mProgressLoading = (ProgressBar) findViewById(R.id.pbImageBrowser);
		mImageZoom = (ImageZoomView) findViewById(R.id.imageBrowser);
		mZoomControl = new DynamicZoomControl();
		mZoomListener = new LongPressZoomListener(this);
        mZoomListener.setZoomControl(mZoomControl);
        getImageZoom().setZoomState(mZoomControl.getZoomState());
        mZoomControl.setAspectQuotient(getImageZoom().getAspectQuotient());
        getImageZoom().setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, new ImagebrowGestureListener());
        
        String sUrl = getIntent().getStringExtra("url");
        URL url = null;
        try {
			url = new URL(sUrl);
		} catch (MalformedURLException e) {
		}
        if (url != null) {
        	AsyncImageLoader loader = new AsyncImageLoader(
        		this, getImageZoom(), R.drawable.thumbg, mProgressLoading
        	);
        	loader.execute(url);
        }
        resetZoomState();
	}
	
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mIsDooming) {
				mIsDooming = false;
				resetZoomState();
				getImageZoom().setOnTouchListener(this);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class ImagebrowGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			if (!mIsDooming) {
				mZoomControl.zoom((float)Math.pow(20, 0.1), 0, 0);
				getImageZoom().setOnTouchListener(mZoomListener);
				mIsDooming = true;
			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}

	/**
     * Reset zoom state and notify observers
     */
    public void resetZoomState() {
    	mZoomControl.getZoomState().setPanX(0.5f);
        mZoomControl.getZoomState().setPanY(0.5f);
        mZoomControl.getZoomState().setZoom(1f);
        mZoomControl.getZoomState().notifyObservers();
    }

	public ImageZoomView getImageZoom() {
		return mImageZoom;
	}
	
	public void setDooming(boolean dooming) {
		mIsDooming = dooming;
	}
}
