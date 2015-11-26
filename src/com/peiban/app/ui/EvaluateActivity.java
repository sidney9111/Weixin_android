/**
 * @Title: GetBackActivity.java 
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-17 上午10:57:55 
 * @version V1.0
 */
package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.widget.RatingBar;

import com.peiban.R;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.vo.CustomerVo;

public class EvaluateActivity extends BaseActivity {
	@ViewInject(id = R.id.AratingBar)
	private RatingBar aratinBar;
	@ViewInject(id = R.id.SRatingBar)
	private RatingBar sratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.evaluate);
		super.onCreate(savedInstanceState);
		baseInit();
	}

	/**
	 * 获取ratingbar的值
	 * */
	// private void onRatingChange() {
	// // TODO Auto-generated method stub
	// aratinBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
	//
	// @Override
	// public void onRatingChanged(RatingBar ratingBar, float rating,
	// boolean fromUser) {
	// // TODO Auto-generated method stub
	// asorce=String.valueOf(aratinBar.getRating());
	//
	// System.out.println("ssorce:"+ssorce+"asorce:"+asorce);
	// }
	// });
	// sratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
	//
	// @Override
	// public void onRatingChanged(RatingBar ratingBar, float rating,
	// boolean fromUser) {
	// // TODO Auto-generated method stub
	// ssorce=String.valueOf(sratingBar.getRating());
	//
	// System.out.println("ssorce:"+ssorce+"asorce:"+asorce);
	// }
	// });
	// }
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.my_evaluate);
		setBtnBack();
		setTitleRight(R.string.btn_enter);
	}

	@Override
	protected void titleBtnBack() {
		// TODO Auto-generated method stub
		super.titleBtnBack();
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		String asorce = String.valueOf(aratinBar.getRating());
		String ssorce = String.valueOf(sratingBar.getRating());
		editSorce(asorce, ssorce);
	}

	public void editSorce(String asorce, String ssorce) {
		CustomerVo customerVo = (CustomerVo) getIntent().getSerializableExtra(
				"data");
		UserInfoApi userInfoApi = new UserInfoApi();
		userInfoApi.editScore(getUserInfoVo().getUid(), customerVo.getUid(), asorce,
				ssorce, new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
						getWaitDialog().setMessage("提交请求");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						System.out.println("data:" + t);
						String data = ErrorCode.getData(getBaseContext(), t);

						if (data != null) {
							if("1".equals(data)){
								getWaitDialog().setMessage("评价成功!");
								finish();
							}else{
								getWaitDialog().setMessage("评价失败!");
							}
						}
						getWaitDialog().cancel();
					}
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().setMessage("提交失败:" + strMsg);
						getWaitDialog().cancel();
					}
				});
	}

}
