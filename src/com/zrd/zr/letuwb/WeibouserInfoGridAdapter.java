package com.zrd.zr.letuwb;

import java.util.List;

import com.zrd.zr.letuwb.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;

public class WeibouserInfoGridAdapter extends ArrayAdapter<WeibouserInfo> {
		private GridView gridView;
	    private AsyncImageLoader asyncImageLoader;
	    Context mContext;
	    
	    public AsyncImageLoader getAsyncImageLoader() {
	    	return asyncImageLoader;
	    }
	    
	    public WeibouserInfoGridAdapter(Activity activity, List<WeibouserInfo> usrs, GridView gridView) {
	        super(activity, 0, usrs);
	        this.gridView = gridView;
	        mContext = (EntranceActivity)activity;
	        asyncImageLoader = new AsyncImageLoader(mContext, usrs);
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        Activity activity = (Activity) getContext();

	        // Inflate the views from XML
	        View rowView = convertView;
	        ViewCache viewCache;
	        if (rowView == null) {
	            LayoutInflater inflater = activity.getLayoutInflater();
	            rowView = inflater.inflate(R.layout.grid_item, null);
	            viewCache = new ViewCache(rowView);
	            rowView.setTag(viewCache);
	        } else {
	            viewCache = (ViewCache) rowView.getTag();
	        }
	        WeibouserInfo wi = getItem(position);

	        // Load the image and set it on the ImageView
	        Integer id = wi.id;
	        TextView textView = viewCache.getTextView();
	        ImageView imageView = viewCache.getImageView();
	        imageView.setTag(id);
	        Drawable cachedImage = asyncImageLoader.loadDrawable(id, new ImageCallback() {
	            public void imageLoaded(Drawable imageDrawable, Integer id) {
	                ImageView imageViewByTag = (ImageView) gridView.findViewWithTag(id);
	                if (imageViewByTag != null) {
	                    imageViewByTag.setImageDrawable(imageDrawable);
	                }
	            }
	        });
			if (cachedImage == null) {
				imageView.setImageResource(R.drawable.cgpretty_small);
				//imageView.setImageDrawable(null);
			}else{
				imageView.setImageDrawable(cachedImage);
			}
	        // Set the text on the TextView
	        textView.setText(wi.location);

	        return rowView;
	    }

}
