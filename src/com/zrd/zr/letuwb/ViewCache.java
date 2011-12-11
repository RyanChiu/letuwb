package com.zrd.zr.letuwb;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCache {

	    private View baseView;
	    private TextView textView;
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