package com.zrd.zr.letuwb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
	
	AsyncSaver(Context context, Bitmap bmp) {
		mContext = context;
		mBitmap = bmp;
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
	
	public ArrayList<String> saveImage(File saveFile) {
		//BitmapDrawable imgBd = (BitmapDrawable) mBrow.getTag();
		// TODO Auto-generated method stub
		ArrayList<String> notes = new ArrayList<String>();
		if (saveFile == null) {
			notes.add(String.format(mContext.getString(R.string.noti_notcreated), ""));
			notes.add(mContext.getString(R.string.noti_notsavedtitle));
			return notes;
		}
		String fn = saveFile.getName();
		if (fn == null || fn.equals("")) {
			notes.add(String.format(mContext.getString(R.string.noti_notcreated), ""));
			notes.add(mContext.getString(R.string.noti_notsavedtitle));
			return notes;
		}
		try {
			FileOutputStream outStream = new FileOutputStream(saveFile);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
			notes.add(String.format(mContext.getString(R.string.noti_saved), fn));
			notes.add(mContext.getString(R.string.noti_savedtitle));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			notes.add(String.format(mContext.getString(R.string.noti_notcreated), fn));
			notes.add(mContext.getString(R.string.noti_notsavedtitle));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			notes.add(String.format(mContext.getString(R.string.noti_notcreated), fn));
			notes.add(mContext.getString(R.string.noti_notsavedtitle));
		}
		
		return notes;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ArrayList<String> notes = (ArrayList<String>) result;
		Toast.makeText(
			mContext,
			notes.get(1) + ":" + notes.get(0),
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