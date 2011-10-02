package com.zrd.zr.letuwb;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zrd.zr.weiboes.Sina;

import weibo4android.Status;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeiboStatusListAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Map<String, Object>> mList = null;
	private int mSelectedItem = -1;
	
	public WeiboStatusListAdapter (Context context, List<Map<String, Object>> list) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mList = list;
	}

	public void setSelectedItem(int selected) {
		this.mSelectedItem = selected;
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
			holder.mTextComments = (TextView)convertView.findViewById(R.id.tvStatusComments);
			holder.mTextReposts = (TextView)convertView.findViewById(R.id.tvStatusReposts);
			holder.mTextSource = (TextView)convertView.findViewById(R.id.tvSource);
			holder.mTextRetweeted = (TextView)convertView.findViewById(R.id.tvRetweeted);
			holder.mProgressStatusImageLoading = (ProgressBar)convertView.findViewById(R.id.pbStatusImageLoading);
			
			convertView.setTag(holder);
		} else {
			holder = (WeiboStatusViewHolder)convertView.getTag();
		}
		
		__drawHolder(mList, holder, position);
		
		/*
		 * dealing with selecting an item
		 */
		if (position == mSelectedItem) {
			convertView.setBackgroundColor(0x66ffc300);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		
		return convertView;
	}

	private void __drawHolder(List<Map<String, Object>> list,
			WeiboStatusViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (list != null) {
			Sina.XStatus xstatus = (Sina.XStatus)mList.get(position).get("xstatus");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dt = xstatus.getStatus().getCreatedAt();
			holder.mTextCreatedAt.setText(sdf.format(dt));
			
			holder.mText.setText(xstatus.getStatus().getText());
			
			/*
			String sReply = xstatus.status.getInReplyToScreenName();
			if (sReply.trim().equals("")) {
				holder.mTextReply.setText("");
			} else {
				holder.mTextReply.setText("Reply:" + sReply);
			}
			*/
			holder.mTextComments.setText("" + xstatus.getComments());
			holder.mTextReposts.setText("" + xstatus.getReposts());
			
			String sSource = xstatus.getStatus().getSource();
			if (sSource.trim().equals("")) {
				holder.mTextSource.setText("");
			} else {
				holder.mTextSource.setText(Html.fromHtml("Source:" + sSource));
			}
			
			Status statusR = xstatus.getStatus().getRetweeted_status();
			if (statusR == null) {
				holder.mTextRetweeted.setVisibility(TextView.GONE);
			} else {
				holder.mTextRetweeted.setVisibility(TextView.VISIBLE);
				holder.mTextRetweeted.setText(
					Html.fromHtml(
						"<b><font color='red'>Quote:</font></b>"
						+ "@" + statusR.getUser().getScreenName() + ":"
						+ statusR.getText()
					)
				);
			}
			
			AsyncImageLoader loader = new AsyncImageLoader(
				mContext, holder.mImage, R.drawable.broken,
				holder.mProgressStatusImageLoading
			);
			URL url = null;
			try {
				String sURL = xstatus.getStatus().getBmiddle_pic();
				url = new URL(sURL);
				loader.execute(url);
			} catch (MalformedURLException e) {
				holder.mImage.setImageResource(R.drawable.empty);
			}
		}
	}

}
