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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
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
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.zrd.zr.pnj.ThreadPNJDealer;
import com.zrd.zr.protos.WeibousersProtos.UCMappings;
import com.zrd.zr.weiboes.Sina;
import com.zrd.zr.weiboes.ThreadSinaDealer;

public class WeiboPage {
	private EntranceActivity parent;
	private int mReferer = -1;
	
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
	private Button mBtnPossess;
	private Button mBtnAtSomeone;
	private Button mBtnComment;
	private Button mBtnRepost;
	private Button mBtnMore;
	private ImageView mBtnTinyProfileImage;
	
	private AlertDialog mDlgRepost;
	private EditText mEditRepost;
	private AlertDialog mDlgComment;
	private EditText mEditComment;
	private AlertDialog mDlgUpdateStatus;
	private EditText mEditUpdateStatus;
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
	Handler mHandler = null;
	
	WeiboPage(EntranceActivity activity) {
		this.parent = activity;
		
		mTextScreenName = (TextView)parent.findViewById(R.id.tvScreenName);
		mImageVerified = (ImageView)parent.findViewById(R.id.ivVerified);
		mTextCreatedAt = (TextView)parent.findViewById(R.id.tvCreatedAt);
		mTextLocation = (TextView)parent.findViewById(R.id.tvLocation);
		mBtnDescription = (ImageButton)parent.findViewById(R.id.btnDescription);
		mTextCounts = (TextView)parent.findViewById(R.id.tvCounts);
		mListStatus = (ListView)parent.findViewById(R.id.lvStatus);
		mProgressStatusLoading = (ProgressBar)parent.findViewById(R.id.pbStatusLoading);
		mBtnPossess = (Button)parent.findViewById(R.id.btnWeiboPossess);
		mBtnAtSomeone = (Button)parent.findViewById(R.id.btnAtSomeone);
		mBtnComment = (Button)parent.findViewById(R.id.btnComment);
		mBtnRepost = (Button)parent.findViewById(R.id.btnRepost);
		mEditRepost  = new EditText(parent);
		mBtnMore = (Button)parent.findViewById(R.id.btnMore);
		mEditComment = new EditText(parent);
		mBtnTinyProfileImage = (ImageButton)parent.findViewById(R.id.btnTinyProfileImage);
		mEditUpdateStatus = new EditText(parent);
		
		mImageVerified.setVisibility(ImageView.GONE);
		mBtnDescription.setVisibility(ImageButton.GONE);
			
		mBtnMoreTimelines = new Button(parent);
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
							getUid().toString(), 
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
		
		mBtnMoreComments = new Button(parent);
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
		
		mBtnTinyProfileImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				Intent intent = new Intent();
				intent.setClass(WeiboShowActivity.this, PicbrowActivity.class);
				intent.putExtra("id", mId);
				startActivity(intent);
				*/
				parent.getBrowPage().setReferer(R.layout.main);
				//parent.switchPage(R.layout.brow, mId);
				parent.switchPage(R.layout.brow);
			}
			
		});
		
		mDlgRepost = new AlertDialog.Builder(parent)
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
		
		mDlgComment = new AlertDialog.Builder(parent)
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
		
		mDlgUpdateStatus = new AlertDialog.Builder(parent)
			.setTitle(R.string.title_updatestatus)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(mEditUpdateStatus)
			.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					/*
					 * This is the place it handles comment
					 */
					Toast.makeText(
						parent,
						R.string.tips_statusupdating,
						Toast.LENGTH_LONG
					).show();
					new Thread(
						new ThreadSinaDealer(
							mSina,
							ThreadSinaDealer.UPDATE_STATUS,
							new String[] {mEditUpdateStatus.getText().toString()},
							mHandler
						)
					).start();
					turnDealing(true);
				}
				
			})
			.setNegativeButton(R.string.label_cancel, null)
			.create();
		
		mDlgDescription = new Dialog(parent, R.style.Dialog_Clean);
		mDlgDescription.setContentView(R.layout.custom_dialog_list);
		
		mDlgComments = new Dialog(parent, R.style.Dialog_Clean);
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
		
		/*
		 * deal actions for components
		 */
		mListStatus.setTag((long)0);
		mListStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				/*
				 * make the selected item different with others
				 */
				int position = arg2;
				HeaderViewListAdapter ha = (HeaderViewListAdapter)parent.getAdapter();
				WeiboStatusListAdapter adapter  = (WeiboStatusListAdapter)ha.getWrappedAdapter();
				adapter.setSelectedItem(position);
				adapter.notifyDataSetInvalidated();
				mIndexOfSelectedStatus = position;
				
				/*
				 * pop up the "more" list
				 */
				//mBtnMore.performClick();
				
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
							parent,
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
					parent,
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
		
		mBtnPossess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(
					new ThreadPNJDealer(
						ThreadPNJDealer.GET_POSSESSIONS,
						EntranceActivity.URL_SITE 
							+ "updpzs.php?"
							+ "clientkey=" + EntranceActivity.getClientKey()
							+ "&channelid=0"
							+ "&uid=" + getUid(),
						mHandler
					)
				).start();
			}
			
		});
		
		mBtnAtSomeone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLastUser == null) {
					Toast.makeText(
						parent,
						R.string.tips_waitforgettinguser,
						Toast.LENGTH_LONG
					).show();
				} else {
					mEditUpdateStatus.setText(
						"@" + mLastUser.getScreenName() + ":"
						+ parent.getString(R.string.tips_routinehello)
					);
					mDlgUpdateStatus.show();
				}
			}
			
		});
		
		mBtnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mSina != null && mSina.isLoggedIn()) {
					if (mIndexOfSelectedStatus == -1) {
						Toast.makeText(
							parent,
							R.string.tips_noitemselected,
							Toast.LENGTH_LONG
						).show();
						return;
					}
					
					mDlgComment.show();
				} else {
					RegLoginActivity.shallWeLogin(R.string.title_loginfirst, parent);
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
							parent,
							R.string.tips_noitemselected,
							Toast.LENGTH_LONG
						).show();
						return;
					}
					
					mDlgRepost.show();
				} else {
					RegLoginActivity.shallWeLogin(R.string.title_loginfirst, parent);
				}
			}
			
		});
		
		mDlgMore = new Dialog(parent, R.style.Dialog_Clean);
		mDlgMore.setContentView(R.layout.custom_dialog_list);
		ListView lvMore = (ListView)mDlgMore.findViewById(R.id.lvCustomList);
		ArrayList<String> mlist = new ArrayList<String>();
		mlist.add(parent.getString(R.string.label_weibo_friendship));
		mlist.add(parent.getString(R.string.label_weibo_favorite));
		mlist.add(parent.getString(R.string.label_comments));
		mlist.add(parent.getString(R.string.label_reload));
		lvMore.setAdapter(
			new ArrayAdapter<String> (
				parent,
				R.layout.item_custom_dialog_list,
				mlist
			)
		);
		lvMore.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View view, 
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					if (mSina != null && mSina.isLoggedIn()) {
						new Thread(
							new ThreadSinaDealer(
								mSina,
								ThreadSinaDealer.CREATE_FRIENDSHIP,
								new String[] {getUid().toString()},
								mHandler
							)
						).start();
						turnDealing(true);
					} else {
						RegLoginActivity.shallWeLogin(R.string.title_loginfirst, parent);
					}
					break;
				case 1:
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
								parent,
								R.string.tips_noitemselected,
								Toast.LENGTH_LONG
							).show();
						}
					} else {
						RegLoginActivity.shallWeLogin(R.string.title_loginfirst, parent);
					}
					break;
				case 2:
					if (mIndexOfSelectedStatus == -1) {
						Toast.makeText(
							parent,
							R.string.tips_noitemselected,
							Toast.LENGTH_LONG
						).show();
						return;
					} else {
						showComments();
						turnDealing(true);
					}
					break;
				case 3:
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
		
		mHandler = new Handler() {
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
				case ThreadPNJDealer.GET_POSSESSIONS:
					UCMappings mappings = (UCMappings)msg.getData().getSerializable(ThreadPNJDealer.KEY_DATA);
					if (mappings != null) {
						if (mappings.getFlag() == 1) {
							Toast.makeText(
								parent,
								R.string.tips_alreadypossessed,
								Toast.LENGTH_LONG
							).show();
						} else if (mappings.getFlag() == 2) {
							Toast.makeText(
								parent,
								R.string.tips_possessed,
								Toast.LENGTH_LONG
							).show();
						} else {
							Toast.makeText(
								parent,
								R.string.tips_failedtopossess,
								Toast.LENGTH_LONG
							).show();
						}
					} else {
						//deal with failing to possess
					}
					break;
				case ThreadSinaDealer.SHOW_USER:
					mLastUser = (User)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
					if (mLastUser != null) {			
						/*
						 * show the profile-image
						 */
						AsyncImageLoader ail = new AsyncImageLoader(
							parent,
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
							parent.getString(R.string.label_weibos) + ":" + mLastUser.getStatusesCount()
							+ " " 
							+ parent.getString(R.string.label_favorites) + ":" + mLastUser.getFavouritesCount()
							+ " "
							+ parent.getString(R.string.label_followers) + ":" + mLastUser.getFollowersCount()
							+ " " 
							+ parent.getString(R.string.label_friends) + ":" + mLastUser.getFriendsCount()
						);
						
						/*
						 * alter the label of "@" button according to the gender of the user
						 */
						if (mLastUser.getGender().equals("f")) {
							mBtnAtSomeone.setText(parent.getString(R.string.label_atsomeone) + parent.getString(R.string.label_her));
						} else if (mLastUser.getGender().equals("m")){
							mBtnAtSomeone.setText(parent.getString(R.string.label_atsomeone) + parent.getString(R.string.label_him));
						} else {
							mBtnAtSomeone.setText(parent.getString(R.string.label_atsomeone) + parent.getString(R.string.label_her) + "/" + parent.getString(R.string.label_him));
						}
					} else {
						mBtnAtSomeone.setText(R.string.label_atsomeone);
						//mTextCreatedAt.setText("Please try again...");
						
						if (wexp != null) {
							Toast.makeText(
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
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
							parent,
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
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
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
								parent,
								R.string.tips_friendsmade,
								Toast.LENGTH_LONG
							).show();
						} else {
							Toast.makeText(
								parent,
								R.string.tips_friendsalready,
								Toast.LENGTH_LONG
							).show();
						}
					} else {
						//deal with failing to make friends
						if (wexp != null) {
							Toast.makeText(
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
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
							parent,
							R.string.tips_favoritemade,
							Toast.LENGTH_LONG
						).show();
					} else {
						//deal with failing to make favorite
						if (wexp != null) {
							Toast.makeText(
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
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
							parent,
							R.string.tips_reposted,
							Toast.LENGTH_LONG
						).show();
					} else {
						//deal with failing to make favorite
						if (wexp != null) {
							Toast.makeText(
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
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
									new WeiboStatusListAdapter(parent, null);
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
										parent,
										R.layout.item_custom_dialog_list,
										contents
									)
								);
								lv.setSelection(
									(getLastCommentsTotalPage() - 1) * COUNT_PERPAGE_COMMENTS
								);
							} else {
								Toast.makeText(
									parent,
									R.string.tips_nocomments,
									Toast.LENGTH_LONG
								).show();
							}
						} else {
							Toast.makeText(
								parent,
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
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
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
							parent,
							R.string.tips_commented,
							Toast.LENGTH_LONG
						).show();
					} else {
						//deal with failing to make favorite
						if (wexp != null) {
							Toast.makeText(
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
								Toast.LENGTH_LONG
							).show();
						}
					}
					turnDealing(false);
					break;
				case ThreadSinaDealer.UPDATE_STATUS:
					status = (Status)msg.getData().getSerializable(ThreadSinaDealer.KEY_DATA);
					if (status != null) {
						Toast.makeText(
							parent,
							R.string.tips_statusupdated,
							Toast.LENGTH_LONG
						).show();
					} else {
						//deal with failing to make favorite
						if (wexp != null) {
							Toast.makeText(
								parent,
								//wexp.toString(),
								R.string.tips_getweiboinfofailed,
								Toast.LENGTH_LONG
							).show();
						}
					}
					turnDealing(false);
					break;
				}
			}

		};
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
		lstWaiting.add(parent.getString(R.string.tips_waitasecond));
		lvComments.removeFooterView(mBtnMoreComments);
		mBtnMoreComments.setTag(0);
		mBtnMoreComments.setEnabled(true);
		lvComments.setAdapter(
			new ArrayAdapter<String>(
				parent,
				R.layout.item_custom_dialog_list,
				lstWaiting
			)
		);
		mDlgComments.show();
	}

	public void reloadLastUser(Long uid) {
		mLastUser = null;
		new Thread(
			new ThreadSinaDealer(
				mSina, 
				ThreadSinaDealer.SHOW_USER, 
				new String[] {uid.toString()}, 
				mHandler
			)
		).start();
	}
	
	protected void reloadAll() {
		// TODO Auto-generated method stub
		if (mLastUser == null) {
			reloadLastUser(getUid());
		}
		
		mLastUserTimeline.clear();
		new Thread(
			new ThreadSinaDealer(
				mSina,
				ThreadSinaDealer.GET_USER_TIMELINE,
				new String[] {getUid().toString(), "" + getLastUserTimelineTotalPage(), "" + COUNT_PERPAGE_TIMELINE},
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
	public void turnDealing(boolean on) {
		if (on == true) {
			mBtnPossess.setEnabled(false);
			mBtnComment.setEnabled(false);
			mBtnRepost.setEnabled(false);
			mBtnMoreTimelines.setEnabled(false);
			mBtnMore.setEnabled(false);
			mBtnMoreComments.setEnabled(false);
			mProgressStatusLoading.setVisibility(ProgressBar.VISIBLE);
		} else {
			mBtnPossess.setEnabled(true);
			mBtnComment.setEnabled(true);
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

	public Long getUid() {
		return mUid;
	}

	public void setUid(Long mUid) {
		this.mUid = mUid;
	}

	public int getReferer() {
		return mReferer;
	}

	public void setReferer(int mReferer) {
		this.mReferer = mReferer;
	}

	public Long getId() {
		return mId;
	}

	public void setId(Long mId) {
		this.mId = mId;
	}
	
	public User getLastUser() {
		return this.mLastUser;
	}
	
	public Button getBtnAtSomeone() {
		return this.mBtnAtSomeone;
	}
}
