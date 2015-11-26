package com.peiban.app.ui;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.peiban.R;
import com.peiban.adapter.PhotoViewAdapter;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.control.DialogAlbum;
import com.peiban.app.control.DialogPothoAlbum;
import com.peiban.vo.PhotoVo;
import com.peiban.vo.UserInfoVo;

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
public class PhotoActivity extends BaseActivity {
	@ViewInject(id = R.id.photo_view)
	private ViewPager photoView;
	@ViewInject(id = R.id.photo_no)
	private TextView photoNo;
	private String aid;// 相册aid
	private String albumName;// 相册名
	private String photoName;
	private String uid;// 用户id
	private PhotoViewAdapter photoViewAdapter;
	private DialogAlbum dialogAlbum;
	private List<PhotoVo> photoList;
	private String pid, photoUrl, tag;
	private int photoindex;
	
	/** 我的照片更多操作 */
	private DialogPothoAlbum dialogPothoAlbum;

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
		UserInfoVo userInfoVo = getUserInfoVo();
		uid = userInfoVo.getUid();
		baseInit();
		tag = getIntent().getExtras().getString("tag");
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
			pid = photoList.get(0).getPid();
//			getBtnTitleRight().setVisibility(View.GONE);
			dialogPothoAlbum.getBtnAddphoto().setVisibility(View.GONE);
			if (!photoList.isEmpty()) {
				photoindex=getIntent().getExtras().getInt("photoindex");
				showPhoto(photoList);
			} else {
				return;
			}
		}
		onClick();
	}
	
	protected void	baseInit() {
		super.baseInit();
		dialogPothoAlbum = new DialogPothoAlbum(PhotoActivity.this);
	}

	private void onClick() {
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
		
		
		View.OnClickListener dialogPhotoOnClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 我要添加照片
				if(v == dialogPothoAlbum.getBtnAddphoto())
				{
					dialogPothoAlbum.cancel();
					selectPhoto();
				}else 
					// 我要认证
					if(v == dialogPothoAlbum.getBtnAuth())
				{
						dialogPothoAlbum.cancel();
						if ("0".equals(tag)) {
							authPhoto("");
						} else {
							authPhoto("1");
						}
				}else 
					// 我要删除
					if(v == dialogPothoAlbum.getBtnDelete())
				{
						dialogPothoAlbum.cancel();
						if (checkNetWork()) {
							if ("0".equals(tag)) {
								delPhoto();
							} else {
								delAlbum();
							}
						} else {
							showToast(getResources().getString(R.string.toast_network));
						}
				}
				
			}
		};
		
		dialogPothoAlbum.addLisener(dialogPhotoOnClickListener);
	}

	class dialogOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == dialogAlbum.getBtnDelete()) {
				if (!"".equals(photoList.get(0).getPid())) {
					delPhoto();
				} else {
					delAlbum();
				}
			}
		}
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
					photoList = JSONArray.parseArray(data, PhotoVo.class);
					showPhoto(photoList);
				} else {
					photoName = albumName + "(" + 0 + "/" + 0 + ")";
					setTitleContent(photoName);
					getWaitDialog().dismiss();
					photoView.setVisibility(View.GONE);
					photoNo.setVisibility(View.VISIBLE);
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

	public void delAlbum() {
		// TODO Auto-generated method stub
		AlbumApi albumApi = new AlbumApi();
		albumApi.delAlbum(uid, pid, new AjaxCallBack<String>() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				getWaitDialog().setMessage("正在删除相片");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				String data = ErrorCode.getData(getBaseContext(), t);
				if ("".equals(data) || data != null) {
					if ("1".equals(data)) {
						getWaitDialog().setMessage("删除成功");
						getWaitDialog().dismiss();
//						Intent intent = new Intent(PhotoActivity.this,
//								AlbumActivity.class);
//						startActivity(intent);
						finish();
					} else {
						getWaitDialog().setMessage("删除失败");
						getWaitDialog().dismiss();
					}
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
	 * 删除照片
	 * */
	public void delPhoto() {
		// TODO Auto-generated method stub
		AlbumApi albumApi = new AlbumApi();
		albumApi.delPhoto(uid, pid, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				getWaitDialog().setMessage("正在删除相片");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				String data = ErrorCode.getData(getBaseContext(), t);
				if ("".equals(data) || data != null) {
					if ("1".equals(data)) {
						getWaitDialog().setMessage("删除成功");
						getWaitDialog().dismiss();
						getPhoto();
					} else {
						getWaitDialog().setMessage("删除失败");
						getWaitDialog().dismiss();
					}
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
	 * 显示照片
	 * */
	protected void showPhoto(List<PhotoVo> photoList) {
		// TODO Auto-generated method stub
		photoViewAdapter = new PhotoViewAdapter(PhotoActivity.this, photoList);
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
		if ("0".equals(tag)) {
			pid = photoList.get(0).getPid();
		} else {
			pid = photoList.get(0).getAid();

		}
		photoView.setVisibility(View.VISIBLE);
		photoView.setCurrentItem(photoindex);
		
		setTitleContent(photoName);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setBtnBack();
		setTitleRight(R.string.more_title);
	}

	@Override
	protected void titleBtnBack() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(PhotoActivity.this, AlbumActivity.class);
//		startActivity(intent);
		finish();
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		dialogPothoAlbum.show();
	}

	/**
	 * 选择相片
	 * */
	private void selectPhoto() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PhotoActivity.this,
				SelectPhotoActivity.class);
		intent.putExtra("aid", aid);
		intent.putExtra("albumName", albumName);
		startActivity(intent);
		finish();
	}

	/** 以下是menu按键 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			super.openOptionsMenu();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
//            Intent intent=new Intent(this,AlbumActivity.class);
//            startActivity(intent);
			setResult(Activity.RESULT_OK);
            finish();
            return false; 
        } else {
			return super.onKeyDown(keyCode, event);
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		menu.add(0, 1, 1, "删除");
//		menu.add(0, 2, 2, "认证我的照片");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		// TODO Auto-generated method stub
//		if (item.getItemId() == 1) {
//			if (checkNetWork()) {
//				if ("0".equals(tag)) {
//					delPhoto();
//				} else {
//					delAlbum();
//				}
//			} else {
//				showToast(getResources().getString(R.string.toast_network));
//			}
//		} else if (item.getItemId() == 2) {
//			// 照片认证
//
//			if ("0".equals(tag)) {
//				authPhoto("");
//			} else {
//				authPhoto("1");
//			}
//		}
		return super.onMenuItemSelected(featureId, item);

	}

	/**
	 * 相片认证
	 * */
	public void authPhoto(String t) {
		AlbumApi albumApi = new AlbumApi();
		albumApi.authPhotoCheck(uid, pid, t, new AjaxCallBack<String>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				getWaitDialog().setMessage("认证检测.....");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				String data = ErrorCode.getData(getBaseContext(), t);
				getWaitDialog().cancel();
				if ("1".equals(data)) {
					getPromptDialog().setTitle("图片正在认证中...您要:");
					getPromptDialog().setConfirmText("重新认证");
					getPromptDialog().setCannelText("取消");
					getPromptDialog().addConfirm(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(PhotoActivity.this,
									SelectPhotoActivity.class);
							intent.putExtra("aid", aid);
							intent.putExtra("albumName", "认证照片");
							intent.putExtra("auth", "1");
							intent.putExtra("pid", pid);
							intent.putExtra("tag", tag);
							startActivity(intent);
						}
					});
					getPromptDialog().addCannel(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							getPromptDialog().cancel();
						}
					});
				} else {
					Intent intent = new Intent(PhotoActivity.this,
							SelectPhotoActivity.class);
					intent.putExtra("aid", aid);
					intent.putExtra("albumName", "认证照片");
					intent.putExtra("auth", "1");
					intent.putExtra("pid", pid);
					intent.putExtra("tag", tag);
					startActivity(intent);
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
			}

		});
	}

}
