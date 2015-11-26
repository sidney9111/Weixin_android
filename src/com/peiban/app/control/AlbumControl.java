/**
 * @Title: AlbumControl.java 
 * @Package com.shangwupanlv.app.control 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Alex.Z   
 * @date 2013-6-6 上午10:22:13 
 * @version V1.0
 */
package com.peiban.app.control;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.peiban.R;
import com.peiban.app.FinalFactory;
import com.peiban.app.ui.AlbumActivity;
import com.peiban.app.ui.FriendAlbumActivity;
import com.peiban.vo.AlbumVo;

public class AlbumControl {
	private Context context;
	private LinearLayout linearLayout;
	private List<AlbumVo> albumList;
	private String uid,touid;
	private static final int MAX=4;
   public AlbumControl(Context cotext,LinearLayout linearLayout,List<AlbumVo> albumList){
	   this.context=cotext;
	   this.linearLayout=linearLayout;
	   this.albumList=albumList;
	   View.OnClickListener l=new btnClick();
	   linearLayout.setOnClickListener(l);
   }
   public void showAlbum(String uid,String touid){
	   this.uid=uid;
	   this.touid=touid;
	   int index = albumList.size();
	   FinalBitmap finalBitmap=FinalFactory.createFinalAlbumBitmap(context);
	   int width = context.getResources().getDimensionPixelSize(R.dimen.album_view_width);
	   int height = context.getResources().getDimensionPixelSize(R.dimen.album_view_height);
	   LayoutParams params=new LayoutParams(width,height);
	   params.leftMargin = this.context.getResources().getDimensionPixelSize(R.dimen.photo_marigin);
	   if(index<MAX){
		   for(int i=0;i<index;i++){
			   ImageView imageView = (ImageView)LayoutInflater.from((context)).inflate(R.layout.details_photo_item, null);
			   finalBitmap.display(imageView, albumList.get(i).getAlbumCover());
			   linearLayout.addView(imageView,params);
		   }
	   }else{
		   for(int i=0;i<MAX;i++){
			   ImageView imageView = (ImageView)LayoutInflater.from((context)).inflate(R.layout.details_photo_item, null);
			   finalBitmap.display(imageView, albumList.get(i).getAlbumCover());
			   linearLayout.addView(imageView,params);
		   }
	   }
	   
   }
   class btnClick implements OnClickListener{
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==linearLayout){
			if(uid.equals(touid)){
			Intent intent=new Intent(context,AlbumActivity.class);
			context.startActivity(intent);		    
			}else{
				Intent intent=new Intent(context,FriendAlbumActivity.class);
				intent.putExtra("touid", touid);
				context.startActivity(intent);
			}
			}
	}
	   
   }
}
