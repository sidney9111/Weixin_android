/**
 * @Title: SalaryDialogLisenerImpl.java 
 * @Package com.shangwupanlv.app.control 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-23 下午6:33:18 
 * @version V1.0
 */
package com.peiban.app.control;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.peiban.R;

public class DialogSalaryLisenerImpl extends Dialog{
	private TextView salaryText;
	private Context context;
	private EditText editSalaryValue;
	private Spinner spinSalaryType;
	private ArrayAdapter<String> adapter;
	private Button btnSalaryok, btnSalarycancel;

	public DialogSalaryLisenerImpl(TextView salaryText, Context context) {
		super(context, R.style.MyDialog);
		this.setContentView(R.layout.dialog_editdata_salary);
		this.salaryText = salaryText;
		this.context = context;
		initWigdt();
		initListner();
	}

	private void initWigdt() {
		String[] type=context.getResources().getStringArray(R.array.editdata_salary);
		editSalaryValue = (EditText) this
				.findViewById(R.id.editdata_edit_salaryvalue);
		btnSalaryok = (Button) this
				.findViewById(R.id.dialog_editdata_btn_salary_ok);
		btnSalarycancel = (Button) this
				.findViewById(R.id.dialog_editdata_btn_salary_cancel);
		spinSalaryType = (Spinner) this
				.findViewById(R.id.editdata_spinner_salarytype);
		adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, type);
		adapter.setDropDownViewResource(R.layout.salary_spinner_down_item);
		spinSalaryType.setAdapter(adapter);

	}
	private void getSalary() {
		// TODO Auto-generated method stub
		String salarytype=spinSalaryType.getSelectedItem().toString();
		String salaryvalue=editSalaryValue.getText().toString();
		if(!TextUtils.isEmpty(salaryvalue)){
		salaryText.setText(salaryvalue + salarytype);
		}else{
			Toast.makeText(context, "薪资为空", Toast.LENGTH_SHORT).show();		
		}
		}
	private void initListner() {
		// TODO Auto-generated method stub
		View.OnClickListener l=new DialogSalarylistener();
		btnSalaryok.setOnClickListener(l);
		btnSalarycancel.setOnClickListener(l);
	}
	class DialogSalarylistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.dialog_editdata_btn_salary_ok:
				getSalary();
				cancel();
			break;
			case R.id.dialog_editdata_btn_salary_cancel:
				cancel();
				break;
				
			}
		}
		
	}
}
