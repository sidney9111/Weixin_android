package com.peiban.app.ui;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.peiban.R;
import com.peiban.app.Constants;
import com.peiban.app.action.ImageInfoAction;
import com.peiban.app.api.AlbumApi;
import com.peiban.app.api.ErrorCode;
import com.peiban.app.api.ProjectApi;
import com.peiban.app.control.DialogImageSelect;
import com.peiban.command.FileTool;
import com.peiban.command.NetworkUtils;

public class NewProjectAlbumActivity extends BaseActivity{
	
	//@ViewInject(id=R.id.txt)
	private EditText txtDescription;
	//@ViewInject(id=R.id.img)
	private ImageView img;
	private Bitmap selectBitmap;
	private AlbumApi albumApi = new AlbumApi();
	private ProjectApi projectApi=new ProjectApi();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_projectalbum_layout);
		Intent intent=getIntent();
		//Bitmap bitmap=intent.getParcelableExtra("bitmap");
		byte [] bis=intent.getByteArrayExtra("bitmap");  
        Bitmap bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
        img=(ImageView) findViewById(R.id.img);
        img.setImageBitmap(bitmap);
        selectBitmap = bitmap;
        
        txtDescription=(EditText) findViewById(R.id.txt);
        super.baseInit();
	}
	@Override
	protected void initTitle() {
		setBtnBack();
		setTitleContent(R.string.project_upload_title);
		setTitleRight(R.string.project_apply);
		//getBtnTitleRight().setVisibility(View.INVISIBLE);
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void titleBtnRight() {
		if (!NetworkUtils.haveInternet(NewProjectAlbumActivity.this)) {
			Toast.makeText(NewProjectAlbumActivity.this,
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
		
		albumApi.upload(getUserInfoVo().getUid(), Constants.PhotoType.PHOTO_NORMAL, selectBitmap,
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
							//uploadRecoverSuccess(photoUrl);
							uploadPhoto(txtDescription.getEditableText().toString(),photoUrl);
							getWaitDialog().setMessage("封面上传成功......");
						}
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						getWaitDialog().dismiss();
						Toast.makeText(NewProjectAlbumActivity.this,
								"服务器响应错误:" + strMsg, Toast.LENGTH_LONG).show();
					}
				});
	}
	/**
	 * 上传项目
	 */
	public void uploadPhoto(String albumname,String photourl){
		projectApi.addProjectAlbum(getUserInfoVo().getUid(), albumname, photourl, new AjaxCallBack<String>() {
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
				final String data = ErrorCode.getData(getBaseContext(),t);
				System.out.println("upload:"+t);
				if (data != null) {
					getWaitDialog().setMessage("提交成功......");
//					FinalBitmap bitmap = getPhotoBitmap();
//					bitmap.addBitmapToCache(data, selectBitmap);
//					Intent intent = new Intent(NewProjectAlbumActivity.this,
//							AlbumActivity.class);
//					startActivity(intent);
					setResult(10);
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
	
	
	
}
