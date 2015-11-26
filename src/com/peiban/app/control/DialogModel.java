package com.peiban.app.control;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peiban.R;

public class DialogModel extends Dialog{
	private LinearLayout layoutContent;
	private TextView txtTitle;
	private ImageView imgIoc;
	
	public DialogModel(Context context) {
		super(context, R.style.DialogPrompt);
		baseInit();
	}

	protected void baseInit() {
		this.setContentView(R.layout.dialog_model);
		this.initView();
	}

	protected void initView() {
		// TODO Auto-generated method stub
		layoutContent = (LinearLayout) this.findViewById(R.id.dialog_model_content);
		txtTitle = (TextView) this.findViewById(R.id.dialog_model_title);
		imgIoc = (ImageView) this.findViewById(R.id.dialog_model_icon);
	}
	
	@Override
	public void setTitle(int id){
		setTitle(getContext().getResources().getString(id));
	}

	@Override
	public void setTitle(CharSequence str){
		txtTitle.setText(str);
	}
	
	public void setIcon(int id){
		imgIoc.setBackgroundResource(id);
	}
	
	public void addChildView(View view){
		layoutContent.addView(view);
	}
	
	public void addChildView(View view, LinearLayout.LayoutParams params){
		layoutContent.addView(view, params);
	}
}
