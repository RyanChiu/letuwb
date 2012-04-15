package com.zrd.zr.letuwb;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.zrd.zr.pnj.PNJ;
import com.zrd.zr.pnj.SecureURL;
import com.zrd.zr.pnj.ThreadPNJDealer;
import com.zrd.zr.protos.WeibousersProtos.UCMappings;
import com.zrd.zr.protos.WeibousersProtos.Weibouser;
import com.zrd.zr.protos.WeibousersProtos.Weibousers;
import com.zrd.zr.weiboes.Sina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage {

	private EntranceActivity parent;
	/*
	 * members for layout "main"
	 * start
	 */
	//components
	private Button mBtnRandom;
	private Button mBtnLatest;
	private Button mBtnHottest;
	private Button mBtnUnhottest;
	private Button mBtnPossessions;
	private LinearLayout mLinearMainBottom;
	
	private ScrollView mScrollMain;
	private LinearLayout mLinearLeft;
	private LinearLayout mLinearMid;
	private LinearLayout mLinearRight;
	//assistants
	private ArrayList<Button> mTopicBtns = null;
	public static SharedPreferences mPreferences = null;
	private int _index = 0;
	
	private Handler mHandler = null;
	
	private static Integer mTopicChoice = 0;
	private static final Integer mLimit = 12;//how many pictures should be get in an expanding page
	private static Integer mCurPage = 1;
	private Integer mPageBeforeBrow = 1;
	private static Integer mTotalPics = 0;
	private static ArrayList<String> mCurTerms = new ArrayList<String>();
	private static ArrayList<WeibouserInfo> mUsrs = new ArrayList<WeibouserInfo>();
	/*
	 * members for layout "main"
	 * end
	 */
	
	MainPage(EntranceActivity activity) {
		this.parent = activity;
        mBtnRandom = (Button) activity.findViewById(R.id.btnRandom);
        mBtnLatest = (Button) activity.findViewById(R.id.btnLatest);
        mBtnHottest = (Button) activity.findViewById(R.id.btnHottest);
        mBtnUnhottest = (Button) activity.findViewById(R.id.btnUnhottest);
        mBtnPossessions = (Button) activity.findViewById(R.id.btnPossessions);
        mTopicBtns = new ArrayList<Button>();
        getTopicBtns().add(mBtnLatest);
        getTopicBtns().add(mBtnHottest);
        getTopicBtns().add(mBtnRandom);
        getTopicBtns().add(mBtnUnhottest);
        getTopicBtns().add(mBtnPossessions);
        mLinearMainBottom = (LinearLayout) activity.findViewById(R.id.linearLayoutMainBottom);
        
        mScrollMain = (ScrollView) activity.findViewById(R.id.svMain);
        mLinearLeft = (LinearLayout) activity.findViewById(R.id.llLeft);
        mLinearMid = (LinearLayout) activity.findViewById(R.id.llMid);
        mLinearRight = (LinearLayout) activity.findViewById(R.id.llRight);
        
        __init();
        
        mHandler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ThreadPNJDealer.DEL_POSSESSION:
					UCMappings mappings = 
						(UCMappings) msg.getData().getSerializable(ThreadPNJDealer.KEY_DATA);
					if (mappings.getFlag() > 0) {
						//TODO: get rid of the selected possession here
						
						renewCurParagraphTitle();
						Toast.makeText(
							parent,
							R.string.tips_possessionremoved,
							Toast.LENGTH_SHORT
						).show();
						return;
					}
					break;
				}
			}
        };

	}
	
	private void __init() {
        mLinearMainBottom.setVisibility(LinearLayout.GONE);
		/*
		 * actions
		 */

	    mBtnRandom.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBtnRandom.setSelected(true);
				mBtnLatest.setSelected(false);
				mBtnHottest.setSelected(false);
				mBtnUnhottest.setSelected(false);
				mBtnPossessions.setSelected(false);
				
				mCurTerms.clear();
		        mCurTerms.add("top");
		        mCurTerms.add("6");
		        mCurPage = 1;
		        initShow(mCurTerms.get(0), mCurTerms.get(1));
			}
	    	
	    });
	    
	    mBtnLatest.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBtnRandom.setSelected(false);
				mBtnLatest.setSelected(true);
				mBtnHottest.setSelected(false);
				mBtnUnhottest.setSelected(false);
				mBtnPossessions.setSelected(false);
				
				mCurTerms.clear();
		        mCurTerms.add("top");
		        mCurTerms.add("0");
		        mCurPage = 1;
		        initShow(mCurTerms.get(0), mCurTerms.get(1));
			}
	    	
	    });
	    
	    mBtnHottest.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBtnRandom.setSelected(false);
				mBtnLatest.setSelected(false);
				mBtnHottest.setSelected(true);
				mBtnUnhottest.setSelected(false);
				mBtnPossessions.setSelected(false);
				
				Toast.makeText(
					parent,
					R.string.tips_hottesttheweek,
					Toast.LENGTH_LONG
				).show();
				
				mCurTerms.clear();
		        mCurTerms.add("top");
		        mCurTerms.add("4");
		        mCurPage = 1;
		        initShow(mCurTerms.get(0), mCurTerms.get(1));
			}
	    	
	    });
	    
	    mBtnUnhottest.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBtnRandom.setSelected(false);
				mBtnLatest.setSelected(false);
				mBtnHottest.setSelected(false);
				mBtnUnhottest.setSelected(true);
				mBtnPossessions.setSelected(false);
				
				Toast.makeText(
					parent,
					R.string.tips_unhottesttheweek,
					Toast.LENGTH_LONG
				).show();
				
				mCurTerms.clear();
		        mCurTerms.add("top");
		        mCurTerms.add("5");
		        mCurPage = 1;
		        initShow(mCurTerms.get(0), mCurTerms.get(1));
			}
	    	
	    });
	    
	    mBtnPossessions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBtnRandom.setSelected(false);
				mBtnLatest.setSelected(false);
				mBtnHottest.setSelected(false);
				mBtnUnhottest.setSelected(false);
				mBtnPossessions.setSelected(true);
				
				Toast.makeText(
					parent,
					R.string.tips_possessions,
					Toast.LENGTH_LONG
				).show();
				
				mCurTerms.clear();
		        mCurTerms.add("clientkey");
		        mCurTerms.add(EntranceActivity.getClientKey());
		        mCurPage = 1;
		        initShow(mCurTerms.get(0), mCurTerms.get(1));
			}
	    	
	    });
	    
		mScrollMain.setOnTouchListener(new OnTouchListener() {

			@Override
			//public boolean onTouch(View arg0, MotionEvent arg1) {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN :
					break;
				case MotionEvent.ACTION_MOVE :
					_index++;
					break;
				default :
					break;
			}
			if (event.getAction() == MotionEvent.ACTION_UP &&  _index > 0) {
				_index = 0;
				View view = ((ScrollView) v).getChildAt(0);
				if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
					/*
					 * put the process codes of "scroll to the end" bellow
					 */
					Toast.makeText(
						parent,
						R.string.tips_loading,
						Toast.LENGTH_SHORT
					).show();
					asyncExpand();
				}
			}
			return false;
			}
        	
        });
	}
	
	/*
	 * methods
	 */
	public Integer getLimit() {
		return mLimit;
	}
	
	public Integer getCurPage() {
		return mCurPage;
	}
	
	public Integer getTotalPics() {
		return mTotalPics;
	}
	
	public void setTotalPics(int total) {
		mTotalPics = total;
	}
	
	public ArrayList<WeibouserInfo> getUsrs() {
		return mUsrs;
	}
	
	public void setUsrs(ArrayList<WeibouserInfo> pics) {
		mUsrs = pics;
	}
		
	/*
	 * get index of current usr in mUsrs by id
	 */
	public int getUsrIndexFromId(long id, List<WeibouserInfo> usrs) {
		if (usrs == null) return -1;
		if (usrs.size() == 0) return -1;
		int i;
		for (i = 0; i < usrs.size(); i++) {
			WeibouserInfo wi = (WeibouserInfo) usrs.get(i); 
			if (id == wi.id) {
				break;
			}
		}
		if (i == usrs.size()) return -1;
		return i;
	}
	
	/*
	 * get picfileInfo from mUsrs by id
	 */
	public WeibouserInfo getPicFromId(long id, List<WeibouserInfo> pics) {
		int idx = getUsrIndexFromId(id, pics);
		if (idx < 0 || idx >= pics.size()) return null;
		return pics.get(idx);
	}
	
    public ArrayList<WeibouserInfo> getPics(String... params) {
    	ArrayList<WeibouserInfo> usrs = new ArrayList<WeibouserInfo>();
    	
    	String sParams = PNJ.getParamsAsStr(params);
    	SecureURL su = new SecureURL();
    	URLConnection conn = su.getConnection(EntranceActivity.URL_SITE + "picsinfo.php?" + sParams);
    	if (conn == null) return usrs;
    	try {
	    	conn.connect();
	    	InputStream is = conn.getInputStream();
	    	Weibousers pbUsrs = Weibousers.parseFrom(is);
	    	long id, uid;
	    	for (Weibouser pbUsr: pbUsrs.getUsrList()) {
	    		try {
		    		id = Long.parseLong(pbUsr.getId());
		    		uid = Long.parseLong(pbUsr.getUid());
		    	} catch (NumberFormatException e) {
		    		id = uid = 0;
		    	}
				WeibouserInfo wi = new WeibouserInfo(
					id, uid, pbUsr.getScreenName(),
					pbUsr.getName(), pbUsr.getProvince(), pbUsr.getCity(),
					pbUsr.getLocation(), pbUsr.getDescription(), pbUsr.getUrl(),
					pbUsr.getProfileImageUrl(), pbUsr.getDomain(), pbUsr.getGender(),
					(long)pbUsr.getFollowersCount(), (long)pbUsr.getFriendsCount(), 
					(long)pbUsr.getStatusesCount(), (long)pbUsr.getFavouritesCount(), 
					pbUsr.getCreatedAt(), pbUsr.getFollowing(),
					pbUsr.getAllowAllActMsg(), pbUsr.getGeoEnabled(), pbUsr.getVerified(), 
					pbUsr.getStatusId(),
					pbUsr.getClicks(), pbUsr.getLikes(), pbUsr.getDislikes());
				usrs.add(wi);
	    	}
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usrs;
    }
    
    /*
     * using protobuf structure to get users information from DB
     */
    public UCMappings updateUser(String... params) {
    	SecureURL su = new SecureURL();
    	URLConnection conn = su.getConnection(
    			EntranceActivity.URL_SITE + "updusr.php"
    		+ PNJ.getParamsAsStr(params)
    	);
    	if (conn == null) return null;
    	try {
			conn.connect();
			InputStream is = conn.getInputStream();
			return UCMappings.parseFrom(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /*
     * renew the current paragraph informations
     */
    public void renewCurParagraphTitle() {
		mLinearMainBottom.setVisibility(LinearLayout.VISIBLE);
		AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);
		anim.setDuration(300);
		mLinearMainBottom.startAnimation(anim);
    }
    
	public void asyncExpand(String... params) {
		AsyncExpand expand = new AsyncExpand();
		expand.execute(params);
    }
    
    public Handler getHandler() {
    	return mHandler;
    }

	public static Integer getTopicChoice() {
		return mTopicChoice;
	}

	public void setTopicChoice(Integer mTopicChoice) {
		MainPage.mTopicChoice = mTopicChoice;
	}

	public Integer getPageBeforeBrow() {
		return mPageBeforeBrow;
	}

	public void setPageBeforeBrow(Integer mPageBeforeBrow) {
		this.mPageBeforeBrow = mPageBeforeBrow;
	}

	public ArrayList<Button> getTopicBtns() {
		return mTopicBtns;
	}

    /*
	 * get total pages number
	 */
    public int getTotalPagesNum() {
		String sBackMsg = "";
		sBackMsg = PNJ.getResponseByGet(
			EntranceActivity.URL_SITE + "stats.php",
			PNJ.getParamsAsStr("total", "pages", "limit", mLimit.toString())
		);
		if (sBackMsg != null) {
			String ss[] = EntranceActivity.getPhpMsg(sBackMsg);
			if (ss != null && ss[0].equals(EntranceActivity.SYMBOL_SUCCESSFUL)) {
				sBackMsg = ss[1];
			} else {
				sBackMsg = "-2";
			}
		} else {
			sBackMsg = "0";
		}
		int i = 0;
		try {
			i = Integer.parseInt(sBackMsg);
		} catch (NumberFormatException e) {
			i = -1;
		}
		return i;
    }
    
    /*
     * get total pictures number
     */
    public static int getTotalPicsNum(String... params) {
 		String sBackMsg = "";
 		if (params.length == 0) {
			sBackMsg = PNJ.getResponseByGet(
				EntranceActivity.URL_SITE + "stats.php",
				PNJ.getParamsAsStr("total", "usrs")
			);
 		} else {
 			sBackMsg = PNJ.getResponseByGet(
				EntranceActivity.URL_SITE + "stats.php",
				PNJ.getParamsAsStr("total", "usrs", "clientkey", params[0])
			);
 		}
		if (sBackMsg != null) {
			String ss[] = EntranceActivity.getPhpMsg(sBackMsg);
			if (ss != null && ss[0].equals(EntranceActivity.SYMBOL_SUCCESSFUL)) {
				sBackMsg = ss[1];
			} else {
				return mUsrs.size();
			}
		} else {
			return mUsrs.size();
		}
		int i = 0;
		try {
			i = Integer.parseInt(sBackMsg);
		} catch (NumberFormatException e) {
			return mUsrs.size();
		}
		return i;
    }

	/*
	 * add "pinterest" liked view
	 */
	public void addPinterestView(LinearLayout linear, WeibouserInfo wi) {
		LayoutInflater inflater = parent.getLayoutInflater();
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.pinterest_item, null);
		ImageView img = (ImageView) ll.findViewById(R.id.ivPinterest);
		TextView text = (TextView) ll.findViewById(R.id.tvPinterest);
		
		AsyncImageLoader imgLoader = new AsyncImageLoader(
			parent, 
			img, 
			R.drawable.icon_gray
		);
		URL url;
		try {
			url = new URL(wi.getBigger_profile_image_url());
			imgLoader.execute(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		text.setText(
			Html.fromHtml(
				"<font color='#1da7ef'>"
				+ wi.description
				+ "</font>"
			)
		);
		
		ll.setTag(getUsrIndexFromId(wi.id, mUsrs));
		ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Sina sina = WeiboPage.getSina();
				if (EntranceActivity.isNowLoggingIn()) {
					Toast.makeText(
						parent,
						R.string.tips_nowisloggingin,
						Toast.LENGTH_LONG
					).show();
				} else {
					if (sina != null && sina.isLoggedIn()) {
						WeibouserInfo wi = mUsrs.get((Integer) arg0.getTag());
						if (mBtnPossessions.isSelected()) {
							parent.getWeiboPage().setReferer(R.layout.main);
							parent.getWeiboPage().reloadLastUser(wi.uid);
							parent.switchPage(R.layout.weibo_show, wi.uid, wi.id);
						} else {
							parent.getBrowPage().setReferer(R.layout.main);
							parent.switchPage(R.layout.brow, wi.id);
						}
					} else {
						Toast.makeText(
							parent,
							R.string.tips_havetologin,
							Toast.LENGTH_LONG
						).show();
						Intent intent = new Intent();
						intent.setClass(parent, RegLoginActivity.class);
						parent.startActivity(intent);
					}
				}
			}
			
		});
		
		linear.addView(ll);
	}
	
	/*
	 * expand new users to mUsrs
	 */
	public ArrayList<WeibouserInfo> expandUsrs(String... params) {
		ArrayList<WeibouserInfo> usrs = getPics(params);
		
		int num;
		if (!mBtnPossessions.isSelected()) {
			num = getTotalPicsNum();
			if (num < 0) {
				Toast.makeText(
					parent,
					R.string.err_noconnection,
					Toast.LENGTH_LONG
				).show();
				mTotalPics = 0;
			} else {
				mTotalPics = num;
			}
		} else {
			num = getTotalPicsNum(EntranceActivity.getClientKey());
			if (num < 0) {
				Toast.makeText(
					parent,
					R.string.err_noconnection,
					Toast.LENGTH_LONG
				).show();
				mTotalPics = 0;
			} else {
				mTotalPics = num;
			}
		}
		
		for (int i = 0; i < usrs.size(); i++) {
			mUsrs.add(usrs.get(i));
		}
		
		return usrs;
	}
	
	/*
	 * show the very first pinterest pieces
	 */
	public void initShow(String termName, String termValue) {
		asyncExpand(termName, termValue);
	}
	
	/*
	 * expand the contents of the parameter "list" to the pinterest
	 */
	public void addPinterests(ArrayList<WeibouserInfo> list) {
		for (int i = 0; i < list.size(); i += 3) {
			WeibouserInfo wi;
			wi = list.get(i);
			addPinterestView(mLinearLeft, wi);
			if (i + 1 < list.size()) {
				wi = list.get(i + 1);
				addPinterestView(mLinearMid, wi);
			}
			if (i + 2 < list.size()) {
				wi = list.get(i + 2);
				addPinterestView(mLinearRight, wi);
			}
		}
	}

	/*
     * classes
     */
	class AsyncExpand extends AsyncTask<String, Object, ArrayList<WeibouserInfo>> {

		@Override
		protected void onPostExecute(ArrayList<WeibouserInfo> result) {
			// TODO Auto-generated method stub
			if (mUsrs.size() == mLimit) {
				mLinearLeft.removeAllViews();
				mLinearMid.removeAllViews();
				mLinearRight.removeAllViews();
			}
			addPinterests(result);
			renewCurParagraphTitle();
			
			super.onPostExecute(result);
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 * if no params at all, it'll only expand a number (of mLimit) of
		 * weibousers to the pinterest; if there are 2 params, it'll clear
		 * the pinterest and expand a number (of mLimit) of weibousers at
		 * the very beginning.
		 */
		@Override
		protected ArrayList<WeibouserInfo> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<WeibouserInfo> usrs = new ArrayList<WeibouserInfo>();
			if (params.length == 0) {
				mCurPage++;
				usrs = expandUsrs(
					mCurTerms.get(0), mCurTerms.get(1),
					"limit", mLimit.toString(),
					"page", mCurPage.toString()
				);
			} else if (params.length == 2){
				String termName = params[0];
				String termValue = params[1];
				mUsrs.clear();
				mCurPage = 1;
				usrs = expandUsrs(
					termName, termValue,
					"limit", mLimit.toString(), 
					"page", mCurPage.toString()
				);
			}
			
			return usrs;
		}
		
	}
}
