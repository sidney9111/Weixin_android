package com.peiban.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.peiban.R;

public class PromptDialog extends Dialog {
	private TextView txtMessage;
	private Button btnConfirm;
	private Button btnCannel;
	
	private Handler handler = new Handler();

	public PromptDialog(Context context) {
		this(context, R.style.DialogPrompt);
	}

	public PromptDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.dialog_prompt);
		this.initWidget();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		txtMessage = (TextView) this.findViewById(R.id.dialog_prompt_message);
		btnConfirm = (Button) this.findViewById(R.id.dialog_prompt_confirm);
		btnCannel = (Button) this.findViewById(R.id.dialog_prompt_cannel);
	}

	public void addConfirm(View.OnClickListener l) {
		btnConfirm.setVisibility(View.VISIBLE);
		btnConfirm.setOnClickListener(l);
	}
	
	public void setMessage(final int id){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtMessage.setText(id);
			}
		});
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.KEYCODE_BACK == keyCode){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	public void setConfirmText(CharSequence charSequence){
		btnConfirm.setText(charSequence);
	}
	
	public void setCannelText(CharSequence charSequence){
		btnCannel.setText(charSequence);
	}

	public void setMessage(final String text){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtMessage.setText(text);
			}
		});
	}

	public void addCannel() {
		setCannelText("取消");
		addCannel(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancel();
			}
		});
	}

	public void addCannel(View.OnClickListener l) {
		btnCannel.setVisibility(View.VISIBLE);
		btnCannel.setOnClickListener(l);
	}
	
	public void removeConfirm(){
		btnConfirm.setVisibility(View.GONE);
	}
	
	public void removeCannel(){
		btnCannel.setVisibility(View.GONE);
	}

}
