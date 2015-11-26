package com.peiban.app.ui;

import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.peiban.R;
import com.peiban.command.TextdescTool;

/**
 * 
 * 功能： 陪聊用户个人信息. <br />
 * 日期：2013-5-28<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public abstract class ChattingDetailActivity extends DetailActivity{
	private TextView txtAscoreTag, txtSscoreTag, txtAscoreNum, txtSscoreNum;
	
	private RatingBar ratingAscore, ratingSscore;
	
	private TextView scoreNum;		// 陪聊被评价的次数
	private static final String SCROENUM = "被评价的次数:";
	
	@Override
	protected void baseInit(){
		super.baseInit();
		
		this.txtAscoreNum = (TextView) this.findViewById(R.id.details_head_txt_appearance_score_num);
		this.txtAscoreTag = (TextView) this.findViewById(R.id.details_head_txt_appearance_score_tag);
		this.ratingAscore = (RatingBar) this.findViewById(R.id.details_head_layout_appearance_score_evaluate);
		
		this.txtSscoreNum = (TextView) this.findViewById(R.id.details_head_txt_service_score_num);
		this.txtSscoreTag = (TextView) this.findViewById(R.id.details_head_txt_service_score_tag);
		this.ratingSscore = (RatingBar) this.findViewById(R.id.details_head_layout_service_score_evaluate);
		this.scoreNum = (TextView) this.findViewById(R.id.details_head_txt_score_num);
		
		txtSscoreTag.setVisibility(View.VISIBLE);
		txtAscoreTag.setVisibility(View.VISIBLE);
		txtAscoreNum.setVisibility(View.VISIBLE);
		txtSscoreNum.setVisibility(View.VISIBLE);
		ratingAscore.setVisibility(View.VISIBLE);
		ratingSscore.setVisibility(View.VISIBLE);
		scoreNum.setVisibility(View.VISIBLE);
		scoreNum.setText(SCROENUM + "0");
		txtSscoreNum.setText("加载..");
		txtAscoreNum.setText("加载..");
		
		scoreNum.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), EvaluateListActivity.class);
				intent.putExtra("data", getCustomer());
				startActivity(intent);
			}
		});
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.shangwupanlv.app.ui.DetailActivity#scoreUploadSuccess(java.util.Map)
	 */
	@Override
	protected void scoreUploadSuccess(Map<String, String> data) {
		if(data == null){
			return;
		}
		scoreUploadSuccess(data.get("ascore"), data.get("sscore"), data.get("usercp"));
	}


	/**
	 * 评分加载成功
	 * @param ascore 外形评分
	 * @param sscore 服务评分
	 */
	void scoreUploadSuccess(String ascore, String sscore, String usercp){
		try {
			ascore = TextdescTool.floatMac(ascore);
		} catch (Exception e) {
			ascore = "0";
		}
		
		try {
			sscore = TextdescTool.floatMac(sscore);
		} catch (Exception e) {
			sscore = "0";
		}
		
		if(!TextUtils.isEmpty(usercp)){
			scoreNum.setText(SCROENUM + usercp);
		}
		
		try {
			float fascore = Float.parseFloat(ascore);
			float fssscore = Float.parseFloat(sscore);
			
			this.txtAscoreNum.setText(ascore);
			ratingAscore.setRating(fascore);
			txtSscoreNum.setText(sscore);
			ratingSscore.setRating(fssscore);
		} catch (Exception e) {
			String errInfo = "错误";
			txtAscoreNum.setText(errInfo);
			txtSscoreNum.setText(errInfo);
			txtSscoreNum.setTextColor(Color.RED);
			txtAscoreNum.setTextColor(Color.RED);
		}
		
	}
	
	@Override
	protected void scoreUploadError(){
		String errInfo = "错误";
		txtAscoreNum.setText(errInfo);
		txtSscoreNum.setText(errInfo);
	}

	/**
	 * @return the 外貌评分数字
	 */
	public TextView getTxtAscoreNum() {
		return txtAscoreNum;
	}

	/**
	 * @return the 服务评分数字
	 */
	public TextView getTxtSscoreNum() {
		return txtSscoreNum;
	}
	
	/**
	 * @return the 外貌评分标记
	 */
	public TextView getTxtAscoreTag() {
		return txtAscoreTag;
	}

	/**
	 * @return the 服务评分标记
	 */
	public TextView getTxtSscoreTag() {
		return txtSscoreTag;
	}

	/**
	 * @return the 外貌评分
	 */
	public RatingBar getRatingAscore() {
		return ratingAscore;
	}

	/**
	 * @return the 服务评分
	 */
	public RatingBar getRatingSscore() {
		return ratingSscore;
	}
	
	
	
}
