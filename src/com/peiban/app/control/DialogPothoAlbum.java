package com.peiban.app.control;

import com.peiban.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class DialogPothoAlbum extends DialogModel{
	/** 添加相片 */
	private Button btnAddphoto;
	private Button btnCancel;
	/** 删除 */
	private Button btnDelete;
	/** 认证 */
	private Button btnAuth;
	public DialogPothoAlbum(Context context) {
		super(context);
		
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		
		setTitle("更多");
		
		btnAddphoto = makeButton("添加照片");
		btnAuth = makeButton("我要认证");
		btnDelete = makeButton("我要删除");
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
		
		super.addChildView(btnAddphoto, params);
		super.addChildView(btnAuth, params);
		super.addChildView(btnDelete, params);
		super.addChildView(btnCancel, params);
	}
	
	private Button makeButton(String str){
		Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.btn_5, null);
		button.setText(str);
		return button;
	}
	
	public void addLisener(View.OnClickListener l){
		this.btnAddphoto.setOnClickListener(l);
		this.btnAuth.setOnClickListener(l);
		this.btnDelete.setOnClickListener(l);
	}
	
	/** 添加照片 */
	public Button getBtnAddphoto() {
		return btnAddphoto;
	}
	/** 取消 */
	public Button getBtnCancel() {
		return btnCancel;
	}
	/** 删除 */
	public Button getBtnDelete() {
		return btnDelete;
	}

	/** 认证 */
	public Button getBtnAuth() {
		return btnAuth;
	}
}
