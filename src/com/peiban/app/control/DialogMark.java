package com.peiban.app.control;

import com.peiban.R;
import com.peiban.vo.CustomerVo;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public abstract class DialogMark extends DialogModel{
	private EditText editMarkName;
	private Button btnConfirm;
	private CustomerVo customerVo;
	public DialogMark(Context context, CustomerVo customerVo) {
		super(context);
		this.customerVo = customerVo;
	}
	@Override
	protected void baseInit() {
		this.setContentView(R.layout.dialog_mark);
		this.initView();
		editMarkName = (EditText) findViewById(R.id.dialog_model_mark);
		btnConfirm = (Button) findViewById(R.id.dialog_model_confirom);
		btnConfirm.setOnClickListener(new MarkOnClick());
		this.setTitle("修改备注名");
	}
	
	public void show(){
		editMarkName.setText(TextUtils.isEmpty(customerVo.getMarkName()) ? "" : customerVo.getMarkName());
		super.show();
	}
	
	protected abstract void markName(String markName);
	
	class MarkOnClick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			String markName = editMarkName.getText().toString();
			if(TextUtils.isEmpty(markName)){
				Toast.makeText(getContext(), "备注不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			markName(markName);
			cancel();
		}
		
	}
}
