/**
 * @Title: {@link UpdateAlbumActivity}
 * @Package com.shangwupanlv.app.ui 
 * @Description: TODO修改相册 
 * @author Alex.Z   
 * @date 2013-5-27 下午4:37:46 
 * @version V1.0
 */
package com.peiban.app.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.FinalFactory;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.action.ImageInfoAction.OnBitmapListener;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.ui.common.AlbumDb;
import com.peiban.application.PeibanApplication;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.AlbumVo;
import com.peiban.vo.UserInfoVo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateAlbumActivity extends BaseActivity {
	/**
	 * 组件相关
	 * */
	@ViewInject(id = R.id.album_edit_name)
	private EditText editName;
	@ViewInject(id = R.id.album_img_recover)
	private ImageView imgRecover;
	@ViewInject(id = R.id.album_check_privacy)
	private CheckBox checkPrivacy;
	/**
	 * 调用本地图片
	 * */
	private ImageInfoAction imageInfoAction;
	private ImageInfoAction.OnBitmapListener imageBitmapListener;
	private Bitmap selectBitmap;
	private int outputx = 250, outputy = 250;
	private String uid;
	private AlbumApi albumApi = new AlbumApi();
    private FinalBitmap finalBitmap;
	private AlbumVo albumVo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_album_layout);
		initBaseInfo();
		baseInit();
		initParam();
//		imgRecover.setDrawingCacheEnabled(true);
//		selectBitmap=Bitmap.createBitmap(imgRecover.getDrawingCache());
//		imgRecover.setDrawingCacheEnabled(false);
		onClick();
		
	}
/**
 * 初始化基本信息
 * */
	private void initBaseInfo() {
		// TODO Auto-generated method stub
		UserInfoVo userinfo = getUserInfoVo();// 获取uid
		uid = userinfo.getUid();
		finalBitmap=getPhotoBitmap();
		albumVo=(AlbumVo)getIntent().getSerializableExtra("albumVo");
		editName.setText(albumVo.getAlbumName());
		if("1".equals(albumVo.getPrivacy())){
			checkPrivacy.setChecked(true);
		}else{
			checkPrivacy.setChecked(false);
		}
		finalBitmap.display(imgRecover, albumVo.getAlbumCover());
	}

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
		imageInfoAction = new ImageInfoAction(UpdateAlbumActivity.this);
		imageInfoAction.setOnBitmapListener(imageBitmapListener);
		imageInfoAction.setOutputX(outputx);
		imageInfoAction.setOutputY(outputy);
	}

	void selectPhoto() {
		imageInfoAction.getCropLocolPhoto();
	}

	void selectPhoto(Bitmap bitmap) {
		if (bitmap == null) {
		} else {
			imgRecover.setImageBitmap(bitmap);
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
		setTitle(R.string.album_update);
		setTitleRight(R.string.update);
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
			uploadRecoverSuccess(albumVo.getAlbumCover());
		} else {
			commitAlbum();
		}
	}

	private void commitAlbum() {
		// TODO Auto-generated method stub
		if (!NetworkUtils.haveInternet(UpdateAlbumActivity.this)) {
			Toast.makeText(UpdateAlbumActivity.this,
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
							String photoUrl = JSON.parseObject(data)
									.getString("photoUrl");
							albumVo.setAlbumCover(photoUrl);
							uploadRecoverSuccess(photoUrl);
							getWaitDialog().setMessage("封面上传成功......");
						}
					}
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().dismiss();
						Toast.makeText(UpdateAlbumActivity.this,
								"服务器响应错误:" + strMsg, Toast.LENGTH_LONG).show();
					}
				});
	}
	/**
	 * 封面提交成功后 上传相册名和隐私
	 * */
	protected void uploadRecoverSuccess(String t) {
		// TODO Auto-generated method stub
		String albumname = editName.getText().toString();
		boolean privacy = checkPrivacy.isChecked();
		albumApi.editAlbum(uid, albumVo.getAid(),albumname, t, privacy ? "1" : "0",
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
						System.out.println("update:"+t);
						if ("1".equals(data)||data!=null) {
							getWaitDialog().setMessage("提交成功......");
							AlbumDb albumDb = new AlbumDb(UpdateAlbumActivity.this, getFinalDb());
							getWaitDialog().setMessage("正在保存......");
							albumVo.setAlbumName(editName.getText().toString());
							albumVo.setPrivacy(checkPrivacy.isChecked() ? "1" : "0");
							albumDb.albumUpdate(albumVo);
							FinalBitmap bitmap = getPhotoBitmap();
							bitmap.addBitmapToCache(t, selectBitmap);
							getWaitDialog().dismiss();
							Intent intent = new Intent(UpdateAlbumActivity.this,
									AlbumActivity.class);
							startActivity(intent);
							finish();
							
						}else{
						getWaitDialog().setMessage("保存失败......");
						getWaitDialog().dismiss();
					
						}}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
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
							UpdateAlbumActivity.this,
							getResources().getString(
									R.string.toast_sdcard_mounted),
							Toast.LENGTH_SHORT).show();
				} else if (!FileTool.isSDCardAvailable()) {
					Toast.makeText(
							UpdateAlbumActivity.this,
							getResources().getString(
									R.string.toast_sdcard_available),
							Toast.LENGTH_SHORT).show();
				} else {
					selectPhoto();
				}
				break;

			}
		}

	}
}
