package com.peiban.app.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.adapter.AlbumAdapter;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.control.DialogAlbum;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;
import com.peiban.vo.AlbumVo;
import com.peiban.vo.PhotoVo;

/**
 * 
 * 功能：好友相册<br />
 * 日期：2013-5-22<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class FriendAlbumActivity extends BaseActivity {
	@ViewInject(id = R.id.album_prompt)
	private View albumPrompt;
	@ViewInject(id = R.id.album_list_gridview)
	private GridView albumGridView;
	private AlbumAdapter albumadapter;
	private DialogAlbum dialogAlbum;
	private AlbumVo albumVo;
	private String touid;
	private List<PhotoVo> photoList;
	private static final int REQUEST = 0xf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.album);
		photoList = new ArrayList<PhotoVo>();
		baseInit();
		initAlbum();
		OnClick();
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
				Intent intent = new Intent(FriendAlbumActivity.this,
						FriendPhotoActivity.class);
				int photoindex=photoList.indexOf(AlbumToPhoto(albumVo));
				if ("0".equals(albumVo.getTag())) {
					intent.putExtra("aid", albumVo.getAid());
					intent.putExtra("albumName", albumVo.getAlbumName());
					intent.putExtra("tag", albumVo.getTag());
					startActivityForResult(intent, REQUEST);
				} else {
					intent.putExtra("aid", albumVo.getAid());
					intent.putExtra("albumName", albumVo.getAlbumName());
					intent.putExtra("photoList", (Serializable) photoList);
					intent.putExtra("tag", albumVo.getTag());
					intent.putExtra("photoindex", photoindex);
					startActivityForResult(intent, REQUEST);
				}
			}
		});
	}

	/**
	 * 初始化相册
	 * */
	private void initAlbum() {
		// TODO Auto-generated method stub
		touid = getIntent().getExtras().getString("touid");
		getNetworkAlbum();

	}

	/**
	 * 从服务器获取相册信息
	 * */
	private void getNetworkAlbum() {
		// TODO 从服务器获取相册信息
		if (!NetworkUtils.isConnected(FriendAlbumActivity.this)) {
			showToast(getResources().getString(R.string.toast_network));
		} else if (!FileTool.isMounted()) {
			showToast(getResources().getString(R.string.toast_sdcard_mounted));
		} else if (!FileTool.isSDCardAvailable()) {
			showToast(getResources().getString(R.string.toast_sdcard_available));
		} else {
			AlbumApi albumApi = new AlbumApi();
			String uid = getUserInfoVo().getUid();

			albumApi.getAlbum(uid, touid, new AjaxCallBack<String>() {
				@Override
				public void onStart() {
					// TODO 准备从服务器获取
					super.onStart();
					getWaitDialog().setMessage("正在获取相册列表......");
					getWaitDialog().show();
				}

				@Override
				public void onSuccess(String t) {
					// TODO 服务器获取成功
					super.onSuccess(t);
					getWaitDialog().dismiss();
					String data = ErrorCode.getData(t);
					if (!"".equals(data)) {
						showAlbum(data);
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO 服务器获取失败
					super.onFailure(t, strMsg);
					getWaitDialog().setMessage("获取列表失败......");
					getWaitDialog().dismiss();
				}
			});
		}
	}

	/**
	 * 显示相册信息
	 * 
	 * */
	private void showAlbum(String data) {
		List<AlbumVo> albumList = JSON.parseArray(data, AlbumVo.class);
		if (!albumList.isEmpty()) {
			photoList.clear();
			for (int i = 0; i < albumList.size(); i++) {
				if (!"0".equals(albumList.get(i).getTag())) {
					photoList.add(AlbumToPhoto(albumList.get(i)));
					
				}
			}
			albumadapter = new AlbumAdapter(albumList, FriendAlbumActivity.this);
			albumGridView.setAdapter(albumadapter);
		} else {
			LayoutInflater fInflater = LayoutInflater
					.from(FriendAlbumActivity.this);
			View view = fInflater.inflate(R.layout.album_prompt, null);
			TextView textView = (TextView) view.findViewById(R.id.albums_txt);
			textView.setVisibility(View.VISIBLE);
			textView.setText(R.string.friend_album_no);
		}
	}

	/**
	 * 将albumvo转换成photovo
	 * */
	private PhotoVo AlbumToPhoto(AlbumVo albumVo) {
		PhotoVo photoVo = new PhotoVo();
		photoVo.setAid(albumVo.getAid());
		photoVo.setPhotoName(albumVo.getAlbumName());
		photoVo.setPhotoUrl(albumVo.getAlbumCover());
		photoVo.setAuth(albumVo.getTag());
		return photoVo;
	}

	/**
	 * 初始化标题栏
	 * */
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleContent(R.string.album_title);
		setBtnBack();
	}
}
