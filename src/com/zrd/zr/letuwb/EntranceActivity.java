package com.zrd.zr.letuwb;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.adwhirl.AdWhirlLayout;
import com.adwhirl.eventadapter.GmAdWhirlEventAdapterData;
import com.mobclick.android.MobclickAgent;
import com.zrd.zr.letuwb.R;
import com.zrd.zr.pnj.PNJ;
import com.zrd.zr.pnj.SecureURL;
import com.zrd.zr.weiboes.Sina;

public class EntranceActivity extends Activity implements OnTouchListener {

	final static String SERIAL_APP = "gbhytfvnjurdcmkiesx,lowaz.;p201108282317";
	final static String TIMEZONE_SERVER = "Asia/Hong_Kong";
	//final static String URL_SITE = "http://hot88.info/letmewb/";
	final static String URL_SITE = "http://122.224.249.74:8080/letmewb/";
	final static String URL_UPDATE = "http://122.224.249.74:8080/";
	final static String URL_STATS = "http://122.224.249.74:8080/letmewb/";
	final static String PATH_COLLECTION = "/letuwb/collection/";
	final static String PATH_CACHE = "/letuwb/cache/";
	final static Integer MAXSIZE_CACHE = 100;// in MB
	final static Integer MAXPERCENTAGE_CACHE = 5; // with %
	final static int REQUESTCODE_PICKFILE = 1001;
	final static String CONFIG_ACCOUNTS = "Accounts";
	final static String CONFIG_CLIENTKEY = "ClientKey";
	final static String CONFIG_RANDOMKEY = "RandomKey";
	final static String CONFIG_TOPICCHOICE = "TopicChoice";
	final static String SYMBOL_FAILED = "~failed~";
	final static String SYMBOL_SUCCESSFUL = "~successful~";
	final static int PERIOD_VOTEAGAIN = 24;//in HOUR
	private static String mClientKey = "";
	private static String mRandomKey = "";
	
	private static boolean mNowLoggingIn = false;

	/*
	 * views for reg/login dialog
	 */
	EditText mEditUsername;
	EditText mEditPassword;
	EditText mEditRepeat;
	TableRow mRowRepeat;
	CheckBox mCheckRemember;
	
	private WebView mWebCount;
	private AlertDialog mQuitDialog;
	private static int mPrivilege = 1;//0 member, 1 guest
	public static SharedPreferences mPreferences = null;
	private static Integer mAccountId = 0;

	/*
	 * ViewFlipper and the pages in it
	 */
	private ViewFlipper mViewFlipper;
	private MainPage mMainPage;
	private BrowPage mBrowPage;
	private WeiboPage mWeiboPage;
	
	/*
	 * the set of "vote components"
	 */
	private RelativeLayout mLayoutVote;
	private LinearLayout mLayoutVoteInfo;
	private TextView mTextUpup;
	private TextView mTextDwdw;
	private TextView mTextVoteRating;
	private ProgressBar mProgressVote;
	private ImageButton mBtnUpup;
	private ImageButton mBtnDwdw;
	
    /* Called when the activity is firstly created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //setContentView(R.layout.main);
        
        //set content view with R.layout.core
        setContentView(R.layout.core);
        //inflate all the viewstubs in layout "core"
        ViewStub vs;
        vs = (ViewStub) findViewById(R.id.vsMain);
		vs.setVisibility(View.VISIBLE);
		vs = (ViewStub) findViewById(R.id.vsBrow);
		vs.setVisibility(View.VISIBLE);
		vs = (ViewStub) findViewById(R.id.vsWeibo);
		vs.setVisibility(View.VISIBLE);
        //get all the views needed in layout "core"
        setViewFlipper((ViewFlipper) findViewById(R.id.vfCore));
        setMainPage(new MainPage(this));
        setBrowPage(new BrowPage(this));
        setWeiboPage(new WeiboPage(this));
        
        mPreferences = getPreferences(EntranceActivity.MODE_PRIVATE);
        
        /**
         * Try to trace exceptions
         */
        //TraceDroid.init(this);
        //TraceDroidEmailSender.sendStackTraces("ralphchiu1@gmail.com", this);
        
        /**
         * Initialize the application
         */
        mClientKey = mPreferences.getString(CONFIG_CLIENTKEY, "");
        mRandomKey = mPreferences.getString(CONFIG_RANDOMKEY, "");
        mMainPage.setTopicChoice(mPreferences.getInt(CONFIG_TOPICCHOICE, 0));
        
