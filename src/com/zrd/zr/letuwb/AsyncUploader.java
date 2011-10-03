package com.zrd.zr.letuwb;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


class AsyncUploader extends AsyncTask <Intent, Object, String> {
	Context mContext;
	Integer mAccountId;
	Dialog mPrgDlg;
	
	public AsyncUploader(Context c, Integer id) {
		super();
		mContext = c;
		mAccountId = id;
		mPrgDlg = new Dialog(mContext, R.style.Dialog_Clean);
		mPrgDlg.setContentView(R.layout.custom_dialog_loading);
		((TextView) mPrgDlg.findViewById(R.id.tvCustomDialogTitle)).setText(c.getString(R.string.msg_uploading));
		mPrgDlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				
			}
    	});
		mPrgDlg.setCancelable(false);
		WindowManager.LayoutParams lp = mPrgDlg.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mPrgDlg.getWindow().setAttributes(lp);
		mPrgDlg.show();			
	}
	
	/**
    * construct the request data through combining way, to implement param&file transfering
    * @param actionUrl //PHP upload page
    * @param params //pairs of values that PHP parameter $_POST array holds (Map<"name", "value"> means $_POST['name'] = 'value', and so on.)
    * @param files //java.io.File objects that hold the local files
    * @return
    * @throws IOException
    */
    public static String post(String actionUrl, Map<String, String> params,
        Map<String, File> files) throws IOException {

      String BOUNDARY = java.util.UUID.randomUUID().toString();
      String PREFIX = "--" , LINEND = "\r\n";
      String MULTIPART_FROM_DATA = "multipart/form-data";
      String CHARSET = "UTF-8";

      URL uri = new URL(actionUrl);
      HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
      conn.setReadTimeout(5 * 1000); // the longest time for connecting
      conn.setDoInput(true);// allow input
      conn.setDoOutput(true);// allow output
      conn.setUseCaches(false); // don't allow cache
      conn.setRequestMethod("POST");
      conn.setRequestProperty("connection", "keep-alive");
      conn.setRequestProperty("Charsert", "UTF-8");
      conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

      // 1st, combine parameters of text type
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, String> entry : params.entrySet()) {
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINEND);
        sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
        sb.append("Content-Type: text/plain; charset=" + CHARSET+LINEND);
        sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
        sb.append(LINEND);
        sb.append(entry.getValue());
        sb.append(LINEND);
      }

      DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
      outStream.write(sb.toString().getBytes());
      // post data
      if(files!=null){
        int i = 0;
        for (Map.Entry<String, File> file: files.entrySet()) {
          StringBuilder sb1 = new StringBuilder();
          sb1.append(PREFIX);
          sb1.append(BOUNDARY);
          sb1.append(LINEND);
          sb1.append("Content-Disposition: form-data; name=\"file"+(i++)+"\"; filename=\""+file.getKey()+"\""+LINEND);
          sb1.append("Content-Type: application/octet-stream; charset="+CHARSET+LINEND);
          sb1.append(LINEND);
          outStream.write(sb1.toString().getBytes());

          InputStream is = new FileInputStream(file.getValue());
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
          }

          is.close();
          outStream.write(LINEND.getBytes());
        }
      }
      
      //request ending flag
      byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
      outStream.write(end_data);
      outStream.flush();

      //get the response code
      int res = conn.getResponseCode();
      InputStream in = null;
      StringBuilder sb2 = new StringBuilder();
      if (res == 200) {
        in = conn.getInputStream();
        int ch;
        while ((ch = in.read()) != -1) {
          sb2.append((char) ch);
        }
      }
      return in == null ? null : in.toString() + "\n\n" + sb2.toString();
    }
    
    public static void upload(int privilege, Activity context) {
		if (privilege == 0) {
			Intent intent = new Intent();
			intent.setClass(context, FilePickerActivity.class);
			context.startActivityForResult(intent, EntranceActivity.REQUESTCODE_PICKFILE);
		} else {
			//Toast.makeText(LetuseeActivity.this, getString(R.string.err_needlogin), Toast.LENGTH_LONG).show();
			RegLoginActivity.shallWeLogin(R.string.err_needlogin, context);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		mPrgDlg.dismiss();
		String msgs[] = EntranceActivity.getPhpMsg(result);
		//AlertDialog dlg = new AlertDialog.Builder(LetuseeActivity.this).setPositiveButton("OK", null).create();//for debug
		//dlg.setTitle("Warning");//for debug
		//dlg.setMessage(msg);//for debug
		//dlg.show();//for debug
		if (msgs != null && msgs[0].equals(EntranceActivity.SYMBOL_SUCCESSFUL)) {
			Toast.makeText(
				mContext,
				R.string.tips_uploaded,
				Toast.LENGTH_LONG
			).show();
		} else {
			Toast.makeText(
				mContext,
				R.string.err_uploadfailed,
				Toast.LENGTH_LONG
			).show();
		}
		
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(Intent... params) {
		// TODO Auto-generated method stub
		
		/*
		 * the part that implement uploading file to remote PHP server
		 */
		Intent data = params[0];
		Map<String, String> action = new HashMap<String, String>();   
		action.put("action", "Upload Image");
		action.put("accountid", mAccountId.toString());
		action.put("intro", data.getStringExtra("intro"));
		Map<String, File> files = new HashMap<String, File>();   
		//files.put("images.jpeg", new File("/sdcard/download/images.jpeg"));
		String path = data.getStringExtra("pickedpath");
		String filename = data.getStringExtra("pickedfile");
		files.put(filename, new File(path + "/" + filename));
		
		String sPost = "";
		try {
			sPost = "Response: " + post(EntranceActivity.URL_SITE + "upload.php", action, files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sPost = "Error: " + EntranceActivity.SYMBOL_FAILED + mContext.getString(R.string.err_noconnection) + EntranceActivity.SYMBOL_FAILED;
		}
		
		return sPost;
	}
	
}