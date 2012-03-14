package com.zrd.zr.letuwb;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {

	    private View baseView;
	    private TextView textView;
	    private TextView textUpup;
	    private TextView textDwdw;
	    private ImageView imageView;
	    private ImageButton btnDel;

	    public ViewCache(View baseView) {
	        this.baseView = baseView;
	    }

	    public TextView getTextView() {
	        if (textView == null) {
	            textView = (TextView) baseView.findViewById(R.id.text);
	        }
	        return textView;
	    }
	    
	    public TextView getTextUpup() {
	        if (textUpup == null) {
	            textUpup = (TextView) baseView.findViewById(R.id.textUpup);
	        }
	        return textUpup;
	    }
	    
	    public TextView getTextDwdw() {
	        if (textDwdw == null) {
	            textDwdw = (TextView) baseView.findViewById(R.id.textDwdw);
	        }
	        return textDwdw;
	    }

	    public ImageView getImageView() {
	        if (imageView == null) {
	            imageView = (ImageView) baseView.findViewById(R.id.image);
	        }
	        return imageView;
	    }

		public ImageButton getBtnDel() {
			if (btnDel == null) {
				btnDel = (ImageButton) baseView.findViewById(R.id.btnDelPossession);
			}
			return btnDel;
		}

}