package com.zrd.zr.letuwb;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AsyncImageLoader extends AsyncTask<Object, Object, Bitmap> {
	Activity mContext;
	Integer mResIdImageView;
	Integer mResIdBadImage;
	
	public AsyncImageLoader(Activity c, Integer resIdImageView, Integer resIdBadImage) {
		super();
		mContext = c;
		mResIdImageView = resIdImageView;
		mResIdBadImage = resIdBadImage;
	}
	
	@Override
	protected Bitmap doInBackground(Object... params) {
		System.gc();
		System.runFinalization();
		System.gc();
		
		// TODO Auto-generated method stub
		
		if (params.length != 1) return null;
		
		URL url = (URL) params[0];
		
		SecureUrl su = new SecureUrl();
		URLConnection conn = su.getConnection(url);
		InputStream is;
		
		if (conn == null) return null;
		try {
			is = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("DEBUGTAG", "Remtoe Image Exception", e);
			e.printStackTrace();
			return null;
		}
		
		if (is == null) return null;
		BufferedInputStream bis = new BufferedInputStream(is, 8192);
		Bitmap bmp = BitmapFactory.decodeStream(bis);
		if (bmp == null) return null;
		try {
			bis.close();
			is.close();
			bis = null;
			is = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("DEBUGTAG", "Remtoe Image Exception", e);
			e.printStackTrace();
			bis = null;
			is = null;
			return null;
		}
		return bmp;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		ImageView iv = (ImageView) mContext.findViewById(mResIdImageView);
		if (iv != null) {
			if (result != null)
				iv.setImageBitmap(result);
			else
				iv.setImageResource(mResIdBadImage);
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
    protected void onCancelled() {
		super.onCancelled();
    }
}