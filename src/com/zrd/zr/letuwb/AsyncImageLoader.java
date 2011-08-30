package com.zrd.zr.letuwb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	static Context mContext;
	static ArrayList<WeibouserInfo> mUsrs = new ArrayList<WeibouserInfo>();

		private HashMap<Integer, SoftReference<Drawable>> imageCache;
		
		public HashMap<Integer, SoftReference<Drawable>> getImageCache() {
			return imageCache;
		}

	     public AsyncImageLoader(Context c, List<WeibouserInfo> pics) {
	    	 AsyncImageLoader.mContext = c;
	    	 for (int i = 0; i < pics.size(); i++) {
	    		 mUsrs.add(pics.get(i));
	    	 }
	    	 imageCache = new HashMap<Integer, SoftReference<Drawable>>();
	     }
	  
	     public Drawable loadDrawable(final Integer id, final ImageCallback imageCallback) {
	         if (imageCache.containsKey(id)) {
	             SoftReference<Drawable> softReference = imageCache.get(id);
	             Drawable drawable = softReference.get();
	             if (drawable != null) {
	                 return drawable;
	             }
	         }
	         final Handler handler = new Handler() {
	             public void handleMessage(Message message) {
	            	 if (message.what == 0) {
	            		 imageCallback.imageLoaded((Drawable) message.obj, id);
	            	 }
	            	 if (message.what == 1) {
	            		 EntranceActivity c = (EntranceActivity) mContext;
	            		 c.mPrgDlg.dismiss();
	            	 }
	             }
	         };
	         new Thread() {
	             @Override
	             public void run() {
	                 Drawable drawable = loadImageFromId(id);
	                 imageCache.put(id, new SoftReference<Drawable>(drawable));
	                 Message message = handler.obtainMessage(0, drawable);
	                 handler.sendMessage(message);
	                 EntranceActivity c = (EntranceActivity) mContext;
	                 if (imageCache.size() == c.mPageUsrs.size()) {
	                	 Message msg = handler.obtainMessage(1);
	                	 handler.sendMessage(msg);
	                 }
	             }
	         }.start();
	         return null;
	     }
	  
		public static Drawable loadImageFromId(Integer id) {
			System.gc();
			System.runFinalization();
			System.gc();
			
		    WeibouserInfo wi = EntranceActivity.getPicFromId(id, mUsrs);
		    String sPath = AsyncSaver.getSdcardDir() + EntranceActivity.PATH_CACHE;
		    String sFname = wi.uid + "t.jg";
		    if (AsyncSaver.probeFile(sPath, sFname) == -2) {
		    	Bitmap bmp = BitmapFactory.decodeFile(sPath + "/" + sFname);
		    	return new BitmapDrawable(bmp);
		    } else {
				try {
					String sUrl = "";
				    if (wi != null) {
				    	sUrl = new String(wi.profile_image_url);
				    	sUrl = sUrl.replace("/50/", "/180/");
				    }
				    
					BitmapDrawable bd;
					URL url = new URL(sUrl);
					URLConnection conn = url.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is, 8192);
					Bitmap preview_bitmap = BitmapFactory.decodeStream(bis);
					bd = new BitmapDrawable(preview_bitmap);
					bis.close();
					is.close();
					File file = AsyncSaver.getSilentFile(sPath, sFname);
					if (file != null) {
						AsyncSaver saver = new AsyncSaver(mContext, preview_bitmap);
						saver.saveImage(file);
						saver = null;
					}
					return bd;
				} catch (Exception e) {
					Log.e("DEBUGTAG", "Remtoe Image Exception", e);
					e.printStackTrace();
					return null;
				}
		    }
		}
	  
	     public interface ImageCallback {
	         public void imageLoaded(Drawable imageDrawable, Integer id);
	     }

}
