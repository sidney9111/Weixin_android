package com.peiban.app.ui;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

import com.peiban.R;
import com.shangwupanlv.widget.DragImageView;

public class DialogBitmapActivity extends BaseActivity {
	private DragImageView imageView;
	private ViewTreeObserver viewTreeObserver;
	private int state_height;// 状态栏的高度

	private int window_width, window_height;// 控件宽度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.bitmap_show);
		imageView = (DragImageView) this.findViewById(R.id.show_bitmap);
		viewTreeObserver = imageView.getViewTreeObserver();
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();

		imageView.setmActivity(DialogBitmapActivity.this);

		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// 获取状况栏高度
							Rect frame = new Rect();
							getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							imageView.setScreen_H(window_height - state_height);
							imageView.setScreen_W(window_width);
						}

					}
				});
		
		Bitmap bitmap = getIntent().getParcelableExtra("data");
		
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub

	}

}