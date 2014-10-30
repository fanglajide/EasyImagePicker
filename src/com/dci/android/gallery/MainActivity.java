/**
 * 
 */
package com.dci.android.gallery;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author chanlevel
 *
 */
public class MainActivity extends Activity {
	TextView tv;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		FrameLayout view = new FrameLayout(this);
		tv = new TextView(this);
		tv.setX(1);
		tv.setY(100);
		tv.setText("tttttttttttt");
		Button button = new Button(this);
		button.setText("go");
		button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,PhotoSelectorActivity.class);
				intent.putExtra("MAXSELECTED", 3);
				startActivityForResult(intent, 100);
			}
		});
		view.addView(tv);
		view.addView(button);
		setContentView(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 100) {
			List<String> list = data.getStringArrayListExtra("PHOTOS");
			StringBuffer sb = new StringBuffer();
			for (String s : list) {
				sb.append(s + "\n");
			}
			tv.setText(sb);

		}

	}

}
