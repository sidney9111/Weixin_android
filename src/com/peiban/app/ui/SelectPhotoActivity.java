package com.peiban.app.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.action.ImageInfoAction.OnBitmapListener;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.control.DialogImageSelect;
import com.peiban.app.ui.NewAlbumActivity.selectClick;
import com.peiban.app.ui.common.BitmapUtils;
import com.peiban.app.ui.common.PhotoDb;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.PhotoVo;
import com.peiban.vo.UserInfoVo;

/**
 * 
 * 功能：上传相片<br />
 * 日期：2013-5-22<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class SelectPhotoActivity extends BaseActivity {
	/**
	 * 组件相关
	 * */
	@ViewInject(id = R.id.album_edit_name)
	private EditText editName;
	@ViewInject(id = R.id.album_img_recover)
	private ImageView imgRecover;
	@ViewInject(id = R.id.album_check_privacy)
	private CheckBox checkPrivacy;
	@ViewInject(id=R.id.txt_name)
	private TextView txtName;
	@ViewInject(id=R.id.img_recover)
	private TextView imgRecovertxt;
	@ViewInject(id=R.id.album_hliner)
	private ImageView hliner;
	private String aid;
	private String albumName;
	private String uid;
	private PhotoVo photoVo;
	private Bitmap tempBitmap;
	private String auth;
	/**
	 * 调用本地图片
	 * */
	private ImageInfoAction imageInfoAction;
	private ImageInfoAction.OnBitmapListener imageBitmapListener;
	private Bitmap selectBitmap;
	private int photoHeight, photoWidth;
	private AlbumApi albumApi = new AlbumApi();
	private PeibanApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_album_layout);
		txtName.setText("相片名:");
		imgRecovertxt.setText("照片:");
		application = (PeibanApplication) getApplication();
		photoVo = new PhotoVo();
		aid = getIntent().getExtras().getString("aid");
		albumName = getIntent().getExtras().getString("albumName");
		auth = getIntent().getExtras().getString("auth");
		if ("1".equals(auth)) {
			txtName.setVisibility(View.GONE);
			editName.setVisibility(View.GONE);
			imgRecovertxt.setText("认证照片:");
			hliner.setVisibility(View.GONE);
		}
		UserInfoVo userinfo = application.getUserInfoVo();// 获取uid
		uid = userinfo.getUid();
		getWindowSize();
		baseInit();
		initParam();
		onClick();

	}

	/**
	 * 获取屏幕大小
	 * */
	private void getWindowSize() {
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay();
		photoWidth = display.getWidth();
		photoHeight = display.getHeight();
	}

	/**
	 * 点击事件
	 * */
	private void onClick() {
		// TODO Auto-generated method stub
		selectClick l = new selectClick();
		imgRecover.setOnClickListener(l);
	}

	private void initParam() {
		// TODO Auto-generated method stub
		imageBitmapListener = new OnBitmapListener() {
			@Override
			public void getToBitmap(int bimType, Bitmap bm) {
				selectPhoto(bm);
			}
		};
		imageInfoAction = new ImageInfoAction(SelectPhotoActivity.this);
		imageInfoAction.setOnBitmapListener(imageBitmapListener);
		imageInfoAction.setOutWidth(photoWidth);
		imageInfoAction.setOutHeight(photoHeight);
	}

	void selectPhoto() {
		imageInfoAction.getLocolPhoto();
	}

	void selectPhoto(Bitmap bitmap) {
		if (bitmap == null) {
		} else {
			if (tempBitmap != null) {
				tempBitmap.recycle();
			}
			tempBitmap = BitmapUtils.resizeImage(bitmap, 150, 150);
			imgRecover.setImageBitmap(tempBitmap);
			if (selectBitmap != null) {
				selectBitmap.recycle();
			}
			selectBitmap = bitmap;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		imageInfoAction.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(albumName);
		setBtnBack();
		setTitleRight(R.string.upload_photo);
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		if("1".equals(auth)){
			if (selectBitmap == null) {
				showToast(getResources().getString(R.string.toast_photobitmap));
			} else {
			if (!checkNetWork()) {
				showToast(getResources().getString(R.string.toast_network));
			} else if (!checkNetWorkOrSdcard()) {
				showToast(getResources().getString(
						R.string.toast_sdcard_mounted));
			} else {
				uploadBitmap();
			}
			}
		}else{
		if ("".equals(editName.getText().toString())) {
			showToast(getResources().getString(R.string.toast_photo));
		} else if (selectBitmap == null) {
			showToast(getResources().getString(R.string.toast_photobitmap));
		} else {
			if (!checkNetWork()) {
				showToast(getResources().getString(R.string.toast_network));
			} else if (!checkNetWorkOrSdcard()) {
				showToast(getResources().getString(
						R.string.toast_sdcard_mounted));
			} else {
				uploadBitmap();
			}
		}
		}
	}

	/**
	 * 上传相片bitmap
	 * */
	private void uploadBitmap() {
		// TODO Auto-generated method stub
		if (!NetworkUtils.isConnected(SelectPhotoActivity.this)) {
			showToast(getResources().getString(R.string.toast_network));
		} else {
			albumApi.upload(uid, Constants.PhotoType.PHOTO_NORMAL,
					selectBitmap, new AjaxCallBack<String>() {
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
							getWaitDialog().setMessage("正在上传......");
							getWaitDialog().show();
						}

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							String data = ErrorCode.getData(getBaseContext(), t);
							if (data != null) {
								try {
									String photoUrl = JSON.parseObject(data)
									.getString("photoUrl");
									if ("1".equals(auth)) {
										txtName.setVisibility(View.GONE);
										editName.setVisibility(View.GONE);
										imgRecovertxt.setText("认证照片:");
										authPhoto(photoUrl);
									} else {
										photoVo.setPhotoUrl(photoUrl);
										uploadPhotoSuccess(photoUrl);
										getWaitDialog().setMessage("相片上传成功......");
										FinalBitmap finalBitmap = getPhotoBitmap();
										finalBitmap.addBitmapToCache(data,
												selectBitmap);
									}
								} catch (Exception e) {
									showToast("失败,错误(0,1)");
								}
								
								getWaitDialog().dismiss();
							}
						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
							getWaitDialog().dismiss();
						}
					});
		}
	}

	/**
	 * 认证图片
	 * */
	private void authPhoto(String photoUrl) {
		try {
			AlbumApi albumApi = new AlbumApi();
			String tag = getIntent().getExtras().getString("tag");
			if ("0".equals(tag)) {
				String pid = getIntent().getExtras().getString("pid");
				albumApi.authPhoto(uid, pid, photoUrl, new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						getWaitDialog().setMessage("正在上传......");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						String data = ErrorCode.getData(getBaseContext(), t);
						if ("1".equals(data)) {
							showToast("提交成功！");
						} else {
							showToast("提交失败!");
						}
						getWaitDialog().dismiss();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						getWaitDialog().dismiss();
					}
				});
			} else {
				String aid = getIntent().getExtras().getString("aid");
				System.out.println("aid:"+aid);
				albumApi.authAlbumPhoto(uid, aid, photoUrl,
						new AjaxCallBack<String>() {
							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								super.onStart();
								getWaitDialog().setMessage("正在上传......");
								getWaitDialog().show();
							}

							@Override
							public void onSuccess(String t) {
								// TODO Auto-generated method stub
								super.onSuccess(t);
								String data = ErrorCode.getData(getBaseContext(), t);
								if ("1".equals(data)) {
									showToast("提交成功！");
									finish();
								} else {
									showToast("提交失败!");
								}
								getWaitDialog().dismiss();
							}

							@Override
							public void onFailure(Throwable t, String strMsg) {
								// TODO Auto-generated method stub
								super.onFailure(t, strMsg);
								getWaitDialog().dismiss();
							}
						});
			}
		} catch (Exception e) {
			showToast("错误(0,2)");
		}
	}

	/**
	 * 上传照片
	 * */
	protected void uploadPhotoSuccess(String photoUrl) {
		// TODO Auto-generated method stub
		albumApi.addPhoto(uid, aid, photoUrl, editName.getText().toString(),
				checkPrivacy.isChecked() ? "1" : "0", "0",
				new AjaxCallBack<String>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						getWaitDialog().setMessage("正在上传......");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						String data = ErrorCode.getData(getBaseContext(), t);
						if (data != null) {
							getWaitDialog().setMessage("相片上传成功......");
							getWaitDialog().dismiss();
//							Intent intent = new Intent(
//									SelectPhotoActivity.this,
//									PhotoActivity.class);
//							intent.putExtra("aid", aid);
//							intent.putExtra("albumName", albumName);
//							intent.putExtra("tag", "0");
//							startActivity(intent);
							finish();
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
					}

				});
	}
	/**
	 * 返回按钮处理事件
	 * */
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
//            Intent intent=new Intent(this,AlbumActivity.class);
//            startActivity(intent);
            finish();
            return false; 
        } 
        return false; 
    }
	class selectClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.album_img_recover:
				if (!FileTool.isMounted()) {
					Toast.makeText(
							SelectPhotoActivity.this,
							getResources().getString(
									R.string.toast_sdcard_mounted),
							Toast.LENGTH_SHORT).show();
				} else if (!FileTool.isSDCardAvailable()) {
					Toast.makeText(
							SelectPhotoActivity.this,
							getResources().getString(
									R.string.toast_sdcard_available),
							Toast.LENGTH_SHORT).show();
				} else {
					final DialogImageSelect dialogImageSelect = new DialogImageSelect(
							SelectPhotoActivity.this);
					dialogImageSelect.show();
					if ("1".equals(auth)) {
						dialogImageSelect.getBtnLocalImage().setVisibility(View.GONE);
					}
					// 选择本地相册图片
					dialogImageSelect.getBtnLocalImage().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialogImageSelect.dismiss();
									selectPhoto();
								}
							});
					// 选择本地相机
					dialogImageSelect.getBtnCamera().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialogImageSelect.dismiss();
									cameraPhoto();

								}
							});
				}
				break;

			}
		}

	}

	void cameraPhoto() {
		imageInfoAction.getCameraPhoto();
	}
}
