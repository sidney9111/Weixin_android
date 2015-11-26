/**
 * @Title: PhotoViewItem.java 
 * @Package com.shangwupanlv.adapter 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-29 上午9:41:57 
 * @version V1.0
 */
package com.peiban.adapter;

import net.tsz.afinal.FinalBitmap;

import com.peiban.R;
import com.peiban.app.FinalFactory;
import com.shangwupanlv.widget.SclaeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 自定义PhotoViewItem方便外部复用
 * */
public class PhotoViewItem extends FrameLayout {
	private SclaeView imgPhoto;
	private ImageView imgAuth;
	private FinalBitmap finalBitmap;
	private String photoUrl, photoAuth;

	public PhotoViewItem(Context context) {
		super(context);
		setupViews();
		finalBitmap = FinalFactory.createFinalBitmap(context);
	}

	public PhotoViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public SclaeView getImgPhoto() {
		return imgPhoto;
	}

	/**
	 * 初始化View
	 * */
	private void setupViews() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.photoview_item, null);
		imgPhoto = (SclaeView) view.findViewById(R.id.photoview_img);
		imgAuth=(ImageView)view.findViewById(R.id.photoview_auth);
		addView(view);
	}

	/**
	 * 填充数据供外部使用
	 * */
	public void setData(String photoUrl, String photoAuth) {
		this.photoUrl = photoUrl;
		this.photoAuth = photoAuth;
		finalBitmap.display(imgPhoto, photoUrl);
		finalBitmap.addBitmapToCache(photoUrl, null);
		if("1".equals(photoAuth)){
			imgAuth.setVisibility(View.GONE);
		}else if("2".equals(photoAuth)){
			imgAuth.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 内存回收，供外部调用
	 * */
//	public void recycle() {
//		finalBitmap.clearCache();
//	}

	/**
	 * 重新加载,供外部调用
	 */
	public void reload() {
		if("1".equals(photoAuth)){
			imgAuth.setVisibility(View.GONE);
		}else if("2".equals(photoAuth)){
			imgAuth.setVisibility(View.VISIBLE);
		}
		finalBitmap.display(imgPhoto, photoUrl);
	}

}
