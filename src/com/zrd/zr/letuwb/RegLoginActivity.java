package com.zrd.zr.letuwb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import weibo4android.OAuthConstant;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.OAuthVerifier;
import weibo4android.http.RequestToken;

import com.zrd.zr.letuwb.R;
import com.zrd.zr.weiboes.Sina;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class RegLoginActivity extends Activity {
	TableLayout mTableBackground;
	EditText mEditUsername;
	EditText mEditPassword;
	EditText mEditRepeat;
	TableRow mRowRepeat;
	CheckBox mCheckRemember;
	Button btnLogin;
	Button btnGuest;
	Button btnReg;
	Button btnLetMeReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_login);
		
		mTableBackground = (TableLayout) findViewById(R.id.tlBackground);
		mEditUsername = (EditText) findViewById(R.id.etUsername);
		mEditPassword = (EditText) findViewById(R.id.etPassword);
		mEditRepeat = (EditText) findViewById(R.id.etRepeat);
		mRowRepeat = (TableRow) findViewById(R.id.trRepeat);
		mCheckRemember = (CheckBox) findViewById(R.id.cbRemember);
		mEditUsername.setText(EntranceActivity.mPreferences.getString(EntranceActivity.CONFIG_USERNAME, ""));
		mEditPassword.setText(EntranceActivity.mPreferences.getString(EntranceActivity.CONFIG_PASSWORD, ""));
		mCheckRemember.setChecked(EntranceActivity.mPreferences.getBoolean(EntranceActivity.CONFIG_REMEMBER, false));
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnGuest = (Button) findViewById(R.id.btnGuest);
		btnReg = (Button) findViewById(R.id.btnReg);
		btnReg.setVisibility(Button.GONE);
		btnLetMeReg = (Button) findViewById(R.id.btnLetmereg);
		btnLetMeReg.setText(Html.fromHtml("<u>" + getString(R.string.label_letmereg) + "</u>"));
		
		btnLetMeReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mRowRepeat.getVisibility() == TableRow.GONE) {
					mRowRepeat.setVisibility(TableRow.VISIBLE);
					btnLogin.setVisibility(Button.GONE);
					btnReg.setVisibility(Button.VISIBLE);
					btnLetMeReg.setText(Html.fromHtml("<u>" + getString(R.string.label_letmelogon) + "</u>"));
				} else {
					mRowRepeat.setVisibility(TableRow.GONE);
					btnLogin.setVisibility(Button.VISIBLE);
					btnReg.setVisibility(Button.GONE);
					btnLetMeReg.setText(Html.fromHtml("<u>" + getString(R.string.label_letmereg) + "</u>"));
				}
			}
			
		});
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mRowRepeat.setVisibility(TableRow.GONE);
				if (mEditUsername.getText().toString().trim().equals("")
					|| mEditPassword.getText().toString().trim().equals("")) {
					Toast.makeText(RegLoginActivity.this, getString(R.string.err_needaccount), Toast.LENGTH_LONG).show();
					return;
				}
				
				/*
				 * get SINA_weibo's token and token secret for the account
				 */
				Weibo weibo;
				RequestToken requestToken;
				try {
					Sina sina = WeiboShowActivity.getSina();
					if (sina == null) {
						sina = new Sina(true);
					}
					weibo = sina.getWeibo();
					requestToken = weibo.getOAuthRequestToken();
					OAuthConstant.getInstance().setRequestToken(requestToken);
					String username = mEditUsername.getText().toString();
					String password = mEditPassword.getText().toString();
					OAuthVerifier oauthVerifier = weibo.getOAuthVerifier(username, password);
					String verifier = oauthVerifier.getVerifier();
					AccessToken accessToken = requestToken.getAccessToken(verifier);
					OAuthConstant.getInstance().setAccessToken(accessToken);
					
					sina.getWeibo().setOAuthAccessToken(
						accessToken.getToken(),
						accessToken.getTokenSecret()
					);
					sina.setLoggedIn(true);
					WeiboShowActivity.setSina(sina);
				} catch (WeiboException e) {
					e.printStackTrace();
					Toast.makeText(
						RegLoginActivity.this, 
						"Oooops, login failed...", 
						Toast.LENGTH_LONG
					).show();
					WeiboShowActivity.getSina().setLoggedIn(false);
				}
				
				/*
				String sBackMsg = login(mEditUsername.getText().toString(), mEditPassword.getText().toString());
				String[] msgparts = sBackMsg.split("\\.");
				if (msgparts.length == 2 && msgparts[0].equals("Logged-in")) {
					Toast.makeText(RegLoginActivity.this, R.string.tips_loggedin, Toast.LENGTH_LONG).show();
					try {
						EntranceActivity.setAccountId(Integer.parseInt(msgparts[1]));
					} catch (NumberFormatException e) {
						EntranceActivity.setAccountId(-1);
					}
					SharedPreferences.Editor editor = EntranceActivity.mPreferences.edit();
					if (mCheckRemember.isChecked()) {
						editor.putBoolean(EntranceActivity.CONFIG_REMEMBER, true);
						editor.putString(EntranceActivity.CONFIG_USERNAME, mEditUsername.getText().toString());
						editor.putString(EntranceActivity.CONFIG_PASSWORD, mEditPassword.getText().toString());
						editor.commit();
					} else {
						editor.putBoolean(EntranceActivity.CONFIG_REMEMBER, false);
						editor.putString(EntranceActivity.CONFIG_USERNAME, "");
						editor.putString(EntranceActivity.CONFIG_PASSWORD, "");
						editor.commit();
					}
					EntranceActivity.setPrivilege(0);
					finish();
				} else {
					Toast.makeText(RegLoginActivity.this, R.string.tips_loginfailed, Toast.LENGTH_LONG).show();
					EntranceActivity.setPrivilege(1);
				}
				*/
			}
		});
		
		btnGuest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		btnReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mRowRepeat.getVisibility() == TableRow.GONE) {
					mRowRepeat.setVisibility(TableRow.VISIBLE);
					return;
				} else {
					if (mEditUsername.getText().toString().trim().equals("")
						|| mEditPassword.getText().toString().trim().equals("")
						|| mEditRepeat.getText().toString().trim().equals("")) {
						Toast.makeText(RegLoginActivity.this, getString(R.string.err_needaccount), Toast.LENGTH_LONG).show();
						return;
					}
					if (mEditPassword.getText().toString().equals(mEditRepeat.getText().toString())) {
						Map<String, String> actions = new HashMap<String, String>();
						actions.put("usr", mEditUsername.getText().toString());
						actions.put("pwd", mEditPassword.getText().toString());
						actions.put("rpt", mEditRepeat.getText().toString());
						actions.put("ckey", EntranceActivity.getClientKey());
						String sBackMsg = "";
						try {
							sBackMsg = AsyncUploader.post(EntranceActivity.URL_SITE + "reglogin.php", actions, null);
							String ss[] = EntranceActivity.getPhpMsg(sBackMsg);
							if (ss != null) {
								sBackMsg = ss[1];
							} else {
								sBackMsg = "~Wrong connection.~";
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							sBackMsg = "~Wrong connection.~";
							e.printStackTrace();
						}
						String[] msgparts = sBackMsg.split("\\.");
						Toast.makeText(RegLoginActivity.this, msgparts[0], Toast.LENGTH_LONG).show();
						if (msgparts.length == 2 && msgparts[0].equals("Registered")) {
							try {
								EntranceActivity.setAccountId(Integer.parseInt(msgparts[1]));
							} catch (NumberFormatException e) {
								EntranceActivity.setAccountId(-1);
							}
							SharedPreferences.Editor editor = EntranceActivity.mPreferences.edit();
							if (mCheckRemember.isChecked()) {
								editor.putBoolean(EntranceActivity.CONFIG_REMEMBER, true);
								editor.putString(EntranceActivity.CONFIG_USERNAME, mEditUsername.getText().toString());
								editor.putString(EntranceActivity.CONFIG_PASSWORD, mEditPassword.getText().toString());
								editor.commit();
							} else {
								editor.putBoolean(EntranceActivity.CONFIG_REMEMBER, false);
								editor.commit();
							}
							EntranceActivity.setPrivilege(0);
							finish();
						} else {
							EntranceActivity.setPrivilege(1);
						}
					} else {
						Toast.makeText(RegLoginActivity.this, getString(R.string.err_needrepeat), Toast.LENGTH_SHORT).show();
						return;
					}
				}
			}
			
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mTableBackground.setBackgroundResource(R.drawable.bg_h);
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mTableBackground.setBackgroundResource(R.drawable.bg);
		}
	}

	public static String login(String username, String password) {
		Map<String, String> actions = new HashMap<String, String>();
		actions.put("usr", username);
		actions.put("pwd", password);
		actions.put("ckey", EntranceActivity.getClientKey());
		String sBackMsg = "";
		try {
			sBackMsg = AsyncUploader.post(EntranceActivity.URL_SITE + "reglogin.php", actions, null);
			String ss[] = EntranceActivity.getPhpMsg(sBackMsg);
			if (ss != null) {
				sBackMsg = ss[1];
			} else {
				sBackMsg = "~Wrong connection.~";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			sBackMsg = "~Wrong connection.~";
			e.printStackTrace();
		}
		return sBackMsg;
	}
	
	/*
	 * show a dialog that let user select to log in or not
	 */
	public static void shallWeLogin(int titleId, final Context context) {
		AlertDialog dlg = new AlertDialog.Builder(context)
			.setPositiveButton(context.getString(R.string.label_letmelogin), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, RegLoginActivity.class);
					context.startActivity(intent);
				}
				
			})
			.setNegativeButton(context.getString(R.string.label_letmebe), null)
			.create();
		dlg.setTitle(context.getString(titleId));
		dlg.show();
	}
}
