package com.peiban.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import com.alibaba.fastjson.JSONArray;
import com.peiban.R;
import com.peiban.adapter.AlbumAdapter;
import com.peiban.app.AlbumPromptDialog;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.control.DialogAlbum;
import com.peiban.vo.AlbumVo;
import com.peiban.vo.PhotoVo;
import com.peiban.vo.UserInfoVo;

/**
 * 
 * 功能：相册<br />
 * 日期：2013-5-22<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class AlbumActivity extends BaseActivity {
	private static final int REQUEST = 0xf;
	@ViewInject(id = R.id.album_prompt)
	private View albumPrompt;
	@ViewInject(id = R.id.album_list_gridview)
	private GridView albumGridView;
	private AlbumAdapter albumadapter;
	private DialogAlbum dialogAlbum;
	private AlbumVo albumVo;
	private UserInfoVo userInfoVo;
	private List<PhotoVo> photoList;
    private List<AlbumVo> albumList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.album);
		dialogAlbum = new DialogAlbum(AlbumActivity.this);
		photoList = new ArrayList<PhotoVo>();
		baseInit();
	}
	protected void baseInit() {
		super.baseInit();
		userInfoVo = getUserInfoVo();
		initAlbum(true);
		OnClick();
	}
	@Override
	protected void onResume() {
		super.onResume();
//		initAlbum();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(REQUEST == requestCode)
		{
			initAlbum(false);
		}
	}
	/**
	 * 点击事件
	 * */
	private void OnClick() {
		// TODO Auto-generated method stub
		albumGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int postion, long i) {
				// TODO Auto-generated method stub
				AlbumVo albumVo = (AlbumVo) albumadapter.getItem(postion);
				Intent intent = new Intent(AlbumActivity.this,
						PhotoActivity.class);
				int photoindex=photoList.indexOf(AlbumToPhoto(albumVo));
				if ("0".equals(albumVo.getTag())) {
					intent.putExtra("aid", albumVo.getAid());
					intent.putExtra("albumName", albumVo.getAlbumName());
					intent.putExtra("tag", albumVo.getTag());
					startActivityForResult(intent, REQUEST);
//					finish();
				} else{
					intent.putExtra("aid", albumVo.getAid());
					intent.putExtra("albumName", albumVo.getAlbumName());
					intent.putExtra("photoList", (Serializable) photoList);
					intent.putExtra("tag", albumVo.getTag());
					intent.putExtra("photoindex", photoindex);
					startActivityForResult(intent, REQUEST);
//					finish();
				}
			}
		});

		/**
		 * 长按 点击事件
		 * */
		albumGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterview,
					View view, int postion, long i) {
				// TODO Auto-generated method stub
				albumVo = (AlbumVo) albumadapter.getItem(postion);
				dialogOnClick l = new dialogOnClick();
				dialogAlbum.getBtnUpdate().setOnClickListener(l);
				dialogAlbum.getBtnDelete().setOnClickListener(l);
				dialogAlbum.show();
				return true;

			}
		});
	}
	/**
	 * 将albumvo转换成photovo
	 * */
			private PhotoVo AlbumToPhoto(AlbumVo albumVo){
				PhotoVo photoVo=new PhotoVo();
				photoVo.setAid(albumVo.getAid());
				photoVo.setPhotoName(albumVo.getAlbumName());
				photoVo.setPhotoUrl(albumVo.getAlbumCover());
				photoVo.setAuth(albumVo.getTag());
				return photoVo;
			}
	/**
	 * 初始化相册
	 * @param flag 是否弹出等待框
	 * */
	private void initAlbum(boolean flag) {
		// TODO Auto-generated method stub
		if (checkNetWork()) {
			getNetworkAlbum(flag);
		} else {
			showToast(getResources().getString(R.string.toast_network));
		}

	}

	/**
	 * 删除相册
	 * */
	public void DeleteAlbum() {
		// TODO 删除相册
		AlbumApi albumApi = new AlbumApi();
		String uid = userInfoVo.getUid();
		albumApi.delAlbum(uid, albumVo.getAid(), new AjaxCallBack<String>() {
			@Override
			public void onStart() {
				// TODO 开始删除
				super.onStart();
				getWaitDialog().setMessage("正在删除......");
				getWaitDialog().show();
			}

			@Override
			public void onSuccess(String t) {
				// TODO 删除成功
				super.onSuccess(t);
				String data = ErrorCode.getData(getBaseContext(), t);
				if(data != null){
					if ("1".equals(data)) {
						getWaitDialog().setMessage("删除成功");
						getWaitDialog().dismiss();
						initAlbum(false);
					} else {
						getWaitDialog().setMessage("删除失败");
						getWaitDialog().dismiss();
					}
				}
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO 删除失败
				super.onFailure(t, strMsg);
			}
		});
	}

	/**
	 * 从服务器获取相册信息
	 * @param isFlag 是否显示dialog
	 * */
	private void getNetworkAlbum(final boolean isFlag) {
		// TODO 从服务器获取相册信息
		
			AlbumApi albumApi = new AlbumApi();
			String uid = userInfoVo.getUid();
			albumApi.getAlbum(uid, uid, new AjaxCallBack<String>() {
				@Override
				public void onStart() {
					// TODO 准备从服务器获取
					super.onStart();
					if(isFlag)
					{
						getWaitDialog().setMessage("正在获取相册列表......");
						getWaitDialog().show();
					}
				}

				@Override
				public void onSuccess(String t) {
					// TODO 服务器获取成功
					super.onSuccess(t);
					if(isFlag)
					{
						getWaitDialog().dismiss();
					}
					String data = ErrorCode.getData(getBaseContext(), t);
//					System.out.println("albumdata:"+data);
					try {
						if (!"".equals(data)) {
							albumList=JSONArray.parseArray(data, AlbumVo.class);
							showAlbum();
						} else {
							
							albumPrompt.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
					}
					
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO 服务器获取失败
					super.onFailure(t, strMsg);
					if(isFlag)
					{
						getWaitDialog().setMessage("获取列表失败......");
						getWaitDialog().dismiss();
					}
				}
			});
	}
	

	/**
	 * 显示相册信息
	 * 
	 * */
	private void showAlbum() {
		photoList.clear();
		for (int i = 0; i < albumList.size(); i++) {
			if (!"0".equals(albumList.get(i).getTag())) {
				photoList.add(AlbumToPhoto(albumList.get(i)));
				
			}
		}
		getWaitDialog().dismiss();
		if (albumList != null) {
			albumadapter = new AlbumAdapter(albumList, AlbumActivity.this);
			albumGridView.setAdapter(albumadapter);
		} else {
			albumPrompt.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化标题栏
	 * */
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.album_title);
		setBtnBack();
		setTitleRight(R.string.title_more);
	}

	@Override
	protected void titleBtnRight() {
		// TODO Auto-generated method stub
		super.titleBtnRight();
		final AlbumPromptDialog albumPromptDialog = new AlbumPromptDialog(
				AlbumActivity.this);
		albumPromptDialog.setConfirmText(getResources().getString(
				R.string.album_add));
		albumPromptDialog.setCannelText(getResources().getString(
				R.string.add_photo));
		albumPromptDialog.show();
		albumPromptDialog.addPhoto(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AlbumActivity.this,
						NewAlbumActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
				finish();
				albumPromptDialog.cancel();
			}
		});
		albumPromptDialog.addAlbum(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AlbumActivity.this,
						NewAlbumActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent); 
				finish();
				albumPromptDialog.cancel();
			}
		});
	}			
	/**
	 * dialog点击事件
	 * */
	class dialogOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == dialogAlbum.getBtnUpdate()) {
				Intent intent = new Intent(AlbumActivity.this,
						UpdateAlbumActivity.class);
				intent.putExtra("albumVo", albumVo);
				startActivity(intent);
				finish();
			} else if (v == dialogAlbum.getBtnDelete()) {
				DeleteAlbum();
				dialogAlbum.cancel();
			}

		}
	}
}
