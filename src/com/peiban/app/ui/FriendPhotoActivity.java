package com.peiban.app.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.bitmap.core.BitmapCommonUtils;
import net.tsz.afinal.http.AjaxCallBack;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.adapter.PhotoViewAdapter;
import com.peiban.adapter.PhotoViewItem;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.control.DialogAlbum;
import com.peiban.app.ui.common.PhotoDb;
import com.peiban.vo.PhotoVo;
import com.shangwupanlv.widget.SclaeView;

/**
 * 
 * 功能：显示相片<br />
 * 日期：2013-5-22<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class FriendPhotoActivity extends BaseActivity {
	@ViewInject(id = R.id.photo_view)
	private ViewPager photoView;
	@ViewInject(id=R.id.photo_no)
	private TextView photoNo;
	private String aid;// 相册aid
	private String albumName;// 相册名
	private String photoName;
	private String uid;// 用户id
	private PhotoViewAdapter photoViewAdapter;
	private PhotoDb photoDb;
	private DialogAlbum dialogAlbum;
	private List<PhotoVo> photoList;
	private String pid,tag;
	private int photoindex;
	
	private int currPostion;  // 当前选择的页面
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getShangwupanlvApplication().setActivity(this);
		this.setContentView(R.layout.photo);
		if (!"".equals(getIntent().getExtras().getString("aid"))
				|| getIntent().getExtras().getString("aid") != null) {
			aid = getIntent().getExtras().getString("aid");
			albumName = getIntent().getExtras().getString("albumName");
		} else {
			showToast("aid为空！");
		}
		uid = getUserInfoVo().getUid();
		baseInit();
		tag=getIntent().getExtras().getString("tag");
		if ("0".equals(tag)) {
			if (!checkNetWork()) {
				showToast(getResources().getString(R.string.toast_network));
			} else if (!checkNetWorkOrSdcard()) {
				showToast(getResources().getString(
						R.string.toast_sdcard_mounted));
			} else {
				getPhoto();
			}
		} else {
			photoList = (List<PhotoVo>) getIntent().getSerializableExtra(
					"photoList");
			pid=photoList.get(0).getAid();			
			if (!photoList.isEmpty()) {
				photoindex=getIntent().getExtras().getInt("photoindex");
				showPhoto(photoList);
			} else {
				return;
			}
		}
		OnClick();
	}

	private void OnClick() {
		// TODO Auto-generated method stub
		photoView.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				photoName = albumName + "(" + (arg0 + 1) + "/"
						+ photoViewAdapter.getCount() + ")";
				if (photoList != null || !photoList.isEmpty()) {
					if ("0".equals(tag)) {
						pid = photoList.get(arg0).getPid();
					} else {
						pid = photoList.get(arg0).getAid();
						photoName = photoList.get(arg0).getPhotoName() + "(" + (arg0 + 1) + "/"
								+ photoViewAdapter.getCount() + ")";
					}
				}
				currPostion = arg0;
				setTitleContent(photoName);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

		});
	}



	/**
	 * 获取图片
	 * */
	private void getPhoto() {
		// TODO Auto-generated method stub
		AlbumApi albumApi = new AlbumApi();
		albumApi.getAlbumPhoto(uid, aid, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				getWaitDialog().setMessage("正在获取相片列表");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				String data = ErrorCode.getData(getBaseContext(), t);
				if (!"".equals(data)) {
					getWaitDialog().setMessage("获取成功");
					getWaitDialog().dismiss();
					photoList =JSON.parseArray(data, PhotoVo.class);
					showPhoto(photoList);
				} else {
					photoName = albumName + "(" + 0 + "/" + 0
							+ ")";
					setTitleContent(photoName);
				   getWaitDialog().dismiss();
				   photoView.setVisibility(View.GONE);
                   photoNo.setVisibility(View.VISIBLE);
                   photoNo.setText(R.string.friend_photo_no);
                   
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				getWaitDialog().setMessage("获取失败");
				getWaitDialog().dismiss();
				super.onFailure(t, strMsg);
			}

		});
	}


	/**
	 * 显示照片
	 * */
	protected void showPhoto(List<PhotoVo> photoList) {
		// TODO Auto-generated method stub
		photoViewAdapter = new PhotoViewAdapter(FriendPhotoActivity.this, photoList);
		currPostion = 0;
		photoView.setAdapter(photoViewAdapter);
		if (photoViewAdapter.getCount() == 0) {
			photoName = albumName + "(" + 0 + "/" + photoViewAdapter.getCount()
					+ ")";
		} else {
			photoName = albumName + "(" + 1 + "/" + photoViewAdapter.getCount()
					+ ")";
		}
		if(photoindex!=0){
			photoName = albumName + "(" + (photoindex+1) + "/" + photoViewAdapter.getCount()
					+ ")";
		}
		photoView.setVisibility(View.VISIBLE);
		photoView.setCurrentItem(photoindex);
		setTitleContent(photoName);
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setBtnBack();
		setTitleRight("保存本地");
	}
	
	@Override
	protected void titleBtnRight() {
		super.titleBtnRight();
		if(photoViewAdapter != null){
			PhotoViewItem photoViewItem = photoViewAdapter.getItem(currPostion);
			if(photoViewItem != null){
				SclaeView sclaeView = photoViewItem.getImgPhoto();
				if(sclaeView != null){
					saveBitmap(sclaeView.getImage());
				}
			}
		}

	}
	
	private void saveBitmap(Bitmap drawable) {
		if (!checkSdcard()) {
			return;
		}

		if (drawable != null) {
			File file = new File(BitmapCommonUtils.getExternalCacheDir(
					getBaseContext()).getParentFile(), "chatImg");

			if (!file.exists()) {
				if (!file.mkdirs()) {
					showToast("创建目录失败!");
				}
				;
			}

			File file2 = new File(file, ImageInfoAction.getPhotoFileName());
			FileOutputStream fos = null;

			try {
				fos = new FileOutputStream(file2);
				boolean flag = drawable.compress(CompressFormat.JPEG, 100, fos);
				if (flag) {
					showToast("保存成功:" + file2.getAbsolutePath());
				} else {
					showToast("保存失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				showToast("保存失败");
			}

		} else {
			showToast("保存失败!");
		}
	}
	
	@Override
	protected void titleBtnBack() {
		// TODO Auto-generated method stub
		finish();
	}
}
