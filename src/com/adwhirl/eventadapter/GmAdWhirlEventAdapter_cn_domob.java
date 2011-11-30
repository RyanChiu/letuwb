package com.adwhirl.eventadapter;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import cn.domob.android.ads.DomobAdListener;
import cn.domob.android.ads.DomobAdManager;
import cn.domob.android.ads.DomobAdView;

import com.adwhirl.AdWhirlLayout;
import com.adwhirl.eventadapter.GmAdWhirlEventHandler.GmEventADType;
import com.adwhirl.eventadapter.IGmAdWhirlEventAdapter.GmAdWhirlCustomEventAdapter;
import com.adwhirl.obj.Extra;

class GmAdWhirlEventAdapter_cn_domob extends GmAdWhirlCustomEventAdapter
		implements DomobAdListener {

	private DomobAdView mADView;

	public GmAdWhirlEventAdapter_cn_domob(AdWhirlLayout adLayout, Object obj) {
		super(adLayout, obj);
	}

	static {
		DomobAdManager.setPublisherId(GmAdWhirlEventAdapterData
				.getPublishID(GmEventADType.domob));
	}

	@Override
	protected void init(Object obj) {
		gmEventAdapterLog("domob->init");
		AdWhirlLayout adLayout = getAdwhirlLayout();
		if (adLayout != null) {
			mADView = new DomobAdView(getAdwhirlActivity());
			mADView.setAdListener(this);

			Extra extra = adLayout.extra;
			int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
			int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
			mADView.setBackgroundColor(bgColor);
			mADView.setPrimaryTextColor(fgColor);
			// mADView.setRequestInterval(10 * 60); // according to DOMOB's doc.
			// mADView.setKeywords(/*keywords*/);

			if (GmAdWhirlEventAdapterData.getDebugEnabled(GmEventADType.domob)) {
				DomobAdManager.setIsTestMode(true);
			}

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			adLayout.addView(mADView, lp);

			// Show AD for certain seconds (extra.cycleTime).
			rotateThreadedDelayed();
			gmEventAdapterLog("domob->rotateThreadedDelayed");
		}
	}

	@Override
	public void dispose() {
		gmEventAdapterLog("domob->dispose");
		AdWhirlLayout tmpLayout = getAdwhirlLayout();
		if (tmpLayout != null) {
			if (mADView != null) {
				mADView.setVisibility(View.GONE);
				mADView.setAdListener(null);
				tmpLayout.removeView(mADView);
				mADView = null;
				gmEventAdapterLog("domob->removed");
			}
		}
		super.dispose();
	}

	@Override
	public void onFailedToReceiveFreshAd(DomobAdView arg0) {
		gmEventAdapterLog("domob->onFailedToReceiveFreshAd");
		if (isActiveAdView(mADView)) {
			if (!isAdFetchDone()) {
				doRollover();
				setAdFetchDone(true);
				gmEventAdapterLog("domob->doRollover");
			}
		}
	}

	@Override
	public void onReceivedFreshAd(DomobAdView arg0) {
		gmEventAdapterLog("domob->onReceivedFreshAd");
		if (isActiveAdView(mADView)) {
			if (!isAdFetchDone()) {
				AdWhirlLayout adLayout = getAdwhirlLayout();
				if (adLayout != null) {
					adLayout.adWhirlManager.resetRollover();
					countImpression();
				}
				setAdFetchDone(true);
				gmEventAdapterLog("domob->resetRollover");
			} else {
				countImpression();
			}
		}
	}

	@Override
	public void onLandingPageClose() {
	}

	@Override
	public void onLandingPageOpening() {
	}

}