        AsyncInit init = new AsyncInit();
        ArrayList<String[]> list = getStoredAccounts();
        if (list.size() == 0) {
        	init.execute(true);
        } else {
        	init.execute(false);
        }
        
        /**
         * Clean cache if needed
         */
        AsyncCacheCleaner acc = new AsyncCacheCleaner(this);
        acc.execute(
        	AsyncSaver.getSdcardDir() + EntranceActivity.PATH_CACHE,
        	MAXSIZE_CACHE.toString(),		//in MB
        	MAXPERCENTAGE_CACHE.toString()	//in percentage
        );
        
        // implement AdWhirl.
        RelativeLayout rlAds = (RelativeLayout)this.findViewById(R.id.rlAdsMain);
        AdWhirlLayout ret = new AdWhirlLayout(this,
			GmAdWhirlEventAdapterData.CONST_STR_APPID_ADWHIRL, null);
        rlAds.addView(ret);
                
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.exchangelist_title);
        RegLoginActivity.addContext(EntranceActivity.this);
        mWebCount = (WebView) findViewById(R.id.wvCount);
        
        mLayoutVote = (RelativeLayout) findViewById(R.id.rlVote);
        mLayoutVoteInfo = (LinearLayout) findViewById(R.id.llVoteInfo);
        mTextUpup = (TextView) findViewById(R.id.tvUpup);
        mTextDwdw = (TextView) findViewById(R.id.tvDwdw);
        mTextVoteRating = (TextView) findViewById(R.id.tvVoteRating);
        mProgressVote = (ProgressBar) findViewById(R.id.pbVote);
        mBtnUpup = (ImageButton) findViewById(R.id.btnUpup);
		mBtnDwdw = (ImageButton) findViewById(R.id.btnDwdw);
        
        //mLayoutVoteInfo.setVisibility(LinearLayout.INVISIBLE);
        //mLayoutVote.setVisibility(LinearLayout.GONE);
        
		mQuitDialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_info).create();
		mQuitDialog.setTitle(getString(R.string.quit_title));
		mQuitDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.label_yes),
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor edit = mPreferences.edit();
					int i;
					for (i = 0; i < mMainPage.getTopicBtns().size(); i++) {
						if (mMainPage.getTopicBtns().get(i).isSelected()) break;
					}
					edit.putInt(CONFIG_TOPICCHOICE, i);
					edit.commit();
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			
			}
		);
		mQuitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.label_no),
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			
			}
		);
		
		/*
		 * event handling with the set of "vote components"
		 */
		mBtnUpup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (EntranceActivity.getClientKey().equals("")) {
					Toast.makeText(
						EntranceActivity.this,
						R.string.tips_notgetserialyet,
						Toast.LENGTH_SHORT
					).show();
				} else {
					WeibouserInfo wi = getMainPage().getPicFromId(getBrowPage().mId, getMainPage().getUsrs());
					Calendar now = Calendar.getInstance();
					now.setTimeZone(TimeZone.getTimeZone(EntranceActivity.TIMEZONE_SERVER));
					
					if ((wi.mLastVoteTime != null
						&& now.getTime().getTime() - wi.mLastVoteTime.getTime() > EntranceActivity.PERIOD_VOTEAGAIN * 3600000)
						|| wi.mLastVoteTime == null) {
						/**
						 * we let the voters think they'll see the result immediately,
						 * and we actually do the voting at background and it'll refresh
						 * the real result lately.
						 */
						//mVibrator.vibrate( new long[]{50, 400, 30, 800},-1);
						wi.likes++;
						wi.mLastVote = 1;
						getBrowPage().zrRenewCurFileInfo();
						AsyncVoter asyncVoter = new AsyncVoter();
						asyncVoter.execute("weibouserid", getBrowPage().mId.toString(), "clientkey", EntranceActivity.getClientKey(), "vote", "1");
					} else {
						Toast.makeText(
							EntranceActivity.this, 
							String.format(getString(R.string.err_voted), wi.mLastVote == 1 ? getString(R.string.label_upup) : getString(R.string.label_dwdw), EntranceActivity.PERIOD_VOTEAGAIN),
							Toast.LENGTH_SHORT
						).show();
					}
				}
			}
			
		});
		
		mBtnDwdw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (EntranceActivity.getClientKey().equals("")) {
					Toast.makeText(
						EntranceActivity.this,
						R.string.tips_notgetserialyet,
						Toast.LENGTH_SHORT
					).show();
				} else {
					WeibouserInfo wi = getMainPage().getPicFromId(getBrowPage().mId, getMainPage().getUsrs());
					Date now = Calendar.getInstance(TimeZone.getTimeZone(EntranceActivity.TIMEZONE_SERVER)).getTime();
					
					if ((wi.mLastVoteTime != null
						&& now.getTime() - wi.mLastVoteTime.getTime() > EntranceActivity.PERIOD_VOTEAGAIN * 3600000)
						|| wi.mLastVoteTime == null) {
						/**
						 * we let the voters think they'll see the result immediately,
						 * and we actually do the voting at background and it'll refresh
						 * the real result lately.
						 */
						//mVibrator.vibrate( new long[]{100,10,100,10},-1);
						wi.dislikes++;
						wi.mLastVote = -1;
						getBrowPage().zrRenewCurFileInfo();
						AsyncVoter asyncVoter = new AsyncVoter();
						asyncVoter.execute("weibouserid", getBrowPage().mId.toString(), "clientkey", EntranceActivity.getClientKey(), "vote", "-1");
					} else {
						Toast.makeText(
							EntranceActivity.this, 
							String.format(getString(R.string.err_voted), wi.mLastVote == 1 ? getString(R.string.label_upup) : getString(R.string.label_dwdw), EntranceActivity.PERIOD_VOTEAGAIN),
							Toast.LENGTH_SHORT
						).show();
					}
				}
			}
			
		});
		
        //show the very first view "main"
        switchPage(R.layout.main);
		
		/*
		 * show the content for layout "main"
		 */
		if (mClientKey.equals("")) {
			mMainPage.getTopicBtns().get(2).performClick();
		} else {
			mMainPage.getTopicBtns().get(1).performClick();
		}
    }
    
	public MainPage getMainPage() {
		return mMainPage;
	}

	public void setMainPage(MainPage mMainPage) {
		this.mMainPage = mMainPage;
	}
    
    public static String getClientKey() {
    	return mClientKey;
    }
    
    public static void setClientKey(String clientkey) {
    	mClientKey = clientkey;
    }
    
    public static String getRandomKey() {
    	return mRandomKey;
    }
    
    public static void resetRandomKey(String key) {
    	mRandomKey = key;
    	if (mPreferences != null) {
	    	SharedPreferences.Editor edit = mPreferences.edit();
			edit.putString(CONFIG_RANDOMKEY, mRandomKey);
			edit.commit();
    	}
    }
    
    public static ArrayList<String[]> getStoredAccounts() {
    	ArrayList<String[]> list = new ArrayList<String[]>();
    	if (mPreferences == null) return list;
    	String contents = mPreferences.getString(CONFIG_ACCOUNTS, "");
    	if (contents.equals("")) return list;
    	String[] pairs = contents.split(",");
    	if (pairs.length % 2 != 0) return list; 
    	
    	for (int i = 0; i < pairs.length; i += 2) {
    		list.add(new String[] {pairs[i], pairs[i + 1]});
    	}
    	return list;
    }
    
    public static void delAccount(String usr, String pwd) {
    	String contents = mPreferences.getString(CONFIG_ACCOUNTS, "");
    	contents = contents.replace(usr + "," + pwd + ",", "");
    	contents = contents.replace(usr + "," + pwd, "");
    	SharedPreferences.Editor edit = mPreferences.edit();
		edit.putString(CONFIG_ACCOUNTS, contents);
		edit.commit();
    }
    
    public static void saveAccount(String usr, String pwd) {
    	ArrayList<String[]> list = getStoredAccounts();
    	int i;
    	for (i = 0; i < list.size(); i++) {
    		if (list.get(i)[0].equals(usr)) break;
    	}
    	if (i != list.size()) {
     		list.remove(i);
    	}
		list.add(0, new String[] {usr, pwd});
		String content = "";
		for (i = 0; i < list.size(); i++) {
			content += list.get(i)[0] + "," + list.get(i)[1];
			if (i != list.size() - 1) content += ",";
		}
		SharedPreferences.Editor edit = mPreferences.edit();
		edit.putString(CONFIG_ACCOUNTS, content);
		edit.commit();
    }
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		/*
		 * for umeng.com
		 */
		MobclickAgent.onPause(this);
		
		/* 
		 * for playing in BrowPage
		 */
		if (mBrowPage.getBtnPause().getVisibility() == ImageButton.VISIBLE) {
			mBrowPage.setPlaying(true);
			mBrowPage.getBtnPause().performClick();
			mBrowPage.setLoading(false);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			public void run() {
				final String TAG = "Letuwb Stats";
				String url = URL_STATS + "count.html";
				mWebCount.setWebViewClient(new WebViewClient() {
					 public boolean shouldOverrideUrlLoading (WebView view, String url) {
						 Log.v(TAG, "loading " + url);
						 return false;
					 }
				});
				WebSettings webSettings = mWebCount.getSettings();
				webSettings.setJavaScriptEnabled(true);
				webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
				mWebCount.loadUrl(url);
				Log.v(TAG, "send " + url);
			}
		});
		
		super.onResume();
		
		/*
		 * for umeng.com
		 */
		MobclickAgent.onResume(this);
		
		/*
		 * for playing in BrowPage
		 */
		if (mBrowPage.wasPlaying()) {
			mBrowPage.setPlaying(false);
			mBrowPage.getBtnPlay().performClick();
		}
		
		/*
		 * for exit
		 */
		if (RegLoginActivity.ifQuitIsSet()) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mBrowPage.getFrameBackground().setBackgroundResource(R.drawable.bg_h);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mBrowPage.getFrameBackground().setBackgroundResource(R.drawable.bg);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		switch (mViewFlipper.getDisplayedChild()) {
		case 0:
			menu.add(Menu.NONE, Menu.FIRST + 4, 4, getString(R.string.omenuitem_reglogin)).setIcon(R.drawable.ic_menu_login);
			break;
		case 1:
			if (mBrowPage.isDooming()) {
				menu.add(Menu.NONE, Menu.FIRST + 1, 1, R.string.label_browse).setIcon(android.R.drawable.ic_menu_zoom);
			} else {
				menu.add(Menu.NONE, Menu.FIRST + 1, 1, R.string.label_zoom).setIcon(android.R.drawable.ic_menu_zoom);
			}
			menu.add(Menu.NONE, Menu.FIRST + 2, 1, R.string.label_reset).setIcon(android.R.drawable.ic_menu_revert);
			menu.add(Menu.NONE, Menu.FIRST + 3, 1, R.string.label_refresh).setIcon(R.drawable.ic_menu_refresh);
			break;
		case 2:
			break;
		}
		menu.add(Menu.NONE, Menu.FIRST + 5, 5, getString(R.string.omenuitem_quit)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(Menu.NONE, Menu.FIRST + 6, 6, getString(R.string.omenuitem_about)).setIcon(android.R.drawable.ic_menu_help);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		/*
		case Menu.FIRST + 1:
			AsyncUploader.upload(mPrivilege, EntranceActivity.this);
			break;
		*/
		case Menu.FIRST + 1:
			mBrowPage.switchBrowseZoom(item);
			break;
		case Menu.FIRST + 2:
			mBrowPage.resetZoomState();
			break;
		case Menu.FIRST + 3:
			mBrowPage.zrAsyncShowPic(mBrowPage.mId, 0);
			break;
		case Menu.FIRST + 4:
			if (mPrivilege == 0) {
				Toast.makeText(
					this,
					R.string.tips_alreadyloggedin,
					Toast.LENGTH_SHORT
				).show();
			} else {
				Intent intent = new Intent();
				intent.setClass(EntranceActivity.this, RegLoginActivity.class);
				startActivity(intent);
			}
			break;
		case Menu.FIRST + 5:
			mQuitDialog.show();
			break;
		case Menu.FIRST + 6:
			Intent intent = new Intent();
			intent.setClass(EntranceActivity.this, AboutActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (mViewFlipper.getDisplayedChild()) {
			case 0:
				mQuitDialog.show();
				break;
			case 1:
				if (mBrowPage.isDooming()) {
					mBrowPage.getBrow().setOnTouchListener(this);
					mBrowPage.getLayoutCtrl().setOnTouchListener(this);
					mBrowPage.setDooming(false);
					mBrowPage.resetZoomState();
					Toast.makeText(
						this,
						getString(R.string.label_browse),
						Toast.LENGTH_LONG
					).show();
				} else {
					switch (mBrowPage.getReferer()) {
					case R.layout.main:
						int idx = mMainPage.getUsrIndexFromId(mBrowPage.mId, mMainPage.getUsrs());
						int par = idx / mMainPage.getPageLimit() + 1;
						if (mMainPage.getPageBeforeBrow() != mMainPage.getCurPage() || par != mMainPage.getCurParagraph())
						{
							mMainPage.mPrgDlg.show();
							mMainPage.setCurParagraph(par);
							mMainPage.mPageUsrs.clear();
							for (int i = (mMainPage.getCurParagraph() - 1) * mMainPage.getPageLimit(); i < mMainPage.getCurParagraph() * mMainPage.getPageLimit(); i++) {
								mMainPage.mPageUsrs.add(mMainPage.getUsrs().get(i));
							}
							WeibouserInfoGridAdapter adapter = new WeibouserInfoGridAdapter(EntranceActivity.this, mMainPage.mPageUsrs, mMainPage.getGridPics());
							mMainPage.getGridPics().setAdapter(adapter);
							mMainPage.renewCurParagraphTitle();
						}
						break;
					case R.layout.weibo_show:
						break;
					}
					switchPage(mBrowPage.getReferer());
				}
				break;
			case 2:
				switchPage(mWeiboPage.getReferer());
				break;
			}
		}
		//return super.onKeyDown(keyCode, event);
		return false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//super.onSaveInstanceState(outState);
		/*
		 * try to save the instance environment when it's
		 * switched to the background.
		 */
		int idx = -1;
		for (int i = 0; i < mMainPage.getTopicBtns().size(); i++) {
			if (mMainPage.getTopicBtns().get(i).isSelected()) {
				idx = i;
				break;
			}
		}
		outState.putInt("topic", idx);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//super.onRestoreInstanceState(savedInstanceState);
		/*
		 * try to get the instance back from the saved instance
		 * environment data.
		 */
		int topic = savedInstanceState.getInt("topic");
		if (topic != -1 && topic < mMainPage.getTopicBtns().size()) {
			mMainPage.getTopicBtns().get(topic).performClick();
		}
	}

	public static void setPrivilege(Integer privilege) {
		if (privilege < 0 || privilege > 1) {
			privilege = 1;
		}
		EntranceActivity.mPrivilege = privilege;
		switch (privilege) {
		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
	}
	
	public static Integer getAccountId() {
		return mAccountId;
	}
	
	public static void setAccountId(Integer id) {
		mAccountId = id;
	}
	
	public static int getPrivilege() {
		return mPrivilege;
	}
	
	public static boolean isNowLoggingIn() {
		return mNowLoggingIn;
	}
    
    /*
     * get the usable msg from post back text.
     * return value is in a string array:
     * the 1st one indicate whether it's successful or failed,
     * the 2nd one indicate the message body.
     */
    public static String[] getPhpMsg(String result) {
    	if (result == null) return null;
		String msg = /*getString(R.string.tips_nothinghappened);*/result;//for debug
		if (result.indexOf(SYMBOL_FAILED) != -1) {
			msg = result.substring(
				result.indexOf(SYMBOL_FAILED) + SYMBOL_FAILED.length(),
				result.lastIndexOf(SYMBOL_FAILED)
			);
			return new String[] {SYMBOL_FAILED, msg};
		}
		if (result.indexOf(SYMBOL_SUCCESSFUL) != -1) {
			msg = result.substring(
				result.indexOf(SYMBOL_SUCCESSFUL) + SYMBOL_SUCCESSFUL.length(),
				result.lastIndexOf(SYMBOL_SUCCESSFUL)
			);
			return new String[] {SYMBOL_SUCCESSFUL, msg};
		}
		return null;
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case REQUESTCODE_PICKFILE:
			if (resultCode == RESULT_OK) {
				AsyncUploader asyncUploader = new AsyncUploader(this, mAccountId);
				asyncUploader.execute(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    /*
     * Initialize stuff that the app needed, should include:
     * 1.try to get a local key
     * 2.automatically login if remembered
     */
    private class AsyncInit extends AsyncTask <Object, Object, Object>{

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			mNowLoggingIn = false;
			String[] msgs = (String[]) result;
			/*
			for (int i = 0; i < msgs.length; i++) {
				if (!msgs[i].equals("")) {
					Toast.makeText(
						LetuseeActivity.this,
						msgs[i],
						Toast.LENGTH_SHORT
					).show();
				}
			}
			*/
			if (msgs[1].equals(getString(R.string.tips_associated))) {
				Toast.makeText(
					EntranceActivity.this,
					R.string.tips_associated,
					Toast.LENGTH_LONG
				).show();
			}
			if (!msgs[2].equals("")
				&& !msgs[2].equals(getString(R.string.tips_alreadylast))
				&& !msgs[2].equals(getString(R.string.err_wrongversioninfos))
				&& !msgs[2].equals(getString(R.string.err_noversioninfos))
				&& !msgs[2].equals(getString(R.string.err_noversion))
			) {
				String[] infos = msgs[2].split(",");
				Intent intent = new Intent();
				intent.putExtra("code", infos[0]);
				intent.putExtra("name", infos[1]);
				intent.putExtra("newname", infos[2]);
				intent.putExtra("newcontent", getString(R.string.locale).equals("en") ? "" : infos[3]);
				intent.setClass(EntranceActivity.this, UpdateActivity.class);
				startActivity(intent);
			}
			
			Sina sina = WeiboPage.getSina();
			if (sina != null && sina.isLoggedIn()) {
				RegLoginActivity.updateTitle(
					R.id.ivTitleIcon, R.id.tvTitleName,
					sina.getLoggedInUser()
				);
			}
				
			super.onPostExecute(result);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			boolean notAutoLogin = (Boolean)params[0];
			String[] msgs = {"", "", ""};
			/*
			 * try to get the client key
			 */
			SecureURL su = new SecureURL();
			if (mClientKey.equals("")) {
				String msg = PNJ.getResponseByGet(
					URL_SITE + "key.php",
					PNJ.getParamsAsStr("serial", su.phpMd5(SERIAL_APP))
				);
				String ss[] = getPhpMsg(msg);
				if (ss != null && ss[0].equals(SYMBOL_SUCCESSFUL)) {
					mClientKey = ss[1];
					SharedPreferences.Editor edit = mPreferences.edit();
					edit.putString(CONFIG_CLIENTKEY, mClientKey);
					edit.commit();
					msgs[0] = getString(R.string.tips_succeededtogetserial);
				} else {
					msgs[0] = getString(R.string.err_failedtogetserial);
				}
			} else {
				String msg = PNJ.getResponseByGet(
					URL_SITE + "key.php",
					PNJ.getParamsAsStr("serial", su.phpMd5(SERIAL_APP), "key", mClientKey)
				);
				String[] ss = getPhpMsg(msg);
				if (ss != null && ss[0].equals(SYMBOL_SUCCESSFUL)) {
					msgs[0] = getString(R.string.tips_succeededtogetserial);
				} else {
					msgs[0] = getString(R.string.err_failedtoconfirmserial);
				}
			}
			
			/*
	    	 * Auto login part begin
	    	 */
			if (!notAutoLogin) {
				mNowLoggingIn = true;
				ArrayList<String[]> list = EntranceActivity.getStoredAccounts();
				//we auto login the first one in the accounts list here
				if (list.size() != 0) {
					String usr = list.get(0)[0];
					String pwd = list.get(0)[1];
					Sina sina = RegLoginActivity.login(usr, pwd);
					WeiboPage.setSina(sina);
					//if login succeed, then we associate the logged in account with clientkey
					if (sina != null && sina.getTag() != null) {
						int idTips = (Integer)sina.getTag();
						msgs[1] = getString(idTips);
					}
				}
			}
	        /*
	    	 * Auto login part end
	    	 */
	        
	        /*
	         * check if update needed
	         */
	        try {
				PackageInfo info = EntranceActivity.this.getPackageManager().getPackageInfo(EntranceActivity.this.getPackageName(), 0);
				String content = PNJ.getResponseByGet(EntranceActivity.URL_SITE + "ver.php", "");
				if (content != null) {
					String[] infos = content.split(",");
					if (infos.length == 6) {
						String sNewCode = infos[1];
						String sNewName = infos[3];
						String sNewContent = infos[5];
						Integer iNewCode = 0;
						try {
							iNewCode = Integer.parseInt(sNewCode);
						} catch (NumberFormatException e) {
							iNewCode = -1;
						}
						if (iNewCode > info.versionCode) {
							msgs[2] = "" + info.versionCode + "," + info.versionName + "," + sNewName + "," + sNewContent;
						} else {
							msgs[2] = getString(R.string.tips_alreadylast);
						}
					} else {
						msgs[2] = getString(R.string.err_wrongversioninfos);
					}
				} else {
					msgs[2] = getString(R.string.err_noversioninfos);
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msgs[2] = getString(R.string.err_noversion);
			}
			return msgs;
		}
    	
    }
    
    /*
     * GestureListsener zone begin
     */
    public boolean onTouch(View view, MotionEvent event) {
    	switch (mViewFlipper.getDisplayedChild()) {
    	case 0:
    		return mMainPage.getGestureDetector().onTouchEvent(event);
    	case 1:
    		return mBrowPage.getGestureDetector().onTouchEvent(event);
    	case 2:
    		break;
    	}
		return true;
    }
    /*
     * GestureListsener zone end
     */
    
	public BrowPage getBrowPage() {
		return mBrowPage;
	}

	public void setBrowPage(BrowPage mBrowPage) {
		this.mBrowPage = mBrowPage;
	}

	public ViewFlipper getViewFlipper() {
		return mViewFlipper;
	}

	public void setViewFlipper(ViewFlipper mViewFlipper) {
		this.mViewFlipper = mViewFlipper;
	}

	public void switchPage(int layout, Object... params) {
		// TODO Auto-generated method stub
		switch (layout) {
		case R.layout.main:
			mLayoutVote.setVisibility(View.GONE);
			mViewFlipper.setDisplayedChild(0);
			break;
		case R.layout.brow:
			if (params.length == 1) {
				long id = (Long)params[0];
				mBrowPage.zrAsyncShowPic(id, 0);
			}
			mViewFlipper.setDisplayedChild(1);
			break;
		case R.layout.weibo_show:
			if (params.length == 2) {
				long uid = (Long)params[0];
				long _id = (Long)params[1];
				mWeiboPage.setUid(uid);
				mWeiboPage.setId(_id);
				mWeiboPage.reloadAll();
				mWeiboPage.turnDealing(true);
				mBrowPage.zrAsyncShowPic(_id, 0);
			}
			mViewFlipper.setDisplayedChild(2);
			break;
		}
	}

	public WeiboPage getWeiboPage() {
		return mWeiboPage;
	}

	public void setWeiboPage(WeiboPage mWeiboPage) {
		this.mWeiboPage = mWeiboPage;
	}
	
	public TextView getTextUpup() {
		return mTextUpup;
	}
	
	public TextView getTextDwdw() {
		return mTextDwdw;
	}
	
	public TextView getTextVoteRating() {
		return mTextVoteRating;
	}
	
	public ProgressBar getProgressVote() {
		return mProgressVote;
	}
	
	public LinearLayout getLayoutVoteInfo() {
		return mLayoutVoteInfo;
	}
	
	public RelativeLayout getLayoutVote() {
		return mLayoutVote;
	}
	
	public boolean vote(String... params) {
		WeibouserInfo wi = getMainPage().getPicFromId(getBrowPage().mId, getMainPage().getUsrs());
				
		String msg = PNJ.getResponseByGet(
			EntranceActivity.URL_SITE + "vote.php",
			PNJ.getParamsAsStr(params)
		);
		if (msg != null) {
			String ss[] = EntranceActivity.getPhpMsg(msg);
			if (ss != null && ss[0].equals(EntranceActivity.SYMBOL_SUCCESSFUL)) {
				if (ss[1].equals("")) {// means it's never voted
					// do nothing
				} else {
					String[] sRecs = ss[1].split(","); 
					if (sRecs.length == 8) {
						wi.mLastVote = Integer.parseInt(sRecs[0]);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						ParsePosition pp = new ParsePosition(0);
						wi.mLastVoteTime = sdf.parse(sRecs[1], pp);
						wi.clicks = Integer.parseInt(sRecs[2]);
						wi.likes = Integer.parseInt(sRecs[3]);
						wi.dislikes = Integer.parseInt(sRecs[4]);
					}
				}
				return true;
			} else return false;
		} else return false;
	}
	
	/*
	 * try to vote under background by using AsyncTask
	 */
	private class AsyncVoter extends AsyncTask <String, Object, Boolean> {
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return vote(params);
		}
		
	}
}