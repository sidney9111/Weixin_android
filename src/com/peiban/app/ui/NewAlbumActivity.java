/**
 * @Title: NewAlbumActivity.java 
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO新建相册 
 * @author Alex.Z   
 * @date 2013-5-27 下午4:37:46 
 * @version V1.0
 */
package com.peiban.app.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.peiban.app.control.DialogAlbum;
import com.peiban.app.control.DialogImageSelect;
import com.peiban.app.ui.common.AlbumDb;
import com.peiban.app.ui.common.BitmapUtils;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.UserInfoVo;

public class NewAlbumActivity extends BaseActivity {
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
	/**
	 * 调用本地图片
	 * */
	private ImageInfoAction imageInfoAction;
	private ImageInfoAction.OnBitmapListener imageBitmapListener;
	private Bitmap selectBitmap;
	private Bitmap tempBitmap;
	private String uid,tag;
	private AlbumApi albumApi = new AlbumApi();
	private PeibanApplication application;
	/**
	 * tag =0：添加相册；1:添加照片；
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_album_layout);
		application = (PeibanApplication) getApplication();
		UserInfoVo userinfo = application.getUserInfoVo();// 获取uid
		uid = userinfo.getUid();
		tag = getIntent().getExtras().getString("tag");
		baseInit();
		if(!"0".equals(tag)){
			txtName.setText("相片名:");
			imgRecovertxt.setText("照片:");
		}
		initParam();
		onClick();
	}

	@Override
	protected void baseInit() {
		super.baseInit();
		checkPrivacy.setVisibility(View.GONE);
		checkPrivacy.setChecked(false);
	}

	private void onClick() {
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
		imageInfoAction = new ImageInfoAction(NewAlbumActivity.this);
		imageInfoAction.setOnBitmapListener(imageBitmapListener);
		imageInfoAction.setOutHeight(320);
		imageInfoAction.setOutWidth(240);
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
		setBtnBack();
		setTitle(R.string.new_album);
		setTitleRight(R.string.register_head_title_right_tag);
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
		if (editName.getText().toString().equals("")) {
			showToast(getResources().getString(R.string.toast_album));
		} else if (selectBitmap == null) {
			showToast(getResources().getString(R.string.toast_albumbitmap));
		} else {
			commitAlbum();
		}
	}

	private void commitAlbum() {
		// TODO Auto-generated method stub
		if (!NetworkUtils.haveInternet(NewAlbumActivity.this)) {
			Toast.makeText(NewAlbumActivity.this,
					getResources().getString(R.string.toast_network),
					Toast.LENGTH_SHORT).show();
		} else {
			uploadBitmap();
		}
	}

	/**
	 * 上传相册封面
	 * */
	private void uploadBitmap() {
		// TODO Auto-generated method stub
		albumApi.upload(uid, Constants.PhotoType.PHOTO_NORMAL, selectBitmap,
				new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
						getWaitDialog().setMessage("正在上传......");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						getWaitDialog().dismiss();
						String data = ErrorCode.getData(getBaseContext(), t);
						if (data != null) {
							String photoUrl = JSON.parseObject(data).getString(
									"photoUrl");
							uploadRecoverSuccess(photoUrl);
							getWaitDialog().setMessage("封面上传成功......");
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().dismiss();
						Toast.makeText(NewAlbumActivity.this,
								"服务器响应错误:" + strMsg, Toast.LENGTH_LONG).show();
					}
				});
	}

	/**
	 * 封面提交成功后 上传相册名和隐私
	 * */
	protected void uploadRecoverSuccess(String photourl) {
		// TODO Auto-generated method stub
		String albumname = editName.getText().toString();
		
		if ("0".equals(tag))
			uploadAlbum(albumname,photourl);
		else
			uploadPhoto(albumname,photourl);
	}

	/**
	 * 上传相册
	 * */
	public void uploadAlbum(String albumname,String photourl){
		boolean privacy = checkPrivacy.isChecked();
		albumApi.addAlbum(uid, albumname, photourl, privacy ? "1" : "0",
				new AjaxCallBack<String>() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						getWaitDialog().setMessage("正在提交......");
						getWaitDialog().show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						final String data = ErrorCode.getData(getBaseContext(),
								t);
						if (data != null) {
							getWaitDialog().setMessage("提交成功......");
							FinalBitmap bitmap = getPhotoBitmap();
							bitmap.addBitmapToCache(data, selectBitmap);
							Intent intent = new Intent(NewAlbumActivity.this,
									AlbumActivity.class);
							startActivity(intent);
							finish();
						}
						getWaitDialog().dismiss();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						getWaitDialog().setMessage("提交失败......");
						getWaitDialog().dismiss();
					}

				});
	}
	/**
	 * 上传照片
	 */
	public void uploadPhoto(String albumname,String photourl){
		albumApi.addAlbumPhoto(uid, albumname, photourl, new AjaxCallBack<String>() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				getWaitDialog().setMessage("正在提交......");
				getWaitDialog().show();
			}
			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				final String data = ErrorCode.getData(getBaseContext(),
						t);
				System.out.println("upload:"+t);
				if (data != null) {
					getWaitDialog().setMessage("提交成功......");
					FinalBitmap bitmap = getPhotoBitmap();
					bitmap.addBitmapToCache(data, selectBitmap);
					Intent intent = new Intent(NewAlbumActivity.this,
							AlbumActivity.class);
					startActivity(intent);
					finish();
				}
				getWaitDialog().dismiss();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				getWaitDialog().setMessage("提交失败......");
				getWaitDialog().dismiss();
			}
		});
	}
	class selectClick implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.album_img_recover:
				if (!FileTool.isMounted()) {
					Toast.makeText(
							NewAlbumActivity.this,
							getResources().getString(
									R.string.toast_sdcard_mounted),
							Toast.LENGTH_SHORT).show();
				} else if (!FileTool.isSDCardAvailable()) {
					Toast.makeText(
							NewAlbumActivity.this,
							getResources().getString(
									R.string.toast_sdcard_available),
							Toast.LENGTH_SHORT).show();
				} else {
					final DialogImageSelect dialogImageSelect=new DialogImageSelect(NewAlbumActivity.this);
					dialogImageSelect.show();
					//选择本地相册图片
					dialogImageSelect.getBtnLocalImage().setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialogImageSelect.dismiss();
							selectPhoto();
						}
					});
					//选择本地相机
					dialogImageSelect.getBtnCamera().setOnClickListener(new OnClickListener() {
						
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
	/**
	 * 返回按钮处理事件
	 * */
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            Intent intent=new Intent(this,AlbumActivity.class);
            startActivity(intent);
            finish();
            return false; 
        } 
        return false; 
    }
	void cameraPhoto(){
		imageInfoAction.getCameraPhoto();
	}
}
