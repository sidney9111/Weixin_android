package com.peiban.app.control;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.action.ImageInfoAction.OnBitmapListener;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.UserInfoApi;
import com.peiban.app.ui.BaseActivity;
import com.peiban.app.ui.common.BitmapUtils;
import com.peiban.application.PeibanApplication;
import com.peiban.vo.CustomerVo;
import com.peiban.vo.UserInfoVo;

/**
 * 
 * 功能： 头像上传接口,实现ImageInfoAction回调接口方法 <br />
 * 日期：2013-5-29<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class HeadUploadAction implements OnBitmapListener{
	private ImageView imgHead;
	private ImageView imgSelect;
	private BaseActivity baseActivity;
	
	private Bitmap mSelectHead;
	
	private UserInfoApi userInfoApi;
	private AlbumApi albumApi;
	
	private String uploadHeadUrl;
	
	private boolean flag;		// 是否直接选择完图片后，直接上传
	
	private PeibanApplication application;
	public HeadUploadAction(Application application){
		this.application = (PeibanApplication) application;
	}
	
	
	/**
	 * @param imgSelect 选择成功后图片显示地方. (null 不显示)
	 * @param imgHead  上传成功后图片显示的位置.(为Null 不显示)
	 * @param baseActivity 需要使用到BaseActivity类中的提示对象.
	 * @param userInfoApi  用户信息接口
	 * @param albumApi     图片上传接口.
	 * @param flag  是否直接选择完图片后，直接上传 true 直接上传， false 使用手动调用 {@link #uploadHead(Bitmap)} 上传
	 */
	public HeadUploadAction(Application application, ImageView imgSelect, ImageView imgHead, BaseActivity baseActivity,
			UserInfoApi userInfoApi, AlbumApi albumApi, boolean flag) {
		this(application);
		this.imgHead = imgHead;
		this.baseActivity = baseActivity;
		this.userInfoApi = userInfoApi;
		this.albumApi = albumApi;
		this.imgSelect = imgSelect;
		this.flag = flag;
	}

	private void callBackBitmap(Bitmap bitmap){
		if(bitmap == null){
			Toast.makeText(baseActivity, "选择图片错误!", Toast.LENGTH_SHORT).show();
		}else{
			Bitmap tempBitmp = BitmapUtils.getRoundedCornerBitmap(bitmap);
			mSelectHead = tempBitmp;
			bitmap.recycle();
			if(imgSelect != null)
				imgSelect.setImageBitmap(mSelectHead);
			
			if(flag)
				uploadHead(mSelectHead);
		}
	}
	
	/**
	 * 头像认证上传
	 * 
	 * */
	public void uploadHead(Bitmap bm) {
		if(bm == null){
			Toast.makeText(baseActivity, "选择的头像没找到,重新选择!", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		if(application.getUserInfoVo() == null || application.getCustomerVo() == null){
			Toast.makeText(baseActivity, "内存不足!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		albumApi.upload(application.getUserInfoVo().getUid(),Constants.PhotoType.PHOTO_HEAD, bm, CompressFormat.PNG, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				baseActivity.getWaitDialog().setMessage("正在上传......");
				baseActivity.getWaitDialog().show();
				super.onStart();
				
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				uploadsuccess(t);
			}
			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				baseActivity.getWaitDialog().dismiss();
				Toast.makeText(baseActivity, strMsg, Toast.LENGTH_SHORT).show();
			}

		});
	}
	/**
	 * 上传成功
	 * */
	private void uploadsuccess(String t) {
		String data = ErrorCode.getData(baseActivity, t);
		if(data != null){
			uploadHeadUrl = JSON.parseObject(data).getString("photoUrl");
			if(uploadHeadUrl == null){
				baseActivity.getWaitDialog().cancel();
				Toast.makeText(baseActivity, "提交失败!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("head", uploadHeadUrl);
			
			// 修改个人信息.
			userInfoApi.editInfo(application.getUserInfoVo().getUid(), params, new AjaxCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					baseActivity.getWaitDialog().setMessage("提交验证...");
					if(!baseActivity.getWaitDialog().isShowing()){
						baseActivity.getWaitDialog().show();
					}
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					baseActivity.getWaitDialog().cancel();
					String data = ErrorCode.getData(baseActivity, t);
					if(data != null && "1".equals(data)){
						FinalFactory.createFinalBitmap(application).addBitmapToCache(uploadHeadUrl, mSelectHead);
						application.getCustomerVo().setHead(uploadHeadUrl);
						application.getCustomerVo().setHeadattest("0");
						submitHeadSuccess();
					}else{
						Toast.makeText(baseActivity, "提交失败!", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					super.onFailure(t, strMsg);
					
					baseActivity.getWaitDialog().cancel();
					Toast.makeText(baseActivity.getBaseContext(), strMsg, Toast.LENGTH_SHORT).show();
				}
				
				
			});
			
		}else{
			baseActivity.getWaitDialog().cancel();
		}
	}
	/**
	 * 提交认证成功
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-24<br />
	 * 修改时间:<br />
	 */
	public void submitHeadSuccess(){
		Toast.makeText(baseActivity.getBaseContext(), "修改成功!", Toast.LENGTH_SHORT).show();
		
		if(imgHead != null)
		{
			imgHead.setImageBitmap(mSelectHead);
		}
			
		
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				try {
					FinalFactory.createFinalBitmap(baseActivity.getBaseContext()).addBitmapToCache(uploadHeadUrl, mSelectHead);
					FinalFactory.createFinalDb(baseActivity.getBaseContext(), application.getUserInfoVo()).update(application.getCustomerVo());
				} catch (Exception e) {
				}
				return null;
			}
			
		}.execute();
		
		
	}

	@Override
	public void getToBitmap(int bimType, Bitmap bm) {
		callBackBitmap(bm);
	}

	/**
	 * @return the mSelectHead
	 */
	public Bitmap getSelectHead() {
		return mSelectHead;
	}
	
}
