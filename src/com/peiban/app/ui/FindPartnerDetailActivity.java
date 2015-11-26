package com.peiban.app.ui;

import java.util.Map;

import com.peiban.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * 功能：寻伴<br />
 * 日期：2013-5-28<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public abstract class FindPartnerDetailActivity extends DetailActivity{
	private TextView txtCharm,txtCharmNum,txtVipState;
	private LinearLayout layoutClass;
	@Override
	protected void baseInit(){
		super.baseInit();
		
		this.txtCharm = (TextView) this.findViewById(R.id.details_head_txt_charm);
		this.txtCharmNum = (TextView) this.findViewById(R.id.details_head_charm_num);
		this.txtVipState = (TextView) this.findViewById(R.id.details_head_vip_state);
		
		layoutClass = (LinearLayout)this.findViewById(R.id.details_head_layout_class);
		
		txtCharm.setVisibility(View.VISIBLE);
		txtCharmNum.setVisibility(View.VISIBLE);
		txtCharmNum.setText("正在获取等级...");
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.shangwupanlv.app.ui.DetailActivity#scoreUploadSuccess(java.util.Map)
	 */
	@Override
	protected void scoreUploadSuccess(Map<String, String> data) {
		// TODO Auto-generated method stub
		String usercp = data.get("usercp");
		try {
			// 一颗钻石=5个星星
			int usercpNum = Integer.parseInt(usercp) / 5;
			int usercpMod = Integer.parseInt(usercp) % 5;
			txtCharmNum.setText(usercp + "分");
			int hg = usercpNum / 20;
			int hz = usercpNum % 20 / 5;
			int xx = usercpNum % 20 % 5;
			
			if(usercpMod > 0){
				xx = xx + 1;
			}
			
			for (int i = 0; i < hg; i++) {
				ImageView imageView = getClassImage();
				imageView.setImageResource(R.drawable.class0);
				if(!addClassImage(imageView)){
					return;
				};
			}
			for (int i = 0; i < hz; i++) {
				ImageView imageView = getClassImage();
				imageView.setImageResource(R.drawable.class1);
				if(!addClassImage(imageView)){
					return;
				};
			}
			for (int i = 0; i < xx; i++) {
				ImageView imageView = getClassImage();
				imageView.setImageResource(R.drawable.class2);
				if(!addClassImage(imageView)){
					return;
				};
			}
			
			
		} catch (Exception e) {
			txtCharmNum.setText("加载等级出现异常.");
			txtCharmNum.setTextColor(Color.RED);
		}
	}
	
	private boolean addClassImage(ImageView image){
		int index = layoutClass.getChildCount();
		if(index < 6){
			layoutClass.addView(image);
			return true;
		}else{
			return false;
		}
	}
	
	private ImageView getClassImage(){
		ImageView imageView = (ImageView) LayoutInflater.from(getBaseContext()).inflate(R.layout.class_image, null);
	
		return imageView;
	}


	/* (non-Javadoc)
	 * @see com.shangwupanlv.app.ui.DetailActivity#scoreUploadError()
	 */
	@Override
	protected void scoreUploadError() {
		txtCharmNum.setText("加载失败");
	}


	/**
	 * @return 等级标签
	 */
	public TextView getTxtCharm() {
		return txtCharm;
	}

	/**
	 * @return 等级值
	 */
	public TextView getTxtCharmNum() {
		return txtCharmNum;
	}

	/**
	 * @return 是否是Vip
	 */
	public TextView getTxtVipState() {
		return txtVipState;
	}

}
