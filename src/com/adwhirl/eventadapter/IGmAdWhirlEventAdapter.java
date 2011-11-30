package com.adwhirl.eventadapter;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlLayout.ViewAdRunnable;
import com.adwhirl.util.AdWhirlUtil;

public interface IGmAdWhirlEventAdapter {
	void dispose();

	abstract class GmAdWhirlCustomEventAdapter implements
			IGmAdWhirlEventAdapter {

		private WeakReference<AdWhirlLayout> mAdLayoutRef = null;

		public AdWhirlLayout getAdwhirlLayout() {
			return mAdLayoutRef.get();
		}

		public Activity getAdwhirlActivity() {
			Activity ret = null;
			AdWhirlLayout tmpLayout = getAdwhirlLayout();
			if (tmpLayout != null) {
				ret = tmpLayout.activityReference.get();
			}
			return ret;
		}

		private boolean mAdFetchDoneFlag = false;

		public boolean isAdFetchDone() {
			return mAdFetchDoneFlag;
		}

		public void setAdFetchDone(boolean adFetchDoneFlag) {
			mAdFetchDoneFlag = adFetchDoneFlag;
		}

		public GmAdWhirlCustomEventAdapter(AdWhirlLayout adLayout, Object obj) {
			mAdLayoutRef = new WeakReference<AdWhirlLayout>(adLayout);
			mAdFetchDoneFlag = false;
			init(obj);
		}

		protected abstract void init(Object obj);

		// check if an AdView is active (not NULL & visible)
		protected boolean isActiveAdView(View view) {
			boolean ret = false;
			if (view != null) {
				if (view.getVisibility() != View.GONE) {
					ret = true;
				}
			}
			return ret;
		}

		protected final void gmEventAdapterLog(String msg) {
			if (GmAdWhirlEventAdapterData.CONST_EVENTADAPTERLOG_ENABLED) {
				Log.d(AdWhirlUtil.ADWHIRL, msg);
			}
		}

		@Override
		public void dispose() {
			AdWhirlLayout tmpLayout = getAdwhirlLayout();
			if (tmpLayout != null) {
				tmpLayout.removeAllViews();
			}
		}

		public void doRollover() {
			if (!(GmAdWhirlEventAdapterData.isForceEventADMode() && GmAdWhirlEventAdapterData.CONST_EVENTADDEBUG_FORCELOOPMODE_ENABLED)) {
				AdWhirlLayout adLayout = getAdwhirlLayout();
				if (adLayout != null) {
					adLayout.rollover();
				}
			}
		}

		public void resetRollover(ViewGroup adView) {
			AdWhirlLayout adLayout = getAdwhirlLayout();
			if (adLayout != null) {
				adLayout.adWhirlManager.resetRollover();
				adLayout.handler.post(new ViewAdRunnable(adLayout, adView));
			}
		}

		public void resetRolloverOnly() {
			AdWhirlLayout adLayout = getAdwhirlLayout();
			if (adLayout != null) {
				adLayout.adWhirlManager.resetRollover();
				countImpression();
			}
		}

		public void countImpression() {
			AdWhirlLayout adLayout = getAdwhirlLayout();
			if (adLayout != null) {
				adLayout.countImpression();
			}
		}

		public void countClick() {
			AdWhirlLayout adLayout = getAdwhirlLayout();
			if (adLayout != null) {
				adLayout.countClick();
			}
		}

		public void rotateThreadedDelayed() {
			AdWhirlLayout adLayout = getAdwhirlLayout();
			if (adLayout != null) {
				adLayout.rotateThreadedDelayed();
			}
		}

		public static int convertDIP2Pixel(Context context, int value) {
			final float SCALE = context.getResources().getDisplayMetrics().density;
			// 0.5f for rounding
			return (int) (value * SCALE + 0.5f);
		}

	}
}
