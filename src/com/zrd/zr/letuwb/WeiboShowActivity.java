package com.zrd.zr.letuwb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weibo4android.Comment;
import weibo4android.Status;
import weibo4android.User;
import weibo4android.WeiboException;

import com.zrd.zr.weiboes.Sina;
import com.zrd.zr.weiboes.ThreadSinaDealer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class WeiboShowActivity extends Activity {
	
	protected static final int COUNT_PERPAGE_TIMELINE = 20;
	protected static final int COUNT_PERPAGE_COMMENTS = 20;
	private TextView mTextScreenName;
	private ImageView mImageVerified;
	private TextView mTextCreatedAt;
	private TextView mTextLocation;
	private ImageButton mBtnDescription;
	private TextView mTextCounts;
	private ListView mListStatus;
	private ProgressBar mProgressStatusLoading;
	private Button mBtnFriend;
	private Button mBtnFavorite;
	private Button mBtnRepost;
	private Button mBtnMore;
	private ImageButton mBtnExchange;
	private ImageView mBtnTinyProfileImage;
	
	private AlertDialog mDlgRepost;
	private EditText mEditRepost;
	private AlertDialog mDlgComment;
	private EditText mEditComment;
	private Button mBtnMoreTimelines;
	private Button mBtnMoreComments;
	private Dialog mDlgDescription;
	private Dialog mDlgComments;
	private Dialog mDlgMore;
	
	private Long mUid = null;
	private Long mId = null;
	private static Sina mSina = null;
	private User mLastUser = null;
	private List<Sina.XStatus> mLastUserTimeline = new ArrayList<Sina.XStatus>();
	private int mIndexOfSelectedStatus = -1;
	private List<Comment> mLastComments = new ArrayList<Comment>();
	
	//private AlphaAnimation mAnimFadein = new AlphaAnimation(0.1f, 1.0f);
	//private AlphaAnimation mAnimFadeout = new AlphaAnimation(1.0f, 0.1f);
	
	/*
	 * Handler for showing all kinds of SINA_weibo data from background thread.
	 */
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			Sina sina = (Sina)msg.getData().getSerializable(ThreadSinaDealer.KEY_SINA);
			if (sina == null) {
				sina = mSina;
			} else {
				setSina(sina);
			}
			WeiboException wexp = (WeiboException)msg.getData().getSerializable(ThreadSinaDealer.KEY_WEIBO_ERR);
			User user;
			Status status;
			Comment comment;
			switch (msg.what) {
			case ThreadSinaDealer.SHOW_USER:
				mLastUser = (User)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (mLastUser != null) {			
					/*
					 * show the profile-image
					 */
					AsyncImageLoader ail = new AsyncImageLoader(
						WeiboShowActivity.this,
						R.id.btnTinyProfileImage,
						R.drawable.person
					);
					ail.execute(mLastUser.getProfileImageURL());
					
					/*
					 * show the screen name
					 */
					mTextScreenName.setText(mLastUser.getScreenName());
					
					/*
					 * show "v" if verified
					 */
					if (mLastUser.isVerified()) {
						mImageVerified.setVisibility(ImageView.VISIBLE);
					} else {
						mImageVerified.setVisibility(ImageView.GONE);
					}
					
					/*
					 * show when was the user created
					 */
					Date dtCreatedAt = mLastUser.getCreatedAt();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					mTextCreatedAt.setText(sdf.format(dtCreatedAt));
					
					/*
					 * show the location and the description
					 */
					mTextLocation.setText(
						mLastUser.getLocation()
					);
					String description = mLastUser.getDescription();
					if (!description.equals("")) {
						mBtnDescription.setVisibility(ImageButton.VISIBLE);
						mBtnDescription.setTag(description);
					} else {
						mBtnDescription.setVisibility(ImageButton.GONE);
						mBtnDescription.setTag(null);
					}
					
					/*
					 * show all kinds of the counts
					 */
					mTextCounts.setText(
						getString(R.string.label_microblogs) + ":" + mLastUser.getStatusesCount()
						+ " " 
						+ getString(R.string.label_favorites) + ":" + mLastUser.getFavouritesCount()
						+ " "
						+ getString(R.string.label_followers) + ":" + mLastUser.getFollowersCount()
						+ " " 
						+ getString(R.string.label_friends) + ":" + mLastUser.getFriendsCount()
					);
				} else {
					mTextCreatedAt.setText("Please try again...");
					
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				break;
			case ThreadSinaDealer.GET_USER_TIMELINE:
				ArrayList<Sina.XStatus> xstatuses = (ArrayList<Sina.XStatus>)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (xstatuses != null) {
					for (int i = 0; i < xstatuses.size(); i++) {
						mLastUserTimeline.add(xstatuses.get(i));
					}
					/*
					 * show the user time_line
					 */
					WeiboStatusListAdapter adapter = new WeiboStatusListAdapter(
						WeiboShowActivity.this,
						getStatusData(ThreadSinaDealer.GET_USER_TIMELINE)
					);
					mListStatus.setAdapter(adapter);
					mListStatus.setSelection(
						(getLastUserTimelineTotalPage() - 1) * COUNT_PERPAGE_TIMELINE
					);
				} else {
					//deal with failing to get time_line
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				turnDealing(false);
				break;
			case ThreadSinaDealer.CREATE_FRIENDSHIP:
				user = (User)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (user != null) {
					if (!user.equals(mSina.getLoggedInUser())) {
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_friendsmade,
							Toast.LENGTH_LONG
						).show();
					} else {
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_friendsalready,
							Toast.LENGTH_LONG
						).show();
					}
				} else {
					//deal with failing to make friends
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				turnDealing(false);
				break;
			case ThreadSinaDealer.CREATE_FAVORITE:
				status = (Status)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (status != null) {
					Toast.makeText(
						WeiboShowActivity.this,
						R.string.tips_favoritemade,
						Toast.LENGTH_LONG
					).show();
				} else {
					//deal with failing to make favorite
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				turnDealing(false);
				break;
			case ThreadSinaDealer.REPOST:
				status = (Status)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (status != null) {
					Toast.makeText(
						WeiboShowActivity.this,
						R.string.tips_reposted,
						Toast.LENGTH_LONG
					).show();
				} else {
					//deal with failing to make favorite
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				turnDealing(false);
				break;
			case ThreadSinaDealer.GET_COMMENTS:
				ArrayList<Comment> comments = (ArrayList<Comment>)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (comments != null) {
					if (comments.size() != 0) {
						for (int i = 0; i < comments.size(); i++) {
							mLastComments.add(comments.get(i));
						}
						if (mLastComments.size() != 0) {
							ListView lv = (ListView)mDlgComments.findViewById(R.id.lvCustomList);
							lv.removeFooterView(mBtnMoreComments);
							lv.addFooterView(mBtnMoreComments);
							ArrayList<String> contents = new ArrayList<String>();
							WeiboStatusListAdapter _tmp = 
								new WeiboStatusListAdapter(WeiboShowActivity.this, null);
							for (int i = 0; i < mLastComments.size(); i++) {
								user = mLastComments.get(i).getUser();
								contents.add(
									"[" + (i + 1) + "] "
									+ (user != null ? user.getScreenName() : "")
									+ "(" + _tmp.getSpecialDateText(mLastComments.get(i).getCreatedAt(), 0) + "):\n"
									+ mLastComments.get(i).getText()
								);
							}
							lv.setAdapter(
								new ArrayAdapter<String>(
									WeiboShowActivity.this,
									R.layout.item_custom_dialog_list,
									contents
								)
							);
							lv.setSelection(
								(getLastCommentsTotalPage() - 1) * COUNT_PERPAGE_COMMENTS
							);
						} else {
							Toast.makeText(
								WeiboShowActivity.this,
								R.string.tips_nocomments,
								Toast.LENGTH_LONG
							).show();
						}
					} else {
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_nomorecomments,
							Toast.LENGTH_LONG
						).show();
						mBtnMoreComments.setTag(2);
						mBtnMoreComments.setEnabled(false);
					}
				} else {
					//deal with failing to get comments
					Integer flag = (Integer)mBtnMoreComments.getTag();
					if (flag != null && flag != 0) {
						//if mBtnMoreComments was clicked, then do something here
						mDlgComments.dismiss();
					} else {
						//if mBtnMoreComments was not clicked, do something here
						
					}
					
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				turnDealing(false);
				break;
			case ThreadSinaDealer.UPDATE_COMMENT:
				comment = (Comment)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
				if (comment != null) {
					Toast.makeText(
						WeiboShowActivity.this,
						R.string.tips_commented,
						Toast.LENGTH_LONG
					).show();
				} else {
					//deal with failing to make favorite
					if (wexp != null) {
						Toast.makeText(
							WeiboShowActivity.this,
							wexp.toString(),
							Toast.LENGTH_LONG
						).show();
					}
				}
				turnDealing(false);
				break;
			}
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.weibo_show);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.exchangelist_title);
		RegLoginActivity.addContext(WeiboShowActivity.this);
		mBtnExchange = (ImageButton) findViewById(R.id.btnExchange);
        mTextScreenName = (TextView)findViewById(R.id.tvScreenName);
		mImageVerified = (ImageView)findViewById(R.id.ivVerified);
		mTextCreatedAt = (TextView)findViewById(R.id.tvCreatedAt);
		mTextLocation = (TextView)findViewById(R.id.tvLocation);
		mBtnDescription = (ImageButton)findViewById(R.id.btnDescription);
		mTextCounts = (TextView)findViewById(R.id.tvCounts);
		mListStatus = (ListView)findViewById(R.id.lvStatus);
		mProgressStatusLoading = (ProgressBar)findViewById(R.id.pbStatusLoading);
		mBtnFriend = (Button)findViewById(R.id.btnFriend);
		mBtnFavorite = (Button)findViewById(R.id.btnFavorite);
		mBtnRepost = (Button)findViewById(R.id.btnRepost);
		mEditRepost  = new EditText(this);
		mBtnMore = (Button)findViewById(R.id.btnMore);
		mEditComment = new EditText(this);
		mBtnTinyProfileImage = (ImageButton)findViewById(R.id.btnTinyProfileImage);
		
		mImageVerified.setVisibility(ImageView.GONE);
		mBtnDescription.setVisibility(ImageButton.GONE);
			
		mBtnMoreTimelines = new Button(this);
		mBtnMoreTimelines.setText(R.string.label_getmore);
		mBtnMoreTimelines.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(
					new ThreadSinaDealer(
						mSina,
						ThreadSinaDealer.GET_USER_TIMELINE,
						new String[] {
							mUid.toString(), 
							"" + (getLastUserTimelineTotalPage() + 1), 
							"" + COUNT_PERPAGE_TIMELINE
						},
						mHandler
					)
				).start();
				turnDealing(true);
			}
			
		});
		mListStatus.addFooterView(mBtnMoreTimelines);
		
		mBtnMoreComments = new Button(this);
		mBtnMoreComments.setText(R.string.label_getmore);
		/*
		 * 0 means not clicked
		 * 1 means clicked
		 * 2 means should not be clicked till next comments showing up
		 */
		mBtnMoreComments.setTag(0);
		mBtnMoreComments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long sid;
				if (mLastUserTimeline == null) {
					sid = mLastUser.getStatus().getId();
				} else {
					sid = mLastUserTimeline.get(mIndexOfSelectedStatus).getStatus().getId();
				}
				new Thread(
					new ThreadSinaDealer(
						mSina,
						ThreadSinaDealer.GET_COMMENTS,
						new String[] {
							"" + sid,
							"" + (getLastCommentsTotalPage() + 1),
							"" + COUNT_PERPAGE_COMMENTS
						},
						mHandler
					)
				).start();
				turnDealing(true);
				mBtnMoreComments.setTag(1);
			}
			
		});
		
		/*
		 * show the whole user/info
		 */
		Intent intent = getIntent();
		mUid = intent.getLongExtra("uid", 0);
		mId = intent.getLongExtra("id", 0);
		reloadAll();
		turnDealing(true);
		
		/*
		 * initialize the title bar
		 */
		Sina sina = WeiboShowActivity.getSina();
		if (sina != null && sina.isLoggedIn()) {
			RegLoginActivity.updateTitle(
				R.id.ivTitleIcon, R.id.tvTitleName,
				sina.getLoggedInUser()
			);
		}
		
		mBtnTinyProfileImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(WeiboShowActivity.this, PicbrowActivity.class);
				intent.putExtra("id", mId);
				startActivity(intent);
			}
			
		});
		
		mDlgRepost = new AlertDialog.Builder(this)
			.setTitle(R.string.title_repost)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(mEditRepost)
			.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
	
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					/*
					 * This is the place it handles reposting
					 */
					long sid;
					if (mLastUserTimeline == null) {
						sid = mLastUser.getStatus().getId();
					} else {
						sid = mLastUserTimeline.get(mIndexOfSelectedStatus).getStatus().getId();
					}
					new Thread(
						new ThreadSinaDealer(
							mSina,
							ThreadSinaDealer.REPOST,
							new String[] {"" + sid, mEditRepost.getText().toString()},
							mHandler
						)
					).start();
					turnDealing(true);
				}
				
			})
			.setNegativeButton(R.string.label_cancel, null)
			.create();
		
		mDlgComment = new AlertDialog.Builder(this)
			.setTitle(R.string.title_comment)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(mEditComment)
			.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					/*
					 * This is the place it handles comment
					 */
					long sid;
					if (mLastUserTimeline == null) {
						sid = mLastUser.getStatus().getId();
					} else {
						sid = mLastUserTimeline.get(mIndexOfSelectedStatus).getStatus().getId();
					}
					new Thread(
						new ThreadSinaDealer(
							mSina,
							ThreadSinaDealer.UPDATE_COMMENT,
							new String[] {mEditComment.getText().toString(), "" + sid, null},
							mHandler
						)
					).start();
					turnDealing(true);
				}
				
			})
			.setNegativeButton(R.string.label_cancel, null)
			.create();
		
		mDlgDescription = new Dialog(this, R.style.Dialog_Clean);
		mDlgDescription.setContentView(R.layout.custom_dialog_list);
		
		mDlgComments = new Dialog(this, R.style.Dialog_Clean);
		mDlgComments.setContentView(R.layout.custom_dialog_list);
		ListView lvComments = (ListView)mDlgComments.findViewById(R.id.lvCustomList);
		lvComments.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mDlgComments.dismiss();
			}
			
		});

		mBtnExchange.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(WeiboShowActivity.this, ExchangeListActivity.class);
                startActivity(intent);
            }
        });
		
		/*
		 * deal actions for components
		 */
		mListStatus.setTag((long)0);
		mListStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				int position = arg2;
				HeaderViewListAdapter ha = (HeaderViewListAdapter)parent.getAdapter();
				WeiboStatusListAdapter adapter  = (WeiboStatusListAdapter)ha.getWrappedAdapter();
				adapter.setSelectedItem(position);
				adapter.notifyDataSetInvalidated();
				mIndexOfSelectedStatus = position;
				
				long lastClickTime;
				/*
				 * check if it's double click
				 */
				lastClickTime = (Long)mListStatus.getTag();
				if (Math.abs(lastClickTime-System.currentTimeMillis()) < 2000) {
					mListStatus.setTag((long)0);
					//to do some double click stuff here
					showComments();
					turnDealing(true);
				} else {
					mListStatus.setTag(System.currentTimeMillis());
				}
			}
			
		});
		
		mListStatus.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				//when it's not scrolling
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					//if it's already to the bottom
					if(view.getLastVisiblePosition()==(view.getCount()-1)){
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_alreadylastone,
							Toast.LENGTH_LONG
						).show();
					}
				}
			}
			
		});

		mBtnDescription.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListView lv = (ListView)mDlgDescription.findViewById(R.id.lvCustomList);
				ArrayList<String> list = new ArrayList<String>();
				String description = (String)mBtnDescription.getTag();
				if (description == null) description = "";
				list.add(description);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					WeiboShowActivity.this,
					R.layout.item_custom_dialog_list,
					list
				);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						mDlgDescription.dismiss();
					}
					
				});
				mDlgDescription.show();
			}
			
		});
		
		mBtnFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mSina != null && mSina.isLoggedIn()) {
					new Thread(
						new ThreadSinaDealer(
							mSina,
							ThreadSinaDealer.CREATE_FRIENDSHIP,
							new String[] {mUid.toString()},
							mHandler
						)
					).start();
					turnDealing(true);
				} else {
					RegLoginActivity.shallWeLogin(R.string.title_loginfirst, WeiboShowActivity.this);
				}
			}
			
		});
		
		mBtnFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mSina != null && mSina.isLoggedIn()) {
					if (mIndexOfSelectedStatus != -1) {
						/*
						 * make it favorite
						 */
						if (mLastUserTimeline == null) {
							new Thread(
								new ThreadSinaDealer(
									mSina,
									ThreadSinaDealer.CREATE_FAVORITE,
									new String[] {"" + mLastUser.getStatus().getId()},
									mHandler
								)
							).start();
						} else {
							new Thread(
								new ThreadSinaDealer(
									mSina,
									ThreadSinaDealer.CREATE_FAVORITE,
									new String[] {"" + mLastUserTimeline.get(mIndexOfSelectedStatus).getStatus().getId()},
									mHandler
								)
							).start();
						}
						turnDealing(true);
					} else {
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_noitemselected,
							Toast.LENGTH_LONG
						).show();
					}
				} else {
					RegLoginActivity.shallWeLogin(R.string.title_loginfirst, WeiboShowActivity.this);
				}
			}
			
		});
		
		mBtnRepost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mSina != null && mSina.isLoggedIn()) {
					if (mIndexOfSelectedStatus == -1) {
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_noitemselected,
							Toast.LENGTH_LONG
						).show();
						return;
					}
					
					mDlgRepost.show();
				} else {
					RegLoginActivity.shallWeLogin(R.string.title_loginfirst, WeiboShowActivity.this);
				}
			}
			
		});
		
		mDlgMore = new Dialog(this, R.style.Dialog_Clean);
		mDlgMore.setContentView(R.layout.custom_dialog_list);
		ListView lvMore = (ListView)mDlgMore.findViewById(R.id.lvCustomList);
		ArrayList<String> mlist = new ArrayList<String>();
		mlist.add(getString(R.string.label_comment));
		mlist.add(getString(R.string.label_comments));
		mlist.add(getString(R.string.label_reload));
		lvMore.setAdapter(
			new ArrayAdapter<String> (
				this,
				R.layout.item_custom_dialog_list,
				mlist
			)
		);
		lvMore.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					if (mSina != null && mSina.isLoggedIn()) {
						if (mIndexOfSelectedStatus == -1) {
							Toast.makeText(
								WeiboShowActivity.this,
								R.string.tips_noitemselected,
								Toast.LENGTH_LONG
							).show();
							return;
						}
						
						mDlgComment.show();
					} else {
						RegLoginActivity.shallWeLogin(R.string.title_loginfirst, WeiboShowActivity.this);
					}
					break;
				case 1:
					if (mIndexOfSelectedStatus == -1) {
						Toast.makeText(
							WeiboShowActivity.this,
							R.string.tips_noitemselected,
							Toast.LENGTH_LONG
						).show();
						return;
					} else {
						showComments();
						turnDealing(true);
					}
					break;
				case 2:
					reloadAll();
					turnDealing(true);
					break;
				}
				mDlgMore.dismiss();
			}
			
		});
		
		mBtnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDlgMore.show();
			}
			
		});
	}
	
	protected void showComments() {
		// TODO Auto-generated method stub
		long sid;
		if (mLastUserTimeline == null) {
			sid = mLastUser.getStatus().getId();
		} else {
			sid = mLastUserTimeline.get(mIndexOfSelectedStatus).getStatus().getId();
		}
		mLastComments.clear();
		new Thread(
			new ThreadSinaDealer(
				mSina,
				ThreadSinaDealer.GET_COMMENTS,
				new String[] {
					"" + sid,
					"" + getLastCommentsTotalPage(),
					"" + COUNT_PERPAGE_COMMENTS
				},
				mHandler
			)
		).start();
		ListView lvComments = (ListView)mDlgComments.findViewById(R.id.lvCustomList);
		ArrayList<String> lstWaiting = new ArrayList<String>();
		lstWaiting.add(getString(R.string.tips_waitasecond));
		lvComments.removeFooterView(mBtnMoreComments);
		mBtnMoreComments.setTag(0);
		mBtnMoreComments.setEnabled(true);
		lvComments.setAdapter(
			new ArrayAdapter<String>(
				WeiboShowActivity.this,
				R.layout.item_custom_dialog_list,
				lstWaiting
			)
		);
		mDlgComments.show();
	}

	protected void reloadAll() {
		// TODO Auto-generated method stub
		new Thread(
			new ThreadSinaDealer(
				mSina, 
				ThreadSinaDealer.SHOW_USER, 
				new String[] {mUid.toString()}, 
				mHandler
			)
		).start();
		
		mLastUserTimeline.clear();
		new Thread(
			new ThreadSinaDealer(
				mSina,
				ThreadSinaDealer.GET_USER_TIMELINE,
				new String[] {mUid.toString(), "" + getLastUserTimelineTotalPage(), "" + COUNT_PERPAGE_TIMELINE},
				mHandler
			)
		).start();
	}

	protected int getLastUserTimelineTotalPage() {
		// TODO Auto-generated method stub
		if (mLastUserTimeline == null) return 1;
		int size = mLastUserTimeline.size();
		if (size == 0) return 1;
		if (size % COUNT_PERPAGE_TIMELINE != 0) return size / COUNT_PERPAGE_TIMELINE + 1;
		else return size / COUNT_PERPAGE_TIMELINE;
		
	}
	
	protected int getLastCommentsTotalPage() {
		if (mLastComments == null) return 1;
		int size = mLastComments.size();
		if (size == 0) return 1;
		if (size % COUNT_PERPAGE_COMMENTS != 0) return size / COUNT_PERPAGE_COMMENTS + 1;
		else return size / COUNT_PERPAGE_COMMENTS;
	}

	public static Sina getSina() {
		return mSina;
	}
	
	public static void setSina(Sina sina) {
		mSina = sina;
	}
	
	private List<Map<String, Object>> getStatusData(int type) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		Sina.XStatus xstatus;
		switch (type) {
		case ThreadSinaDealer.SHOW_USER:
			if (mLastUser != null) {
				xstatus = mSina.getXStatus();
				xstatus.setStatus(mLastUser.getStatus());
				if (xstatus != null) {
					map = new HashMap<String, Object>();
					map.put("xstatus", xstatus);
					list.add(map);
				}	
			}
			break;
		case ThreadSinaDealer.GET_USER_TIMELINE:
			if (mLastUserTimeline != null) {
				for (int i = 0; i < mLastUserTimeline.size(); i++) {
					xstatus = mLastUserTimeline.get(i);
					map = new HashMap<String, Object>();
					map.put("xstatus", xstatus);
					list.add(map);
				}
			}
			break;
		}
		return list;
	}
	
	/*
	 * change the status for the components when dealing with SINA_weibo data
	 */
	private void turnDealing(boolean on) {
		if (on == true) {
			mBtnFriend.setEnabled(false);
			mBtnFavorite.setEnabled(false);
			mBtnRepost.setEnabled(false);
			mBtnMoreTimelines.setEnabled(false);
			mBtnMore.setEnabled(false);
			mBtnMoreComments.setEnabled(false);
			mProgressStatusLoading.setVisibility(ProgressBar.VISIBLE);
		} else {
			mBtnFriend.setEnabled(true);
			mBtnFavorite.setEnabled(true);
			mBtnRepost.setEnabled(true);
			mBtnMoreTimelines.setEnabled(true);
			mBtnMore.setEnabled(true);
			Integer flag = (Integer)mBtnMoreComments.getTag();
			if (flag != null && flag == 2) {
				
			} else {
				mBtnMoreComments.setEnabled(true);
			}
			mProgressStatusLoading.setVisibility(ProgressBar.GONE);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
