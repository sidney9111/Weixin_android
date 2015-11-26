package com.peiban.app.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.peiban.R;

public class DialogEditDataModel extends DialogModel{
	public DialogEditDataModel(Context context) {
		super(context);
	}
	
	public void makeButton(int[] ids, int[] strs, View.OnClickListener l){
		makeButton(R.layout.btn_5, ids, strs, l);
	}
	
	public void makeButton(int sourceId ,int[] ids, int[] strs, View.OnClickListener l){
		int marginBottom = getContext().getResources().getDimensionPixelOffset(R.dimen.marigin);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = marginBottom;
		
		for (int i = 0; i < ids.length; i++) {
			Button view = (Button) LayoutInflater.from(getContext()).inflate(sourceId, null); 
			view.setText(strs[i]);
			view.setId(ids[i]);
			view.setOnClickListener(l);
			addChildView(view, params);
		}
	}
}
