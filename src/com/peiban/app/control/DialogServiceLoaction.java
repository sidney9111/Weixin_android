package com.peiban.app.control;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.peiban.R;

/**
 * 
 * 功能：服务场合<br />
 * 日期：2013-5-23<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class DialogServiceLoaction extends DialogEditDataModel{
	private TextView txtServiceLoacion;
	private int[] ids = new int[]{R.id.dialog_service_location_btn_single,
			R.id.dialog_service_location_btn_intercourse,
			R.id.dialog_service_location_btn_undefind
			
	};
	private int[] strs = new int[]{R.string.service_single,
			R.string.service_intercourse,
			R.string.service_undefind
	};
	
	private View.OnClickListener l;
	
	public DialogServiceLoaction(Context context, TextView txtServiceLoacion) {
		super(context);
		this.txtServiceLoacion = txtServiceLoacion;
		this.l = new DialogServiceLocationListener();
		this.setTitle(R.string.register_grzl_service_location);
		this.initWidget();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		makeButton(ids, strs, l);
	}
	
	void singleAction(Button button){
		txtServiceLoacion.setText(button.getText());
		txtServiceLoacion.setTag("1");
		cancel();
	}
	
	void undifindAction(Button button){
		txtServiceLoacion.setText(button.getText());
		txtServiceLoacion.setTag("3");
		cancel();
	}
	
	void intercourseAction(Button button){
		txtServiceLoacion.setText(button.getText());
		txtServiceLoacion.setTag("2");
		cancel();
	}
	
	class DialogServiceLocationListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button button = (Button)v;
			int id = v.getId();
			switch (id) {
			case R.id.dialog_service_location_btn_single:
				singleAction(button);
				break;
			case R.id.dialog_service_location_btn_undefind:
				undifindAction(button);
				break;
			case R.id.dialog_service_location_btn_intercourse:
				intercourseAction(button);
				break;
			default:
				break;
			}
		}
		
	}
}
