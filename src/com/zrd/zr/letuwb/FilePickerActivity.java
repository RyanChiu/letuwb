package com.zrd.zr.letuwb;

import com.zrd.zr.letuwb.R;
import com.zrd.zr.letuwb.FileListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class FilePickerActivity extends Activity {
	FileListView fileListView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file);
		
		fileListView = new FileListView(this, "/");

		addContentView(
			fileListView,
			new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
			)
		);

	}
}
