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

public class AlbumPromptDialog extends Dialog {
	private TextView txtMessage;
	private Button btnAlbum;
	private Button btnPhoto;
	
	private Button btnCancel;
	
	private Handler handler = new Handler();

	public AlbumPromptDialog(Context context) {
		this(context, R.style.DialogPrompt);
	}

	public AlbumPromptDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.dialog_album_prompt);
		this.initWidget();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		btnAlbum = (Button) this.findViewById(R.id.dialog_album_prompt_confirm);
		btnPhoto = (Button) this.findViewById(R.id.dialog_album_prompt_cannel);
		btnCancel = (Button) this.findViewById(R.id.dialog_album_prompt_c);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancel();
			}
		});
	}

	public void addAlbum(View.OnClickListener l) {
		btnAlbum.setVisibility(View.VISIBLE);
		btnAlbum.setOnClickListener(l);
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
		btnAlbum.setText(charSequence);
	}
	
	public void setCannelText(CharSequence charSequence){
		btnPhoto.setText(charSequence);
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
//
//	public void addCannel() {
//		setCannelText("取消");
//		addCannel(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				cancel();
//			}
//		});
//	}

	public void addPhoto(View.OnClickListener l) {
		btnPhoto.setVisibility(View.VISIBLE);
		btnPhoto.setOnClickListener(l);
	}
	
	public void removeAlbum(){
		btnAlbum.setVisibility(View.GONE);
	}
	
	public void removePhoto(){
		btnAlbum.setVisibility(View.GONE);
	}

}
