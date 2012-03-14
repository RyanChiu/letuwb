package com.zrd.zr.letuwb;

import java.util.List;

import com.zrd.zr.letuwb.AsyncThumLoader.ImageCallback;
import com.zrd.zr.pnj.ThreadPNJDealer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class WeibouserInfoGridAdapter extends ArrayAdapter<WeibouserInfo> {
		private GridView gridView;
	    private AsyncThumLoader asyncImageLoader;
	    private Context mContext;
	    private Object[] params;
	    
	    public AsyncThumLoader getAsyncImageLoader() {
	    	return asyncImageLoader;
	    }
	    
	    public WeibouserInfoGridAdapter(
	    	Activity activity, 
	    	List<WeibouserInfo> usrs, 
	    	GridView gridView,
	    	Object... params
	    ) {
	        super(activity, 0, usrs);
	        this.gridView = gridView;
	        mContext = (EntranceActivity)activity;
	        asyncImageLoader = new AsyncThumLoader(mContext, usrs);
	        this.params = params;
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
	        Long id = wi.id;
	        ImageButton btnDel = viewCache.getBtnDel();
	        TextView textView = viewCache.getTextView();
	        ImageView imageView = viewCache.getImageView();
	        imageView.setTag(id);
	        Drawable cachedImage = asyncImageLoader.loadDrawable(id, new ImageCallback() {
	            public void imageLoaded(Drawable imageDrawable, Long id) {
	                ImageView imageViewByTag = (ImageView) gridView.findViewWithTag(id);
	                if (imageViewByTag != null) {
	                    imageViewByTag.setImageDrawable(imageDrawable);
	                }
	            }
	        });
			if (cachedImage == null) {
				//imageView.setImageResource(R.drawable.persons);
				imageView.setImageDrawable(null);
			}else{
				imageView.setImageDrawable(cachedImage);
			}
	        // Set the text on the TextView
	        textView.setText(
	        	wi.screen_name
	        	+ (wi.verified == 1 ? " (V)" : "")
	        );
	        btnDel.setTag(wi);
	        // Set the upup/dwdw text for the views
	        viewCache.getTextUpup().setText(
	        	"+" + wi.likes
	        );
	        viewCache.getTextDwdw().setText(
	        	"-" + wi.dislikes
	        );
	        
	        /*
             * show the del button or not determinated by
             * whether the params[0] equals to true or not.
             */
            if (params == null || params.length == 0) {
            	btnDel.setVisibility(View.GONE);
            } else {
            	boolean isVisible = (Boolean) params[0];
            	if (isVisible) {
            		btnDel.setVisibility(View.VISIBLE);
            		btnDel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							gridView.setTag(v.getTag());
							
							new AlertDialog.Builder(mContext)
								.setTitle(R.string.tips_confirmdelpossession)
								.setPositiveButton(
									R.string.label_ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											WeibouserInfo wi = (WeibouserInfo) gridView.getTag();
											new Thread(
												new ThreadPNJDealer(
													ThreadPNJDealer.DEL_POSSESSION,
													EntranceActivity.URL_SITE
														+ "delpzs.php?"
														+ "clientkey=" + EntranceActivity.getClientKey()
														+ "&channelid=0"
														+ "&uid=" + wi.uid,
													((EntranceActivity) mContext).getMainPage().getHandler()
												)
											).start();
											Toast.makeText(
												mContext,
												R.string.tips_possessioncanceling,
												Toast.LENGTH_SHORT
											).show();
											dialog.dismiss();
										}

									}
								)
								.setNegativeButton(R.string.label_cancel, null)
								.create()
								.show();
						}
            			
            		});
            	} else {
            		viewCache.getBtnDel().setVisibility(View.GONE);
            	}
            }
	        
	        return rowView;
	    }

}
