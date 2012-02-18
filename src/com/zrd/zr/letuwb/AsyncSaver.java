package com.zrd.zr.letuwb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

/*
 * try to save file by using AsyncTask
 */
public class AsyncSaver extends AsyncTask <Object, Object, Object> {
	Context mContext;
	Bitmap mBitmap;
	URL mUrl;
	
	AsyncSaver(Context context, Bitmap bmp) {
		mContext = context;
		mBitmap = bmp;
	}
	
	AsyncSaver(Context context, URL url) {
		mContext = context;
		mUrl = url;
	}
	
	/*
	 * @return "" means SDCARD not available.
	 */
	public static String getSdcardDir() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else return "";
	}
	
	/*
	 * @return 0 means SDCARD not available, -1 means failed to create directory
	 * with sPath, -2 means file sPath/sFname already exists, 1 means file
	 * sPath/sFname could be created directly.
	 */
	public static int probeFile(String sPath, String sFname) {
		if (getSdcardDir().equals("")) return 0;
		File path = new File(sPath);
		if (!path.exists()) {
			if (!path.mkdirs())	return -1;
		}
		File file = new File(path, sFname);
		if (file.exists()) return -2;
		else return 1;
	}
	
	public static File getSilentFile(String sPath, String sFname) {
		int i = probeFile(sPath, sFname);
		if (i <= 0) return null;
		else return new File(new File(sPath), sFname);
	}
	
	public String saveImage(File saveFile) {
		//BitmapDrawable imgBd = (BitmapDrawable) mBrow.getTag();
		// TODO Auto-generated method stub
		if (saveFile == null) {
			return mContext.getString(R.string.tips_filenotcreated);
		}
		String fn = saveFile.getName();
		if (fn == null || fn.equals("")) {
			return mContext.getString(R.string.tips_filenotcreated);
		}
		if (mBitmap == null && mUrl == null) {
			return mContext.getString(R.string.tips_nothingtosave);
		}
		
		if (mBitmap != null)
		{
			try {
				FileOutputStream outStream = new FileOutputStream(saveFile);
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
				return mContext.getString(R.string.tips_imagesaved);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return mContext.getString(R.string.tips_imagenotsaved);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return mContext.getString(R.string.tips_imagenotsaved);
			}
		}
		
		if (mUrl != null) {
			try {
				URLConnection conexion = mUrl.openConnection();
	            conexion.connect();
	            // download the file
	            InputStream input = new BufferedInputStream(mUrl.openStream());
	            OutputStream output = new FileOutputStream(saveFile);
	            byte data[] = new byte[1024];
	            int count;
	            while ((count = input.read(data)) != -1) {
	            	output.write(data, 0, count);
	            }
	            output.flush();
	            output.close();
	            input.close();
	            return mContext.getString(R.string.tips_imagesaved);
			} catch (Exception e) {
				e.printStackTrace();
				return mContext.getString(R.string.tips_imagenotsaved);
			}
		}
		
		return mContext.getString(R.string.tips_imagenotsaved); 
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		String msg = (String) result;
		Toast.makeText(
			mContext,
			msg,
			Toast.LENGTH_LONG
		).show();
		super.onPostExecute(result);
	}

	@Override
	protected Object doInBackground(Object... params) {
		File saveFile = (File) params[0];
		return saveImage(saveFile);
	}
	
}