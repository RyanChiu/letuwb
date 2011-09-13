package com.zrd.zr.letuwb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeiboStatusListAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList = null;
	
	public WeiboStatusListAdapter (Context context, List<Map<String, Object>> list) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		WeiboStatusViewHolder holder = null;
		if (convertView == null) {
			holder = new WeiboStatusViewHolder();
			convertView = mInflater.inflate(R.layout.list_weibo_status, null);
			holder.mTextCreatedAt = (TextView)convertView.findViewById(R.id.tvStatusCreatedAt);
			holder.mText = (TextView)convertView.findViewById(R.id.tvStatusText);
			holder.mImage = (ImageView)convertView.findViewById(R.id.ivStatusImage);
			holder.mTextReply = (TextView)convertView.findViewById(R.id.tvStatusReply);
			convertView.setTag(holder);
		} else {
			holder = (WeiboStatusViewHolder)convertView.getTag();
		}
		if (mList != null) {
			holder.mTextCreatedAt.setText((String)mList.get(position).get("createdat"));
			holder.mText.setText((String)mList.get(position).get("text"));
			holder.mTextReply.setText((String)mList.get(position).get("reply"));
			AsyncImageLoader loader = new AsyncImageLoader(mContext, holder.mImage, R.drawable.broken);
			URL url = null;
			try {
				url = new URL((String)mList.get(position).get("image"));
			} catch (MalformedURLException e) {
				
			}
			loader.execute(url);
		}
		return convertView;
	}

}
