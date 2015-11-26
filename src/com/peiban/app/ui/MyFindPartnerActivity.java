package com.peiban.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.peiban.R;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.control.DialogImageSelect;
import com.peiban.app.control.HeadUploadAction;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;

/**
 * 
 * 功能：我的寻伴用户信息查看<br />
 * 日期：2013-5-29<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class MyFindPartnerActivity extends FindPartnerDetailActivity {
	public static final int REQUEST_CODE = 10;
	private View.OnClickListener mClickListener;
	private ImageInfoAction imageInfoAction;

	private HeadUploadAction headUploadAction;

	private DialogImageSelect mImageSelect;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shangwupanlv.app.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.chatting_info);
		this.baseInit();
		getNetworkAlbum();
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		mImageSelect = new DialogImageSelect(this);
		headUploadAction = new HeadUploadAction(getApplication(), null, getImgHead(), this,
				getUserInfoApi(), new AlbumApi(), true);
		mClickListener = new MyChattingOnclick();
		getImgHead().setOnClickListener(mClickListener);
		mImageSelect.getBtnCamera().setOnClickListener(mClickListener);
		mImageSelect.getBtnLocalImage().setOnClickListener(mClickListener);
		// 隐藏申请
		hideApply();
		// 显示个人信息
		showInfo();

		imageInfoAction = new ImageInfoAction(this);
		imageInfoAction.setOnBitmapListener(headUploadAction);
		// 头像是否认证
		isVip();
	}
	@Override
	protected void getApplyAuth() {
	}
	protected void getNetworkAlbum() {
		// TODO 从服务器获取相册信息
		super.getNetworkAlbum();
	}
	/**
	 * 判断头像认证的状态
	 */
	private void isVip() {
		if ("1".equals(getCustomer().getVip())) {
			getBtnVip().setVisibility(View.GONE);
		} else {
			getBtnVip().setVisibility(View.VISIBLE);
			getBtnVip().setText("成为VIP");

			getBtnVip().setOnClickListener(mClickListener);
		}
	}

	@Override
	protected void initTitle() {
		super.initTitle();
		setTitleRight(R.string.editdata_title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shangwupanlv.app.ui.BaseActivity#titleBtnRight()
	 */
	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		editDataAction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(resultCode == Activity.RESULT_OK ){
				updateHeadInfo();
			}
		}else{
			imageInfoAction.onActivityResult(requestCode, resultCode, data);
		}
	}

	/** 编辑资料 */
	void editDataAction() {
		Intent intent = new Intent(this, EditDataActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	/** 头像认证 */
	void applyVip() {
		// 跳转到我的积分，购买
		Intent intent = new Intent(MyFindPartnerActivity.this, MyPointActivity.class);
		startActivity(intent);
	}

	/** 修改头像 */
	protected void modifyHeadAction() {
		if (!FileTool.isMounted()) {
			Toast.makeText(getBaseContext(),
					getResources().getString(R.string.toast_sdcard_mounted),
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!FileTool.isSDCardAvailable()) {
			Toast.makeText(getBaseContext(),
					getResources().getString(R.string.toast_sdcard_available),
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!NetworkUtils.isnetWorkAvilable(getBaseContext())) {
			Toast.makeText(getBaseContext(),
					getResources().getString(R.string.toast_network),
					Toast.LENGTH_SHORT).show();
			return;
		}

		mImageSelect.show();
	}

	// 选择本地图片
	private void selectLocalImage() {
		imageInfoAction.getCropLocolPhoto();
		mImageSelect.cancel();
	}

	// 选择相机
	private void selectCamera() {
		imageInfoAction.getCropCameraPhoto();
		mImageSelect.cancel();
	}

	class MyChattingOnclick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == getImgHead()) {
				modifyHeadAction();
			} else if (v == getBtnVip()) {
				applyVip();
			} else if (v == mImageSelect.getBtnCamera()) {
				selectCamera();
			} else if (v == mImageSelect.getBtnLocalImage()) {
				selectLocalImage();
			}
		}

	}	
}
