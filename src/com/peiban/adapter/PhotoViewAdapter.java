/**
 * @Title: PhotoViewAdapter.java 
 * @Package com.shangwupanlv.adapter 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-5-29 上午9:23:37 
 * @version V1.0
 */
package com.peiban.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONException;
import com.peiban.vo.PhotoVo;

public class PhotoViewAdapter extends PagerAdapter {
	private Context context;
	private HashMap<Integer, PhotoViewItem> photoHashMap;
	private List<PhotoVo> photoList;

	public PhotoViewAdapter(Context context, List<PhotoVo> photoList) {
		this.context = context;
		photoHashMap = new HashMap<Integer, PhotoViewItem>();
		this.photoList=photoList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photoList.size();
	}
	
	public PhotoViewItem getItem(int index){
		return photoHashMap.get(index);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		System.out.println("----------------加载----------:" + position);
		PhotoViewItem photoViewItem;
		if (photoHashMap.containsKey(position)) {
			photoViewItem = photoHashMap.get(position);
			photoViewItem.reload();
		} else {
			photoViewItem = new PhotoViewItem(context);
			
			photoHashMap.put(position, photoViewItem);
		    container.addView(photoViewItem);
		}
		try {
			String photoUrl=photoList.get(position).getPhotoUrl();
			String photoAuth=photoList.get(position).getAuth();
			photoViewItem.setData(photoUrl,photoAuth);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return photoViewItem;
	}
	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {

	}
}
