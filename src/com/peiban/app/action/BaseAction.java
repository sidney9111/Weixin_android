package com.peiban.app.action;

import android.content.Context;

public abstract class BaseAction {
	protected Context context;
	
	public BaseAction(Context context){
		this.context = context;
	}
}
