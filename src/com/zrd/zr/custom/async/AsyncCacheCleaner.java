package com.zrd.zr.custom.async;

import com.zrd.zr.letuwb.FileUtil;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncCacheCleaner extends AsyncTask<String, Object, Integer> {

	Context mContext;
	
	public AsyncCacheCleaner(Context c) {
		mContext = c;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		if (result == -1) {
			//Toast.makeText(mContext, "No need to clean.", Toast.LENGTH_LONG).show();
		} else if (result == 0) {
			//Toast.makeText(mContext, "Not cleaned.", Toast.LENGTH_LONG).show();
		} else if (result == 1) {
			//Toast.makeText(mContext, "Cleaned.", Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(result);
	}
	@Override
	protected Integer doInBackground(String... args) {
		// TODO Auto-generated method stub
		String path = args[0];
		Integer maxsize; //in MB
		Integer maxpercentage; // for example: 5, means 5%
		try {
			maxsize = Integer.parseInt(args[1]);
			maxpercentage = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			maxsize = 10;
			maxpercentage = 5;
		}
		double size = FileUtil.getSize(path);
		double free = FileUtil.getFreeSizeOfSd();
		Integer flag = -1;//means no moves at all.
		if (size > maxsize || size > free * maxpercentage / 100) {
			flag = FileUtil.deleteDirectory(path) ? 1 : 0;
		}
		return flag;
	}

}
