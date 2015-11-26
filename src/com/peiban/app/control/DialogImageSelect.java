package com.peiban.app.control;

import com.peiban.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class DialogImageSelect extends DialogModel{
	private Button btnCamera;
	private Button btnLocalImage;
	private Button btnCancel;
	public DialogImageSelect(Context context) {
		super(context);
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		
		setTitle("选择图片");
		
		btnCamera = makeButton("使用相机");
		btnLocalImage = makeButton("使用相册");
		btnCancel = makeButton("取消");
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		
		int marginBottom = getContext().getResources().getDimensionPixelOffset(R.dimen.marigin);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = marginBottom;
		
		super.addChildView(btnCamera, params);
		super.addChildView(btnLocalImage, params);
		super.addChildView(btnCancel, params);
	}
	
	private Button makeButton(String str){
		Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.btn_5, null);
		button.setText(str);
		return button;
	}

	/**
	 * @return the btnCamera
	 */
	public Button getBtnCamera() {
		return btnCamera;
	}
	/**
	 * @return the btnLocalImage
	 */
	public Button getBtnLocalImage() {
		return btnLocalImage;
	}		
}
