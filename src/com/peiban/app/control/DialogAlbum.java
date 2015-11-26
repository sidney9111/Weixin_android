package com.peiban.app.control;

import com.peiban.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class DialogAlbum extends DialogModel{
	private Button btnUpdate;
	private Button btnCancel;
	private Button btnDelete;
	public DialogAlbum(Context context) {
		super(context);
		
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		
		setTitle("相册选项");
		
		btnUpdate = makeButton("我要修改");
		btnDelete = makeButton("我要删除");
		btnCancel = makeButton("我点错了");
		btnCancel.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		
		int marginBottom = getContext().getResources().getDimensionPixelOffset(R.dimen.marigin);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = marginBottom;
		
		super.addChildView(btnUpdate, params);
		super.addChildView(btnDelete, params);
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
	public Button getBtnUpdate() {
		return btnUpdate;
	}

	/**
	 * @return the btnLocalImage
	 */
	public Button getBtnDelete() {
		return btnDelete;
	}
	
	
	
}
