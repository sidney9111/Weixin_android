/**
 * @Title: SalaryDialogLisenerImpl.java 
 * @Package com.shangwupanlv.app.control 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-23 下午6:33:18 
 * @version V1.0
 */
package com.peiban.app.control;

import com.peiban.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SalaryDialogLisenerImpl {
	private TextView salaryText;
	private Context context;
	private Dialog salaryDialog;
	private EditText editSalaryValue;
	private Spinner spinSalaryType;
	private static final String[] type = { "小时", "天" };
	private ArrayAdapter<String> adapter;

	public SalaryDialogLisenerImpl(TextView salaryText, Context context) {
		this.salaryText = salaryText;
		this.context = context;
		initWigdt();
	}

	private void initWigdt() {
		salaryDialog = new Dialog(context, R.style.MyDialog);
		salaryDialog.setContentView(R.layout.dialog_editdata_salary);
		editSalaryValue = (EditText) salaryDialog
				.findViewById(R.id.editdata_edit_salaryvalue);
		spinSalaryType = (Spinner) salaryDialog
				.findViewById(R.id.editdata_spinner_salarytype);
		adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, type);
		spinSalaryType.setAdapter(adapter);
		
	}
	
}
