/**
 * @Title: ServiceDialogLisenerImpl.java 
 * @Package com.shangwupanlv.app.control 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-23 下午6:09:08 
 * @version V1.0
 */
package com.peiban.app.control;

import com.peiban.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ServiceDialogLisenerImpl {
	private TextView textService;
	private Dialog dialogService;
	private Context context;
	private RadioGroup radioService;
	private RadioButton radiosocail;

	public ServiceDialogLisenerImpl(TextView textService, Context context) {
		this.textService = textService;
		this.context = context;
		initDialog();
	}

	private void initDialog() {
		dialogService = new Dialog(context, R.style.MyDialog);
		dialogService.setContentView(R.layout.dialog_editdata_service);		
	}
	public void setChanged() {
	}
}
