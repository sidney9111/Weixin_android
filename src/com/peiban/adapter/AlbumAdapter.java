/**
 * @Title: AlbumAdapter.java 
 * @Package com.shangwupanlv.app.adapter 
 * @Description: TODO相册适配 
 * @author Alex.Z   
 * @date 2013-5-28 下午4:03:40 
 * @version V1.0
 */
package com.peiban.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.peiban.R;
import com.peiban.app.FinalFactory;
import com.peiban.vo.AlbumVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter {
	private List<AlbumVo> albumList;
	private Context context;
	private FinalBitmap finalBitmap;
	private AlbumVo albumVo;
	
	public AlbumAdapter(List<AlbumVo> albumList, Context context) {
		this.albumList = albumList;
		this.context = context;
		finalBitmap = FinalFactory.createFinalAlbumBitmap(context);
	}

	public void removeObject(AlbumVo albumVo){
		albumList.remove(albumVo);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albumList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewHodler;
		
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.album_item, null);
			viewHodler.albumCover = (ImageView) convertView
					.findViewById(R.id.album_item_img_cover);
			viewHodler.albumName = (TextView) convertView
					.findViewById(R.id.album_item_txt_albumname);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
			finalBitmap.display(viewHodler.albumCover, albumList.get(position).getAlbumCover());
			viewHodler.albumName.setText(albumList.get(position).getAlbumName());
			
		

		return convertView;
	}

	class ViewHodler {
		ImageView albumCover;
		TextView albumName;
	}
}
