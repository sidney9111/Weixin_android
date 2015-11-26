package com.peiban.app.ui.common;

import net.tsz.afinal.FinalBitmap;

import com.peiban.R;
import com.peiban.vo.CustomerVo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

public class FinalOnloadBitmap {
	
	public static void finalDisplay(Context context, CustomerVo customerVo, ImageView imageView, FinalBitmap finalBitmap){
		String head = customerVo.getHead();
		Bitmap bitmap = FinalOnloadBitmap.getBitmap(context, customerVo);
		
		
		if(bitmap != null){
			if(TextUtils.isEmpty(head)){
				imageView.setImageBitmap(bitmap);
			}else{
				finalBitmap.display(imageView, customerVo.getHead(), bitmap, bitmap);
			}
		}
	}
	
	public static Bitmap getBitmap(Context context, CustomerVo customerVo){
		try {
			if("1".equals(customerVo.getSex())){
				return ((BitmapDrawable)context.getResources().getDrawable(R.drawable.head_man)).getBitmap();
			}else{
				return ((BitmapDrawable)context.getResources().getDrawable(R.drawable.head_woman)).getBitmap();
			}
		} catch (Exception e) {
			return null;
		}

	}
}
