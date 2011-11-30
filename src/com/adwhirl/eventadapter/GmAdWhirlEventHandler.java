package com.adwhirl.eventadapter;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.util.Log;

import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlLayout.AdWhirlInterface;
import com.adwhirl.AdWhirlLayout.IGmOnEventAdapterChangedListener;
import com.adwhirl.util.AdWhirlUtil;
//import com.vpon.adon.android.AdOnPlatform;

public class GmAdWhirlEventHandler implements AdWhirlInterface,
		IGmAdWhirlEventAdapter {

	public enum GmEventADType {
		// domestic
		domob, adwo, waps, vpon, vpontw, airad, adchina, lmmob, youmi, appmedia, smartmad, zhidian, baidu, mobwin, wiyun, dipai, wooboo, anji,
		// international
		tapjoy, mobfox,
	};

	public static int getEventADSupportLangSet(GmEventADType type) {
		int ret = AdWhirlUtil.CONST_INT_ADLANGUAGE_UNKNOWN;

		switch (type) {
		// international
		case tapjoy:
		case mobfox:
			ret = AdWhirlUtil.CONST_INT_ADLANGUAGE_ENGLISH;
			break;
		// domestic
		case domob:
		case adwo:
		case lmmob:
		case smartmad:
		case waps:
		case vpon:
		case vpontw:
		case youmi:
		case adchina:
		case baidu:
		case airad:
		case appmedia:
		case mobwin:
		case wiyun:
		case dipai:
		case wooboo:
		case anji:
		case zhidian:
			ret = AdWhirlUtil.CONST_INT_ADLANGUAGE_CHINESE;
			break;
		}
		return ret;
	}

	private WeakReference<AdWhirlLayout> mAdLayoutRef = null;
	// used to control event adapter.
	private IGmAdWhirlEventAdapter mAdapter = null;

	public GmAdWhirlEventHandler(AdWhirlLayout adLayout,
			IGmOnEventAdapterChangedListener ls) {
		if (adLayout != null) {
			mAdLayoutRef = new WeakReference<AdWhirlLayout>(adLayout);
			adLayout.setAdWhirlInterface(this);
		}
		mOnEventAdapterChangedListener = ls;
	}

	private IGmOnEventAdapterChangedListener mOnEventAdapterChangedListener = null;

	public IGmOnEventAdapterChangedListener getOnEventAdapterChangedListener() {
		return mOnEventAdapterChangedListener;
	}

	public synchronized void setOnEventAdapterChangedListener(
			IGmOnEventAdapterChangedListener ls) {
		mOnEventAdapterChangedListener = ls;
	}

	@Override
	public void adWhirlGeneric() {
		// TODO Auto-generated method stub

	}

	private void disposeAdapter() {
		if (mAdapter != null) {
			mAdapter.dispose();
			mAdapter = null;
		}
		AdWhirlLayout adLayout = mAdLayoutRef.get();
		if (adLayout != null) {
			adLayout.removeAllViews();
		}
	}

	@Override
	public void dispose() {
		disposeAdapter();
	}

	private synchronized void createEventAdapter(GmEventADType adType)
			throws Exception {
		AdWhirlLayout adLayout = mAdLayoutRef.get();

		if (adLayout == null) {
			return;
		}

		Activity activity = adLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		// dispose adapter
		disposeAdapter();

		if (GmAdWhirlEventAdapterData.CONST_FITADLANGUAGE_ENABLED) {
			int contextLanguage = AdWhirlUtil.getContextLanguage(activity);
			int adSupportLanguage = getEventADSupportLangSet(adType);
			if ((contextLanguage & adSupportLanguage) == 0) {
				Log.e("AdWhirl", "Skip EventAD:" + adType.toString());
				throw new Exception();
			}
		}

		switch (adType) {
		/*
		case adchina:
			mAdapter = new GmAdWhirlEventAdapter_cn_adchina(adLayout, null);
			break;
		*/
		case adwo:
			mAdapter = new GmAdWhirlEventAdapter_cn_adwo(adLayout, null);
			break;
		/*
		case airad:
			mAdapter = new GmAdWhirlEventAdapter_cn_airad(adLayout, null);
			break;
		case anji:
			mAdapter = new GmAdWhirlEventAdapter_cn_anji(adLayout, null);
			break;
		case appmedia:
			mAdapter = new GmAdWhirlEventAdapter_cn_appmedia(adLayout, null);
			break;
		case baidu:
			mAdapter = new GmAdWhirlEventAdapter_cn_baidu(adLayout, null);
			break;
		case dipai:
			mAdapter = new GmAdWhirlEventAdapter_cn_dipai(adLayout, null);
			break;
		*/
		case domob:
			mAdapter = new GmAdWhirlEventAdapter_cn_domob(adLayout, null);
			break;
		/*
		case lmmob:
			mAdapter = new GmAdWhirlEventAdapter_cn_lmmob(adLayout, null);
			break;
		case mobwin:
			mAdapter = new GmAdWhirlEventAdapter_cn_mobwin(adLayout, null);
			break;
		case smartmad:
			mAdapter = new GmAdWhirlEventAdapter_cn_smartmad(adLayout, null);
			break;
		case vpon:
			mAdapter = new GmAdWhirlEventAdapter_cn_vpon(adLayout,
					AdOnPlatform.CN);
			break;
		case vpontw:
			mAdapter = new GmAdWhirlEventAdapter_cn_vpon(adLayout,
					AdOnPlatform.TW);
			break;
		case waps:
			mAdapter = new GmAdWhirlEventAdapter_cn_waps(adLayout, null);
			break;
		case wiyun:
			mAdapter = new GmAdWhirlEventAdapter_cn_wiyun(adLayout, null);
			break;
		case wooboo:
			mAdapter = new GmAdWhirlEventAdapter_cn_wooboo(adLayout, null);
			break;
		case youmi:
			mAdapter = new GmAdWhirlEventAdapter_cn_youmi(adLayout, null);
			break;
		case zhidian:
			mAdapter = new GmAdWhirlEventAdapter_cn_zhidian(adLayout, null);
			break;
		case mobfox:
			mAdapter = new GmAdWhirlEventAdapter_mobfox(adLayout, null);
			break;
		case tapjoy:
			mAdapter = new GmAdWhirlEventAdapter_tapjoy(adLayout, null);
			break;
		*/
		}
		if (mOnEventAdapterChangedListener != null) {
			mOnEventAdapterChangedListener.onEventAdapterChanged(adType);
		}
	}

	public void adWhirlEventInterstitial_adchina() throws Exception {
		createEventAdapter(GmEventADType.adchina);
	}

	public void adWhirlEventInterstitial_adwo() throws Exception {
		createEventAdapter(GmEventADType.adwo);
	}

	public void adWhirlEventInterstitial_airad() throws Exception {
		createEventAdapter(GmEventADType.airad);
	}

	public void adWhirlEventInterstitial_anji() throws Exception {
		createEventAdapter(GmEventADType.anji);
	}

	public void adWhirlEventInterstitial_appmedia() throws Exception {
		createEventAdapter(GmEventADType.appmedia);
	}

	public void adWhirlEventInterstitial_baidu() throws Exception {
		createEventAdapter(GmEventADType.baidu);
	}

	public void adWhirlEventInterstitial_dipai() throws Exception {
		createEventAdapter(GmEventADType.dipai);
	}

	public void adWhirlEventInterstitial_domob() throws Exception {
		createEventAdapter(GmEventADType.domob);
	}

	public void adWhirlEventInterstitial_lmmob() throws Exception {
		createEventAdapter(GmEventADType.lmmob);
	}

	public void adWhirlEventInterstitial_mobwin() throws Exception {
		createEventAdapter(GmEventADType.mobwin);
	}

	public void adWhirlEventInterstitial_smartmad() throws Exception {
		createEventAdapter(GmEventADType.smartmad);
	}

	public void adWhirlEventInterstitial_vpon() throws Exception {
		createEventAdapter(GmEventADType.vpon);
	}

	public void adWhirlEventInterstitial_vpontw() throws Exception {
		createEventAdapter(GmEventADType.vpontw);
	}

	public void adWhirlEventInterstitial_waps() throws Exception {
		createEventAdapter(GmEventADType.waps);
	}

	public void adWhirlEventInterstitial_wiyun() throws Exception {
		createEventAdapter(GmEventADType.wiyun);
	}

	public void adWhirlEventInterstitial_wooboo() throws Exception {
		createEventAdapter(GmEventADType.wooboo);
	}

	public void adWhirlEventInterstitial_youmi() throws Exception {
		createEventAdapter(GmEventADType.youmi);
	}

	public void adWhirlEventInterstitial_zhidian() throws Exception {
		createEventAdapter(GmEventADType.zhidian);
	}

	public void adWhirlEventInterstitial_mobfox() throws Exception {
		createEventAdapter(GmEventADType.mobfox);
	}

	public void adWhirlEventInterstitial_tapjoy() throws Exception {
		createEventAdapter(GmEventADType.tapjoy);
	}
}
