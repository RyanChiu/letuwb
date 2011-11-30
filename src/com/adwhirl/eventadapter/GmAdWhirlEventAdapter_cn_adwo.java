package com.adwhirl.eventadapter;

import android.view.View;
import android.widget.RelativeLayout;

import com.adwhirl.AdWhirlLayout;
import com.adwo.adsdk.AdListener;
import com.adwo.adsdk.AdwoAdView;
import com.adwo.adsdk.ErrorCode;
import com.adwhirl.eventadapter.GmAdWhirlEventHandler.GmEventADType;
import com.adwhirl.eventadapter.IGmAdWhirlEventAdapter.GmAdWhirlCustomEventAdapter;

class GmAdWhirlEventAdapter_cn_adwo extends GmAdWhirlCustomEventAdapter
		implements AdListener {

	private AdwoAdView mADView;

	public GmAdWhirlEventAdapter_cn_adwo(AdWhirlLayout adLayout, Object obj) {
		super(adLayout, obj);
	}

	@Override
	protected void init(Object obj) {
		gmEventAdapterLog("adwo->init");
		AdWhirlLayout adLayout = getAdwhirlLayout();
		if (adLayout != null) {
			// Extra extra = adLayout.extra;
			// int bgColor = Color.rgb(extra.bgRed, extra.bgGreen,
			// extra.bgBlue);
			// int fgColor = Color.rgb(extra.fgRed, extra.fgGreen,
			// extra.fgBlue);

			mADView = new AdwoAdView(getAdwhirlActivity(),// Context
					GmAdWhirlEventAdapterData.getPublishID(GmEventADType.adwo),// APPID
					GmAdWhirlEventAdapterData
							.getDebugEnabled(GmEventADType.adwo),// 测试模式
					0// banner广告刷新请求时间
			);
			mADView.setListener(this);

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			adLayout.addView(mADView, lp);

			// Show AD for certain seconds (extra.cycleTime).
			rotateThreadedDelayed();
			gmEventAdapterLog("adwo->rotateThreadedDelayed");
		}
	}

	@Override
	public void dispose() {
		gmEventAdapterLog("adwo->dispose");
		AdWhirlLayout tmpLayout = getAdwhirlLayout();
		if (tmpLayout != null) {
			if (mADView != null) {
				// //It seems that adwo do not like adView to be GONE.
				// mADView.setVisibility(View.GONE);
				mADView.setListener(null);
				tmpLayout.removeView(mADView);
				mADView = null;
				gmEventAdapterLog("adwo->removed");
			}
		}
		super.dispose();
	}

	// check if an AdView is active (not NULL & visible)
	@Override
	protected boolean isActiveAdView(View view) {
		boolean ret = false;
		if (view != null) {
			// Here I do not use View.Visible (Special for Adwo)
			ret = true;
		}
		return ret;
	}

	@Override
	public void onFailedToReceiveAd(AdwoAdView arg0) {
		// //已弃用
		// gmEventAdapterLog("adwo->onFailedToReceiveAd");
		// if (isActiveAdView(mADView)) {
		// if (!isAdFetchDone()) {
		// doRollover();
		// setAdFetchDone(true);
		// gmEventAdapterLog("adwo->doRollover");
		// }
		// }
	}

	@Override
	public void onFailedToReceiveAd(AdwoAdView adView, ErrorCode code) {
		gmEventAdapterLog("adwo->onFailedToReceiveAd");
		if (isActiveAdView(mADView)) {
			if (!isAdFetchDone()) {
				doRollover();
				setAdFetchDone(true);
				gmEventAdapterLog("adwo->doRollover");
			}
		}
	}

	@Override
	public void onFailedToReceiveRefreshedAd(AdwoAdView adView) {
		// //已弃用
		// gmEventAdapterLog("adwo->onFailedToReceiveRefreshedAd");
		// if (isActiveAdView(mADView)) {
		// if (!isAdFetchDone()) {
		// doRollover();
		// setAdFetchDone(true);
		// gmEventAdapterLog("adwo->doRollover");
		// }
		// }
	}

	@Override
	public void onReceiveAd(AdwoAdView adView) {
		gmEventAdapterLog("adwo->onReceiveAd");
		if (isActiveAdView(mADView)) {
			if (!isAdFetchDone()) {
				resetRollover(mADView);
				setAdFetchDone(true);
				gmEventAdapterLog("adwo->resetRollover");
			} else {
				countImpression();
			}
		}
	}

}
