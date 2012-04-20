package com.zrd.zr.customctrls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ZRScrollView extends ScrollView {
	private Runnable scrollerTask;
	private int initialPosition;

	private int newCheck = 100;
	@SuppressWarnings("unused")
	private static final String TAG = "MyScrollView";

	public interface OnScrollStoppedListener{
	    void onScrollStopped();
	}

	private OnScrollStoppedListener onScrollStoppedListener;

	public ZRScrollView(Context context, AttributeSet attrs) {
	    super(context, attrs);

	    scrollerTask = new Runnable() {

	        public void run() {

	            int newPosition = getScrollY();
	            if(initialPosition - newPosition == 0){//has stopped

	                if(onScrollStoppedListener!=null){

	                    onScrollStoppedListener.onScrollStopped();
	                }
	            }else{
	                initialPosition = getScrollY();
	                ZRScrollView.this.postDelayed(scrollerTask, newCheck);
	            }
	        }
	    };
	}

	public void setOnScrollStoppedListener(ZRScrollView.OnScrollStoppedListener listener){
	    onScrollStoppedListener = listener;
	}

	public void startScrollerTask(){

	    initialPosition = getScrollY();
	    ZRScrollView.this.postDelayed(scrollerTask, newCheck);
	}

}
