package com.peiban.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.ui.BaseActivity;
import com.peiban.app.ui.KitingActivity;
import com.peiban.app.ui.PanlvWebActivity;
import com.peiban.app.ui.ToCashActivity;

public class PointPromptDialog extends Dialog {
	private TextView txtMessage;
	private Button btnRecord;
	private Button btnRule;
	private Button btnApply;
	private Context context;
	private Handler handler = new Handler();
	private BaseActivity activity;

	public PointPromptDialog(BaseActivity activity) {
		this(activity, R.style.DialogPrompt);
		this.activity = activity;
	}

	public PointPromptDialog(Context context, int theme) {
		super(context, theme);

		this.setContentView(R.layout.dialog_point_more);
		this.initWidget();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		btnClick l = new btnClick();
		btnRecord = (Button) this.findViewById(R.id.dialog_point_record);
		btnRule = (Button) this.findViewById(R.id.dialog_point_rule);
		btnApply = (Button) this.findViewById(R.id.dialog_point_apply);
		btnRecord.setOnClickListener(l);
		btnRule.setOnClickListener(l);
		btnApply.setOnClickListener(l);
	}

	class btnClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.dialog_point_record:
//				Toast.makeText(context, "功能开发中", Toast.LENGTH_LONG).show();
				activity.startActivity(new Intent(activity, KitingActivity.class));
				// 积分记录
				cancel();
				break;
			case R.id.dialog_point_rule:
				// 体现规则
				Intent intent = new Intent(activity, PanlvWebActivity.class);
				intent.putExtra("url", Constants.ApiUrl.CASHRULE);
				intent.putExtra("titleName", "提现规则");
				activity.startActivity(intent);
//				Toast.makeText(context, "功能开发中", Toast.LENGTH_LONG).show();
				cancel();
				break;
			}
		}
	}

	public Button getBtnApply() {
		return btnApply;
	}
}
