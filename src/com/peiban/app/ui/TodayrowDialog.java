package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;

import com.peiban.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class TodayrowDialog extends Dialog implements android.view.View.OnClickListener{
	
	//@ViewInject(id = R.id.btn_Photo)
	private Button  btnPhoto;
	//@ViewInject(id = R.id.btn_Import)
	private Button btnImport;
	public TodayrowDialog(Context context) {
	
		this(context, R.style.DialogPrompt);
	}

	public TodayrowDialog(Context context, int theme) {
		super(context, theme);
		this.setContentView(R.layout.dialog_prompt_todayrow);
		
		btnPhoto=(Button) this.findViewById(R.id.btn_Photo);
		btnPhoto.setOnClickListener(this);
		btnImport=(Button) this.findViewById(R.id.btn_Import);
		btnImport.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==btnPhoto){
			
		}
		else if(v==btnImport){
			
			
			getContext().startActivity(new Intent(getContext(),ProjectImportActivity.class));
			
		}
	}

}
