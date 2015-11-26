package com.peiban.app.control;

import java.util.Calendar;

import com.peiban.R;
import com.peiban.command.TextdescTool;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

public class DatePickerLisenerImpl extends Dialog {

	public TextView txtShowDate; // 显示选择时间的位置
	private Context context;
	private Button btnBirthdayOk, btnBirthdaycancle;
	private DatePicker datePicker;
	private int year, month, day;

	public DatePickerLisenerImpl(Context context, TextView txtShowDate
			) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.setContentView(R.layout.dialog_editdata_birthday);
		this.txtShowDate = txtShowDate;
		String birthday= txtShowDate.getText().toString();
		Calendar calendar = TextdescTool.getCalendar(birthday);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		initWidget();
		initListener();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		
		datePicker = (DatePicker) this
				.findViewById(R.id.dialog_editdata_birthday);
		datePicker.requestFocus();
		btnBirthdayOk = (Button) this
				.findViewById(R.id.dialog_editdata_btn_birthday_ok);
		btnBirthdaycancle = (Button) this
				.findViewById(R.id.dialog_editdata_btn_birthday_cancel);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				DatePickerLisenerImpl.this.year = year;
				DatePickerLisenerImpl.this.month = monthOfYear;
				DatePickerLisenerImpl.this.day = dayOfMonth;
			}
		});
	}

	private void initListener() {
		// TODO Auto-generated method stub
		View.OnClickListener l = new btnOnClickListener();
		btnBirthdayOk.setOnClickListener(l);
		btnBirthdaycancle.setOnClickListener(l);
	}

	class btnOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.dialog_editdata_btn_birthday_ok:
				datePicker.clearFocus();
				if(showdate(year, month, day)){
					cancel();
				}else{
					Toast.makeText(context, "请选择正确的日期", Toast.LENGTH_SHORT).show();
				};
				
				break;
			case R.id.dialog_editdata_btn_birthday_cancel:
				cancel();
				break;
			}
		}
	}

	private boolean showdate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		int cYear = calendar.get(Calendar.YEAR);
		int cMonth = calendar.get(Calendar.MONTH);
		int cDay = calendar.get(Calendar.DAY_OF_MONTH);
		
		if(year> cYear){
			return false;
		}
		
		if(year == cYear && month > cMonth){
			return false;
		}
		
		if(year == cYear && month == cMonth && day > cDay){
			return false;
		}
		
		
		int trueMonth = (month + 1);
		String sMonth = trueMonth > 9 ? (trueMonth+"") : ("0" + trueMonth);
		String sDay = day > 9 ? (day + "") : ("0" + day);
		final String date = year + "/" + sMonth + "/" + sDay;
		txtShowDate.setText(date);
		
		return true;
	}
}