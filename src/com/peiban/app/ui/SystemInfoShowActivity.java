package com.peiban.app.ui;

import com.peiban.R;
import com.peiban.vo.NotifiyVo;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.widget.TextView;

public class SystemInfoShowActivity extends BaseActivity{
	@ViewInject(id = R.id.show_system_text)
	private TextView showSystemInfo;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.system_msg_show);
		baseInit();
		
	}
	
	protected void baseInit(){
		super.baseInit();
		initIntent();
	}

	@Override
	protected void initTitle() {
		setBtnBack();
		setTitleContent("系统消息");
	}
	
	
	private void initIntent(){
		NotifiyVo notifiyVo = (NotifiyVo) getIntent().getSerializableExtra("data");
		
		if(notifiyVo == null){
			finish();
			return;
		}
		
		showSystemInfo.setText(notifiyVo.getContent());
	}
}
