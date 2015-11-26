package com.peiban.app.control;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.peiban.R;

/**
 * 功能：性别选择.<br />
 * 日期：2013-5-23<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class DialogSexSelect extends DialogEditDataModel{
	private TextView texSex;
	private int[] ids = new int[]{R.id.dialog_sex_select_btn_man,
			R.id.dialog_sex_select_btn_woman
	};
	private int[] strs = new int[]{R.string.conditionfilter_boy,
			R.string.conditionfilter_girl
	};
	
	private View.OnClickListener l = new DialogSexSelectBtnLiener();
	/**
	 * 
	 * @param context 上下文
	 * @param textSex 选择后文字显示的位置
	 */
	public DialogSexSelect(Context context, TextView textSex) {
		super(context);
		this.texSex = textSex;
		this.setTitle(R.string.register_grzl_sex);
		initWidget();
	}
	
	private void initWidget(){
		makeButton(ids, strs, l);
	}
	
	void manAction(Button btn){
		texSex.setText(btn.getText());
		texSex.setTag("1");
		cancel();
	}
	
	void womanAction(Button btn){
		texSex.setText(btn.getText());
		texSex.setTag("0");
		cancel();
	}
	
	class DialogSexSelectBtnLiener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button btn = (Button)v;
			int id = v.getId();
			switch (id) {
			case R.id.dialog_sex_select_btn_man:
				manAction(btn);
				break;
			case R.id.dialog_sex_select_btn_woman:
				womanAction(btn);
				break;

			default:
				break;
			}
		}
		
	}
}
