package com.peiban.app.ui;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.action.ImageInfoAction.OnBitmapListener;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.UserInfoVo;

public class RegisterHeadAuthActivity extends BaseActivity{
	@ViewInject(id = R.id.register_head_img_photo)
	private ImageView imgHead;
	@ViewInject(id = R.id.register_head_btn_select_photo)
	private Button btnSelect;
	
	private View.OnClickListener selectOnClickListener;
	
	private ImageInfoAction imageInfoAction;
	
	private ImageInfoAction.OnBitmapListener imageBitmapListener;
	
	private Bitmap selectBitmap;
	
	private PeibanApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.register_head_auth);
		super.onCreate(savedInstanceState);
		addLisener();
		initParam();
		baseInit();
	}
	
	protected void baseInit() {
		super.baseInit();
		
		application = (PeibanApplication) getApplication();
	}

	private void initParam() {
		imageInfoAction = new ImageInfoAction(RegisterHeadAuthActivity.this);
		imageBitmapListener = new OnBitmapListener() {
			
			@Override
			public void getToBitmap(int bimType, Bitmap bm) {
				cameraPhoto(bm);
			}
		};
		imageInfoAction.setOnBitmapListener(imageBitmapListener);
		
	}


	private void addLisener() {
		selectOnClickListener = new RegisterHeadAuthOnClickLisener();
		btnSelect.setOnClickListener(selectOnClickListener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode){
			titleBtnLeft();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initTitle() {
		setTitleContent(R.string.register_head_auth_title);
		setTitleRight(R.string.register_head_auth_title_right_tag);
		setTitleLeft(R.string.register_head_auth_title_left_tag);
	}
	
	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		if (selectBitmap == null) {
			Toast.makeText(RegisterHeadAuthActivity.this, "您还未选择图片......",
					Toast.LENGTH_LONG).show();
		}
		else if(!NetworkUtils.isnetWorkAvilable(getBaseContext())){
			Toast.makeText(RegisterHeadAuthActivity.this, getResources().getString(R.string.toast_network),
					Toast.LENGTH_LONG).show();
		}
		else {
			uploadHead(selectBitmap);
		}
	}
	
	@Override
	protected void titleBtnLeft() {
		super.titleBtnLeft();
		
		Intent intent = new Intent(this, IndexTabActivity.class);
		
		startActivity(intent);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		imageInfoAction.onActivityResult(requestCode, resultCode, data);

	}

	void cameraPhoto(){
		imageInfoAction.getCropCameraPhoto();
	}
	
	void cameraPhoto(Bitmap bitmap){
		if(bitmap == null){
			
		}else{
			imgHead.setImageBitmap(bitmap);
			if(selectBitmap != null){
				selectBitmap.recycle();
			}
			selectBitmap = bitmap;
		}
	}
	/**
	 * 头像认证上传
	 * 
	 * */
	private void uploadHead(Bitmap bm) {
		AlbumApi albumApi = new AlbumApi();
		UserInfoVo userInfoVo = application.getUserInfoVo();
		
		albumApi.upload(userInfoVo.getUid(),Constants.PhotoType.PHOTO_HEAD, bm, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				getWaitDialog().setMessage("正在上传......");
				getWaitDialog().show();
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
				getWaitDialog().dismiss();
				Toast.makeText(getBaseContext(), strMsg, Toast.LENGTH_SHORT).show();
			}

		});
	}
	/**
	 * 上传成功
	 * */
	private void uploadsuccess(String t) {
		String data = ErrorCode.getData(getBaseContext(), t);
		if(data != null){
			String url = JSON.parseObject(data).getString("photoUrl");
			// 上传图片为认证
			UserInfoVo userInfoVo = application.getUserInfoVo();
			new AlbumApi().addAuthPhoto(userInfoVo.getUid(), url, new AjaxCallBack<String>() {

				@Override
				public void onStart() {
					super.onStart();
					getWaitDialog().setMessage("提交验证...");
					if(!getWaitDialog().isShowing()){
						getWaitDialog().show();
					}
				}

				@Override
				public void onSuccess(String t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					getWaitDialog().cancel();
					String data = ErrorCode.getData(getBaseContext(), t);
					if(data != null){
						submitAuthSuccess();
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					
					getWaitDialog().cancel();
					Toast.makeText(getBaseContext(), strMsg, Toast.LENGTH_SHORT).show();
				}
				
				
			});
			
		}else{
			getWaitDialog().cancel();
		}
	}
	/**
	 * 提交认证成功
	 * 
	 * 作者:fighter <br />
	 * 创建时间:2013-5-24<br />
	 * 修改时间:<br />
	 */
	void submitAuthSuccess(){
		getPromptDialog().setMessage("恭喜您,上传成功,等待后台人员操作！");
		getPromptDialog().addConfirm(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPromptDialog().cancel();
				titleBtnLeft();
			}
		});
		getPromptDialog().removeCannel();
		getPromptDialog().show();
	}
	
	class RegisterHeadAuthOnClickLisener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			if(!FileTool.isMounted()){
				Toast.makeText(getBaseContext(), getResources().getString(R.string.toast_sdcard_mounted), Toast.LENGTH_SHORT).show();
				return;
			}else if(!FileTool.isSDCardAvailable()){
				Toast.makeText(
						getBaseContext(),
						getResources().getString(R.string.toast_sdcard_available),
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			int id = v.getId();
			switch (id) {
			case R.id.register_head_btn_select_photo:
				cameraPhoto();
				break;

			default:
				break;
			}
		}
		
	}
}
