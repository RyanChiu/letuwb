package com.zrd.zr.letuwb;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.sonyericsson.zoom.DynamicZoomControl;
import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.LongPressZoomListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImageActivity extends Activity implements OnTouchListener {
	private ProgressBar mProgressLoading;
	private ImageZoomView mImageZoom;
	private GifView mImageGif;
	/** Zoom control */
    private DynamicZoomControl mZoomControl;
    /** On touch listener for zoom view */
    private LongPressZoomListener mZoomListener;
    private GestureDetector mGestureDetector = null;
    
    private URL mUrl;
    private boolean mIsDooming = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_brow);
		
		mProgressLoading = (ProgressBar) findViewById(R.id.pbImageBrowser);
		mImageZoom = (ImageZoomView) findViewById(R.id.imageBrowser);
		mImageGif = (GifView) findViewById(R.id.imageGif);
		mImageGif.setGifImageType(GifImageType.WAIT_FINISH);
		mImageGif.setOnTouchListener(this);
		mZoomControl = new DynamicZoomControl();
		mZoomListener = new LongPressZoomListener(this);
        mZoomListener.setZoomControl(mZoomControl);
        mImageZoom.setZoomState(mZoomControl.getZoomState());
        mZoomControl.setAspectQuotient(mImageZoom.getAspectQuotient());
        mImageZoom.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, new ImagebrowGestureListener());
        
        String sUrl = getIntent().getStringExtra("url");
        int idx = sUrl.lastIndexOf(".");
        String sExt = "";
        if (idx != -1) {
        	sExt = sUrl.substring(idx).toLowerCase();
        }
        
        URL url = null;
        try {
			url = new URL(sUrl);
		} catch (MalformedURLException e) {
		}
        mUrl = url;
        
        if (sExt.equals(".gif")) {
        	Toast.makeText(
        		this,
        		R.string.tips_gifimage,
        		Toast.LENGTH_SHORT
        	).show();
        	mImageZoom.setVisibility(View.GONE);
        	mImageGif.setVisibility(View.VISIBLE);
        	try {
	            InputStream input = new BufferedInputStream(mUrl.openStream());
	        	mImageGif.setGifImage(input);
        	} catch (Exception e) {
        		Toast.makeText(
            		this,
            		R.string.tips_failedtoshowgif,
            		Toast.LENGTH_LONG
            	).show();
        	}
        } else {
        	mImageZoom.setVisibility(View.VISIBLE);
        	mImageGif.setVisibility(View.GONE);
        	if (mUrl != null) {
            	AsyncImageLoader loader = new AsyncImageLoader(
            		this, mImageZoom, R.drawable.thumbg, mProgressLoading
            	);
            	loader.execute(mUrl);
            }
            
            resetZoomState();
        }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, getString(R.string.label_save));
		menu.add(Menu.NONE, Menu.FIRST + 2, 1, getString(R.string.omenuitem_goback));
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST + 1://save
			Toast.makeText(
				this,
				R.string.tips_saving,
				Toast.LENGTH_SHORT
			).show();
			AsyncSaver saver = new AsyncSaver(this, mUrl);
			String sPath = AsyncSaver.getSdcardDir() + EntranceActivity.PATH_COLLECTION;
			String sFile = mUrl.getFile();
			int idxFind = sFile.lastIndexOf("/");
			if (idxFind != -1) {
				sFile = sFile.substring(idxFind);
			}
			saver.execute(AsyncSaver.getSilentFile(sPath, sFile));
			break;
		case Menu.FIRST + 2://go back
			this.finish();
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
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
			ImageActivity.this.openOptionsMenu();
			
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
