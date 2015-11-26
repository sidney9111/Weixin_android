package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;

import com.peiban.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
/**
 * 
 * 功能： 显示头像 <br />
 * 日期：2013-6-27<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class ShowHeadActivity extends BaseActivity{
	@ViewInject(id = R.id.show_head_img)
	private ImageView imgHead;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.show_head);
		
		try {
			String url = getIntent().getExtras().getString("data");
			Bitmap bitmap = getHeadBitmap().getBitmapFromCache(url);
			if(bitmap != null){
				imgHead.setImageBitmap(bitmap);
			}else{
				finish();
				return;
			}
		} catch (Exception e) {
			finish();
			return;
		}
	}

	@Override
	protected void initTitle() {}

}
