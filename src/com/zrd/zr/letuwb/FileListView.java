package com.zrd.zr.letuwb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * FileListView uses to display files and folder under assigned directory.
 * At the moment, it plays action when some item was clicked. If we clicked
 * file, it would be chosen, if we clicked folder, it would be open.
 *  
 * @author Qaohao
 * @version 1.0
 */
public class FileListView extends ListView {
	protected Context context;
	EditText mEditIntro = null;
	int mPosition = 0;
	
	// Current path.
	protected String pathname;

	// Root directory set by customer.
	protected String root;
	
	// All fileInfos under current directory.
	protected List<FileInfo> fileInfos = null;

	public FileListView(Context context, String pathname) {
		super(context);
		this.context = context;
		mEditIntro = new EditText(context);
		mEditIntro.setFilters(new InputFilter[]{new InputFilter.LengthFilter(64)});
		
		this.root = this.pathname = pathname;
		fileInfos = listFileInfos(pathname);

		// Display all files under root.
		setAdapter(new FilesListAdapter(context));

		setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				OnItemClickProcess(arg0, v, position, arg3);
			}
		});
	}
	
	/**
	 * When item was clicked, this would be recall.
	 */
	protected void OnItemClickProcess(AdapterView<?> listView, View item,
			int position, long time) {
		// Click a folder to get it open or get it close.
		FileInfo fileInfo = fileInfos.get(position);
		
		if (!fileInfo.isFile) {// Open folder opened, list all files under it.
			// Calculate exactly path.
			if ("..".equals(fileInfo.filename)) {
				pathname = pathname.substring(0, pathname.lastIndexOf('/'));
			} else {
				pathname += "/" + fileInfo.filename;

			}

			fileInfos = listFileInfos(pathname);

			/*
			 * List all files in ListView , when current path name is not root ,
			 * set an item named ".." to go back its parent is at first.
			 */
			if (!root.equals(pathname)) {
				fileInfos.add(0, new FileInfo("..", 0, false, true));
			}

			// Display its sub files.
			setAdapter(new FilesListAdapter(FileListView.this.context));
		} else {
			/*
			 * extra added by A.I.
			 */
			mPosition = position;
			AlertDialog dlg = new AlertDialog.Builder(FileListView.this.context)
				.setTitle(R.string.intro_title)
				.setMessage(R.string.tips_enterintro)
				.setView(mEditIntro)
				.setPositiveButton(R.string.label_ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							Toast.makeText(
								FileListView.this.context,
								"\"" + fileInfos.get(mPosition).filename + "\" " + context.getString(R.string.msg_uploading).toLowerCase(),
								Toast.LENGTH_LONG
							).show();
							FilePickerActivity fpa = (FilePickerActivity)context;
							Intent resultIntent = new Intent();
							resultIntent.putExtra("pickedpath", pathname);
							resultIntent.putExtra("pickedfile", fileInfos.get(mPosition).filename);
							resultIntent.putExtra("intro", mEditIntro.getText().toString());
							if (fpa.getParent() == null) {
								fpa.setResult(Activity.RESULT_OK, resultIntent);
							} else {
								fpa.getParent().setResult(Activity.RESULT_OK, resultIntent);
							}
							fpa.finish();
						}
				
					}
				).create();
			dlg.show();
		}
	}

	/**
	 * List all files info below this explored directory.
	 * 
	 * @param pathname
	 *            Explored directory name.
	 * @return All files info below this explored directory.
	 */
	protected List<FileInfo> listFileInfos(String pathname) {
		File parent = new File(pathname);
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		try {
			File files[] = parent.listFiles();
			if (files == null) return fileInfos;
			for (File file : files) {
				if (file.isFile()) {
					String fName = file.getName();
					int dotIndex = fName.lastIndexOf(".");
					if(dotIndex >= 0) {
						String ext = fName.substring(dotIndex,fName.length()).toLowerCase();
						if (ext.compareTo(".png") == 0 
								|| ext.compareTo(".jpg") == 0 
								|| ext.compareTo(".jpeg") == 0
								|| ext.compareTo(".bmp") == 0) {
							fileInfos.add(new FileInfo(file.getName(), 0, file.isFile(), false));
						}
					}
				} else {
					fileInfos.add(new FileInfo(file.getName(), 0, file.isFile(), false));
				}
			}
			Collections.sort(fileInfos);
		} catch (SecurityException e) {
			
		}
		return fileInfos;
	}

	/**
	 * Refresh ListView items.
	 */
	public void refresh() {
		fileInfos = listFileInfos(pathname);
		
		// Display its sub files.
		setAdapter(new FilesListAdapter(FileListView.this.context));
	}

	public String getCurrentDirectory() {
		return pathname;
	}

	public void setRoot(String root) {
		this.root = root;
	}
	
	/**
	 * Uses for setting data into file listView.
	 */
	protected class FilesListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public FilesListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 */
		public int getCount() {
			return fileInfos.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficient to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// Retrieve file information at assigned position.
			FileInfo fileInfo = fileInfos.get(position);

			// Display them.
			convertView = inflater.inflate(R.layout.list_item_icon_text, null);
			((TextView) convertView.findViewById(R.id.text))
					.setText(fileInfo.filename);
			((ImageView) convertView.findViewById(R.id.icon))
					.setImageResource(fileInfo.isParent ? R.drawable.parent32x32
							: fileInfo.isFile ? R.drawable.file32x32 : R.drawable.folder32x32);

			return convertView;
		}

	}

	/**
	 * File Information Holder.
	 */
	protected class FileInfo implements Comparable<FileInfo> {
		public final String filename;
		public final long size;
		public final boolean isFile;
		public final boolean isParent;

		public FileInfo(String filename, long size, boolean isFile,
				boolean isParent) {
			this.filename = filename;
			this.size = size;
			this.isFile = isFile;
			this.isParent = isParent;
		}

		public int compareTo(FileInfo other) {
			if (isParent) {
				return 1;
			}
			if (isFile) {
				return other.isFile ? 0 : 1;
			} else {
				return other.isFile ? -1 : 0;
			}
		}
	}

}
